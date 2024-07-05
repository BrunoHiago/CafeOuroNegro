package com.example.trabalhofinal.pages.order

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.trabalhofinal.R
import com.example.trabalhofinal.classes.clients.ClientListViewModel
import com.example.trabalhofinal.elements.TopBar
import com.example.trabalhofinal.pages.client.ClientItem

@Composable
fun SelectClientPage(navController: NavHostController, clientViewModel: ClientListViewModel) {
    val loadKey = remember { mutableIntStateOf(0) }

    LaunchedEffect(loadKey.intValue) {
        clientViewModel.loadClients()
    }
    val listClient = clientViewModel.clients.observeAsState().value
    var searchText by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
    ) {
        TopBar(
            title = "Seleciona o Clientes",
            isBack = true,
            onBack = {
                navController.navigate("Order")
            },
            onAction = {  },
            icon = { }
        )
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText; clientViewModel.searchClients(newText)
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = { Text("Buscar") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            placeholder = { Text("O que você procura?") },
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            if (listClient != null) {
                items(listClient, key = { it.id }) { client ->
                    ClientItem(client = client, clientViewModel, navController, Modifier){
                        clientViewModel.setSelectedClient(client)
                        navController.navigate("SelectProduct")
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(110.dp))
                }
            }
        }
    }
}


