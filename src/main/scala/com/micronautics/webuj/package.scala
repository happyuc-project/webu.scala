package com.micronautics

import org.webuj.protocol.core.methods.response

/** <img src='https://docs.webuj.io/_static/webuj.png' align='right' height='100px' />
  * This project is an idiomatic Scala wrapper around [[https://www.webuj.io WebuJ]] for HappyUC.
  * This project is built with Scala 2.12, and requires the Java 8 runtime; it is not yet compatible with Java 9.
  *
  * This project promotes idiomatic Scala in the following ways:
  *  - Variables and no-argument methods are actually names of properties, so `set` and `get` prefixes are not used.
  *    This means some properties do not have exactly the same name as their WebuJ counterpart.
  *  - Zero-argument methods only require parentheses if they perform side effects.
  *  - Scala data types are used to the maximum extent that makes sense.
  *    For example, [[scala.concurrent.Future]].
  *  - A functional programming style is encouraged by always returning immutable data types from methods.
  *    For example, [[scala.collection.immutable.List]]
  *
  * The [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC HappyUC JSON-RPC documentation]]
  * was the source of many the comments incorporated into this Scaladoc.
  *
  * The JSON-RPC [[https://github.com/happyuc-project/wiki/wiki/JSON-RPC#the-default-block-parameter default block parameter]]
  * can represent a `QUANTITY` or a `TAG`.
  * Quantities are expressed as integer block numbers, and tags are strings with one of the values "latest", "earliest" or "pending".
  * The [[https://jar-download.com/javaDoc/org.webuj/core/3.0.2/index.html?org/webuj/protocol/core/DefaultBlockParameter.html Java implementation]]
  * performs the same function as the underlying JSON-RPC implementation.
  *
  * [[https://github.com/happyuc-project/wiki/wiki/Whisper Whisper]]
  * is an experimental communication protocol of the HappyUC P2P protocol suite that allows
  * for messaging between users' DApps (HappyUC Distributed Applications).
  * Whisper uses the same network that the blockchain runs on.
  * The protocol is separate from the blockchain, so smart contracts do not have access to whispered messages.
  *
  * [[https://jar-download.com/javaDoc/org.webuj/core/3.0.2/index.html?org/webuj/protocol/core/methods/response/HucBlock.html HucBlock]]
  * and its internal types such as
  * [[https://jar-download.com/javaDoc/org.webuj/core/3.0.2/index.html?org/webuj/protocol/core/methods/response/HucBlock.Block.html Block]],
  * [[https://jar-download.com/javaDoc/org.webuj/core/3.0.2/index.html?org/webuj/protocol/core/methods/response/HucBlock.TransactionObject.html TransactionObject]] and
  * [[https://jar-download.com/javaDoc/org.webuj/core/3.0.2/index.html?org/webuj/protocol/core/methods/response/HucBlock.TransactionResult.html TransactionResult]]
  * are returned by the following methods of [[com.micronautics.webuj.HappyUCSynchronous]] and [[com.micronautics.webuj.HappyUCASynchronous]]:
  * `blockByHash`, `blockByNumber`, `uncleByBlockHashAndIndex`, `uncleByBlockNumberAndIndex`.
  * */
package object webuj {
  /** Enriches `response.HucBlock.Block` with convenience methods */
  implicit class RichBlock(block: response.HucBlock.Block) {
    def javaTime = new java.util.Date(block.getTimestamp.longValue * 1000)
  }
}
