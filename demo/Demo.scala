package demo

import java.math.BigInteger
import com.micronautics.webuj.{Cmd, Huc, HappyUCSynchronous, WebuJScala}
import Cmd.{isMac, isWindows}
import org.webuj.protocol.Webuj
import org.webuj.protocol.ipc.{UnixIpcService, WindowsIpcService}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Promise}
import org.webuj.protocol.core.DefaultBlockParameterName._

object Demo {
  val gasPrice: Huc = Huc(1)
  val gasLimit: BigInteger = BigInt(2).bigInteger

  val walletDir: String = Cmd.home(
    if (isWindows) s"${ sys.props("user.home") }\\AppData\\Roaming\\HappyUC\\"
    else if (isMac) "~/Library/HappyUC/"
    else "~/.happyuc/"
  )
}

class Demo(implicit ec: ExecutionContext) {
  // Setup for running command lines from Scala
  val cmd = new Cmd()

  // Instantiate an instance of the underlying WebuJ library:
  val webuj: Webuj = WebuJScala.fromHttp()  // defaults to http://localhost:8545/
  val webujScala: WebuJScala = new WebuJScala(webuj)

  // Example of a synchronous request:
  val webujClientVersion1: String = webujScala.sync.versionWebuJ
  println(s"WebuJ version = $webujClientVersion1")

  // Contrived example of an asynchronous request, which provides no benefit over using a synchronous request:
  val webujClientVersion2: String = Await.result(webujScala.async.versionWebuJ, Duration.Inf)
  println(s"WebuJ version = $webujClientVersion2")

  // Better example of an asynchronous request:
  private val promise: Promise[String] = Promise[String]
  webujScala.async.versionWebuJ.foreach { webujClientVersion =>
    println(s"WebuJ version = $webujClientVersion")
    promise.complete(scala.util.Success("Done"))
  }
  Await.ready(promise.future, Duration.Inf) // pause while the async request completes

  val eSync: HappyUCSynchronous = webujScala.sync
  eSync.accounts match {
    case Nil => println("No accounts found.")
    case accounts =>
      accounts.foreach {
        account => println(s"The balance of $account is ${ eSync.balance(account, LATEST) } Wei")
      }
  }

  val happyucDir: String = Cmd.home(
    if (isWindows) "~/AppData/Roaming/HappyUC"
    else if (isMac) "~/Library/HappyUC/"
    else "~/.happyuc/"
  ) + "devnet/" // todo discover the happyuc name at runtime

  // WebuJ supports fast inter-process communication (IPC) via file sockets to clients running on the same host as WebuJ.
  // To connect simply use the relevant IpcService implementation instead of HttpService when you create your service:
  val webuJ3: Webuj = try {
    if (isWindows)
      Webuj.build(new WindowsIpcService(happyucDir))
    else
      Webuj.build(new UnixIpcService(happyucDir + "ghuc.ipc"))
  } catch {
    case e: Throwable =>
      System.err.println(e.getMessage)
      webuj
  }
}
