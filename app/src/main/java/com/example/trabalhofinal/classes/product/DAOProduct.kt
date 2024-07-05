package com.example.trabalhofinal.classes.product

import android.graphics.Bitmap
import android.util.Log
import com.example.trabalhofinal.classes.Database
import com.example.trabalhofinal.classes.clients.Client
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class DAOProduct {

    private val collection = Database().getProductCollection()

    suspend fun addProduct(product: Product):Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val serialNumber = getNextSerialNumberProduct()
                product.id = serialNumber
                collection.document(serialNumber.toString()).set(product)
                true
                } catch (e: Exception) {
                println(e.message)
                false
            }
        }

    }

    suspend fun getProduct(id: Int): Product? {
        return try {
            val document = collection.document(id.toString())
            val snapshot = document.get().await()
            snapshot.toObject(Product::class.java)
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun getProducts(): List<Product> {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Product::class.java)
        } catch (e: Exception) {
            println(e.message)
            emptyList()
        }
    }

    fun deleteProduct(id: Int) {
        collection.document(id.toString()).delete().addOnFailureListener {
            println(it.message)
        }
    }

    suspend fun updateProduct(product: Product):Boolean {
        Log.d("Product", product.toString())
        return withContext(Dispatchers.IO) {
            try {
                collection.document(product.id.toString()).set(product)
                true
            } catch (e: Exception) {
                println(e.message)
                false
            }
        }
    }

     private suspend fun getNextSerialNumberProduct(): Int {
        val db = Database().db
        val docRef = Database().getNumberSerialCollection().document("product_id")
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

    suspend fun uploadPhoto(product: Product, bitmap: Bitmap):Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val storageRef = Firebase.storage.reference

                val imageName = "image_${product.id}.jpg"
                val imageRef = storageRef.child("/images/product/${imageName}")

                imageRef.putBytes(bitmapToByteArray(bitmap)).await()
                product.productPhotoUrl = imageRef.downloadUrl.await().toString()
                collection.document(product.id.toString()).set(product).await()
                true
            } catch (e: Exception) {
                println(e.message)
                false
            }
        }
    }


    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

}