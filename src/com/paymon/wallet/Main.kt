package com.paymon.wallet

import com.google.gson.JsonParser
import com.paymon.wallet.net.API
import com.paymon.wallet.utils.BACKUP_VERSION
import com.paymon.wallet.utils.WalletAccount
import com.paymon.wallet.utils.restoreFromBackup
import org.bouncycastle.util.encoders.Hex
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.swing.JOptionPane
import javax.swing.JPanel
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.system.exitProcess

var authForm = CreateNewWallet()
var walletForm = WalletForm()
var loadForm = LoadExistWallet()
var tx = TransactionForm()
var jsonSave = JsonFileSave()
var running = true
val HASH_SIZE = 20
val ADDRESS_SIZE = HASH_SIZE + 1
val ADDRESS_NULL = Address(ByteArray(ADDRESS_SIZE))
val HASH_NULL = ByteArray(HASH_SIZE)
val api = API()

fun main(args: Array<String>) {
    initListeners()

    thread {
        updateThread()
    }
}

fun initListeners() {
    authForm.createNewWalletButton.addActionListener {
        if (authForm.createButtonHandler()) {
            authForm.contentPane = jsonSave.contentPane
            authForm.repaintMainPanel()
        }
    }

    authForm.loadWalletButton.addActionListener {
        authForm.dispose()
        val path = loadForm.loadFilePath()
        if(path != null){
            loadForm.setFileExplorerText(path)
        }
        loadForm.isVisible = true
    }

    loadForm.loadButton.addActionListener {
        if (loadForm.loadButtonHandler()) {
            val acc = restoreFromBackup(loadForm.password, loadForm.path)
            if (acc != null) {
                if (acc.version == 1) {
                    JOptionPane.showMessageDialog(loadForm,
                            "This backup file is obsolete. " +
                                    "Please create new wallet account.")
                    return@addActionListener
                } else if (acc.version < BACKUP_VERSION) {
                    println("Your backup version (${acc.version}) is too old from the latest one ($BACKUP_VERSION)")
                }
                api.account = acc
                loadForm.saveFilePath(loadForm.path)
                updateAddress()
                updateBalance()
                loadForm.dispose()
                walletForm.isVisible = true
            }
        }
    }

    loadForm.backButton.addActionListener {
        loadForm.dispose()
        authForm.isVisible = true
        authForm.clear()
    }

    walletForm.createNewTransactionButton.addActionListener {
        updateAddress()
        updateBalance()
        walletForm.contentPane = tx.contentPane
        tx.clear()
        walletForm.repaintMainPanel()
    }

    tx.backToWalletPageButton.addActionListener {
        walletForm.contentPane = walletForm.panel
        walletForm.repaintMainPanel()
    }

    jsonSave.backButton.addActionListener {
        authForm.contentPane = authForm.panel
        authForm.repaintMainPanel()
    }

    jsonSave.nextButton.addActionListener {

        if (jsonSave.checkBoxHandler() && jsonSave.filePathHandler()) {
            val password = authForm.password
            createBackup(password, jsonSave.filePath)
            api.account = restoreFromBackup(authForm.password, jsonSave.filePath + ".json")
            updateAddress()
            updateBalance()
            loadForm.saveFilePath(jsonSave.filePath + ".json")
            authForm.dispose()
            authForm.contentPane = authForm.panel
            walletForm.isVisible = true

        }else{
            val message: String
            if(!jsonSave.checkBoxHandler() && !jsonSave.filePathHandler()) {
                message = "<html>Please, select a folder<br> and agree with the conditions"
            }else{
                if (!jsonSave.checkBoxHandler()) {
                    message = "Please, agree with the conditions"
                }else {
                    if (!jsonSave.filePathHandler()) {
                        message = "Please, select a folder"
                    }else{
                        message = ""
                    }
                }
            }
            jsonSave.showExceptionMessage(true, message)
        }


    }
    tx.sendButton.addActionListener {
        val bal: Int
        val address = api.account?.address
        if (address != null) {
            val balance = api.getBalanceRequest(address)
            if (balance != null) {
                bal = balance.toInt()
            }else{
                bal = 0
            }
        }else{
            bal = 0
        }

        if (tx.txHandler(bal)) {
            if(walletForm.glassPane != null) {
                walletForm.glassPane.isVisible = true
                walletForm.repaintMainPanel()
            }else{
                println("Nullable TransactionForm GlassPane")
            }
            api.sendCoins(Address(tx.recipientAddress), tx.amount.toLong()) {
                walletForm.glassPane.isVisible = false
                walletForm.repaintMainPanel()
                if(!it){
                    tx.showExceptionMessage(!it, "Transaction error")
                }else{
                    tx.showExceptionMessage(Color(0x45ba56), "Transaction sent")
                }
            }
        }
    }
    walletForm.logoutToolbarButton.addActionListener{
        walletForm.dispose()
        walletForm = WalletForm()
        loadForm = LoadExistWallet()
        authForm = CreateNewWallet()
        initListeners()
        authForm.isVisible = true
    }

}

