package org.paymon.wallet

interface Signable<P, SK, S> {
    fun generateKeypair() : Pair<SK, P>
    fun sign(privateKey: SK, message: ByteArray) : S
    fun verify(publicKey: P, signature: S, message: ByteArray) : Boolean
}