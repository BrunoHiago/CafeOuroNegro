package com.example.trabalhofinal.pages.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.trabalhofinal.R
import com.example.trabalhofinal.classes.product.Product
import com.example.trabalhofinal.classes.product.ProductListViewModel
import com.example.trabalhofinal.elements.SwipeBox
import com.example.trabalhofinal.elements.TopBar
import com.example.trabalhofinal.pages.client.ClientPhotoDefault

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductPage(navController: NavHostController, productViewModel: ProductListViewModel) {

    val listProduct = productViewModel.products.observeAsState().value

    var searchText by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()

    ) {
        TopBar(
            title = "Produtos",
            isBack = false,
            onBack = { /*TODO*/ },
            onAction = { navController.navigate("AddProduct") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.soma),
                    contentDescription = "Add",
                    modifier = Modifier.scale(3f),
                    tint = Color.White
                )
            }
        )
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText -> searchText = newText; productViewModel.searchProducts(newText) },
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
            if (listProduct != null)
                items(listProduct, key = { it.id }) { product ->
                    SwipeBox(onDelete = {
                        productViewModel.deleteProduct(product)
                        println("Deletado $product")
                    }, onEdit = {
                        productViewModel.setSelectedProduct(product)
                        navController.navigate("EditProduct")
                    },modifier = Modifier.animateItemPlacement())
                    { innerModifier->
                        ProductItem(product = product, productViewModel, navController, innerModifier)
                    }

                }
            item {
                Spacer(modifier = Modifier.height(110.dp))
            }

        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    productViewModel: ProductListViewModel,
    navController: NavHostController,
    modifier: Modifier
) {

    ListItem(modifier = modifier.clickable {
        productViewModel.setSelectedProduct(product)
        navController.navigate("ProductDetails")
    }, headlineContent = {
        Text(
            text = "Café " + product.typeOfGrains,
            color = Color(0xFFA0522D),
            fontSize = TextUnit(22F, TextUnitType.Sp)
        )
    }, supportingContent = {
        Text(
            text = "Torra " + product.roastingPoint,
            color = Color(0xFFC28000),
            fontSize = TextUnit(18F, TextUnitType.Sp)
        )
    }, leadingContent = {
        if (product.productPhotoUrl != null) {
            AsyncImage(
                model = product.productPhotoUrl,
                contentDescription = "ProductPhoto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)

            )
        } else {
            ProductPhotoDefault()
        }
    }, colors = ListItemDefaults.colors(
        containerColor = Color(0xFF2c1a12),
        headlineColor = Color.White,
    )
    )
}

@Composable
fun ProductPhotoDefault(details: Boolean = false) {
    Image(
        painter = painterResource(id = R.drawable.cafe),
        contentDescription = null,
        modifier = if (details) Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .shadow(20.dp) else Modifier
            .size(100.dp)
            .clip(CircleShape)
            .shadow(20.dp),
        contentScale = ContentScale.Crop
    )
}