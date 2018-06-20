package com.paymon.wallet

import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JButton
import kotlin.concurrent.thread

var authForm = CreateNewWallet()
var walletForm = WalletForm()
var running = true

fun main(args: Array<String>) {
    initListeners()

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
            println("top")
        }
    }

    walletForm.transactionListModel.addElement(TransactionElement(1))
    walletForm.transactionListModel.addElement(TransactionElement(2))
    walletForm.transactionListModel.addElement(TransactionElement(3))
    walletForm.transactionListModel.addElement(TransactionElement(4))

    walletForm.toolbar.isFloatable = false
    walletForm.toolbar.addSeparator()
    var button = JButton("File")
    walletForm.toolbar.add(button)
    walletForm.toolbar.addSeparator()
    var button2 = JButton("Options")
    walletForm.toolbar.add(button2)
    walletForm.toolbar.addSeparator()
}

fun updateThread() {
    while (running) {
        println("update")
        Thread.sleep(1000)
    }
}