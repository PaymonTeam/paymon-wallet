package org.paymon.wallet

import com.paymon.wallet.net.Serializable
import com.paymon.wallet.utils.SerializableData
import org.bouncycastle.jcajce.provider.digest.SHA3
import org.bouncycastle.util.encoders.Hex
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import kotlin.experimental.and

//typealias Signature = ByteArray
//typealias PublicKey = ByteArray
//typealias PrivateKey = ByteArray
typealias Hash = ByteArray
typealias ContractAddress = Address

fun calculateAddressChecksum(bytes: ByteArray): Byte {
    var checksumByte: Short = 0
    for ((i, b: Byte) in bytes.withIndex()) {
        checksumByte = if ((i and 1) == 0) {
            (checksumByte + b).toShort()
        } else {
            (checksumByte + (b * 2)).toShort()
        }
        checksumByte = (checksumByte % 256).toShort()
    }

    return checksumByte.toByte()
}

class Address {
    companion object {
        fun fromPublicKey(pk: PublicKey): Address {
//            pk.encoded
            val bytes = pk.q.xCoord.encoded + pk.q.yCoord.encoded
            println(String(Hex.encode(bytes)))
            val sha = SHA3.Digest256()
            val digest = sha.digest(bytes)
//            println("=" + String(Hex.encode(digest)))
            val offset = 32 - ADDRESS_SIZE + 1

            val checksumByte = calculateAddressChecksum(digest.sliceArray(offset..(32 - 1)))
            val addr = ADDRESS_NULL.clone()
            System.arraycopy(digest, offset, addr.inner, 0, ADDRESS_SIZE - 1)
            addr.inner[ADDRESS_SIZE - 1] = checksumByte

            return addr
        }

        fun fromByteArray(bytes: ByteArray): Address {
            val sha = SHA3.Digest256()
            val digest = sha.digest(bytes)
            val offset = 32 - ADDRESS_SIZE + 1

            val checksumByte = calculateAddressChecksum(digest.sliceArray(offset..(32 - 1)))
            val addr = ADDRESS_NULL.clone()
            System.arraycopy(digest, offset, addr.inner, 0, ADDRESS_SIZE - 1)
            addr.inner[ADDRESS_SIZE - 1] = checksumByte

            return addr
        }
    }

    var inner: ByteArray = ByteArray(ADDRESS_SIZE)

    constructor(bytes: ByteArray) {
        this.inner = bytes
    }

    constructor(str: String) {
        this.inner = Hex.decode(str.substring(1))
    }

    fun verify(): Boolean {
        return calculateAddressChecksum(inner.sliceArray(0..(ADDRESS_SIZE - 2))) == inner[ADDRESS_SIZE - 1]
    }

    override fun toString(): String {
        return "P" + String(Hex.encode(inner)).toUpperCase()
    }

    fun clone(): Address {
        return Address(inner.clone())
    }
}

enum class TransactionType(_v: Int) {
    HashOnly(0),
    Full(1),
}

class Transaction(val obj: TransactionObject) {
    fun calculateSignature(sk: SecretKey, pk: PublicKey): Signature {
        val secp = Secp256k1()
        val privateKey = getPrivateKey()!!
        println("private key: $privateKey")
        val signature = secp.sign(privateKey, obj.hash)
//        println("signature: ${Hex.toHexString(signature)})")
        return signature
    }

    fun calculateHash(): Hash {
        val sha = SHA3.Digest256()
        val bb = ByteBuffer.allocate(ADDRESS_SIZE + 4 + 8 + HASH_SIZE + this.obj.data.length)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.position(0)
        bb.put(obj.address.inner)
        // TODO: change to Long
        bb.putInt(obj.value.toInt())
        bb.putLong(obj.timestamp)
        bb.put(obj.tag)
        bb.put(obj.data.toByteArray())
        println(Hex.toHexString(bb.array()))

        return sha.digest(bb.array()).sliceArray(0..(HASH_SIZE - 1))
    }

