package demo

import org.webuj.crypto.{Credentials, WalletUtils}
import org.webuj.protocol.core.RemoteCall

object CreateSmartContracts extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  new DemoSmartContracts(new Demo)
}

/** WebuJ can auto-generate smart contract wrapper code to deploy and interact with smart contracts without leaving the JVM.
  * This program creates a smart contract wrapper around a [[../src/test/resources/basic_info_getter.sol sample smart contract]]
  * and demonstrates how to work with smart contracts using JVM wrappers.
  *
  * Run this program before [[Main running the demo]].
  *
  * @see See [[https://webuj.readthedocs.io/en/latest/smart_contracts.html WebuJ Smart Contracts]] */
class DemoSmartContracts(demo: Demo) {
  import com.micronautics.webuj.WebuJScala.{solc,wrapAbi}
  import com.micronautics.solidity._
  import Demo._

  // Compile the smart contract before generating the wrapper code
  println(solc("src/test/resources/basic_info_getter.sol"))
  println(wrapAbi("basic_info_getter"))

  try {
    val credentials: Credentials = WalletUtils.loadCredentials("password", walletDir)

    val basicInfoGetter: RemoteCall[BasicInfoGetter] = BasicInfoGetter.deploy(demo.webuj, credentials, gasPrice.bigInteger, gasLimit)
    val x: BasicInfoGetter = basicInfoGetter.send
    println(x)

    // Or use an existing contract:
    // val contract2 = YourSmartContract.load( "0x<address>", webuj, credentials, gasPrice, gasLimit)

    // Transact with a smart contract
//    val result = basicInfoGetter.send().getContractAddress.send()
//    println(s"basicInfoGetter.getContractAddress = $result")
  } catch {
    case e: Throwable =>
      System.err.println(e.getMessage)
  }
}
