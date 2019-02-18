package org.paymon.wallet

import com.google.gson.JsonParser
import com.paymon.wallet.net.API
import com.paymon.wallet.net.TokenContract
import org.bouncycastle.jce.interfaces.ECPrivateKey
import org.paymon.wallet.utils.BACKUP_VERSION
import org.paymon.wallet.utils.WalletAccount
import org.paymon.wallet.utils.restoreFromBackup
import org.bouncycastle.util.encoders.Hex
import java.awt.Color
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.swing.JList
import javax.swing.JOptionPane
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

var authForm = CreateNewWallet()
var walletForm = WalletForm()
var loadForm = LoadExistWallet()
var tx = TransactionForm()
var jsonSave = JsonFileSave()
var running = true
val HASH_SIZE = 32
val ADDRESS_SIZE = 20 + 1
val ADDRESS_NULL = Address(ByteArray(ADDRESS_SIZE))
val HASH_NULL = ByteArray(HASH_SIZE)
val api = API()

fun main(args: Array<String>) {
    //b43707099d126bb7734ff228825335eb4d9c1fd4ed14bc438c0cd3c93dbb2b681e6a896e24355d50e15b3625b68f26a1181f6bd72607a2c7f33f6ec2786e4102
    val secp = Secp256k1()
    val sk = Secp256k1.generateSecretKeyFromBytes(Hex.decode("68f7b4aa14cc6ef6bc4658f426a51ce52eb035b77a5e7497388e11fc9bf1a711"))
    val pk = Secp256k1.generatePublicKeyFromSecretKey(sk)
    val msg = Hex.decode("ece17ed9e985c696223bcee70295f681ac2d19e104ddbf0f5df6d9425c91f809")
    val sig = secp.sign(sk, msg)
    println("pk=" + Hex.toHexString(pk.q.xCoord.encoded) + "  " + Hex.toHexString(pk.q.yCoord.encoded))
    println("sig=" + Hex.toHexString(sig.r.toByteArray()) + "  " + Hex.toHexString(sig.r.toByteArray()))
    println("msg=" + Hex.toHexString(msg))
    println(secp.verify(pk, sig, msg))
//    println(Hex.toHexString(pk.q.xCoord.encoded))
//    println(Hex.toHexString(pk.q.yCoord.encoded))

//    val sk = secp.generateKeypair().first//Secp256k1.generateSecretKeyFromBytes(Hex.decode("01"))
//    println(String(Hex.encode(sk.d.toByteArray())))
//    val wallet = WalletAccount(sk, 3)
//    println(wallet.address)
//    createBackup(wallet, "80758075", "C:\\Development\\pmnc.json")

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
        if (path != null) {
            loadForm.setFileExplorerText(path)
        }
        loadForm.isVisible = true
    }

    loadForm.loadButton.addActionListener {
        if (loadForm.loadButtonHandler()) {
            val acc = restoreFromBackup(loadForm.password, loadForm.path)
            if (acc != null) {
                if (acc.version == 1) {
                    JOptionPane.showMessageDialog(
                        loadForm,
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
                loadForm.showLoadPane()
                loadForm.glassPane = loadForm.processPanel
                loadForm.glassPane.isVisible = true
                loadForm.repaintMainPanel()
                for (i in 0..100) {
                    loadForm.progressBar.value = i
                    Thread.sleep(100)
                }
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

            val secp = Secp256k1()
            val sk = secp.generateKeypair().first
//            println(String(Hex.encode(sk.encoded)))
            val newAcc = WalletAccount(sk, BACKUP_VERSION)
            api.account = newAcc
            createBackup(newAcc, password, jsonSave.filePath)
            api.account = restoreFromBackup(authForm.password, jsonSave.filePath + ".json")
            updateAddress()
            updateBalance()
            loadForm.saveFilePath(jsonSave.filePath + ".json")
            authForm.dispose()
            authForm.contentPane = authForm.panel
            walletForm.isVisible = true

        } else {
            val message: String
            if (!jsonSave.checkBoxHandler() && !jsonSave.filePathHandler()) {
                message = "<html>Please, select a folder<br> and agree with the conditions"
            } else {
                if (!jsonSave.checkBoxHandler()) {
                    message = "Please, agree with the conditions"
                } else {
                    if (!jsonSave.filePathHandler()) {
                        message = "Please, select a folder"
                    } else {
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
            } else {
                bal = 0
            }
        } else {
            bal = 0
        }

        if (tx.txHandler(bal)) {
            walletForm.glassPane = walletForm.processPanel
            walletForm.glassPane.isVisible = true
            walletForm.repaintMainPanel()

            api.sendCoins(Address(tx.recipientAddress), tx.amount.toLong()) {
                walletForm.glassPane.isVisible = false
                walletForm.repaintMainPanel()
                if (!it) {
                    tx.showExceptionMessage(!it, "Transaction error")
                } else {
                    tx.showExceptionMessage(Color(0x45ba56), "Transaction sent")
                }
            }
        }
    }

    walletForm.logoutToolbarButton.addActionListener {
        walletForm.dispose()
        walletForm = WalletForm()
        loadForm = LoadExistWallet()
        authForm = CreateNewWallet()
        initListeners()
        authForm.isVisible = true
    }

    walletForm.balanceButton.addActionListener {
        createContract()
    }
}

fun createContract() {
    api.createContractRequest(TokenContract("Test", "TST", 10000, 1_000_000, HashMap(), HashMap()))
}

fun updateAddress() {
    walletForm.setAddress(api.account?.address.toString())
    tx.setAddress(api.account?.address.toString())
}

fun updateBalance() {
    val address = api.account?.address
    if (address != null) {
        try {
            val balance = api.getBalanceRequest(address)
            if (balance != null) {
                val b = balance.toInt()
                walletForm.setBalance(b)
                tx.setBalance(b)
            }
        } catch (e: Exception) {
            walletForm.setBalance(0)
            tx.setBalance(0)
        }
    }
}

fun updateThread() {
    val txInfos: ArrayList<TransactionInfoInWalletForm> = ArrayList()
    while (running) {
        try {
            txInfos.clear()
            val addr = api.account?.address
            if (addr != null) {
                println("privkey=" + String(Hex.encode(api.account!!.privateKey.d.toByteArray())))

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
                        for (tx in txs) {
                            val from = Address.fromPublicKey(tx.signature_pubkey!!)
                            val txInfo = TransactionInfoInWalletForm(String(Hex.encode(tx.hash)),
                                    from.toString(),
                                    if (tx.address.toString() == addr.toString()) {
                                        "You"
                                    } else {
                                        tx.address.toString()
                                    },
                                    if (tx.address.toString() == addr.toString()) {
                                        tx.value.toInt()
                                    } else {
                                        -tx.value.toInt()
                                    },
                                    Date(tx.timestamp * 1000))
                            txInfo.isConfirmed = true
                            txInfos.add(txInfo)
                        }
                        sortTxInfo(txInfos)
                        val mouseListener = object : MouseAdapter() {
                            override fun mouseClicked(mouseEvent: MouseEvent) {
                                val theList = mouseEvent.source as JList<*>
                                if (mouseEvent.clickCount == 2) {
                                    val index = theList.locationToIndex(mouseEvent.point)
                                    if (index >= 0) {
                                        if (index < txInfos.size) {
                                            walletForm.showTxInfo(txInfos[index])
                                        } else {
                                            println("Index $index out-of-bounds")
                                        }
                                        walletForm.glassPane.isVisible = true
                                        println("selected tx index $index")
                                    }
                                }
                            }
                        }
                        walletForm.setList(txInfos)
                        walletForm.showExceptionMessage(false, "")
                        walletForm.updateJListPanel(mouseListener)
                    }
                } else {
                    walletForm.initJListPanel()
                }
            }
        } catch (e: Exception) {
            println("Error: $e ${e.message}")
            walletForm.showExceptionMessage(true, "$e ${e.message}")
        }
        Thread.sleep(10_000)
    }
}

fun sortTxInfo(list: ArrayList<TransactionInfoInWalletForm>) {
    Collections.sort(list, TransactionInfoInWalletForm.COMPARE_BY_DATE.reversed())
}

fun buckupTest() {
    val backup = api.account?.generateBackup("123456789")
    try {
        Files.write(Paths.get("backup.json"), backup.toString().toByteArray())
    } catch (e: IOException) {
        println("Failed to create backup")
    }
    val bu = JsonParser().parse(String(Files.readAllBytes(Paths.get("backup.json"))))
    restoreFromBackup(bu, "123456789")
}

fun createBackup(wallet: WalletAccount, password: String, path: String): File? {
    var pathJson = path
    if (!pathJson.endsWith(".json")) {
        pathJson += ".json"
    }

    val backup = wallet.generateBackup(password)
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

fun getPrivateKey(): ECPrivateKey? {
    return api.account?.privateKey
}

fun restoreFromBackup(password: String, path: String): WalletAccount? {
    try {
        val bu = JsonParser().parse(String(Files.readAllBytes(Paths.get(path))))
        return restoreFromBackup(bu, password)
    } catch (e: Exception) {
        println("Error: ${e.message}")
        loadForm.showExceptionMessage(true, "Incorrect password or backup file")
    }
    return null
}