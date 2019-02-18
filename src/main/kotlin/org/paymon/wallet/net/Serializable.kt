package com.paymon.wallet.net

import com.paymon.wallet.utils.SerializableData
import com.paymon.wallet.utils.SerializedBuffer

open class Serializable {
    val size: Int
        get() {
            val byteBuffer = sizeCalculator.get()
            byteBuffer.rewind()
            serializeToStream(sizeCalculator.get())
            return byteBuffer.length()
        }

    open fun readParams(stream: SerializableData, exception: Boolean) {

    }

    open fun serializeToStream(stream: SerializableData) {

    }

    open fun deserializeResponse(stream: SerializableData, constructor: Int, exception: Boolean): Serializable? {
        return null
    }

    companion object {
        private val sizeCalculator = object : ThreadLocal<SerializedBuffer>() {
            override fun initialValue(): SerializedBuffer {
                return SerializedBuffer(true)
            }
        }
    }
}
