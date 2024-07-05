package com.example.trabalhofinal.pages.order

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.trabalhofinal.R
import com.example.trabalhofinal.classes.clients.Client
import com.example.trabalhofinal.classes.clients.ClientListViewModel
import com.example.trabalhofinal.classes.order.Order
import com.example.trabalhofinal.classes.order.OrderListViewModel
import com.example.trabalhofinal.classes.order.OrderProduct
import com.example.trabalhofinal.elements.SwipeBox
import com.example.trabalhofinal.elements.TopBar
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderPage(
    navController: NavHostController,
    viewModel: OrderListViewModel,
    clientListViewModel: ClientListViewModel
) {

    val listOrder = viewModel.orders.observeAsState().value
    val listClient = clientListViewModel.clients.observeAsState().value


    var searchText by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
    ) {
        TopBar(
            title = "Pedidos",
            isBack = false,
            onBack = { /*TODO*/ },
            onAction = { navController.navigate("SelectClient") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.soma),
                    contentDescription = "AddOrder",
                    modifier = Modifier.scale(3f),
                    tint = Color.White
                )
            }
        )
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText -> searchText = newText; viewModel.searchOrders(searchText)},
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
            if (listOrder != null)
                items(listOrder, key = { it.id }) { order->
                    if (listClient != null) {
                        SwipeBox(
                            onDelete = {
                                viewModel.deleteOrder(order)
                                println("Deletado $order")
                            },
                            onEdit = {
                                viewModel.setSelectedOrder(order)
                                navController.navigate("EditOrder")
                            },
                            modifier = Modifier.animateItemPlacement()
                        ){
                            OrderListItem(
                                order = order,
                                listClient.find { client -> client.id == order.clientId })

                        }

                    }
                }
            item {
                Spacer(modifier = Modifier.height(110.dp))
            }


        }
    }
}


@Composable
fun OrderListItem(order: Order, find: Client?) {
    var open by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { open = !open }
            .background(Color(0xFF2c1a12))
            .padding(10.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        if (find != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cliente: ${find.name}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
                Text(
                    text = order.id.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.LightGray
                    )
                )
            }
        }
        Text(
            text = "Data do Pedido: ${formatTimestamp(order.orderDate)}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 18.sp,
                color = Color.White
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        if (open) {
            HorizontalDividerCustom(color = Color.LightGray)
            ProductsSection(order.products)
        }
        Text(
            text = "Total: R$ ${"%.2f".format(order.totalAmount)}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 19.sp,
                color = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = if (open) TextAlign.End else TextAlign.Start
        )
    }
}

@Composable
fun ProductsSection(products: List<OrderProduct>) {
    Column(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        products.forEach { product ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${product.productName}: ${product.quantity}x ${product.priceUnit} = R$ ${("%.2f".format(product.priceUnit * product.quantity))}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,color = Color.White)
                )
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun formatTimestamp(timestamp: Timestamp): String {
    val date = Date(timestamp.toDate().time)
    val sdf = SimpleDateFormat("dd:MM:yyyy HH:mm:ss")
    return sdf.format(date)
}

@Composable
fun HorizontalDividerCustom(color: Color, thickness: Dp = 1.dp) {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = thickness,
        color = color
    )
}

