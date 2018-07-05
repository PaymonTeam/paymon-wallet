package com.paymon.wallet.utils

import java.nio.ByteOrder
import java.nio.ByteBuffer
import java.nio.charset.Charset

class SerializedBuffer : SerializableData {
    var buffer = ByteBuffer.allocate(1)
    private var justCalc = false
    private var len: Int = 0
    var address: Int = 0
        private set
    private var reused = true

    override val position: Int
        get() = buffer.position()

    private constructor(address: Int, wrap: Boolean) {

    }

    @Throws(Exception::class)
    constructor(size: Int) {
        if (size >= 0) {
//            address = native_getFreeBuffer(size)
//            if (address != 0) {
                buffer = ByteBuffer.allocate(size) //native_getJavaByteBuffer(address)
                buffer.position(0)
                buffer.limit(size)
                buffer.order(ByteOrder.LITTLE_ENDIAN)
//            }
        } else {
            throw Exception("invalid SerializedBuffer size")
        }
    }

    constructor(calculate: Boolean) {
        justCalc = calculate
    }

    fun position(): Int {
        return buffer.position()
    }

    fun position(position: Int) {
        buffer.position(position)
    }

    fun capacity(): Int {
        return buffer.capacity()
    }

    fun limit(): Int {
        return buffer.limit()
    }

    fun limit(limit: Int) {
        buffer.limit(limit)
    }

    fun put(buff: ByteBuffer) {
        buffer.put(buff)
    }

    fun rewind() {
        if (justCalc) {
            len = 0
        } else {
            buffer.rewind()
        }
    }

    fun compact() {
        buffer.compact()
    }

    fun hasRemaining(): Boolean {
        return buffer.hasRemaining()
    }

    override fun writeInt32(x: Int) {
        try {
            if (!justCalc) {
                buffer.putInt(x)
            } else {
                len += 4
            }
        } catch (e: Exception) {
            println("write int32 error")
        }

    }

    override fun writeInt64(x: Long) {
        try {
            if (!justCalc) {
                buffer.putLong(x)
            } else {
                len += 8
            }
        } catch (e: Exception) {
            println("write int64 error")
        }

    }

    override fun writeBool(value: Boolean) {
        if (!justCalc) {
            if (value) {
                writeInt32(0x6e4a64b3)
            } else {
                writeInt32(0x3f5d29c3)
            }
        } else {
            len += 4
        }
    }

    override fun writeBytes(b: ByteArray) {
        try {
            if (!justCalc) {
                buffer.put(b)
            } else {
                len += b.size
            }
        } catch (e: Exception) {
            println("write raw error")
        }

    }

    override fun writeBytes(b: ByteArray, offset: Int, count: Int) {
        try {
            if (!justCalc) {
                buffer.put(b, offset, count)
            } else {
                len += count
            }
        } catch (e: Exception) {
            println("write raw error")
        }

    }

    override fun writeByte(i: Int) {
        writeByte(i.toByte())
    }

    override fun writeByte(b: Byte) {
        try {
            if (!justCalc) {
                buffer.put(b)
            } else {
                len += 1
            }
        } catch (e: Exception) {
            println("write byte error")
        }

    }

    override fun writeString(s: String) {
        try {
            if (s != null) {
                writeByteArray(s.toByteArray(charset("UTF-8")))
            } else {
                writeByteArray("".toByteArray(charset("UTF-8")))
            }
        } catch (e: Exception) {
            println("write string error")
        }

    }

    override fun writeByteArray(b: ByteArray, offset: Int, count: Int) {
        try {
            if (count <= 253) {
                if (!justCalc) {
                    buffer.put(count.toByte())
                } else {
                    len += 1
                }
            } else {
                if (!justCalc) {
                    buffer.put(254.toByte())
                    buffer.put(count.toByte())
                    buffer.put((count shr 8).toByte())
                    buffer.put((count shr 16).toByte())
                } else {
                    len += 4
                }
            }
            if (!justCalc) {
                buffer.put(b, offset, count)
            } else {
                len += count
            }
            var i = if (count <= 253) 1 else 4
            while ((count + i) % 4 != 0) {
                if (!justCalc) {
                    buffer.put(0.toByte())
                } else {
                    len += 1
                }
                i++
            }
        } catch (e: Exception) {
            println("write byte array error")
        }

    }

