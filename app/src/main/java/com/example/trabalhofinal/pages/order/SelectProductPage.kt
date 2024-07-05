package com.example.trabalhofinal.pages.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.trabalhofinal.R
import com.example.trabalhofinal.classes.clients.ClientListViewModel
import com.example.trabalhofinal.classes.order.Order
import com.example.trabalhofinal.classes.order.OrderListViewModel
import com.example.trabalhofinal.classes.order.OrderProduct
import com.example.trabalhofinal.classes.product.Product
import com.example.trabalhofinal.classes.product.ProductListViewModel
import com.example.trabalhofinal.elements.TopBar
import com.example.trabalhofinal.pages.product.ProductPhotoDefault
import com.google.firebase.Timestamp

@Composable
fun SelectProductPage(
    navController: NavHostController,
    productViewModel: ProductListViewModel,
    orderListViewModel: OrderListViewModel,
    clientListViewModel: ClientListViewModel
) {
    val selectedOrder = orderListViewModel.selectedOrder.observeAsState().value
    val listProduct = productViewModel.products.observeAsState().value
    var searchText by remember { mutableStateOf("") }
    var total by remember { mutableFloatStateOf(0f) }
    val listOrderProduct = remember { mutableListOf<OrderProduct>() }
    val selectedClient = clientListViewModel.selectedFarm.observeAsState().value


    LaunchedEffect(key1 = listProduct?.firstOrNull()?.id) {
        listProduct?.forEach { product ->
            val orderProduct =
                OrderProduct(
                    productId = product.id,
                    productName = product.typeOfGrains,
                    priceUnit = product.price,
                    quantity = 0
                )
            listOrderProduct.add(orderProduct)
        }
        selectedOrder?.products?.forEach { orderProduct ->
            listOrderProduct.find { it.productId == orderProduct.productId }?.quantity =
                orderProduct.quantity
        }

    }

        fun calculateTotal() {
            total = 0f
            listOrderProduct.forEach { orderProduct ->
                total += orderProduct.priceUnit * orderProduct.quantity
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 25.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TopBar(
                title = "Pedido",
                isBack = true,
                onBack = {
                    navController.navigate("SelectClient")
                },
                onAction = { /*TODO*/ },
                icon = {}
            )
            OutlinedTextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText; productViewModel.searchProducts(
                    newText
                )
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
            LazyColumn(Modifier.fillMaxHeight(0.8f)) {
                if (listProduct != null)
                    items(listProduct, key = { it.id }) {
                        OrderItem(product = it, listOrderProduct) {
                            calculateTotal()
                        }
                    }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Color.LightGray),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: R$ " + "%.2f".format(total),
                    color = Color.Black,
                    fontSize = TextUnit(20F, TextUnitType.Sp),
                    modifier = Modifier.padding(start = 20.dp)
                )
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxHeight()
                        .width(100.dp)
                        .clickable {
                            listOrderProduct.filter { it.quantity != 0 }
                            if (selectedClient != null) {
                                orderListViewModel.addOrder(
                                    Order(
                                        id = 0,
                                        clientId = selectedClient.id,
                                        orderDate = Timestamp.now(),
                                        totalAmount = total,
                                        listOrderProduct
                                    )
                                )
                                orderListViewModel.setSelectedOrder(null)
                                navController.navigate("Order")
                            }

                        }, verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painterResource(id = R.drawable.seta_direita),
                        contentDescription = "Finalizar",
                        modifier = Modifier
                            .scale(3f),
                        tint = Color.Black
                    )
                }

            }
        }
    }


    @Composable
    fun OrderItem(
        product: Product,
        listProduct: MutableList<OrderProduct>,
        onCalculate: () -> Unit = {}
    ) {

        val orderProduct = listProduct.find { it.productId == product.id }
        val quantityState = remember { mutableIntStateOf(orderProduct?.quantity ?: 0) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(10.dp)
        ) {
            Row {
                if (product.productPhotoUrl == null) {
                    ProductPhotoDefault()
                } else {
                    AsyncImage(
                        model = product.productPhotoUrl!!.toUri(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = product.typeOfGrains,
                        color = Color.White,
                        fontSize = TextUnit(20F, TextUnitType.Sp)
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "R$ " + product.price.toString(),
                            color = Color.LightGray,
                            fontSize = TextUnit(15F, TextUnitType.Sp)
                        )
                        Row(
                            Modifier
                                .width(130.dp)
                                .border(1.dp, Color.White, CircleShape)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(id = R.drawable.menos),
                                modifier = Modifier
                                    .scale(2f)
                                    .fillMaxWidth(0.3f)
                                    .clickable {
                                        if (quantityState.intValue > 0) {
                                            quantityState.intValue--
                                            orderProduct?.quantity = quantityState.intValue
                                            onCalculate()
                                        }
                                    },
                                contentDescription = "Menos",
                                tint = Color.White
                            )
                            if (orderProduct != null) {
                                Text(
                                    text = quantityState.intValue.toString(),
                                    modifier = Modifier.fillMaxWidth(0.4f),
                                    color = Color.White,
                                    fontSize = TextUnit(20F, TextUnitType.Sp)
                                )
                            }
                            if (orderProduct != null) {
                                Icon(
                                    painterResource(id = R.drawable.soma),
                                    modifier = Modifier
                                        .scale(2f)
                                        .fillMaxWidth(0.3f)
                                        .clickable {
                                            quantityState.intValue++
                                            orderProduct.quantity = quantityState.intValue
                                            onCalculate()
                                        },
                                    contentDescription = "Add",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                }

            }
        }
    }




