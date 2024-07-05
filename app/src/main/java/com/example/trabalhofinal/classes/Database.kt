package com.example.trabalhofinal.classes


import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Database {
    val db  = Firebase.firestore

    fun getClientCollection() = db.collection("Client")
    fun getProductCollection() = db.collection("Product")
    fun getOrderCollection() = db.collection("Order")
    fun getOrderItemsCollection() = db.collection("OrderItems")
    fun getNumberSerialCollection() = db.collection("NumberSerial")
}

