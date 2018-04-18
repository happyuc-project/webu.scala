package com.micronautics.webuj

import java.math.BigInteger
import java.util.Optional
import org.webuj.protocol.Webuj
import org.webuj.protocol.core.DefaultBlockParameter
import org.webuj.protocol.core.DefaultBlockParameterName.LATEST
import org.webuj.protocol.core.methods.request
import org.webuj.protocol.core.methods.response.{HucBlock, HucCompileSolidity, HucGetWork, HucLog, ShhMessages, Transaction, TransactionReceipt}
import scala.collection.JavaConverters._
import scala.collection.immutable.List
import scala.compat.java8.FutureConverters._
import scala.concurrent.ExecutionContext.{global => defaultExecutionContext}
import scala.concurrent.{ExecutionContext, Future}

/** All of the mhucods in this class return a [[scala.concurrent.Future]] and do not block.
  * @param webuj can be shared with [[HappyUCSynchronous]]
  * @param ec if no [[scala.concurrent.ExecutionContext]] is implicitly available, the default Scala
  *           [[scala.concurrent.ExecutionContext]] is used. */
class HappyUCASynchronous(val webuj: Webuj)
                          (implicit val ec: ExecutionContext = defaultExecutionContext) {

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_accounts huc_accounts]] JSON-RPC endpoint.
    * @return the list of addresses owned by the client */
  def accounts: Future[List[Address]] =
    webuj.hucAccounts.sendAsync.toScala.map(_.getAccounts.asScala.toList.map(Address))

  /** Add the given identity address to the Whisper group.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_addtogroup shh_addtogroup]] JSON-RPC endpoint.
    * @return true if the identity was successfully added to the group */
  def addToGroup(identityAddress: Address): Future[Boolean] =
    webuj.shhAddToGroup(identityAddress.value).sendAsync.toScala.map(_.addedToGroup)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getbalance huc_getbalance]] JSON-RPC endpoint.
    * @param defaultBlockParameter either an integer block number, or the string "latest", "earliest" or "pending".
    * See the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#the-default-block-parameter specification]].
    * @return the balance of the account of given address */
  def balance(address: Address, defaultBlockParameter: DefaultBlockParameter): Future[Huc] =
    webuj.hucGetBalance(address.value, defaultBlockParameter).sendAsync.toScala.map(x => Huc(x.getBalance))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getblockbyhash huc_getblockbyhash]] JSON-RPC endpoint.
    * @return Option[HucBlock.Block] */
  def blockByHash(blockHash: BlockHash, returnFullTransactionObjects: Boolean): Future[Option[HucBlock.Block]] =
    webuj.hucGetBlockByHash(blockHash.value, returnFullTransactionObjects).sendAsync.toScala.map(x => Option(x.getBlock))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getblockbyhash huc_getblockbyhash]] JSON-RPC endpoint.
    * @return Some(block object), or None if no block was found */
  def blockByNumber(
    defaultBlockParameter: DefaultBlockParameter,
    returnFullTransactionObjects: Boolean = false
  ): Future[Option[HucBlock.Block]] =
    webuj
      .hucGetBlockByNumber(defaultBlockParameter, returnFullTransactionObjects)
      .sendAsync
      .toScala
      .map(x => Option(x.getBlock))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_blocknumber huc_blocknumber]] JSON-RPC endpoint.
    * @return the number of the most recent block */
  def blockNumber: Future[BigInteger] = webuj.hucBlockNumber.sendAsync.toScala.map(_.getBlockNumber)

  def blockTransactionCountByHash(blockHash: String): Future[BigInteger] =
    webuj.hucGetBlockTransactionCountByHash(blockHash).sendAsync.toScala.map(_.getTransactionCount)

  def blockTransactionCountByNumber(defaultBlockParameter: DefaultBlockParameter): Future[BigInteger] =
    webuj.hucGetBlockTransactionCountByNumber(defaultBlockParameter).sendAsync.toScala.map(_.getTransactionCount)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_call huc_call]] JSON-RPC endpoint.
    * @return value of executed contract, without creating a transaction on the block chain */
  def call(transaction: request.Transaction, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    webuj.hucCall(transaction, defaultBlockParameter).sendAsync.toScala.map(_.getValue)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getcode huc_getcode]] JSON-RPC endpoint.
    * @return code at a given address */
  def code(address: Address, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    webuj.hucGetCode(address.value, defaultBlockParameter).sendAsync.toScala.map(_.getCode)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_coinbase huc_coinbase]] JSON-RPC endpoint.
    * @return the client coinbase address */
  def coinbaseAddress: Future[Address] = webuj.hucCoinbase.sendAsync.toScala.map(x => Address(x.getAddress))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_compilelll huc_compilelll]] JSON-RPC endpoint.
    * @return compiled LLL code */
  def compileLLL(sourceCode: LLLSource): Future[LLLCompiled] =
    webuj.hucCompileLLL(sourceCode.value).sendAsync.toScala.map(x => LLLCompiled(x.getCompiledSourceCode))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_compileserpent huc_compileserpent]] JSON-RPC endpoint.
    * @return compiled Serpent code */
  def compileSerpent(sourceCode: SerpentSource): Future[SerpentCompiled] =
    webuj.hucCompileSerpent(sourceCode.value).sendAsync.toScala.map(x => SerpentCompiled(x.getCompiledSourceCode))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_compilesolidity huc_compilesolidity]] JSON-RPC endpoint.
    * @return compiled Solidity code */
  def compileSolidity(sourceCode: SoliditySource): Future[Map[String, HucCompileSolidity.Code]] =
    webuj.hucCompileSolidity(sourceCode.value).sendAsync.toScala.map(_.getCompiledSolidity.asScala.toMap)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getcompilers huc_getcompilers]] JSON-RPC endpoint.
    * @return a list of available compilers found by the underlying WebuJ library */
  def compilers: Future[List[Compiler]] =
    webuj.hucGetCompilers.sendAsync.toScala.map(_.getCompilers.asScala.toList.map(Compiler))

  /** Makes a call or transaction, which won't be added to the blockchain and returns the used gas, which can be used
    * for estimating the used gas.
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_estimategas huc_estimategas]] JSON-RPC endpoint.
    * @return amount of gas estimated */
  def estimateGas(transaction: request.Transaction): Future[Huc] =
    webuj.hucEstimateGas(transaction).sendAsync.toScala.map(x => Huc(x.getAmountUsed))

  /** Polling mhucod for an huc filter.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getfilterchanges huc_getfilterchanges]] JSON-RPC endpoint.
    * @return List of log items since last poll, could be Nil if nothing changed since the last poll */
  def filterChangesHuc(filterId: FilterId): Future[List[HucLog.LogResult[_]]] =
    webuj.hucGetFilterChanges(filterId.value).sendAsync.toScala.map(_.getLogs.asScala.toList)

  /** Polling mhucod for a Whisper filter.
    *
    * Note: calling shh_getMessages will reset the buffer for this mhucod to avoid duplicate messages.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_getfilterchanges shh_getfilterchanges]] JSON-RPC endpoint.
    * @return List of messages since the last poll; could be Nil if nothing changed since the last poll */
  def filterChangesShh(filterId: FilterId): Future[List[ShhMessages.SshMessage]] =
    webuj.shhGetFilterChanges(filterId.value).sendAsync.toScala.map(_.getMessages.asScala.toList)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gasprice huc_gasprice]] JSON-RPC endpoint.
    * @return the current price per gas in wei */
  def gasPrice: Future[Huc] = webuj.hucGasPrice.sendAsync.toScala.map(x => Huc(x.getGasPrice))

  /** Used for submitting mining hash rate
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_submithashrate huc_submithashrate]] JSON-RPC endpoint.
    * @return true if submitting successfully */
  def hashRate(hashRate: String, clientId: String): Future[Boolean] =
    webuj.hucSubmitHashrate(hashRate, clientId).sendAsync.toScala.map(_.submissionSuccessful)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_hashrate huc_hashrate]] JSON-RPC endpoint.
    * @return the number of hashes per second that the node is mining at */
  def hashRate: Future[BigInteger] = webuj.hucHashrate.sendAsync.toScala.map(_.getHashrate)

  /** Checks if the client hold the private keys for a given identity.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_hasidentity shh_hasidentity]] JSON-RPC endpoint.
    * @return returns true if this client holds the private key for that identity */
  def hasIdentity(identityAddress: Address): Future[Boolean] =
    webuj.shhHasIdentity(identityAddress.value).sendAsync.toScala.map(_.hasPrivateKeyForIdentity)

  /** Retrieves binary data from the local database.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#db_ghucex db_ghucex]] JSON-RPC endpoint.
    * @return the retrieved value */
  @deprecated("deprecated", "")
  def hexFrom(databaseName: String, keyName: String): Future[String] =
    webuj.dbGetHex(databaseName, keyName).sendAsync.toScala.map(_.getStoredValue)

  /** Stores binary data in the local database.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#db_puthex db_puthex]] JSON-RPC endpoint.
    * @return true if the value was stored */
  @deprecated("deprecated", "")
  def hexTo(databaseName: String, keyName: String, dataToStore: String): Future[Boolean] =
    webuj.dbPutHex(databaseName, keyName, dataToStore).sendAsync.toScala.map(_.valueStored)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#net_listening net_listening]] JSON-RPC endpoint.
    * @return true if this client is actively listening for network connections */
  def isListening: Future[Boolean] = webuj.netListening.sendAsync.toScala.map(_.isListening)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_mining huc_mining]] JSON-RPC endpoint. */
  def isMining: Future[Boolean] = webuj.hucMining.sendAsync.toScala.map(_.isMining)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_syncing huc_syncing]] JSON-RPC endpoint. */
  def isSyncing: Future[Boolean] = webuj.hucSyncing.sendAsync.toScala.map(_.isSyncing)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getlogs huc_getlogs]] JSON-RPC endpoint.
    * @return List of all log items matching a given filter object */
  def logs(hucFilter: request.HucFilter): Future[List[HucLog.LogResult[_]]] =
    webuj.hucGetLogs(hucFilter).sendAsync.toScala.map(_.getLogs.asScala.toList)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getfilterlogs huc_getfilterlogs]] JSON-RPC endpoint.
    * @return List of all log items with the matching filter id */
  def logs(filterId: FilterId): Future[List[HucLog.LogResult[_]]] =
    webuj.hucGetFilterLogs(filterId.value).sendAsync.toScala.map(_.getLogs.asScala.toList)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_getmessages shh_getmessages]] JSON-RPC endpoint.
    * @return all Whisper messages matching a filter */
  def messages(filterId: FilterId): Future[List[ShhMessages.SshMessage]] =
    webuj.shhGetMessages(filterId.value).sendAsync.toScala.map(_.getMessages.asScala.toList)

  /** Creates a filter in the node, to notify when the state changes (logs).
    * To check if the state has changed, call `filterChanges`.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_newblockfilter huc_newblockfilter]] JSON-RPC endpoint.
    * @return filter id */
  def newBlockFilter: Future[FilterId] = webuj.hucNewBlockFilter.sendAsync.toScala.map(x => FilterId(x.getFilterId))

  /** Get the next available nonce before creating a transaction */
  // todo should a Nonce type be created?
  def nextNonce(address: Address): Future[Nonce] = nonce(address, LATEST)

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
  def newFilter(hucFilter: request.HucFilter): Future[FilterId] =
    webuj.hucNewFilter(hucFilter).sendAsync.toScala.map(x => FilterId(x.getFilterId))

  /** Create filter that notifies the client when whisper message is received that matches the filter options.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_newfilter shh_newfilter]] JSON-RPC endpoint.
    * @return The newly created filter as a BigInteger */
  def newFilter(shhFilter: request.ShhFilter): Future[FilterId] =
    webuj.shhNewFilter(shhFilter).sendAsync.toScala.map(x => FilterId(x.getFilterId))

  /** New Whisper group.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_newgroup shh_newgroup]] JSON-RPC endpoint.
    * @return address of the new group */
  def newGroup: Future[Address] = webuj.shhNewGroup.sendAsync.toScala.map(x =>  Address(x.getAddress))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_newidentity shh_newidentity]] JSON-RPC endpoint.
    * @return address of the new whisper identity */
  def newIdentity: Future[Address] = webuj.shhNewIdentity.sendAsync.toScala.map(x => Address(x.getAddress))

  def newPendingTransactionFilter: Future[FilterId] =
    webuj.hucNewPendingTransactionFilter.sendAsync.toScala.map(x => FilterId(x.getFilterId))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gettransactioncount huc_gettransactioncount]] JSON-RPC endpoint.
    * @see See [[https://github.com/happyuc-project/wiki/wiki/Glossary Glossary]]
    * @return the number of transactions sent from an address */
  def nonce(address: Address, defaultBlockParameter: DefaultBlockParameter): Future[Nonce] =
    webuj.hucGetTransactionCount(address.value, defaultBlockParameter).sendAsync.toScala.map(x => Nonce(x.getTransactionCount))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#net_peercount net_peercount]] JSON-RPC endpoint.
    * @return number of peers currently connected to this client */
  def peerCount: Future[BigInteger] = webuj.netPeerCount.sendAsync.toScala.map(_.getQuantity)

  /** Sends a whisper message.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_post shh_post]] JSON-RPC endpoint.
    * @return true if the message was sent */
  def post(shhPost: request.ShhPost): Future[Boolean] = webuj.shhPost(shhPost).sendAsync.toScala.map(_.messageSent)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_sendrawtransaction huc_sendrawtransaction]] JSON-RPC endpoint.
    * @return new message call transaction or a contract creation for signed transactions */
  def sendRawTransaction(signedTransactionData: SignedData): Future[TransactionHash] =
    webuj.hucSendRawTransaction(signedTransactionData.value).sendAsync.toScala.map(x => TransactionHash(x.getTransactionHash))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_sendtransaction huc_sendtransaction]] JSON-RPC endpoint.
    * @return a new contract if the {{{Transaction.data}}} field contains code, else return a new transaction */
  def sendTransaction(transaction: request.Transaction): Future[TransactionHash] =
    webuj.hucSendTransaction(transaction).sendAsync.toScala.map(x => TransactionHash(x.getTransactionHash))

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#webuj_sha3 webuj_sha3]] JSON-RPC endpoint.
    * @param data the data to convert into an SHA3 hash
    * @return Keccak-256 hash (not the standardized SHA3-256 hash) of the given data */
  def sha3(data: String): Future[Keccak256Hash] = webuj.webujSha3(data).sendAsync.toScala.map(x => Keccak256Hash(x.getResult))

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
  def sign(address: Address, sha3HashOfDataToSign: Keccak256Hash): Future[Signature] =
    webuj.hucSign(address.value, sha3HashOfDataToSign.value).sendAsync.toScala.map(x => Signature(x.getSignature))

  /** Obtains a string from the local database.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#db_getstring db_getstring]] JSON-RPC endpoint.
    * @return previously stored value */
  @deprecated("deprecated", "")
  def stringFrom(databaseName: String, keyName: String): Future[String] =
    webuj.dbGetString(databaseName, keyName).sendAsync.toScala.map(_.getStoredValue)

  /** Stores a string in the local database
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#db_putstring db_putstring]] JSON-RPC endpoint.
    * @return true if the value was stored */
  @deprecated("deprecated", "")
  def stringTo(databaseName: String, keyName: String, stringToStore: String): Future[Boolean] =
    webuj.dbPutString(databaseName, keyName, stringToStore).sendAsync.toScala.map(_.valueStored)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getstorageat huc_getstorageat]] JSON-RPC endpoint.
    * @return the value from a storage position at a given address */
  def storageAt(address: Address, position: BigInteger, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    webuj.hucGetStorageAt(address.value, position, defaultBlockParameter).sendAsync.toScala.map(_.getData)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gettransactionbyblockhashandindex huc_gettransactionbyblockhashandindex]] JSON-RPC endpoint.
    * @return Some containing transaction information by block hash and transaction index position, or None if no matching transaction was found */
  def transactionByBlockHashAndIndex(blockHash: BlockHash, transactionIndex: BigInteger): Future[Optional[Transaction]] =
    webuj.hucGetTransactionByBlockHashAndIndex(blockHash.value, transactionIndex).sendAsync.toScala.map(_.getTransaction)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gettransactionbyblocknumberandindex huc_gettransactionbyblocknumberandindex]] JSON-RPC endpoint.
    * @return Some containing transaction information by block hash and transaction index position, or None if no matching transaction was found */
  def transactionByBlockNumberAndIndex(
    defaultBlockParameter: DefaultBlockParameter,
    transactionIndex: BigInteger
  ): Future[Optional[Transaction]] =
    webuj
      .hucGetTransactionByBlockNumberAndIndex(defaultBlockParameter, transactionIndex)
      .sendAsync
      .toScala
      .map(_.getTransaction)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gettransactionbyhash huc_gettransactionbyhash]] JSON-RPC endpoint.
    * @return Future containing Some(transaction object), or None when no transaction was found */
  def transactionByHash(transactionHash: TransactionHash): Future[Optional[Transaction]] =
    webuj.hucGetTransactionByHash(transactionHash.value).sendAsync.toScala.map(_.getTransaction)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_gettransactionreceipt huc_gettransactionreceipt]] JSON-RPC endpoint.
    * @return the receipt of a transaction, identified by transaction hash. (Note: receipts are not available for pending transactions.) */
  def transactionReceipt(transactionHash: TransactionHash): Future[Optional[TransactionReceipt]] =
    webuj.hucGetTransactionReceipt(transactionHash.value).sendAsync.toScala.map(_.getTransactionReceipt)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getunclecountbyblockhash huc_getunclecountbyblockhash]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block hash */
  def uncleCountByBlockHash(blockHash: BlockHash): Future[BigInteger] =
    webuj.hucGetUncleCountByBlockHash(blockHash.value).sendAsync.toScala.map(_.getUncleCount)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getunclecountbyblocknumber huc_getunclecountbyblocknumber]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block number */
  def uncleCountByBlockNumber(defaultBlockParameter: DefaultBlockParameter): Future[BigInteger] =
    webuj.hucGetUncleCountByBlockNumber(defaultBlockParameter).sendAsync.toScala.map(_.getUncleCount)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getunclebyblocknumberandindex huc_getunclebyblocknumberandindex]] JSON-RPC endpoint.
    * @return information about a uncle of a block by hash and uncle index position */
  def uncleByBlockNumberAndIndex(
    defaultBlockParameter: DefaultBlockParameter,
    transactionIndex: BigInteger
  ): Future[HucBlock.Block] =
    webuj.hucGetUncleByBlockNumberAndIndex(defaultBlockParameter, transactionIndex).sendAsync.toScala.map(_.getBlock)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getunclebyblockhashandindex huc_getunclebyblockhashandindex]] JSON-RPC endpoint.
    * @return information about a uncle of a block by hash and uncle index position */
  def uncleByBlockHashAndIndex(blockHash: BlockHash, transactionIndex: BigInteger): Future[HucBlock.Block] =
    webuj.hucGetUncleByBlockHashAndIndex(blockHash.value, transactionIndex).sendAsync.toScala.map(_.getBlock)

  /** Uninstalls a filter with the given id.
    * Should always be called when watch is no longer needed.
    *
    * Note: Filters time out when they aren't requested with filterChanges for a period of time.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_uninstallfilter huc_uninstallfilter]] JSON-RPC endpoint.
    * @return true if the filter was successfully uninstalled */
  def uninstallFilter(filterId: FilterId): Future[Boolean] =
    webuj.hucUninstallFilter(filterId.value).sendAsync.toScala.map(_.isUninstalled)

  /** Uninstalls a Whisper filter with the given id.
    * Should always be called when watch is no longer needed.
    *
    * Note: Filters time out when they aren't requested with filterChanges for a period of time.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_uninstallfilter shh_uninstallfilter]] JSON-RPC endpoint.
    * @return true if the filter was successfully uninstalled */
  def uninstallShhFilter(filterId: FilterId): Future[Boolean] =
    webuj.shhUninstallFilter(filterId.value).sendAsync.toScala.map(_.isUninstalled)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#webuj_clientversion webuj_clientversion]] JSON-RPC endpoint.
    * @return the WebuJ client version used by this client */
  def versionWebuJ: Future[String] = webuj.webujClientVersion.sendAsync.toScala.map(_.getWebuClientVersion)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#net_version net_version]] JSON-RPC endpoint.
    * @return the current network id */
  def versionNet: Future[String] = webuj.netVersion.sendAsync.toScala.map(_.getNetVersion)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_protocolversion huc_protocolversion]] JSON-RPC endpoint.
    * @return happyuc protocol version used by this client */
  def versionProtocol: Future[String] = webuj.hucProtocolVersion.sendAsync.toScala.map(_.getProtocolVersion)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#shh_version shh_version]] JSON-RPC endpoint.
    * @return the current whisper protocol version. */
  def versionShh: Future[String] = webuj.shhVersion.sendAsync.toScala.map(_.getVersion)

  /** Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getwork huc_getwork]] JSON-RPC endpoint.
    * @return the hash of the current block, the seedHash, and the boundary condition to be met ("target").
    * The Array with the following properties:
    *
    * DATA, 32 Bytes - current block header pow-hash
    * DATA, 32 Bytes - the seed hash used for the DAG.
    * DATA, 32 Bytes - the boundary condition ("target"), 2^^256 / difficulty. */
  def work: Future[HucGetWork] = webuj.hucGetWork.sendAsync.toScala

  /** Used for submitting a proof-of-work solution.
    *
    * Invokes the [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_submitwork huc_submitwork]] JSON-RPC endpoint.
    * @return true if the provided solution is valid */
  // fixme What type of hash is headerPowHash?
  def work(nonce: Nonce, headerPowHash: Keccak256Hash, mixDigest: Digest): Future[Boolean] =
    webuj.hucSubmitWork(nonce.toString, headerPowHash.value, mixDigest.value).sendAsync.toScala.map(_.solutionValid)
}
