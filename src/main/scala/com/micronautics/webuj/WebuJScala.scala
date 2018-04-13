package com.micronautics.webuj

import com.micronautics.webuj.InfuraNetwork._
import org.webuj.protocol.Webuj
import org.webuj.protocol.http.HttpService
import org.webuj.protocol.infura.InfuraHttpService
import rx.Observable
import scala.concurrent.ExecutionContext

/** [[https://www.webuj.io WebuJ]] builders and stateless methods. */
object WebuJScala {
  lazy val cmd: Cmd = new Cmd

  /** @see See [[https://docs.webuj.io/management_apis.html?highlight=httpservice Management APIs]] */
  def fromHttp(url: String = "http://localhost:8545"): Webuj = {
    val webuj = Webuj.build(new HttpService(url))
    verifyConnection(webuj)
    webuj
  }

  /** @see See [[http://www.ziggify.com/blog/blockchain-stack-1-installing-happyuc-ghuc-smart-contract/ Installing HappyUC Ghuc and your first smart contract]]
    * @see See the [[http://faucet.ropsten.be:3001/ HappyUC Ropsten Faucet]]
    * @see See [[https://docs.webuj.io/infura.html Using Infura with webuj]]
    * @param network defaults to the Ropsten test network */
  def fromInfura(token: String, network: InfuraNetwork = ROPSTEN): Webuj = {
    val webuj = Webuj.build(new InfuraHttpService(s"https://${ network.name }.infura.io/$token"))
    verifyConnection(webuj)
    webuj
  }

  /** Invokes fn on all elements observed from the given Observable[T] */
  def observe[T](observable: Observable[T])
                (fn: T => Unit): Unit =
    observable.subscribe(fn(_))

  /** Only runs fn on the first n elements observed from the given Observable[T] */
  def observe[T](n: Int)
                (observable: Observable[T])
                (fn: T => Unit): Unit =
    observable.limit(n).doOnEach { t =>
      fn(t.getValue.asInstanceOf[T])
    }

  /** Compile the smart contract.
    * Note that `solc` generates [[https://en.wikipedia.org/wiki/Camel_case camelCase]] names from
    * [[https://en.wikipedia.org/wiki/Snake_case snake_case]] names.
    * {{{solc --bin --abi --optimize --overwrite -o abi/ src/test/resources/basic_info_getter.sol}}} */
  def solc(solCFileName: String, outputDirectory: String="abi/"): String = cmd.getOutputFrom(
    "solc",
      "--bin",
      "--abi",
      "--optimize",
      "--overwrite",
      "-o", outputDirectory,
      solCFileName
  )

  /** Verify webuj is connected to a JSON-RPC endpoint */
  def verifyConnection(webuj: Webuj): Boolean = try {
      webuj.webujClientVersion.send.getWebuClientVersion
      true
    } catch {
      case e: Exception =>
        println(s"${ e.getMessage }. Is ghuc or huc running?")
        System.exit(0)
        false
    }

  /** Generate the wrapper code from the compiled smart contract using webuj’s command-line tools
    * The `bin` and `abi` files are both read from the same directory, specified by `-o`.
    * {{{bin/webuj solidity generate abi/basic_info_getter.bin abi/basic_info_getter.abi -o abiWrapper/ -p com.micronautics.solidity}}} */
  def wrapAbi(
    filename: String,
    packageName: String = "com.micronautics.solidity",
    inputDirectory: String = "abi/",
    outputDirectory: String = "abiWrapper/"
  ): String = cmd.getOutputFrom(
    "bin/webuj", "solidity",
      "generate", s"$inputDirectory/${ toCamelCase(filename) }.bin", s"$inputDirectory/${ toCamelCase(filename) }.abi",
      "-o", outputDirectory,
      "-p", packageName
  )

  protected def toCamelCase(s: String): String = {
    val words = s.split("_")
    val tail = words.tail.map { word => word.head.toUpper + word.tail }
    words.head + tail.mkString
  }
}

/** Wrapper for WebuJ */
class WebuJScala(val webuj: Webuj)
                (implicit ec: ExecutionContext) {
  lazy val async = new HappyUCASynchronous(webuj)
  lazy val sync = new HappyUCSynchronous(webuj)
}
