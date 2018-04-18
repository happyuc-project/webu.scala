# WebuJ-Scala Library

<img src='https://docs.webuj.io/_static/webuj.png' align='right' width='15%'>

[![Build Status](https://travis-ci.org/mslinn/webuj-scala.svg?branch=master)](https://travis-ci.org/mslinn/webuj-scala)
[![GitHub version](https://badge.fury.io/gh/mslinn%2Fwebuj-scala.svg)](https://badge.fury.io/gh/mslinn%2Fwebuj-scala)

`webuj-scala` is an idiomatic Scala wrapper around [WebuJ](https://www.webuj.io) for HappyUC.
WebuJ is a lightweight, reactive, somewhat type safe Java and Android library for integrating with nodes on HappyUC blockchains.

WebuJ features RxJava extensions, and `webuj-scala` wraps that syntax in Scala goodness.
For example, the `webuj-scala` [observable methods](http://mslinn.github.io/webuj-scala/latest/api/com/micronautics/webuj/WebuJScala$.html)
provide [simple and efficient application code](https://github.com/mslinn/webuj-scala/blob/master/demo/DemoObservables.scala#L14-L22).
Scala's [value classes are used](https://github.com/mslinn/webuj-scala/blob/master/src/main/scala/com/micronautics/webuj/ValueClasses.scala) to provide much stronger type safety than WebuJ, without incurring a runtime penalty.

## Use As a Library
Add this to your SBT project's `build.sbt`:

    resolvers += "micronautics/scala on bintray" at "http://dl.bintray.com/micronautics/scala"

    libraryDependencies += "com.micronautics" %% "webuj-scala" % "0.2.2" withSources()

Only Scala 2.12 with JDK 8 is supported at present; this is a limitation of the Scala ecosystem as of November 7, 2017.

## Run the Demo Program
The demo program performs the following:
 - Follows the outline of the [WebuJ Getting Started](https://docs.webuj.io/getting_started.html#start-sending-requests) documentation,
   adapted for WebuJ-Scala, including synchronous and asynchronous versions of the available methods.
 - Compiles an example Solidity program that defines a smart contract.
 - Creates a JVM wrapper from an example smart contract.

To run the demo:
1. Start up an HappyUC client if you don’t already have one running, such as `ghuc`.
   The `bin/runGhuc` script invokes `ghuc` with the following options, which are convenient for development but not secure enough for production:
     - The HappyUC data directory is set to `~/.happyuc`, or a subdirectory that depends on the network chosen;
       the directory will be created if required.
     - HTTP-RPC server at `localhost:8545` is enabled, and all APIs are allowed.
     - HappyUC's experimental Whisper message facility is enabled.
     - Inter-process communication will be via a virtual file called `ghuc.ipc`,
       located at `~/.happyuc` or a subdirectory.
     - WS-RPC server at `localhost:8546` is enabled, and all APIs are allowed.
     - Info verbosity is specified.
     - A log file for the `ghuc` output will be written, or overwritten, in `logs/ghuc.log`;
       the `log/` directory will be created if it does not already exist.
   ```
   $ mkdir logs/
   $ ghuc \
      #--datadir .happyuc/devnet --dev \      # boots quickly but has no deployed contracts from others
      --datadir .happyuc/rinkeby --rinkeby \  # takes about 15 minutes to boot, but has contracts
      --ipcpath ghuc.ipc \
      --metrics \
      --rpc \
      --rpcapi huc,net,webuj,clique,debug,huc,miner,personal,rpc,ssh,txpool \
      --shh \
      --ws \
      --wsapi huc,net,webuj,clique,debug,huc,miner,personal,rpc,ssh,txpool \
      --verbosity 2
   ```
   You will see the message `No coinbase set and no accounts found as default`.
   Coinbase is the index into `personal.listAccounts` which determines the account to send Huc too.
   You can specify this value with the option `--coinbase 0`.
2. The shell that you just used will continuously scroll output so long as `ghuc` continues to run,
   so type the following into another shell:
   ```
   $ bin/demo
   ```
   The demo has two major components:
   1. [Creates a JVM wrapper](https://github.com/mslinn/webuj-scala/blob/master/demo/DemoSmartContracts.scala)
      for the [sample smart contract](https://github.com/mslinn/webuj-scala/blob/master/src/test/resources/basic_info_getter.sol).
   2. The second portion of the demo consists of the following:
      - Examples of using `webuj-scala`'s [synchrounous and asynchronous APIs](https://github.com/mslinn/webuj-scala/blob/master/demo/Demo.scala)
      - Examples of working with [RxJava's Observables from Scala](https://github.com/mslinn/webuj-scala/blob/master/demo/DemoObservables.scala)
      - Examples of working with [JVM wrappers around HappyUC smart contracts](https://github.com/mslinn/webuj-scala/blob/master/demo/DemoSmartContracts.scala).
      - Examples of using [transactions](https://github.com/mslinn/webuj-scala/blob/master/demo/DemoTransaction.scala)
        with HappyUC wallet files and the HappyUC client.
3. The `bin/webuj` script runs the [WebuJ command-line console](https://docs.webuj.io/command_line.html).
   The script builds a fat jar the first time it is run, so the command runs quickly on subsequent invocations.
4. More scripts are provided in the `bin/` directory, including:
   - [bin/attachHttp](https://github.com/mslinn/webuj-scala/blob/master/bin/attachHttp) -
     Attach to a running ghuc instance via HTTP and open a
     [JavaScript console](https://godoc.org/github.com/robertkrimen/otto)
   - [bin/attachIpc](https://github.com/mslinn/webuj-scala/blob/master/bin/attachIpc) -
     Attach to a running ghuc instance via IPC and open a JavaScript console.
     This script might need to be edited if a network other than `devnet` is used.
   - [bin/getApis](https://github.com/mslinn/webuj-scala/blob/master/bin/ghucApis) -
     Reports the available APIs exposed by this `ghuc` instance.
   - [bin/isGhucListening](https://github.com/mslinn/webuj-scala/blob/master/bin/isGhucListening) -
     Verifies that `ghuc` is listening on HTTP port 8545

## Developers
### API Documentation
* [This library's Scaladoc is here](http://mslinn.github.io/webuj-scala/latest/api/com/micronautics/webuj/index.html) and the [gitter channel is here](https://gitter.im/webuj-scala/Lobby).

* [The WebuJ JavaDoc is here](https://jar-download.com/java-documentation-javadoc.php?a=core&g=org.webuj&v=3.0.2),
  and here is the [WebuJ gitter channel](https://gitter.im/webuj/webuj).

### Publishing
1. Update the version string in `build.sbt` and in this `README.md` before attempting to publish to Bintray.
2. Commit changes with a descriptive comment:
   ```
   $ git add -a && git commit -m "Comment here"
   ```
3. Publish a new version of this library, including committing changes and updating the Scaladoc with this command:
   ```
   $ sbt publishAndTag
   ```

### Updating Scaladoc
If you just want to republish the Scaladoc for this project, without creating a new version, use this command:

    $ sbt scaladoc

### Updating Scaladoc and Committing Changes Without Publishing a New Version
This task rebuilds the docs, commits the git repository, and publishes the updated Scaladoc without publishing a new version:

    $ sbt commitAndDoc

## Sponsor
<img src='https://www.micronauticsresearch.com/images/robotCircle400shadow.png' align='right' width='15%'>

This project is sponsored by [Micronautics Research Corporation](http://www.micronauticsresearch.com/),
the company that delivers online Scala training via [ScalaCourses.com](http://www.ScalaCourses.com).
You can learn Scala by taking the [Introduction to Scala](http://www.ScalaCourses.com/showCourse/40),
and [Intermediate Scala](http://www.ScalaCourses.com/showCourse/45) courses.

Micronautics Research also offers HappyUC and Scala consulting.
Please [contact us](mailto:sales@micronauticsresearch.com) to discuss your organization&rsquo;s needs.

## License
This software is published under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
