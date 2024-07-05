package com.example.trabalhofinal.pages.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.trabalhofinal.classes.product.Product
import com.example.trabalhofinal.classes.product.ProductListViewModel
import com.example.trabalhofinal.classes.product.RoastingPoint
import com.example.trabalhofinal.elements.DecimalInputField
import com.example.trabalhofinal.elements.StylishInputField
import com.example.trabalhofinal.elements.TopBar

@Composable
fun EditProductPage(navController: NavHostController, productViewModel: ProductListViewModel){
    val selectedProduct = productViewModel.selectedProduct.value
    var typeOfGrains by remember { mutableStateOf(selectedProduct?.let { TextFieldValue(it.typeOfGrains) }) }
    var price by remember { mutableStateOf(TextFieldValue(selectedProduct?.price.toString())) }
    var blend by remember { mutableStateOf(selectedProduct?.blend ?: false) }
    var roastingPoint by remember { mutableStateOf(selectedProduct?.roastingPoint ?: RoastingPoint.Clara) }

    Column {
        TopBar(
            title = "Editar Produto",
            isBack = true,
            onBack = { navController.popBackStack() },
            onAction = { /*TODO*/ }) {

        }

        typeOfGrains?.let {
            StylishInputField(
                it,
                { typeOfGrains = it },
                "Tipo do Grão",
                "Tipo do grão"
            )
        }
        DecimalInputField(price, { price = it }, "Preço", "Preço do café")
        SelectInput(roastingPoint){ roastingPoint = it}
        Row(modifier = Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = blend,
                onCheckedChange = { blend = it },
                modifier = Modifier.padding(10.dp)
            )
            Text(text = "Café Blend", color = Color.White, fontSize = TextUnit(17F, TextUnitType.Sp))

        }
        Spacer(modifier = Modifier.fillMaxHeight(0.2f))
        Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
            Button(onClick = {
                typeOfGrains?.let {
                    Product(
                        selectedProduct?.id ?: 0,
                        it.text,
                        roastingPoint,
                        price.text.toFloat(),
                        blend,
                        null
                    )
                }?.let {
                    productViewModel.updateClient(
                        it
                    )
                }
                navController.navigate("Products")
            }) {
                Text(text = "Editar", color = Color.White, fontSize = TextUnit(17F, TextUnitType.Sp))
            }
        }


    }

}