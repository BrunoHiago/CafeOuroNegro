package com.example.trabalhofinal.classes.clients

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Exclude
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Client(
    var id: Int,
    var cpf: String,
    var name: String,
    var phone: String,
    var address: String,
    var instagram: String,
    var userPhotoUrl: String? = null

) {
    constructor() : this(0, "", "", "", "", "", null)
}

class ClientListViewModel() : ViewModel() {
    private val _clients = MutableLiveData<List<Client>>(emptyList())
    private var banco = DAOClient()
    val clients: LiveData<List<Client>> get() = _clients

    private val _selectedClient = MutableLiveData<Client>()
    val selectedFarm: LiveData<Client> get() = _selectedClient

    init {
        loadClients()
    }

    fun loadClients() {
        viewModelScope.launch(Dispatchers.IO) {
            _clients.postValue(banco.getClients())
        }
    }

    fun searchClients(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val listClient = banco.getClients()

            val filteredList = listClient.filter {
                it.name.contains(text, ignoreCase = true) || it.cpf.contains(text)
            }

            _clients.postValue(filteredList)
        }
    }


    fun addClient(client: Client) {
        viewModelScope.launch {
            val result = banco.addClient(client)
            if (result) {
                loadClients()
                Log.d("ClientListViewModel", "Cliente adicionado com sucesso")
            } else {
                Log.e("ClientListViewModel", "Erro ao adicionar cliente")
            }
        }
    }

    fun updateClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = banco.updateClient(client)
            if (result) {
                loadClients()
                Log.d("ClientListViewModel", "Cliente atualizado com sucesso")
            } else {
                Log.e("ClientListViewModel", "Erro ao atualizar cliente")
            }
        }
    }

    fun uploadImage(bitmap: Bitmap, client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = banco.uploadPhoto(client, bitmap)
            if (result) {
                loadClients()
                Log.d("ClientListViewModel", "Imagem atualizada com sucesso")
            } else {
                Log.e("ClientListViewModel", "Erro ao atualizar imagem")
            }
        }
    }

    fun deleteClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            banco.deleteClient(client.id)
            loadClients()
        }
    }

    fun setSelectedClient(client: Client) {
        _selectedClient.value = client
    }
}