    override fun writeByteArray(b: ByteArray) {
        try {
            if (b.size <= 253) {
                if (!justCalc) {
                    buffer.put(b.size.toByte())
                } else {
                    len += 1
                }
            } else {
                if (!justCalc) {
                    buffer.put(254.toByte())
                    buffer.put(b.size.toByte())
                    buffer.put((b.size shr 8).toByte())
                    buffer.put((b.size shr 16).toByte())
                } else {
                    len += 4
                }
            }
            if (!justCalc) {
                buffer.put(b)
            } else {
                len += b.size
            }
            var i = if (b.size <= 253) 1 else 4
            while ((b.size + i) % 4 != 0) {
                if (!justCalc) {
                    buffer.put(0.toByte())
                } else {
                    len += 1
                }
                i++
            }
        } catch (e: Exception) {
            println("write byte array error")
        }

    }

    override fun writeDouble(d: Double) {
        try {
            writeInt64(java.lang.Double.doubleToRawLongBits(d))
        } catch (e: Exception) {
            println("write double error")
        }

    }

    override fun writeByteBuffer(b: SerializedBuffer) {
        try {
            val l = b.limit()
            if (l <= 253) {
                if (!justCalc) {
                    buffer.put(l.toByte())
                } else {
                    len += 1
                }
            } else {
                if (!justCalc) {
                    buffer.put(254.toByte())
                    buffer.put(l.toByte())
                    buffer.put((l shr 8).toByte())
                    buffer.put((l shr 16).toByte())
                } else {
                    len += 4
                }
            }
            if (!justCalc) {
                b.rewind()
                buffer.put(b.buffer)
            } else {
                len += l
            }
            var i = if (l <= 253) 1 else 4
            while ((l + i) % 4 != 0) {
                if (!justCalc) {
                    buffer.put(0.toByte())
                } else {
                    len += 1
                }
                i++
            }
        } catch (e: Exception) {
            println(e.message)
        }

    }

    fun writeBytes(b: SerializedBuffer) {
        if (justCalc) {
            len += b.limit()
        } else {
            b.rewind()
            buffer.put(b.buffer)
        }
    }

    private fun getIntFromByte(b: Byte): Int {
        return if (b >= 0) b.toInt() else b.toInt() + 256
    }

    override fun length(): Int {
        return if (!justCalc) {
            buffer.position()
        } else len
    }

    override fun skip(count: Int) {
        if (count == 0) {
            return
        }
        if (!justCalc) {
            buffer.position(buffer.position() + count)
        } else {
            len += count
        }
    }

    override fun readInt32(exception: Boolean): Int {
        try {
            return buffer.int
        } catch (e: Exception) {
            if (exception) {
                throw RuntimeException("read int32 error", e)
            } else {
                println("read int32 error")
            }
        }

        return 0
    }

    override fun readBool(exception: Boolean): Boolean {
        val consructor = readInt32(exception)

        if (consructor == 0x6e4a64b3) {
            return true
        } else if (consructor == 0x3f5d29c3) {
            return false
        }
        if (exception) {
            throw RuntimeException("Not bool value!")
        } else {
            println("Not bool value!")
        }
        return false
    }

    override fun readInt64(exception: Boolean): Long {
        try {
            return buffer.getLong()
        } catch (e: Exception) {
            if (exception) {
                throw RuntimeException("read int64 error", e)
            } else {
                println("read int64 error")
            }
        }

        return 0
    }

    override fun readBytes(b: ByteArray, exception: Boolean) {
        try {
            buffer.get(b)
        } catch (e: Exception) {
            if (exception) {
                throw RuntimeException("read raw error", e)
            } else {
                println("read raw error")
            }
        }

    }

