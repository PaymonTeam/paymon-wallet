package com.paymon.wallet.net

import com.google.gson.*
import com.paymon.wallet.*
import com.paymon.wallet.utils.SerializedBuffer
import com.paymon.wallet.utils.WalletAccount
import org.bouncycastle.util.encoders.Hex
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class API {
    var account: WalletAccount? = null
    var neighbors = ArrayList<Neighbor>()
    var lock = Object()

    class Neighbor(uri: URI) {
        val addr = InetSocketAddress(uri.host, uri.port)
    }

    companion object {
        lateinit var gson: Gson
    }

    init {
        val hosts = arrayOf("tcp://91.226.80.26:79")
//        val hosts = arrayOf("tcp://127.0.0.1:79")
        Arrays.stream(hosts).distinct()
                .filter { s -> !s.isEmpty() }
                .map { s -> Optional.of(URI(s)) }
                .map { u -> u.get() }
                .filter { u ->
                    if (u != null) {
                        if (u.scheme == "tcp" || u.scheme == "udp") {
                            if (InetSocketAddress(u.host, u.port).address != null) {
                                return@filter true
                            }
                        }
                        println("$u is not valid address")
                        return@filter false
                    }
                    println("Invalid URI scheme $u")
                    false
                }.map { u -> Neighbor(u) }
                .peek { u ->
                    println("Adding neighbor ${u.addr}")
                }
                .forEach { n -> neighbors.add(n) }

        val gsonb = GsonBuilder()
        gsonb.registerTypeAdapter(Address::class.java, AddressSerializer())
        gsonb.registerTypeAdapter(Address::class.java, AddressDeserializer())

        gsonb.registerTypeAdapter(Hash::class.java, HashSerializer())
        gsonb.registerTypeAdapter(Hash::class.java, HashDeserializer())

        gsonb.registerTypeAdapter(PublicKey::class.java, PublicKeySerializer())
        gsonb.registerTypeAdapter(PublicKey::class.java, PublicKeyDeserializer())

        gsonb.registerTypeAdapter(PrivateKey::class.java, PrivateKeySerializer())
        gsonb.registerTypeAdapter(PrivateKey::class.java, PrivateKeyDeserializer())

        gsonb.registerTypeAdapter(Signature::class.java, SignatureSerializer())
        gsonb.registerTypeAdapter(Signature::class.java, SignatureDeserializer())

//        gsonb.registerTypeAdapter(TransactionType::class.java, TransactionTypeSerializer())
//        gsonb.registerTypeAdapter(TransactionType::class.java, TransactionTypeDeserializer())

        gson = gsonb.create()
    }

    private fun sendRequest(json: JsonElement, neighbor: Neighbor): String? {
        return this.sendRequest(json.toString(), neighbor)
    }

    private fun sendRequest(json: String, neighbor: Neighbor): String? {
        val length = json.length
        val requestStr =
                "POST / HTTP/1.0\r\n" +
                        "content-type:application/json\r\n" +
                        "X-PMNC-API-Version: 0.1\r\n" +
                        "Host:localhost\r\n" +
                        "content-length:$length" +
                        "\r\n\r\n" +
                        json

        val sock = Socket()
//        try {
        sock.connect(neighbor.addr, 5000)
//        } catch (e: Exception) {
//            println("${e.message}")
//        }
        val streamOut = sock.getOutputStream()
        val streamIn = InputStreamReader(sock.getInputStream())
        streamOut.write(requestStr.toByteArray())

        var resp = ""
        for (l in streamIn.readLines()) {
            resp = l
            if (!l.isEmpty()) {
                if (resp.startsWith("HTTP")) {
                    val retCode = resp.split(' ', ignoreCase = true, limit = 2)[1]
                    if (!retCode.startsWith("200")) {
                        println("Error code: $retCode")
                        return null
                    }
                } else if (resp.startsWith('{')) {
                    break
                }
            }
            resp = ""
        }

//        println("resp=$resp")

        streamOut.close()
        streamIn.close()
        sock.close()

        return resp
    }

    fun getBalanceRequest(address: Address): Long? {
        return getBalanceRequest(LinkedList(arrayListOf(address)))
    }

    fun getBalanceRequest(addresses: LinkedList<Address>): Long? {
        if (neighbors.isEmpty()) {
            return null
        }

        val obj = GetBalances(addresses, LinkedList(arrayListOf()), 50)
        val json = API.gson.toJsonTree(obj)
        json.asJsonObject.addProperty("method", "getBalances")
//        println(json.toString())
        val resp = api.sendRequest(json, neighbors[0])
        val respObj = API.gson.fromJson(resp, Balances::class.java)
        if (respObj != null && !respObj.balances.isEmpty()) {
            val balance = respObj.balances[0]
            return balance
        }
        return null
    }

    fun getTransactionToApprove(): TransactionsToApprove? {
        if (neighbors.isEmpty()) return null

        val obj = GetTransactionsToApprove(1, 5, HASH_NULL.clone())
        val json = API.gson.toJsonTree(obj)
        json.asJsonObject.addProperty("method", "getTransactionsToApprove")
        val resp = sendRequest(json, neighbors[0])
        if (resp != null) {
            return API.gson.fromJson(resp, TransactionsToApprove::class.java)
        }
        return null
    }

    fun createTransaction(to: Address, amount: Long, trunk: Hash, branch: Hash): Transaction? {
        if (neighbors.isEmpty() || account == null) {
            return null
        }

        val transactionObject = TransactionObject()
        transactionObject.address = to
        transactionObject.value = amount
        transactionObject.trunk_transaction = trunk
        transactionObject.branch_transaction = branch
        transactionObject.timestamp = System.currentTimeMillis()
        transactionObject.data_type = TransactionType.Full

        val transaction = Transaction(transactionObject)
        val mwm = 16 // 32
        transaction.obj.nonce = transaction.findNonce(mwm)
        transaction.obj.hash = transaction.calculateHash()
        synchronized(lock) {
            transaction.obj.signature = transaction.calculateSignature(account!!.privateKey, account!!.publicKey)
            transaction.obj.signature_pubkey = account!!.publicKey.clone()
        }

        return transaction
    }

    fun broadcastTransaction(transaction: Transaction): Boolean {
        if (neighbors.isEmpty()) {
            return false
        }

        val obj = BroadcastTransaction(transaction.obj)
        val json = API.gson.toJsonTree(obj)
        json.asJsonObject.addProperty("method", "broadcastTransaction")
        println("Sending tx ${String(Hex.encode(transaction.obj.hash))}")
        val resp = sendRequest(json, neighbors[0])
        return resp != null
    }

    fun sendCoins(to: Address, amount: Long, callback: (Boolean) -> Unit) {
        thread {
            val txs = getTransactionToApprove()
            if (txs != null) {
                val transaction = createTransaction(to, amount, txs.trunk, txs.branch)
                if (transaction != null) {
                    callback(broadcastTransaction(transaction))
                    return@thread
                }
            }

            callback(false)
        }
    }

    fun getAddressTransactionHashes(address: Address): LinkedList<Hash>? {
        if (neighbors.isEmpty()) return null

        val obj = FindTransactions(LinkedList(arrayListOf(address)), LinkedList(), LinkedList())
        val json = API.gson.toJsonTree(obj)
        json.asJsonObject.addProperty("method", "findTransactions")
        val resp = sendRequest(json, neighbors[0])

        if (resp != null) {
            val respObj = API.gson.fromJson(resp, FoundedTransactions::class.java)
            return respObj.hashes
        }
        return null
    }

    fun getTransactions(hashes: LinkedList<Hash>): LinkedList<TransactionObject>? {
        if (neighbors.isEmpty()) return null

        val obj = GetTransactionsData(hashes)
        val json = API.gson.toJsonTree(obj)
        json.asJsonObject.addProperty("method", "getTransactionsData")
        val resp = sendRequest(json, neighbors[0])

        if (resp != null) {
            val txs = LinkedList<TransactionObject>()
            val respObj = API.gson.fromJson(resp, TransactionsData::class.java)
            if (respObj != null) {
                for (txBase64 in respObj.transactions) {
                    val bytes = Base64.getDecoder().decode(txBase64)
                    val buffer = SerializedBuffer(bytes.size)
                    buffer.writeBytes(bytes)
                    buffer.position(0)
                    val tx = TransactionObject()
                    tx.readParams(buffer, true)
                    txs.add(tx)
                }
            }
            return txs
        }
        return null
    }

    private class AddressSerializer : JsonSerializer<Address> {
        override fun serialize(p0: Address?, p1: Type?, p2: JsonSerializationContext?): JsonElement {
            return JsonPrimitive("P" + String(Hex.encode(p0?.inner)).toUpperCase())
        }
    }

    private class AddressDeserializer : JsonDeserializer<Address> {
        override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): Address? {
            return Address(Hex.decode(p0?.asJsonPrimitive?.asString))
        }
    }

    private class HashSerializer : JsonSerializer<Hash> {
        override fun serialize(p0: Hash?, p1: Type?, p2: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(String(Hex.encode(p0!!)))
        }
    }

    private class HashDeserializer : JsonDeserializer<Hash> {
        override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): Hash? {
            return Hex.decode(p0?.asJsonPrimitive?.asString)
        }
    }

    private class PublicKeySerializer : JsonSerializer<PublicKey> {
        override fun serialize(p0: PublicKey?, p1: Type?, p2: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(String(Hex.encode(p0!!)))
        }
    }

    private class PublicKeyDeserializer : JsonDeserializer<PublicKey> {
        override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): PublicKey? {
            return Hex.decode(p0?.asJsonPrimitive?.asString)
        }
    }

    private class PrivateKeySerializer : JsonSerializer<PrivateKey> {
        override fun serialize(p0: PrivateKey?, p1: Type?, p2: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(String(Hex.encode(p0!!)))
        }
    }

    private class PrivateKeyDeserializer : JsonDeserializer<PrivateKey> {
        override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): PrivateKey? {
            return Hex.decode(p0?.asJsonPrimitive?.asString)
        }
    }

    private class SignatureSerializer : JsonSerializer<Signature> {
        override fun serialize(p0: Signature?, p1: Type?, p2: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(String(Hex.encode(p0!!)))
        }
    }

    private class SignatureDeserializer : JsonDeserializer<Signature> {
        override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): Signature? {
            return Hex.decode(p0?.asJsonPrimitive?.asString)
        }
    }

    private class TransactionTypeSerializer : JsonSerializer<TransactionType> {
        override fun serialize(p0: TransactionType?, p1: Type?, p2: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(p0!!.ordinal)
        }
    }

    private class TransactionTypeDeserializer : JsonDeserializer<TransactionType> {
        override fun deserialize(p0: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): TransactionType? {
            val v = p0?.asJsonPrimitive?.asInt
            return when (v) {
                0 -> TransactionType.HashOnly
                1 -> TransactionType.Full
                else -> null
            }
        }
    }
}

class GetNodeInfo()

class NodeInfo(val name: String)

class AttachTransaction(val transaction: TransactionObject)

class BroadcastTransaction(val transaction: TransactionObject)

class GetTransactionsToApprove(val depth: Int, val num_walks: Int, val reference: Hash)

class TransactionsToApprove(val trunk: Hash, val branch: Hash)

class GetBalances(val addresses: LinkedList<Address>, val tips: LinkedList<Hash>, val threshold: Byte)

class Balances(val balances: LinkedList<Long>)

class GetInclusionStates(val transactions: LinkedList<Hash>, val tips: LinkedList<Hash>)

class InclusionStates(val transactions: LinkedList<Hash>, val tips: LinkedList<Hash>)

class GetTips
class Tips(val hashes: LinkedList<Hash>)

class FindTransactions(val addresses: LinkedList<Address>, val tags: LinkedList<Hash>, val approvees: LinkedList<Hash>)
class FoundedTransactions(val hashes: LinkedList<Hash>)

class GetTransactionsData(val hashes: LinkedList<Hash>)
class TransactionsData(val transactions: LinkedList<String>)