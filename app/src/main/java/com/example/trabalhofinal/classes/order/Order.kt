package com.example.trabalhofinal.classes.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalhofinal.classes.clients.DAOClient
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Order(
    var id: Int,
    var clientId: Int,
    var orderDate: Timestamp,
    val totalAmount: Float,
    val products: List<OrderProduct>
){
    constructor(): this(-1,-1, Timestamp.now(),0f, emptyList())
}

data class OrderProduct(
    val productId: Int,
    val productName: String,
    val priceUnit: Float,
    var quantity: Int
){
    constructor():this(-1,"",1f,-1)
}

class OrderListViewModel() : ViewModel() {
    private val _orders = MutableLiveData<List<Order>>(emptyList())
    private var banco = DAOOrder()
    val orders: LiveData<List<Order>> get() = _orders

    private val _selectedOrder = MutableLiveData<Order>()
    val selectedOrder: LiveData<Order> get() = _selectedOrder

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = banco.getOrders()
            list.sortedBy { it.orderDate }
            _orders.postValue(list)
            println(_orders)
        }
    }

    fun searchOrders(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = DAOClient()
            val listOrders = banco.getOrders()
            val listClient = db.getClients()

            val filteredListClient = listClient.filter {
                it.name.contains(text, ignoreCase = true)
            }

            val filteredList = listOrders.filter {
                it.clientId in filteredListClient.map { client -> client.id }
            }

            filteredList.sortedByDescending { it.orderDate }
            _orders.postValue(filteredList)
        }
    }


    fun addOrder(order: Order) {
        viewModelScope.launch {
            val result = banco.addOrder(order)
            if (result) {
                loadOrders()
                Log.d("OrderViewModel", "Pedido adicionado com sucesso")
            } else {
                Log.e("OrderViewModel", "Erro ao adicionar pedido")
            }
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = banco.updateOrder(order)
            if (result) {
                loadOrders()
                Log.d("OrderViewModel", "Pedido atualizado com sucesso")
            } else {
                Log.e("OrderViewModel", "Erro ao atualizado pedido")
            }
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            banco.deleteOrder(order.id)
            loadOrders()
        }
    }

    fun setSelectedOrder(order: Order?) {
        _selectedOrder.value = order
    }
}