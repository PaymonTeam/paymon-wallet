package com.paymon.wallet.utils

interface SerializableData {
    val position: Int
    fun writeInt32(x: Int)

    fun writeInt64(x: Long)

    fun writeBool(value: Boolean)

    fun writeBytes(b: ByteArray)

    fun writeBytes(b: ByteArray, offset: Int, count: Int)

    fun writeByte(i: Int)

    fun writeByte(b: Byte)

    fun writeString(s: String?)

    fun writeByteArray(b: ByteArray, offset: Int, count: Int)

    fun writeByteArray(b: ByteArray)

    fun writeDouble(d: Double)

    fun writeByteBuffer(buffer: SerializedBuffer)

    fun readInt32(exception: Boolean = true): Int

    fun readBool(exception: Boolean = true): Boolean

    fun readInt64(exception: Boolean = true): Long

    fun readBytes(b: ByteArray, exception: Boolean = true)

    fun readData(count: Int, exception: Boolean = true): ByteArray

    fun readString(exception: Boolean = true): String?

    fun readByteArray(exception: Boolean = true): ByteArray

    fun readByteBuffer(exception: Boolean = true): SerializedBuffer?

    fun readDouble(exception: Boolean = true): Double

    fun readByte(exception: Boolean = true): Byte

    fun length(): Int

    fun skip(count: Int)
}