    override fun readByte(exception: Boolean): Byte {
        try {
            return buffer.get()
        } catch (e: Exception) {
            if (exception) {
                throw RuntimeException("read raw error", e)
            } else {
                println("read raw error")
            }
        }

        return -1
    }

    fun remaining(): Int {
        return limit() - position()
    }

    override fun readData(count: Int, exception: Boolean): ByteArray {
        val arr = ByteArray(count)
        readBytes(arr, exception)
        return arr
    }

    override fun readString(exception: Boolean): String? {
        try {
            var sl = 1
            var l = getIntFromByte(buffer.get())
            if (l >= 254) {
                l = getIntFromByte(buffer.get()) or (getIntFromByte(buffer.get()) shl 8) or (getIntFromByte(buffer.get()) shl 16)
                sl = 4
            }
            val b = ByteArray(l)
            buffer.get(b)
            var i = sl
            while ((l + i) % 4 != 0) {
                buffer.get()
                i++
            }
            return String(b, Charset.forName("UTF-8"))
        } catch (e: Exception) {
            if (exception) {
                throw RuntimeException("read string error", e)
            } else {
                println("read string error")
            }
        }

        return null
    }

    override fun readByteArray(exception: Boolean): ByteArray {
        try {
            var sl = 1
            var l = getIntFromByte(buffer.get())
            if (l >= 254) {
                l = getIntFromByte(buffer.get()) or (getIntFromByte(buffer.get()) shl 8) or (getIntFromByte(buffer.get()) shl 16)
                sl = 4
            }
            val b = ByteArray(l)
            buffer.get(b)
            var i = sl
            while ((l + i) % 4 != 0) {
                buffer.get()
                i++
            }
            return b
        } catch (e: Exception) {
            if (exception) {
                throw RuntimeException("read byte array error", e)
            } else {
                println("read byte array error")
            }
        }

        return ByteArray(0)
    }

    override fun readByteBuffer(exception: Boolean): SerializedBuffer? {
        try {
            var sl = 1
            var l = getIntFromByte(buffer.get())
            if (l >= 254) {
                l = getIntFromByte(buffer.get()) or (getIntFromByte(buffer.get()) shl 8) or (getIntFromByte(buffer.get()) shl 16)
                sl = 4
            }
            val b = SerializedBuffer(l)
            val old = buffer.limit()
            buffer.limit(buffer.position() + l)
            b.buffer.put(buffer)
            buffer.limit(old)
            b.buffer.position(0)
            var i = sl
            while ((l + i) % 4 != 0) {
                buffer.get()
                i++
            }
            return b
        } catch (e: Exception) {
            if (exception) {
                throw RuntimeException("read byte array error", e)
            } else {
                println("read byte array error")
            }
        }

        return null
    }

    override fun readDouble(exception: Boolean): Double {
        try {
            return java.lang.Double.longBitsToDouble(readInt64(exception))
        } catch (e: Exception) {
            if (exception) {
                throw RuntimeException("read double error", e)
            } else {
                println("read double error")
            }
        }

        return 0.0
    }

    fun reuse() {
        if (address != 0) {
            reused = true
            native_reuse(address)
        }
    }

    companion object {

        private val addressWrapper = object : ThreadLocal<SerializedBuffer>() {
            override fun initialValue(): SerializedBuffer {
                return SerializedBuffer(0, true)
            }
        }

        fun wrap(address: Int): SerializedBuffer {
            val result = addressWrapper.get()
            if (address != 0) {
                if (!result.reused) {
                    //                println("forgot to reuse?");
                }
                result.address = address
                result.reused = false
                result.buffer = native_getJavaByteBuffer(address)
                result.buffer.limit(native_limit(address))
                val position = native_position(address)
                if (position <= result.buffer.limit()) {
                    result.buffer.position(position)
                }
                result.buffer.order(ByteOrder.LITTLE_ENDIAN)
            }
            return result
        }

        external fun native_getFreeBuffer(length: Int): Int
        external fun native_getJavaByteBuffer(address: Int): ByteBuffer
        external fun native_limit(address: Int): Int
        external fun native_position(address: Int): Int
        external fun native_reuse(address: Int)
    }
}
