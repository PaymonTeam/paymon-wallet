package com.paymon.wallet

import com.google.gson.JsonParser
import com.paymon.wallet.net.API
import com.paymon.wallet.utils.WalletAccount
import com.paymon.wallet.utils.restoreFromBackup
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JButton
import kotlin.concurrent.thread
import org.bouncycastle.util.encoders.Hex
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess



var authForm = CreateNewWallet()
var walletForm = WalletForm()
var loadForm = LoadExistWallet()
var tx = TransactionForm()
var jsonSave = JsonFileSave()
var pkSave = PKSave()
var running = true
val HASH_SIZE = 20
val ADDRESS_SIZE = HASH_SIZE + 1
val ADDRESS_NULL = Address(ByteArray(ADDRESS_SIZE))
val HASH_NULL = ByteArray(HASH_SIZE)
val api = API()
fun main(args: Array<String>) {
    try {
        api.account = WalletAccount(Hex.decode("8003FFFFF6882320DEEE46AC97069EC8C56928643907230EA3A80BA656312A5351768E1525CDDC40D661C4E6646FB5C0A9D52DB66A74C86F1ADD764AC3CF7BD66D16E9D619EF4090E996C350B75BA5CE856285775792700AF58203B65198012953514B5B0F1F86D8DDC8D6BB9ADF0A9665B6CADA1C6D166B93E704BDB063ACEAF6A519D6A58F88836E6B4AD431A576B13DBE59D4A603D833DAAD7EAF4AC5B48015522E1C3163A751EEAF34D8EE692806C88ABE6CB151DA79BE48C13CA894AC1DD3D4361B7F5574D1BC28754916B04849A066A8659CEEE9C334CEA0C327B99D458CC64257EC37C9B4216C9CE3469FD5B23DBC964488780E282790198443EA7A1F1FC824C51FDB7D18B5A6C188A2907446224B6C6FDCD264095E0BE053D293B544E22875470B55D58F5707EFD58E8DC5DDB475F25C5A660E63B202669524F02D4F973B5D4C2D52AC2C62BFCD5B54614F92F22B4B94E51E43AD0BEED8"))
    } catch (e: Exception) {
        println(e.message)
        exitProcess(-1)
    }



    initListeners()

    api.sendCoins(Address("PE138221B1A9CBEFCEAF03E17934A7373D6289F0536"), 100) {
        println("Result $it")
    }

    thread {
        updateThread()
    }
}

fun initListeners() {
    authForm.createNewWalletButton.addActionListener(object : ActionListener{
        override fun actionPerformed(e: ActionEvent?) {
            if(authForm.createButtonHandler()) {
                authForm.contentPane = jsonSave.contentPane
                authForm.repaintMainPanel()
            }
        }
    })
    authForm.loadWalletButton.addActionListener(object : ActionListener{
        override fun actionPerformed(e: ActionEvent?) {
            authForm.dispose()
            loadForm.isVisible = true
        }
    })
    loadForm.loadButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            if (loadForm.loadButtonHandler()){
                api.account = restoreFromBackup(loadForm.getPassword(), loadForm.getPath())
                loadForm.dispose()
                walletForm.isVisible = true
            }
        }

    })
    loadForm.backButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            loadForm.dispose()
            authForm.isVisible = true
        }

    })
    walletForm.createNewTransactionButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            walletForm.contentPane = tx.contentPane
            walletForm.repaintMainPanel()
            walletForm.pack()
        }
    })
    tx.backToWalletPageButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
           walletForm.contentPane = walletForm.panel
            walletForm.repaintMainPanel()
            walletForm.setSize(480, 480);
        }
    })
    jsonSave.backButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            authForm.contentPane = authForm.panel
            authForm.repaintMainPanel()
        }
    })
    jsonSave.nextButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            if(jsonSave.checkBoxHandler()) {
                val password = authForm.password
                createBackup(password, jsonSave.filePath)
                authForm.contentPane = pkSave.contentPane
                authForm.repaintMainPanel()
                pkSave.privateKeyTextField.text = String(Hex.encode(getPrivateKey()))
            }
        }
    })
    pkSave.backButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            authForm.contentPane = jsonSave.contentPane
            authForm.repaintMainPanel()
        }
    })
    pkSave.finishButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            authForm.contentPane = authForm.panel
            authForm.repaintMainPanel()
        }
    })
    pkSave.copyButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            if(pkSave.privateKeyTextField.text != null) {
                pkSave.setClipboard(pkSave.privateKeyTextField.text)
            }
        }
    })


    //updateAddress()
}

fun updateAddress() {
    walletForm.addressButton.text = api.account?.address.toString()
}

fun updateThread() {
    while (running) {
        val addr = api.account?.address
        if (addr != null) {
            walletForm.setAddress(addr.toString())
            val balance = api.getBalanceRequest(addr)
            if(balance != null) {
                walletForm.setBalance(balance.toInt())
            }
            println("Current balance: $balance")
            val txHashes = api.getAddressTransactionHashes(addr)
            if (txHashes != null) {
//                for (hash in txHashes) {
//                    println("request hash=${String(Hex.encode(hash))}")
//                }
                val txs = api.getTransactions(txHashes)
                if(txs != null) {
                    walletForm.list.clear()
                    for (i in txs) {
                        walletForm.addToList(i.hash.toString(),
                                addr.toString(),
                                i.address.toString(),
                                i.value.toInt())
                    }
                }
            }

        }

        Thread.sleep(10000)
    }
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
fun createBackup(password: String, path_name: String): File?{
    val splitted = path_name.split("\\.")
    val pathJson: String
    //TODO
    if(splitted.size > 1) {
        if (splitted[splitted.size - 1] != "json"){
            pathJson = path_name.plus(".json")
        }else{
            pathJson = path_name
        }
    }else{
        pathJson = path_name.plus(".json")
    }
    val backup = api.account?.createBackup(password)
    val file: File
    return try{
        val path = Paths.get(pathJson)
        Files.write(path, backup.toString().toByteArray())
        file = path.toFile()
        return file
    } catch (e: IOException) {
        println("Failed to create backup")
        return null
    }
}
fun getPrivateKey(): ByteArray{
    val key = api.account?.privateKey
    if (key != null){
        return key
    }else{
        println("Failed to get key")
    }
    return "Empty key".toByteArray()
}
fun restoreFromBackup(password: String, path: String): WalletAccount?{
    val bu = JsonParser().parse(String(Files.readAllBytes(Paths.get(path))))
    return restoreFromBackup(bu, password)
}