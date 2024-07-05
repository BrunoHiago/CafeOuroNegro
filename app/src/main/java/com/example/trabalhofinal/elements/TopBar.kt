package com.example.trabalhofinal.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(title: String, isBack:Boolean, onBack: () -> Unit, onAction: () -> Unit, icon:@Composable ()-> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 10.dp, end = 10.dp, top = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isBack){
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.scale(1.7f),
                    tint = Color.White
                )
            }
        }else{
            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
        }

        Text(text = title, fontSize = TextUnit(25F, TextUnitType.Sp), color = Color.White)
        IconButton(onClick = onAction, Modifier.fillMaxWidth(0.2f)) {
            icon()
        }
    }

}