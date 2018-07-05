package com.paymon.wallet

import com.paymon.wallet.net.API
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JButton
import kotlin.concurrent.thread
import org.bouncycastle.util.encoders.Hex

var authForm = CreateNewWallet()
var walletForm = WalletForm()
var running = true
val HASH_SIZE = 20
val ADDRESS_SIZE = HASH_SIZE + 1
val ADDRESS_NULL = Address(ByteArray(ADDRESS_SIZE))
val HASH_NULL = ByteArray(HASH_SIZE)
val api = API()

fun main(args: Array<String>) {
    api.privateKey = Hex.decode("8003FFFFF6882320DEEE46AC97069EC8C56928643907230EA3A80BA656312A5351768E1525CDDC40D661C4E6646FB5C0A9D52DB66A74C86F1ADD764AC3CF7BD66D16E9D619EF4090E996C350B75BA5CE856285775792700AF58203B65198012953514B5B0F1F86D8DDC8D6BB9ADF0A9665B6CADA1C6D166B93E704BDB063ACEAF6A519D6A58F88836E6B4AD431A576B13DBE59D4A603D833DAAD7EAF4AC5B48015522E1C3163A751EEAF34D8EE692806C88ABE6CB151DA79BE48C13CA894AC1DD3D4361B7F5574D1BC28754916B04849A066A8659CEEE9C334CEA0C327B99D458CC64257EC37C9B4216C9CE3469FD5B23DBC964488780E282790198443EA7A1F1FC824C51FDB7D18B5A6C188A2907446224B6C6FDCD264095E0BE053D293B544E22875470B55D58F5707EFD58E8DC5DDB475F25C5A660E63B202669524F02D4F973B5D4C2D52AC2C62BFCD5B54614F92F22B4B94E51E43AD0BEED8")
    api.publicKey = NTRUMLSNative.publicKeyFromPrivate(api.privateKey)
    api.address = addressFromPublicKey(api.publicKey!!)

    initListeners()

    api.sendCoins(Address("PE138221B1A9CBEFCEAF03E17934A7373D6289F0536"), 100) {
        println("Result $it")
    }

    thread {
        updateThread()
    }
}

fun initListeners() {
    authForm.createNewWalletButton.addMouseListener(object : MouseListener {
        override fun mouseReleased(e: MouseEvent?) {
        }

        override fun mouseEntered(e: MouseEvent?) {
        }

        override fun mouseClicked(e: MouseEvent?) {
        }

        override fun mouseExited(e: MouseEvent?) {
        }

        override fun mousePressed(e: MouseEvent?) {
            authForm.dispose()
            walletForm.isVisible = true
        }
    })

    walletForm.list1.addListSelectionListener {
        if (!it.valueIsAdjusting) {
            println("clicked")
        }
    }

    walletForm.toolbar.isFloatable = false
    walletForm.toolbar.addSeparator()
    var button = JButton("File")
    walletForm.toolbar.add(button)
    walletForm.toolbar.addSeparator()
    var button2 = JButton("Options")
    walletForm.toolbar.add(button2)
    walletForm.toolbar.addSeparator()

    updateAddress()
}

fun updateAddress() {
    walletForm.addressButton.text = api.address?.toString()
}

fun updateThread() {
    while (running) {
        val addr = api.address
        if (addr != null) {
            val balance = api.getBalanceRequest(addr)
            println("Current balance: $balance")
            val txHashes = api.getAddressTransactionHashes(addr)
            if (txHashes != null) {
//                for (hash in txHashes) {
//                    println("request hash=${String(Hex.encode(hash))}")
//                }
                val txs = api.getTransactions(txHashes)
            }
        }

        Thread.sleep(10000)
    }
}