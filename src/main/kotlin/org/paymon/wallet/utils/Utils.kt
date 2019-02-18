package org.paymon.wallet.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.bouncycastle.crypto.engines.AESEngine
import org.bouncycastle.crypto.modes.CBCBlockCipher
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.crypto.params.ParametersWithIV
import org.bouncycastle.jce.interfaces.ECPrivateKey
import org.bouncycastle.util.encoders.Hex
import org.paymon.wallet.Address
import org.paymon.wallet.PublicKey
import org.paymon.wallet.Secp256k1
import javax.crypto.KeyGenerator

const val BACKUP_VERSION = 3

class WalletAccount(sk: ECPrivateKey, val version: Int) {
    var privateKey = sk
    var publicKey: PublicKey
    var address: Address

    init {
        publicKey = Secp256k1.generatePublicKeyFromSecretKey(privateKey) //?: throw IllegalArgumentException("Invalid private key")
        address = Address.fromPublicKey(publicKey)
        println("init addr=$address")
    }

    fun generateBackup(password: String): JsonElement {
        println("backup addr=${this.address}")

        val cipher = PaddedBufferedBlockCipher(CBCBlockCipher(AESEngine()))
        val ivGen = KeyGenerator.getInstance("AES")
        ivGen.init(128)
        val iv = ivGen.generateKey().encoded
        cipher.init(true, ParametersWithIV(KeyParameter(generateKeySpec(password)), iv))

        val privateKeyBytes = privateKey.d.toByteArray()
        val outputBuffer = ByteArray(cipher.getOutputSize(privateKeyBytes.size))
        val l1 = cipher.processBytes(privateKeyBytes, 0, privateKeyBytes.size, outputBuffer, 0)
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
    val bytes = ByteArray(len) {
        if (it < passKeyLen) {
            return@ByteArray keyBytes[it]
        }

        return@ByteArray 0.toByte()
    }
    return bytes
}

fun restoreFromBackup(json: JsonElement, password: String): WalletAccount? {
    if (!json.isJsonObject) {
        println("Invalid format")
        return null
    }

    val obj = json.asJsonObject

    var v = 0
    if (obj.has("version") && obj.get("version").isJsonPrimitive) {
        v = obj.get("version").asJsonPrimitive.asNumber.toInt()
    }

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
        val wallet = WalletAccount(Secp256k1.generateSecretKeyFromBytes(decrypted), v)
        println("restore addr=${wallet.address}")

        if (!wallet.address.inner.contentEquals(addr.inner)) {
            println("Different addresses")
        }
        wallet
    } catch (e: Exception) {
        println("Failed to create WalletAccount")
        null
    }
}