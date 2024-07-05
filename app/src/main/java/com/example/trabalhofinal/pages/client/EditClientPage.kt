package com.example.trabalhofinal.pages.client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.navigation.NavHostController
import com.example.trabalhofinal.classes.clients.Client
import com.example.trabalhofinal.classes.clients.ClientListViewModel
import com.example.trabalhofinal.elements.CpfInputField
import com.example.trabalhofinal.elements.PhoneInputField
import com.example.trabalhofinal.elements.StylishInputField
import com.example.trabalhofinal.elements.TopBar

@Composable
fun EditClientPage(navController: NavHostController, clientViewModel: ClientListViewModel) {
    val client = clientViewModel.selectedFarm.value
    var name by remember { mutableStateOf(client?.let { TextFieldValue(it.name) }) }
    var cpf by remember { mutableStateOf(client?.let { TextFieldValue(it.cpf) }) }
    var telefone by remember { mutableStateOf(client?.let { TextFieldValue(it.phone) }) }
    var address by remember { mutableStateOf(client?.let { TextFieldValue(it.address) }) }
    var instagram by remember { mutableStateOf(client?.let { TextFieldValue(it.instagram) }) }

    Column {
        TopBar(
            title = "Editar Cliente",
            isBack = true,
            onBack = { navController.popBackStack() },
            onAction = { /*TODO*/ }) {

        }

        name?.let { StylishInputField(it, { name = it }, "Nome", "Informe o nome do cliente") }
        cpf?.let {
            CpfInputField(
                value = it,
                onValueChange = { cpf = it },
                label = "CPF",
                placeholder = "Informe o CPF"
            )
        }
        telefone?.let {
            PhoneInputField(
                value = it,
                onValueChange = { telefone = it },
                label = "Telefone",
                placeholder = "Informe o telefone"
            )
        }
        address?.let { StylishInputField(it, { address = it }, "Endereço", "Informe o endereço") }
        instagram?.let {
            StylishInputField(
                it,
                { instagram = it },
                "Instagram",
                "Informe o instagram"
            )
        }


        Spacer(modifier = Modifier.fillMaxHeight(0.2f))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                clientViewModel.updateClient(
                    Client(
                        client?.id!!,
                        cpf!!.text,
                        name!!.text,
                        telefone!!.text,
                        address!!.text,
                        instagram!!.text,
                        client.userPhotoUrl
                    )
                )
                navController.popBackStack()
            }) {
                Text(
                    text = "Editar",
                    color = Color.White,
                    fontSize = TextUnit(17F, TextUnitType.Sp)
                )
            }
        }


    }
}