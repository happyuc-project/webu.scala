package com.micronautics.webuj

import java.math.BigInteger
import java.util.Optional
import org.webuj.protocol.Webuj
import org.webuj.protocol.core.DefaultBlockParameter
import org.webuj.protocol.core.DefaultBlockParameterName.LATEST
import org.webuj.protocol.core.methods.request
import org.webuj.protocol.core.methods.request.ShhFilter
import org.webuj.protocol.core.methods.response.{HucBlock, HucCompileSolidity, HucGetWork, HucLog, ShhMessages, Transaction, TransactionReceipt}
import scala.collection.JavaConverters._
import scala.collection.immutable.List

/** All of the methods in this class block until a value is ready to be returned to the caller.
  * @param webuj can be shared with [[HappyUCASynchronous]] */
class HappyUCSynchronous(val webuj: Webuj) {

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_accounts huc_accounts]] JSON-RPC endpoint.
    * @return the list of addresses owned by the client */
  def accounts: List[Address] = webuj.hucAccounts.send.getAccounts.asScala.toList.map(Address)

  /** Add the given identity address to the Whisper group.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_addtogroup shh_addtogroup]] JSON-RPC endpoint.
    * @return true if the identity was successfully added to the group */
  def addToGroup(identityAddress: Address): Boolean = webuj.shhAddToGroup(identityAddress.value).send.addedToGroup

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getbalance huc_getbalance]] JSON-RPC endpoint.
    * @param defaultBlockParameter either an integer block number, or the string "latest", "earliest" or "pending".
    * See the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#the-default-block-parameter specification]].
    * @return the balance of the account at given address */
  def balance(address: Address, defaultBlockParameter: DefaultBlockParameter): Huc =
    Huc(webuj.hucGetBalance(address.value, defaultBlockParameter).send.getBalance)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getblockbyhash huc_getblockbyhash]] JSON-RPC endpoint.
    * @return Some(block object), or None if no block was found */
  def blockByHash(blockHash: BlockHash, returnFullTransactionObjects: Boolean): Option[HucBlock.Block] =
    Option(webuj.hucGetBlockByHash(blockHash.value, returnFullTransactionObjects).send.getBlock)

  def blockByNumber(
    defaultBlockParameter: DefaultBlockParameter,
    returnFullTransactionObjects: Boolean = false
  ): Option[HucBlock.Block] =
    Option(webuj.hucGetBlockByNumber(defaultBlockParameter, returnFullTransactionObjects).send.getBlock)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_blocknumber huc_blocknumber]] JSON-RPC endpoint.
    * @return the number of the most recent block */
  // todo define a type for BlockNumber?
  def blockNumber: BigInteger = webuj.hucBlockNumber.send.getBlockNumber

  def blockTransactionCountByHash(blockHash: BlockHash): BigInteger =
    webuj.hucGetBlockTransactionCountByHash(blockHash.value).send.getTransactionCount

  def blockTransactionCountByNumber(defaultBlockParameter: DefaultBlockParameter): BigInteger =
    webuj.hucGetBlockTransactionCountByNumber(defaultBlockParameter).send.getTransactionCount

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_call huc_call]] JSON-RPC endpoint.
    * @return value of executed contract, without creating a transaction on the block chain */
  def call(transaction: request.Transaction, defaultBlockParameter: DefaultBlockParameter): String =
    webuj.hucCall(transaction, defaultBlockParameter).send.getValue

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getcode huc_getcode]] JSON-RPC endpoint.
    * @return code at a given address */
  def code(address: Address, defaultBlockParameter: DefaultBlockParameter): String =
    webuj.hucGetCode(address.value, defaultBlockParameter).send.getCode

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_coinbase huc_coinbase]] JSON-RPC endpoint.
    * @return the client coinbase address */
  def coinbaseAddress: Address = Address(webuj.hucCoinbase.send.getAddress)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_compilelll huc_compilelll]] JSON-RPC endpoint.
    * @return compiled LLL code */
  def compileLLL(sourceCode: LLLSource): LLLCompiled =
    LLLCompiled(webuj.hucCompileLLL(sourceCode.value).send.getCompiledSourceCode)

  def compileSerpent(sourceCode: SerpentSource): SerpentCompiled =
    SerpentCompiled(webuj.hucCompileSerpent(sourceCode.value).send.getCompiledSourceCode)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_compilesolidity huc_compilesolidity]] JSON-RPC endpoint.
    * @return compiled Solidity code */
  def compileSolidity(sourceCode: SoliditySource): Map[String, HucCompileSolidity.Code] =
    webuj.hucCompileSolidity(sourceCode.value).send.getCompiledSolidity.asScala.toMap

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getcompilers huc_getcompilers]] JSON-RPC endpoint.
    * @return a list of available compilers found by the underlying WebuJ library */
  def compilers: List[Compiler] = webuj.hucGetCompilers.send.getCompilers.asScala.toList.map(Compiler)

  /** Makes a call or transaction, which won't be added to the blockchain and returns the used gas, which can be used
    * for estimating the used gas.
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_estimategas huc_estimategas]] JSON-RPC endpoint.
    * @return amount of gas estimated */
  def estimateGas(transaction: request.Transaction): Huc = Huc(webuj.hucEstimateGas(transaction).send.getAmountUsed)

  /** Polling method for an huc filter.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getfilterchanges huc_getfilterchanges]] JSON-RPC endpoint.
    * @return List of log items since last poll, could be an empty array if nothing has changed since last poll */
  def filterChangesHuc(filterId: FilterId): List[HucLog.LogResult[_]] =
    webuj.hucGetFilterChanges(filterId.value).send.getLogs.asScala.toList

  /** Polling method for a Whisper filter.
    *
    * Note: calling shh_getMessages will reset the buffer for this mhucod to avoid duplicate messages.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_getfilterchanges shh_getfilterchanges]] JSON-RPC endpoint.
    * @return List of messages since the last poll; could be Nil if nothing changed since the last poll */
  def filterChangesShh(filterId: FilterId): List[ShhMessages.SshMessage] =
    webuj.shhGetFilterChanges(filterId.value).send.getMessages.asScala.toList

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gasprice huc_gasprice]] JSON-RPC endpoint.
    * @return the current price per gas in wei */
  def gasPrice: Huc = Huc(webuj.hucGasPrice.send.getGasPrice)

  /** Query the hash rate.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_hashrate huc_hashrate]] JSON-RPC endpoint.
    * @return number of hashes per second that the node is mining at */
  def hashRate: BigInteger = webuj.hucHashrate.send.getHashrate

  /** Used for submitting mining hash rate
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_submithashrate huc_submithashrate]] JSON-RPC endpoint.
    * @return true if submitting successfully */
  def hashRate(hashRate: String, clientId: String): Boolean =
    webuj.hucSubmitHashrate(hashRate, clientId).send.submissionSuccessful

  /** Checks if the client hold the private keys for a given identity.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_hasidentity shh_hasidentity]] JSON-RPC endpoint.
    * @return returns true if this client holds the private key for that identity */
  def hasIdentity(identityAddress: Address): Boolean =
    webuj.shhHasIdentity(identityAddress.value).send.hasPrivateKeyForIdentity

  /** Retrieves binary data from the local database.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#db_ghucex db_ghucex]] JSON-RPC endpoint.
    * @return the retrieved value */
  @deprecated("deprecated", "")
  def hexFrom(databaseName: String, keyName: String): String =
    webuj.dbGetHex(databaseName, keyName).send.getStoredValue

  /** Stores binary data in the local database.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#db_puthex db_puthex]] JSON-RPC endpoint.
    * @return true if the value was stored */
  @deprecated("deprecated", "")
  def hexTo(databaseName: String, keyName: String, dataToStore: String): Boolean =
    webuj.dbPutHex(databaseName, keyName, dataToStore).send.valueStored

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#net_listening net_listening]] JSON-RPC endpoint.
    * @return true if this client is actively listening for network connections */
  def isListening: Boolean = webuj.netListening.send.isListening

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_mining huc_mining]] JSON-RPC endpoint. */
  def isMining: Boolean    = webuj.hucMining.send.isMining

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_syncing huc_syncing]] JSON-RPC endpoint. */
  def isSyncing: Boolean   = webuj.hucSyncing.send.isSyncing

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getlogs huc_getlogs]] JSON-RPC endpoint.
    * @return List of all log items matching a given filter object */
  def logs(hucFilter: request.HucFilter): List[HucLog.LogResult[_]] =
    webuj.hucGetLogs(hucFilter).send.getLogs.asScala.toList

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getfilterlogs huc_getfilterlogs]] JSON-RPC endpoint.
    * @return List of all log items with the matching filter id */
  def logs(filterId: FilterId): List[HucLog.LogResult[_]] =
    webuj.hucGetFilterLogs(filterId.value).send.getLogs.asScala.toList

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_getmessages shh_getmessages]] JSON-RPC endpoint.
    * @return all Whisper messages matching a filter */
  def messages(filterId: FilterId): List[ShhMessages.SshMessage] =
    webuj.shhGetMessages(filterId.value).send.getMessages.asScala.toList

  /** Creates a filter in the node, to notify when the state changes (logs).
    * To check if the state has changed, call `filterChanges`.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_newblockfilter huc_newblockfilter]] JSON-RPC endpoint.
    * @return filter id */
  def newBlockFilter: FilterId = FilterId(webuj.hucNewBlockFilter.send.getFilterId)

  /** Creates a filter object, based on filter options, to notify when the state changes (logs).
    * To check if the state has changed, call `filterChanges`.
    *
    * Topics are order-dependent.
    * A transaction with a log with topics [A, B] will be matched by the following topic filters:
    *
    * - [] "anything"
    * - [A] "A in first position (and anything after)"
    * - [null, B] "anything in first position AND B in second position (and anything after)"
    * - [A, B] "A in first position AND B in second position (and anything after)"
    * - [ [A, B], [A, B] ] "(A OR B) in first position AND (A OR B) in second position (and anything after)"
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_newfilter huc_newfilter]] JSON-RPC endpoint.
    * @return filter id */
  def newFilter(hucFilter: request.HucFilter): FilterId = FilterId(webuj.hucNewFilter(hucFilter).send.getFilterId)

  /** Create filter that notifies the client when whisper message is received that matches the filter options.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_newfilter shh_newfilter]] JSON-RPC endpoint.
    * @return The newly created filter as a BigInteger */
  def newFilter(shhFilter: ShhFilter): FilterId = FilterId(webuj.shhNewFilter(shhFilter).send.getFilterId)

  /** New Whisper group.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_newgroup shh_newgroup]] JSON-RPC endpoint.
    * @return address of the new group */
  def newGroup: Address = Address(webuj.shhNewGroup.send.getAddress)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_newidentity shh_newidentity]] JSON-RPC endpoint.
    * @return address of the new whisper identity */
  def newIdentity: Address = Address(webuj.shhNewIdentity.send.getAddress)

  def newPendingTransactionFilter: FilterId = FilterId(webuj.hucNewPendingTransactionFilter.send.getFilterId)

  /** Get the next available nonce before creating a transaction */
  def nextNonce(address: Address): Nonce = nonce(address, LATEST)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gettransactioncount huc_gettransactioncount]] JSON-RPC endpoint.
    * @see See [[https://github.com/happyuc-project/wiki/wiki/Glossary Glossary]]
    * @return the number of transactions sent from an address */
  def nonce(address: Address, defaultBlockParameter: DefaultBlockParameter): Nonce =
    Nonce(webuj.hucGetTransactionCount(address.value, defaultBlockParameter).send.getTransactionCount)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#net_peercount net_peercount]] JSON-RPC endpoint.
    * @return number of peers currently connected to this client */
  def peerCount: BigInteger = webuj.netPeerCount.send.getQuantity

  /** Sends a whisper message.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_post shh_post]] JSON-RPC endpoint.
    * @return true if the message was sent */
  def post(shhPost: request.ShhPost): Boolean = webuj.shhPost(shhPost).send.messageSent

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_sendrawtransaction huc_sendrawtransaction]] JSON-RPC endpoint.
    * @return new message call transaction or a contract creation for signed transactions */
  def sendRawTransaction(signedTransactionData: SignedData): TransactionHash=
    TransactionHash(webuj.hucSendRawTransaction(signedTransactionData.value).send.getTransactionHash)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_sendtransaction huc_sendtransaction]] JSON-RPC endpoint.
    * @return a new contract if the {{{Transaction.data}}} field contains code, else return a new transaction */
  def sendTransaction(transaction: request.Transaction): TransactionHash =
    TransactionHash(webuj.hucSendTransaction(transaction).send.getTransactionHash)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#webuj_sha3 webuj_sha3]] JSON-RPC endpoint.
    * @param data the data to convert into an SHA3 hash
    * @return Keccak-256 hash (not the standardized SHA3-256 hash) of the given data */
  def sha3(data: String): Keccak256Hash = Keccak256Hash(webuj.webujSha3(data).send.getResult)

  /** Calculates an HappyUC-specific signature with:
    * {{{sign(keccak256("\x19HappyUC Signed Message:\n" + len(message) + message)))}}}
    *
    * By adding a prefix to the message makes the calculated signature recognisable as an HappyUC-specific signature.
    * This prevents misuse where a malicious DApp can sign arbitrary data (e.g. transaction) and use the signature to impersonate the victim.
    *
    * Note: the address to sign with must be unlocked.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_sign huc_sign]] JSON-RPC endpoint.
    * @return Signature */
  def sign(address: Address, sha3HashOfDataToSign: String): Signature =
    Signature(webuj.hucSign(address.value, sha3HashOfDataToSign).send.getSignature)

  /** Obtains a string from the local database.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#db_getstring db_getstring]] JSON-RPC endpoint.
    * @return previously stored value */
  @deprecated("deprecated", "")
  def stringFrom(databaseName: String, keyName: String): String =
    webuj.dbGetString(databaseName, keyName).send.getStoredValue

  /** Stores a string in the local database
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#db_putstring db_putstring]] JSON-RPC endpoint.
    * @return true if the value was stored */
  @deprecated("deprecated", "")
  def stringTo(databaseName: String, keyName: String, stringToStore: String): Boolean =
    webuj.dbPutString(databaseName, keyName, stringToStore).send.valueStored

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getstorageat huc_getstorageat]] JSON-RPC endpoint.
    * @return the value from a storage position at a given address */
  def storageAt(address: Address, position: BigInteger, defaultBlockParameter: DefaultBlockParameter): String =
    webuj.hucGetStorageAt(address.value, position, defaultBlockParameter).send.getData

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gettransactionbyblockhashandindex huc_gettransactionbyblockhashandindex]] JSON-RPC endpoint.
    * @return Some containing transaction information by block hash and transaction index position, or None if no matching transaction was found */
  def transactionByBlockHashAndIndex(blockHash: BlockHash, transactionIndex: BigInteger): Optional[Transaction] =
    webuj.hucGetTransactionByBlockHashAndIndex(blockHash.value, transactionIndex).send.getTransaction

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gettransactionbyblocknumberandindex huc_gettransactionbyblocknumberandindex]] JSON-RPC endpoint.
    * @return Some containing transaction information by block hash and transaction index position, or None if no matching transaction was found */
  def transactionByBlockNumberAndIndex(
    defaultBlockParameter: DefaultBlockParameter,
    transactionIndex: BigInteger
  ): Optional[Transaction] =
    webuj.hucGetTransactionByBlockNumberAndIndex(defaultBlockParameter, transactionIndex).send.getTransaction

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gettransactionbyhash huc_gettransactionbyhash]] JSON-RPC endpoint.
    * @return Future containing Some(transaction object), or None when no transaction was found */
  def transactionByHash(transactionHash: TransactionHash): Optional[Transaction] =
    webuj.hucGetTransactionByHash(transactionHash.value).send.getTransaction

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gettransactionreceipt huc_gettransactionreceipt]] JSON-RPC endpoint.
    * @return the receipt of a transaction, identified by transaction hash. (Note: receipts are not available for pending transactions.) */
  def transactionReceipt(transactionHash: TransactionHash): Optional[TransactionReceipt] =
    webuj.hucGetTransactionReceipt(transactionHash.value).send.getTransactionReceipt

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getunclebyblocknumberandindex huc_getunclebyblocknumberandindex]] JSON-RPC endpoint.
    * @return information about a uncle of a block by hash and uncle index position */
  def uncleByBlockNumberAndIndex(defaultBlockParameter: DefaultBlockParameter, transactionIndex: BigInteger): HucBlock.Block =
    webuj.hucGetUncleByBlockNumberAndIndex(defaultBlockParameter, transactionIndex).send.getBlock

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getunclebyblockhashandindex huc_getunclebyblockhashandindex]] JSON-RPC endpoint.
    * @return information about a uncle of a block by hash and uncle index position */
  def uncleByBlockHashAndIndex(blockHash: BlockHash, transactionIndex: BigInteger): HucBlock.Block =
    webuj.hucGetUncleByBlockHashAndIndex(blockHash.value, transactionIndex).send.getBlock

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getunclecountbyblockhash huc_getunclecountbyblockhash]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block hash */
  def uncleCountByBlockHash(blockHash: BlockHash): BigInteger =
    webuj.hucGetUncleCountByBlockHash(blockHash.value).send.getUncleCount

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getunclecountbyblocknumber huc_getunclecountbyblocknumber]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block number */
  def uncleCountByBlockNumber(defaultBlockParameter: DefaultBlockParameter): BigInteger =
    webuj.hucGetUncleCountByBlockNumber(defaultBlockParameter).send.getUncleCount

  /** Uninstalls a filter with given id.
    * Should always be called when watch is no longer needed.
    *
    * Note: Filters time out when they aren't requested with filterChanges for a period of time.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_uninstallfilter huc_uninstallfilter]] JSON-RPC endpoint.
    * @return true if the filter was successfully uninstalled */
  def uninstallFilter(filterId: FilterId): Boolean =
    webuj.hucUninstallFilter(filterId.value).send.isUninstalled

  /** Uninstalls a Whisper filter with the given id.
    * Should always be called when watch is no longer needed.
    *
    * Note: Filters time out when they aren't requested with filterChanges for a period of time.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_uninstallfilter shh_uninstallfilter]] JSON-RPC endpoint.
    * @return true if the filter was successfully uninstalled */
  def uninstallShhFilter(filterId: FilterId): Boolean =
    webuj.shhUninstallFilter(filterId.value).send.isUninstalled

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#webuj_clientversion webuj_clientversion]] JSON-RPC endpoint.
    * @return the WebuJ client version used by this client */
  def versionWebuJ: String = webuj.webujClientVersion.send.getWebuClientVersion

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#net_version net_version]] JSON-RPC endpoint.
    * @return the current network id */
  def versionNet: String = webuj.netVersion.send.getNetVersion

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_protocolversion huc_protocolversion]] JSON-RPC endpoint.
    * @return happyuc protocol version used by this client */
  def versionProtocol: String = webuj.hucProtocolVersion.send.getProtocolVersion

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_version shh_version]] JSON-RPC endpoint.
    * @return the current whisper protocol version. */
  def versionShh: String = webuj.shhVersion.send.getVersion

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getwork huc_getwork]] JSON-RPC endpoint.
    * @return the hash of the current block, the seedHash, and the boundary condition to be met ("target").
    * The Array with the following properties:
    *
    * DATA, 32 Bytes - current block header pow-hash
    * DATA, 32 Bytes - the seed hash used for the DAG.
    * DATA, 32 Bytes - the boundary condition ("target"), 2^^256 / difficulty. */
  def work: HucGetWork = webuj.hucGetWork.send

  /** Used for submitting a proof-of-work solution.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_submitwork huc_submitwork]] JSON-RPC endpoint.
    * @return true if the provided solution is valid */
  // todo what type of Hash should headerPowHash be?
  def work(nonce: Nonce, headerPowHash: Keccak256Hash, mixDigest: Digest): Boolean =
    webuj.hucSubmitWork(nonce.toString, headerPowHash.value, mixDigest.value).send.solutionValid
}
