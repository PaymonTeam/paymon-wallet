package com.paymon.wallet

import java.util.*
import javax.swing.AbstractListModel

class TransactionElement(value: Long) {
}

class TransactionListModel : AbstractListModel<TransactionElement>() {
    private val elements = Vector<TransactionElement>()

    override fun getSize(): Int {
        return elements.size
    }

    override fun getElementAt(index: Int): TransactionElement {
        return elements[index]
    }

    fun addElement(element: TransactionElement) {
        val index = elements.size
        elements.addElement(element)
        fireIntervalAdded(this, index, index)
    }

}