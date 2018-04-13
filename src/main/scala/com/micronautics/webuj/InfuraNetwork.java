package com.micronautics.webuj;

/** From the [[https://docs.webuj.io/transactions.html#happyuc-testnets webuj testnet docs]]:
 *  For development, its recommended you use the Rinkeby (ghuc only) or Kovan (Parity only) test networks.
 *  This is because they use a Proof of Authority (PoA) consensus mechanism,
 *  ensuring transactions and blocks are created in a consistent and timely manner.
 *  The Ropsten testnet, although closest to the Mainnet as it uses Proof of Work (PoW) consensus,
 *  has been subject to attacks in the past and tends to be more problematic for developers. */
public enum InfuraNetwork {
    MAINNET, KOVAN, RINKEBY, ROPSTEN
}
