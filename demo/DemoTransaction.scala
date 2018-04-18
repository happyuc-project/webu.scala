package demo

import java.math.BigInteger
import com.micronautics.webuj.{Address, Nonce}
import org.webuj.crypto.{Credentials, RawTransaction, TransactionEncoder, WalletUtils}
import org.webuj.protocol.admin.Admin
import org.webuj.protocol.admin.methods.response.PersonalUnlockAccount
import org.webuj.protocol.core.DefaultBlockParameterName._
import org.webuj.protocol.core.methods.response.TransactionReceipt
import org.webuj.protocol.http.HttpService
import org.webuj.tx.Transfer
import org.webuj.utils.Convert.Unit.{HUC, WEI}

/** WebuJ provides support for both working with HappyUC wallet files (recommended) and HappyUC client admin commands
  * for sending transactions. */
class DemoTransaction(demo: Demo) {
  import Demo._, demo._

  //  Send Huc to another party using your HappyUC wallet file
  val credentials: Credentials = WalletUtils.loadCredentials("password", walletDir)
  val transactionReceipt: TransactionReceipt =
    Transfer.sendFunds(webuj, credentials, "0x...", BigDecimal.valueOf(0.01).bigDecimal, HUC).send
  println(format(transactionReceipt))

  // Before creating a custom transaction, first get the next available nonce
  val nonce: Nonce = demo.webujScala.sync.nextNonce(Address("address"))

  // Create a custom transaction
  val rawTransaction: RawTransaction =
    RawTransaction.createHucTransaction(nonce.bigInteger, gasPrice.bigInteger, gasLimit, "toAddress", BigInt(1).bigInteger)
  println(format(rawTransaction))

  // Sign & send the transaction
  val signedMessage: Array[Byte] = TransactionEncoder.signMessage(rawTransaction, credentials)
  val hexValue: String = javax.xml.bind.DatatypeConverter.printHexBinary(signedMessage)
  val transactionHash: String = webuj.hucSendRawTransaction(hexValue).send.getTransactionHash

  // Now let's transfer some funds. Be sure a wallet is available in the client’s keystore. TODO how?
  // One option is to use webuj’s `Transfer` class for transacting with Huc.
  Transfer.sendFunds(webuj, credentials, "toAddress", BigDecimal(1).bigDecimal, WEI)

  // Here is how to use the HappyUC client’s admin commands:
  val webujAdmin: Admin = Admin.build(new HttpService)
  val personalUnlockAccount: PersonalUnlockAccount = webujAdmin.personalUnlockAccount("0x000...", "a password").sendAsync.get
  if (personalUnlockAccount.accountUnlocked) {
       // todo send a transaction
  }

  // Todo demonstrate the use of Parity’s Personal, Trace, or ghuc’s personal client APIs, by using the org.webuj:parity and org.webuj:ghuc modules respectively.

  protected def format(tx: RawTransaction): String =
    s"""Raw transaction:
       |  Data         = ${ tx.getData }
       |  Gas limit    = ${ tx.getGasLimit }
       |  Gas price    = ${ tx.getGasPrice }
       |  Gas limit    = ${ tx.getGasLimit }
       |  Nonce        = ${ tx.getNonce }
       |  To           = ${ tx.getTo }
       |  Value        = ${ tx.getValue }
       |""".stripMargin

  protected def format(tx: TransactionReceipt): String =
    s"""Transaction receipt:
       |  Block hash              = ${ tx.getBlockHash }
       |  Block number            = ${ tx.getBlockNumber }
       |  Raw block number        = ${ tx.getBlockNumberRaw }
       |  Contract address        = ${ tx.getContractAddress }
       |  Cumulative gas used     = ${ tx.getCumulativeGasUsed }
       |  Raw cumulative gas used = ${ tx.getCumulativeGasUsedRaw }
       |  From                    = ${ tx.getFrom }
       |  Gas used                = ${ tx.getGasUsed }
       |  Raw gas used            = ${ tx.getGasUsedRaw }
       |  Logs                    = ${ tx.getLogs }
       |  Log bloom               = ${ tx.getLogsBloom }
       |  Root                    = ${ tx.getRoot }
       |  To                      = ${ tx.getTo }
       |  Transaction hash        = ${ tx.getTransactionHash }
       |  Transaction index       = ${ tx.getTransactionIndex }
       |  Raw transaction index   = ${ tx.getTransactionIndexRaw }
       |""".stripMargin
}
