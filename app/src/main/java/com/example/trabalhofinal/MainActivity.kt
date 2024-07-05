package com.example.trabalhofinal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trabalhofinal.classes.clients.ClientListViewModel
import com.example.trabalhofinal.classes.order.OrderListViewModel
import com.example.trabalhofinal.classes.product.ProductListViewModel
import com.example.trabalhofinal.pages.client.AddClientPage
import com.example.trabalhofinal.pages.product.AddProductPage
import com.example.trabalhofinal.pages.client.ClientDetailsPage
import com.example.trabalhofinal.pages.client.ClientPage
import com.example.trabalhofinal.pages.client.EditClientPage
import com.example.trabalhofinal.pages.HomePage
import com.example.trabalhofinal.pages.order.EditOrderPage
import com.example.trabalhofinal.pages.order.OrderPage
import com.example.trabalhofinal.pages.order.SelectClientPage
import com.example.trabalhofinal.pages.order.SelectProductPage
import com.example.trabalhofinal.pages.product.EditProductPage
import com.example.trabalhofinal.pages.product.ProductDetailsPage
import com.example.trabalhofinal.pages.product.ProductPage
import com.example.trabalhofinal.ui.theme.TrabalhoFinalTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory


class MainActivity : ComponentActivity() {
//    val navController = rememberNavController()
//    val openDialog = remember { mutableStateOf(false) }
//    val activity = LocalContext.current as Activity
//    val sharedViewModel = viewModel(SharedViewModel::class.java)
//    val viewModel = FarmsListViewModel(LocalContext.current)
//    val farms by viewModel.farms.observeAsState()
//    val currentRoute = CurrentRoute(navController)
//    val context = LocalContext.current


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            TrabalhoFinalTheme {
                Screen()


                }
            }
        }

    
}

@Composable
fun Screen () {

    val navController = rememberNavController()
    val listGradientColor = listOf(
        Color(0xFF2c1a12),
        Color(0xFF2d1a12),
    )

    var showFab by rememberSaveable { mutableStateOf(true) }

    val clientViewModel = ClientListViewModel()
    val productViewModel = ProductListViewModel()
    val orderListViewModel = OrderListViewModel()

    Scaffold(
        floatingActionButton = {
            if (showFab){
                NavBar(navController = navController)
            }

        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(listGradientColor))
                .padding(it)
        ) {
            NavHost(navController, startDestination = "Home") {
                composable("Home") {
                    showFab = true
                    HomePage(navController)
                }
                composable("Clients") {
                    showFab = true
                    ClientPage(navController,clientViewModel)
                }
                composable("ClientDetails") {
                    showFab = false
                    ClientDetailsPage( clientViewModel, navController)
                }
                composable("AddClient") {
                    showFab = false
                    AddClientPage(navController,clientViewModel)
                }
                composable("EditClient") {
                    showFab = false
                    EditClientPage(navController, clientViewModel)
                }
                composable("Products") {
                    showFab = true
                    ProductPage(navController, productViewModel)
                }
                composable("AddProduct") {
                    showFab = false
                    AddProductPage(navController, productViewModel)
                }
                composable("EditProduct") {
                    showFab = false
                    EditProductPage(navController, productViewModel)
                }
                composable("ProductDetails") {
                    showFab = false
                    ProductDetailsPage(navController, productViewModel)
                }

                composable("Order") {
                    showFab = true
                    OrderPage(navController, orderListViewModel, clientViewModel)
                }
                composable("SelectClient") {
                    showFab = false
                    SelectClientPage(navController, clientViewModel)
                }
                composable("SelectProduct") {
                    showFab = false
                    SelectProductPage(navController, productViewModel, orderListViewModel, clientViewModel)
                }
                composable("EditOrder") {
                    showFab = false
                    EditOrderPage(navController, productViewModel, orderListViewModel, clientViewModel)
                }

            }
        }

    }


}

@Composable
fun NavBar(navController:NavHostController){
    Box(
        Modifier
            .height(100.dp)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .padding(start = 37.dp, bottom = 20.dp)
                .clip(CircleShape)
                .background(Color(106, 63, 20, 200)),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            AppBarItem(
                description = "Home",
                icon = Icons.Filled.Home,
                painter = null,
                color = Color.White
            ) {
                navController.navigate("Home")
            }
            AppBarItem(
                description = "Clientes",
                icon = Icons.Filled.Person,
                painter = null,
                color = Color.White
            ) {
                navController.navigate("Clients")
            }
            AppBarItem(
                description = "Produtos",
                icon = null,
                painter = painterResource(id = R.drawable.grao),
                color = Color.White
            ) {
                navController.navigate("Products")
            }
            AppBarItem(
                description = "Pedidos",
                icon = null,
                painter = painterResource(id = R.drawable.pedido),
                color = Color.White
            ) {
                navController.navigate("order")
            }

        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppBarItem(
    description: String,
    icon: ImageVector? = null,
    painter: Painter? = null,
    color: Color = Color.Black,
    onClick: () -> Unit
) {
    Box(Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
        IconButton(onClick = onClick, Modifier.fillMaxHeight()) {
            if (icon !== null) {
                Icon(icon, contentDescription = description, Modifier.scale(2f), tint = color)
            } else if (painter !== null) {
                Icon(
                    painter,
                    contentDescription = description,
                    Modifier.scale(2f),
                    tint = color
                )
            }

        }
    }
}