fun updateAddress() {
    walletForm.setAddress(api.account?.address.toString())
    tx.setAddress(api.account?.address.toString())
}

fun updateBalance() {
    val address = api.account?.address
    if (address != null) {
        val balance = api.getBalanceRequest(address)
        if (balance != null) {
            walletForm.setBalance(balance.toInt())
            tx.setBalance(balance.toInt())
        }
    }
}

fun updateThread() {
    while (running) {
        try {
            val addr = api.account?.address
            if (addr != null) {
                walletForm.setAddress(addr.toString())
                val balance = api.getBalanceRequest(addr)
                if (balance != null) {
                    walletForm.setBalance(balance.toInt())
                }
                println("Current balance: $balance")
                val txHashes = api.getAddressTransactionHashes(addr)
                if (txHashes != null) {
                    val txs = api.getTransactions(txHashes)
                    if (txs != null) {
                        val txInfos : ArrayList<TransactionInfoInWalletForm> = ArrayList()
                        for (tx in txs) {
                            val from = addressFromPublicKey(tx.signature_pubkey)
                            val txInfo = TransactionInfoInWalletForm(String(Hex.encode(tx.hash)),
                                    from.toString(),
                                    if(tx.address.toString() == addr.toString()) {
                                        "You"
                                    }else{
                                        tx.address.toString()
                                    },
                                    tx.value.toInt(),
                                    Date(tx.timestamp * 1000))
                            txInfo.isConfirmed = true
                           txInfos.add(txInfo)
                        }
                        sortTxInfo(txInfos)
                        walletForm.setList(txInfos)
                        walletForm.showExceptionMessage(false, "")
                    }
                }
                walletForm.updateJListPanel()
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            walletForm.showExceptionMessage(true, e.message)
        }
        Thread.sleep(10_000)
    }
}
fun sortTxInfo(list: ArrayList<TransactionInfoInWalletForm>){
    Collections.sort(list, TransactionInfoInWalletForm.COMPARE_BY_DATE.reversed())
}
fun buckupTest() {
    val backup = api.account?.createBackup("123456789")
    try {
        Files.write(Paths.get("backup.json"), backup.toString().toByteArray())
    } catch (e: IOException) {
        println("Failed to create backup")
    }
    val bu = JsonParser().parse(String(Files.readAllBytes(Paths.get("backup.json"))))
    restoreFromBackup(bu, "123456789")
}

fun createBackup(password: String, path_name: String): File? {
    val splitted = path_name.split("\\.")
    val pathJson: String
    //TODO
    if (splitted.size > 1) {
        if (splitted[splitted.size - 1] != "json") {
            pathJson = path_name.plus(".json")
        } else {
            pathJson = path_name
        }
    } else {
        pathJson = path_name.plus(".json")
    }

    val sk = NTRUMLSNative.generateKeyPair().privateKey
    println(String(Hex.encode(sk)))
    api.account = WalletAccount(sk, BACKUP_VERSION)
    val backup = api.account?.createBackup(password)
    val file: File
    return try {
        val path = Paths.get(pathJson)
        Files.write(path, backup.toString().toByteArray())
        file = path.toFile()
        file
    } catch (e: IOException) {
        println("Failed to create backup")
        null
    }
}

fun getPrivateKey(): ByteArray {
    val key = api.account?.privateKey
    if (key != null) {
        return key
    } else {
        println("Failed to get key")
    }
    return "Empty key".toByteArray()
}

fun restoreFromBackup(password: String, path: String): WalletAccount? {
    try {
        val bu = JsonParser().parse(String(Files.readAllBytes(Paths.get(path))))
        return restoreFromBackup(bu, password)
    }catch (e:Exception){
        println("Error: ${e.message}")
        loadForm.showExceptionMessage(true, "Incorrect password or backup file")
    }
    return null
}