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
fun AddClientPage(navController: NavHostController, clientViewModel: ClientListViewModel) {

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var cpf by remember { mutableStateOf(TextFieldValue("")) }
    var telefone by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var instagram by remember { mutableStateOf(TextFieldValue("")) }

    Column {
        TopBar(
            title = "Novo Cliente",
            isBack = true,
            onBack = { navController.popBackStack() },
            onAction = { /*TODO*/ }) {

        }

        StylishInputField(name, { name = it }, "Nome", "Informe o nome do cliente")
        CpfInputField(
            value = cpf,
            onValueChange = { cpf = it },
            label = "CPF",
            placeholder = "Informe o CPF"
        )
        PhoneInputField(
            value = telefone,
            onValueChange = { telefone = it },
            label = "Telefone",
            placeholder = "Informe o telefone"
        )
        StylishInputField(address, { address = it }, "Endereço", "Informe o endereço")
        StylishInputField(instagram, { instagram = it }, "Instagram", "Informe o instagram")


        Spacer(modifier = Modifier.fillMaxHeight(0.2f))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                clientViewModel.addClient(
                    Client(
                        0,
                        cpf.text,
                        name.text,
                        telefone.text,
                        address.text,
                        instagram.text,
                        null
                    )
                )

                navController.navigate("Clients")


            }) {
                Text(
                    text = "Cadastrar",
                    color = Color.White,
                    fontSize = TextUnit(17F, TextUnitType.Sp)
                )
            }
        }


    }
}