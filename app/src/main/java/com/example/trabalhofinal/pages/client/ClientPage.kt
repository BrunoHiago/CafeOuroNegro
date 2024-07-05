package com.example.trabalhofinal.pages.client

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.trabalhofinal.R
import com.example.trabalhofinal.classes.clients.Client
import com.example.trabalhofinal.classes.clients.ClientListViewModel
import com.example.trabalhofinal.elements.SwipeBox
import com.example.trabalhofinal.elements.TopBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientPage(navController: NavHostController, clientViewModel: ClientListViewModel) {

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
            title = "Clientes",
            isBack = false,
            onBack = { /*TODO*/ },
            onAction = { navController.navigate("AddClient") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.adicionar_usuario),
                    contentDescription = "AddUser",
                    tint = Color.White
                )
            }
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
                    SwipeBox(
                        onDelete = {
                            clientViewModel.deleteClient(client)
                            println("Deletado $client")
                        },
                        onEdit = {
                            clientViewModel.setSelectedClient(client)
                            navController.navigate("EditClient")
                        },
                        modifier = Modifier.animateItemPlacement()
                    ) { innerModifier ->
                        ClientItem(client = client, clientViewModel, navController, innerModifier)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(110.dp))
                }
            }
        }
    }
}


@Composable
fun ClientItem(
    client: Client,
    clientViewModel: ClientListViewModel,
    navController: NavHostController,
    modifier: Modifier,
    onClientClick: () -> Unit = {
        clientViewModel.setSelectedClient(client)
        navController.navigate("ClientDetails")
    }
) {

    ListItem(modifier = modifier.clickable {
        onClientClick()
    }, headlineContent = {
        Text(
            text = client.name,
            color = Color.White,
            fontSize = 20.sp
        )
    }, supportingContent = {
        Text(
            text = client.cpf,
            color = Color.LightGray,
            fontSize = 15.sp
        )
    }, leadingContent = {
        if (client.userPhotoUrl != null) {
            AsyncImage(
                model = client.userPhotoUrl,
                contentDescription = "UserPhoto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)

            )
        } else {
            ClientPhotoDefault()
        }

    }, colors = ListItemDefaults.colors(
        containerColor = Color(0xFF2c1a12),
        headlineColor = Color.White,
    )
    )
}


@Composable
fun ClientPhotoDefault() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color(0XFF6A3F14)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "UserPhoto",
            modifier = Modifier.size(50.dp),
            tint = Color.Black
        )
    }
}