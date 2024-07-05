package com.example.trabalhofinal.classes.product

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalhofinal.classes.clients.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Product(
    var id: Int,
    var typeOfGrains: String,
    var roastingPoint: RoastingPoint,
    var price: Float,
    var blend: Boolean,
    var productPhotoUrl: String?,
) {
    constructor() : this(0, "", RoastingPoint.Clara, 0f, false, null)
}

enum class RoastingPoint {
    Clara, Media, Escura
}


class ProductListViewModel() : ViewModel() {
    private val _products = MutableLiveData<List<Product>>(emptyList())
    private var banco = DAOProduct()
    val products: LiveData<List<Product>> get() = _products

    private val _selectedProduct = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product> get() = _selectedProduct

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _products.postValue(banco.getProducts())
            Log.d("ProductListViewModel", "Dados carregados com sucesso"+_products.value)
        }
    }

    fun searchProducts(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val listClient = banco.getProducts()

            val filteredList = listClient.filter {
                it.typeOfGrains.contains(text, ignoreCase = true)
            }
            println(filteredList)
            _products.postValue(filteredList)
        }
    }


    fun addProducts(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = banco.addProduct(product)
            if (result) {
                loadProducts()
            }
        }
    }

    fun updateClient(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = banco.updateProduct(product)
            if (result) {
                loadProducts()
            }
        }
    }

    fun uploadImage(bitmap: Bitmap, product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = banco.uploadPhoto(product, bitmap)
            if (result) {
                loadProducts()
                Log.d("ProductListViewModel", "Imagem atualizada com sucesso")
            } else {
                Log.e("ProductListViewModel", "Erro ao atualizar imagem")
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            banco.deleteProduct(product.id)
            loadProducts()
        }
    }

    fun setSelectedProduct(product: Product) {
        _selectedProduct.value = product
    }
}