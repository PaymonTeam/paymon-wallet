package com.paymon.wallet

import org.bouncycastle.jcajce.provider.digest.SHA3
import org.bouncycastle.util.encoders.Hex
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.and

typealias Signature = ByteArray
typealias PublicKey = ByteArray
typealias PrivateKey = ByteArray
typealias Hash = ByteArray

fun addressFromPublicKey(pk: PublicKey) : Address {
    val sha = SHA3.Digest256()
    val digest = sha.digest(pk)
    val offset = 32 - ADDRESS_SIZE + 1

    var checksumByte: Short = 0
    val bytes = digest.sliceArray(offset..32)
    for ((i, b:Byte) in bytes.withIndex()) {
        checksumByte = if ((i and 1) == 0) {
            (checksumByte + b).toShort()
        } else {
            (checksumByte + (b * 2)).toShort()
        }
        checksumByte = (checksumByte % 256).toShort()
    }

    bytes[ADDRESS_SIZE - 1] = checksumByte.toByte()
    return Address(bytes)
}

class Address {
    var inner: ByteArray = ByteArray(ADDRESS_SIZE)

    constructor(bytes: ByteArray) {
        this.inner = bytes
    }

    constructor(str: String) {
        this.inner = Hex.decode(str.substring(1))
    }

    override fun toString(): String {
        return "P" + String(Hex.encode(inner)).toUpperCase()
    }
}

enum class TransactionType(_v: Int) {
    HashOnly(0),
    Full(1),
}

class Transaction(val obj: TransactionObject) {
    fun calculateSignature(sk: PrivateKey, pk: PublicKey) : Signature {
        return NTRUMLSNative.sign(obj.hash, sk, pk)
    }

    fun calculateHash() : Hash {
        val sha = SHA3.Digest256()
        val bb = ByteBuffer.allocate(ADDRESS_SIZE + 4 + 8 + HASH_SIZE)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.position(0)
        bb.put(obj.address.inner)
        // TODO: change to Long
        bb.putInt(obj.value.toInt())
        bb.putLong(obj.timestamp)
        bb.put(obj.tag)
        return sha.digest(bb.array()).sliceArray(0..(HASH_SIZE - 1))
    }

    fun findNonce(mwm: Int) : Long {
        var nonce = 0L
        val sha = SHA3.Digest256()
        val bb = ByteBuffer.allocate(8 + HASH_SIZE * 2)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.position(0)
        bb.put(obj.branch_transaction)
        bb.put(obj.trunk_transaction)
        bb.putLong(nonce)
        loop@while(true) {
            val buf = sha.digest(bb.array())
            for (i in 0..mwm) {
                val x = buf[i / 8]
                val y = (0b10000000 shr (i % 8)).toByte()

                if ((x and y) != 0.toByte()) {
                    sha.reset()
                    nonce++
                    bb.position(40)
                    bb.putLong(nonce)
                    continue@loop
                }
            }
            break@loop
        }
        return nonce
    }
}

class TransactionObject {
    var address = ADDRESS_NULL
    var attachment_timestamp = 0L
    var attachment_timestamp_lower_bound = 0L
    var attachment_timestamp_upper_bound = 0L
    var branch_transaction = HASH_NULL
    var trunk_transaction = HASH_NULL
    var hash = HASH_NULL
    var nonce = 0L
    var tag = HASH_NULL
    var timestamp = 0L
    var value = 0L
    var data_type = TransactionType.HashOnly
    var signature = ByteArray(0)
    var signature_pubkey = ByteArray(0)
    var snapshot = 0
    var solid = false
    var height = 0
}
