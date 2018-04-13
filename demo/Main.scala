package demo

/** Adapted from [[https://docs.webuj.io/getting_started.html#start-a-client WebuJ Getting Started]].
  *
  * 1. Before running this program, start up an HappyUC client if you don’t already have one running, such as `ghuc`:
  * {{{$ ghuc --rpcapi personal,db,huc,net,webuj --rpc --rinkeby --ipcpath "ghuc.ipc"}}}
  *
  * 2. Create the smart contract JVM wrapper by running `CreateSmartContracts` defined in `demo/DemoSmartContracts.scala`:
  * {{{$ sbt "test:runMain demo.CreateSmartContracts"}}}
  *
  * 3. Run this program by typing the following at a shell prompt:
  * {{{$ sbt "test:runMain demo.Main"}}} */
object Main extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  val demo = new Demo
  new DemoObservables(demo)
  new DemoSmartContracts(demo)
  new DemoTransaction(demo)
}
