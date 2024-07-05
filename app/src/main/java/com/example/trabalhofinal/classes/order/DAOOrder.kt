package com.example.trabalhofinal.classes.order

import com.example.trabalhofinal.classes.Database
import com.example.trabalhofinal.classes.clients.Client
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DAOOrder {
    private val collection = Database().getOrderCollection()

    suspend fun addOrder(order: Order):Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val serialNumber = getNextSerialNumberOrder()
                order.id = serialNumber
                collection.document(serialNumber.toString()).set(order)
                true
            } catch (e: Exception) {
                println(e.message)
                false
            }
        }
    }

    suspend fun getOrder(id: Int): Order? {
        return try {
            val document = collection.document(id.toString())
            val snapshot = document.get().await()
            snapshot.toObject(Order::class.java)
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun getOrders(): List<Order> {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Order::class.java)
        } catch (e: Exception) {
            println(e.message)
            emptyList()
        }
    }

    fun deleteOrder(id: Int) {
        collection.document(id.toString()).delete().addOnFailureListener {
            println(it.message)
        }
    }

    suspend fun updateOrder(order: Order):Boolean {
        return withContext(Dispatchers.IO) {
            try {
                collection.document(order.id.toString()).set(order)
                true
            } catch (e: Exception) {
                println(e.message)
                false
            }
        }
    }

    private suspend fun getNextSerialNumberOrder(): Int {
        val db = Database().db
        val docRef = Database().getNumberSerialCollection().document("order_id")
        return try {
            db.runTransaction { transaction ->
                val doc = transaction.get(docRef)
                val currentSerialNumber = doc.getLong("value") ?: 0
                val newSerialNumber = currentSerialNumber + 1
                transaction.update(docRef, "value", newSerialNumber)
                newSerialNumber.toInt()
            }.await()
        } catch (e: Exception) {
            println(e.message)
            -1
        }
    }
}