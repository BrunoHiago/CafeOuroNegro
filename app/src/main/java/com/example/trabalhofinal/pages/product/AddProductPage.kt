package com.example.trabalhofinal.pages.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
fun AddProductPage(navController: NavHostController, productViewModel: ProductListViewModel) {

    var typeOfGrains by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(TextFieldValue("")) }
    var blend by remember { mutableStateOf(false) }
    var roastingPoint by remember { mutableStateOf(RoastingPoint.Clara) }

    Column {
        TopBar(
            title = "Novo Produto",
            isBack = true,
            onBack = { navController.popBackStack() },
            onAction = { /*TODO*/ }) {

        }

        StylishInputField(typeOfGrains, { typeOfGrains = it }, "Tipo do Grão", "Tipo do grão")
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
                productViewModel.addProducts(
                    Product(
                        0,
                        typeOfGrains.text,
                        roastingPoint,
                        price.text.toFloat(),
                        blend,
                        null
                    )
                )
                navController.navigate("Products")
            }) {
                Text(text = "Cadastrar", color = Color.White, fontSize = TextUnit(17F, TextUnitType.Sp))
            }
        }


    }
}

@Composable
fun SelectInput(selected: RoastingPoint = RoastingPoint.Clara, onSelectedChange: (RoastingPoint) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
            .background(Color.LightGray),
        verticalAlignment = Alignment.CenterVertically

    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
                .background(if (selected == RoastingPoint.Clara) MaterialTheme.colorScheme.primary else Color.Transparent)
                .clickable{onSelectedChange(RoastingPoint.Clara)},
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = RoastingPoint.Clara.name, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = if (selected == RoastingPoint.Clara) Color.White else Color.Black
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
                .background(if (selected == RoastingPoint.Media) MaterialTheme.colorScheme.primary else Color.Transparent)
                .clickable { onSelectedChange(RoastingPoint.Media) }
            ,
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = RoastingPoint.Media.name, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = if (selected == RoastingPoint.Media) Color.White else Color.Black
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .clip(RoundedCornerShape(corner = CornerSize(15.dp)))
                .background(if (selected == RoastingPoint.Escura) MaterialTheme.colorScheme.primary else Color.Transparent)
                .clickable { onSelectedChange(RoastingPoint.Escura) }
            ,
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = RoastingPoint.Escura.name, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = if (selected == RoastingPoint.Escura) Color.White else Color.Black
            )
        }



    }
}