    fun findNonce(mwm: Int): Long {
        var nonce = 0L
        val sha = SHA3.Digest256()
        val bb = ByteBuffer.allocate(Long.SIZE_BYTES + HASH_SIZE * 2)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.position(0)
        bb.put(obj.branch_transaction)
        bb.put(obj.trunk_transaction)
        bb.putLong(nonce)
        loop@ while (true) {
            val buf = sha.digest(bb.array())
            for (i in 0..mwm) {
                val x = buf[i / 8]
                val y = (0b10000000 shr (i % 8)).toByte()

                if ((x and y) != 0.toByte()) {
                    sha.reset()
                    nonce++
                    bb.position(HASH_SIZE * 2)
                    bb.putLong(nonce)
                    continue@loop
                }
            }
            break@loop
        }
        return nonce
    }

    override fun toString(): String {
        return "Transaction(obj=$obj)"
    }
}

class TransactionObject : Serializable() {
    var address = ADDRESS_NULL.clone()
    var attachment_timestamp = 0L
    var attachment_timestamp_lower_bound = 0L
    var attachment_timestamp_upper_bound = 0L
    var branch_transaction = HASH_NULL.clone()
    var trunk_transaction = HASH_NULL.clone()
    var hash = HASH_NULL.clone()
    var nonce = 0L
    var tag = HASH_NULL.clone()
    var timestamp = 0L
    var value = 0L
    var data_type = TransactionType.HashOnly
    var signature: Signature? = null
    var signature_pubkey: PublicKey? = null //ByteArray(0)
    var snapshot = 0
    var solid = false
    var height = 0L
    var data: String = ""

    override fun serializeToStream(stream: SerializableData) {
        stream.writeInt32(svuid)
        stream.writeBytes(hash)
        stream.writeBytes(address.inner)
        stream.writeInt64(attachment_timestamp)
        stream.writeInt64(attachment_timestamp_lower_bound)
        stream.writeInt64(attachment_timestamp_upper_bound)
        stream.writeBytes(branch_transaction)
        stream.writeBytes(trunk_transaction)
        stream.writeInt64(nonce)
        stream.writeBytes(tag)
        stream.writeInt64(timestamp)
        stream.writeInt32(value.toInt())
        val b = when (data_type) {
            TransactionType.HashOnly -> 0
            TransactionType.Full -> 1
        }
        stream.writeByte(b)
        stream.writeByteArray(signature!!.r.toByteArray() + signature!!.s.toByteArray())
        stream.writeByteArray(ByteArray(1) { 2 } + signature_pubkey!!.q.xCoord.encoded)
        stream.writeInt32(snapshot)
        stream.writeBool(solid)
        stream.writeInt64(height)
        stream.writeString(data)
    }

    override fun readParams(stream: SerializableData, exception: Boolean) {
        stream.readBytes(hash)
        stream.readBytes(address.inner)
        attachment_timestamp = stream.readInt64()
        attachment_timestamp_lower_bound = stream.readInt64()
        attachment_timestamp_upper_bound = stream.readInt64()
        stream.readBytes(branch_transaction)
        stream.readBytes(trunk_transaction)
        nonce = stream.readInt64()
        stream.readBytes(tag)
        timestamp = stream.readInt64()
        value = stream.readInt32().toLong()
        data_type = when (stream.readByte()) {
            0.toByte() -> TransactionType.HashOnly
            else -> TransactionType.Full
        }
        signature = Signature.fromByteArray(stream.readByteArray())
        signature_pubkey = Secp256k1.generatePublicKeyFromBytes(stream.readByteArray()) //readObject() as PublicKey?
        snapshot = stream.readInt32()
        solid = stream.readBool()
        height = stream.readInt64()
        data = stream.readString() ?: ""
    }

    companion object {
        var svuid = 342631123
    }
}
