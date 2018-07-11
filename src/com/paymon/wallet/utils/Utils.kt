package com.paymon.wallet.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.paymon.wallet.*
import org.bouncycastle.crypto.engines.AESEngine
import org.bouncycastle.crypto.modes.CBCBlockCipher
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.crypto.params.ParametersWithIV
import org.bouncycastle.util.encoders.Hex
import javax.crypto.KeyGenerator

const val BACKUP_VERSION = 1

class WalletAccount(sk: PrivateKey) {
    var privateKey = sk
    var publicKey: PublicKey
    var address: Address

    init {
        publicKey = NTRUMLSNative.publicKeyFromPrivate(privateKey) ?: throw IllegalArgumentException("Invalid private key")
        address = addressFromPublicKey(publicKey)
    }

    fun createBackup(password: String): JsonElement {
        val cipher = PaddedBufferedBlockCipher(CBCBlockCipher(AESEngine()))
        val ivGen = KeyGenerator.getInstance("AES")
        ivGen.init(128)
        val iv = ivGen.generateKey().encoded
        cipher.init(true, ParametersWithIV(KeyParameter(generateKeySpec(password)), iv))

        val outputBuffer = ByteArray(cipher.getOutputSize(privateKey.size))
        val l1 = cipher.processBytes(privateKey, 0, privateKey.size, outputBuffer, 0)
        val l2 = cipher.doFinal(outputBuffer, l1)
        val encrypted = ByteArray(l1 + l2)
        System.arraycopy(outputBuffer, 0, encrypted, 0, encrypted.size)

        val json = JsonObject()
        json.addProperty("version", BACKUP_VERSION)
        json.addProperty("address", address.toString())
        val jsonCrypto = JsonObject()
        jsonCrypto.addProperty("ciphertext", String(Hex.encode(encrypted)))
        jsonCrypto.addProperty("cipher", "aes-128-cbc")
        val jsonCipherParams = JsonObject()
        jsonCipherParams.addProperty("iv", String(Hex.encode(iv)))
        jsonCrypto.add("cipherparams", jsonCipherParams)
        json.add("Crypto", jsonCrypto)
        return json
    }
}

private fun generateKeySpec(key: String): ByteArray {
    val len = 128 / 8
    val keyBytes = key.toByteArray()
    val passKeyLen = keyBytes.size
    val bytes = ByteArray(len, {
        if (it < passKeyLen) {
            return@ByteArray keyBytes[it]
        }

        return@ByteArray 0.toByte()
    })
    return bytes
}

fun restoreFromBackup(json: JsonElement, password: String): WalletAccount? {
    if (!json.isJsonObject) {
        println("Invalid format")
        return null
    }

    val obj = json.asJsonObject
    if (!obj.has("address") || !obj.has("Crypto")) {
        println("No address or Cipher params")
        return null
    }

    val addr = Address(obj.get("address").asString)
    if (!obj.get("Crypto").isJsonObject) {
        println("Invalid Cipher param")
        return null
    }

    val cryptoObj = obj.get("Crypto").asJsonObject
    if (!cryptoObj.has("cipherparams") || !cryptoObj.has("ciphertext")) {
        println("No cipherparams or ciphertext params")
        return null
    }

    if (!cryptoObj.get("cipherparams").isJsonObject) {
        println("Invalid cipherparams param")
        return null
    }

    val paramsObj = cryptoObj.get("cipherparams").asJsonObject

    if (!paramsObj.has("iv") || !paramsObj.get("iv").isJsonPrimitive || !paramsObj.getAsJsonPrimitive("iv").isString) {
        println("Invalid or no 'iv' param")
        return null
    }

    val iv = Hex.decode(paramsObj.getAsJsonPrimitive("iv").asString)
    if (iv.isEmpty()) {
        println("Invalid 'iv' param")
        return null
    }

    if (!cryptoObj.get("ciphertext").isJsonPrimitive || !cryptoObj.getAsJsonPrimitive("ciphertext").isString) {
        println("Invalid ciphertext param")
        return null
    }

    val data = Hex.decode(cryptoObj.getAsJsonPrimitive("ciphertext").asString)
    if (data.isEmpty()) {
        println("Invalid ciphertext param")
        return null
    }

    val cipher = PaddedBufferedBlockCipher(CBCBlockCipher(AESEngine()))
    cipher.init(false, ParametersWithIV(KeyParameter(generateKeySpec(password)), iv))
    val outputBuffer = ByteArray(cipher.getOutputSize(data.size))
    val l1 = cipher.processBytes(data, 0, data.size, outputBuffer, 0)
    val l2 = cipher.doFinal(outputBuffer, l1)
    val decrypted = ByteArray(l1 + l2)
    System.arraycopy(outputBuffer, 0, decrypted, 0, decrypted.size)

    return try {
        val wallet = WalletAccount(decrypted)
        if (!wallet.address.inner.contentEquals(addr.inner)) {
            println("Different addresses")
        }
        wallet
    } catch (e: Exception) {
        println("Failed to create WalletAccount")
        null
    }
}