package com.example.trabalhofinal.classes.clients

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.trabalhofinal.classes.Database
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class DAOClient() {
    private val collection = Database().getClientCollection()
    private val orderCollection = Database().getOrderCollection()
    var storageRef = FirebaseStorage.getInstance().reference


    private suspend fun getNextSerialNumber(): Int {
        val db = Database().db
        val docRef = Database().getNumberSerialCollection().document("client_id")
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

    suspend fun addClient(client: Client): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val serialNumber = getNextSerialNumber()
                client.id = serialNumber
                collection.document(serialNumber.toString()).set(client)
                true
            } catch (e: Exception) {
                println(e.message)
                false
            }
        }
    }


    suspend fun getClient(id: Int): Client? {
        return try {
            val document = collection.document(id.toString())
            val snapshot = document.get().await()
            snapshot.toObject(Client::class.java)
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun getClients(): List<Client> {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Client::class.java)
        } catch (e: Exception) {
            Log.e("TAG", "Error getting documents: ", e)
            emptyList()
        }
    }
    suspend fun deleteClient(id: Int) {
        collection.document(id.toString()).delete().addOnFailureListener {
            println(it.message)
        }
        val query: Query = orderCollection.whereEqualTo("clientid", id)
        val snapshot = query.get().await()

        for (document in snapshot.documents) {
            document.reference.delete().await()
        }
    }

    suspend fun updateClient(client: Client): Boolean {
        return try {
            val document = collection.document(client.id.toString())
            document.set(client).await()
            true
        } catch (e: Exception) {
            println(e.message)
            false
        }
    }

    suspend fun uploadPhoto(client: Client, bitmap: Bitmap):Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val storageRef = Firebase.storage.reference

                val imageName = "image_${client.id}.jpg"
                val imageRef = storageRef.child("/images/client/${imageName}")

                imageRef.putBytes(bitmapToByteArray(bitmap)).await()
                client.userPhotoUrl = imageRef.downloadUrl.await().toString()
                collection.document(client.id.toString()).set(client).await()
                true
            } catch (e: Exception) {
                println(e.message)
                false
            }
        }
    }


    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

}
