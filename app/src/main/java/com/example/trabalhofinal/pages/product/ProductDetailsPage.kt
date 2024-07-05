package com.example.trabalhofinal.pages.product

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.trabalhofinal.R
import com.example.trabalhofinal.classes.product.ProductListViewModel
import com.example.trabalhofinal.elements.TopBar
import com.example.trabalhofinal.pages.client.createImageFile
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsPage(navController: NavHostController, productViewModel: ProductListViewModel) {
    val selectedProduct = productViewModel.selectedProduct.value

    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var isCamera by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var url by remember { mutableStateOf(selectedProduct?.productPhotoUrl?.toUri()) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap = ImageDecoder.decodeBitmap(source)
                url = it

                if (selectedProduct != null) {
                    productViewModel.uploadImage(bitmap!!, selectedProduct)
                }
                showDialog=false

            }
        }
    val file = context.createImageFile()

    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            uri?.let {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap = ImageDecoder.decodeBitmap(source)
                url = it

                if (selectedProduct != null) {
                    productViewModel.uploadImage(bitmap!!, product = selectedProduct)
                }
                showDialog=false
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            if (!isCamera){
                launcher.launch("image/*")
            }else{
                cameraLauncher.launch(uri)
            }

        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                showDialog = false
            },
            sheetState = sheetState
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .width(70.dp)
                            .height(70.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Red)
                            .clickable {
                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        READ_MEDIA_IMAGES
                                    )
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    launcher.launch("image/*")
                                } else {
                                    isCamera=false
                                    permissionLauncher.launch(READ_MEDIA_IMAGES)

                                }
                            }

                    ) {

                        Icon(
                            painterResource(id = R.drawable.galeria),
                            contentDescription = "Camera",
                            modifier = Modifier
                                .scale(0.7f)
                        )

                    }

                    Text(
                        text = "Galeria",
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .width(70.dp)
                            .height(70.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Red)
                            .clickable {
                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    )
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(uri)
                                } else {
                                    isCamera=true
                                    permissionLauncher.launch(Manifest.permission.CAMERA)

                                }
                            }

                    ) {


                        Icon(
                            painterResource(id = R.drawable.camera),
                            contentDescription = "Camera",
                            modifier = Modifier
                                .scale(0.7f)
                        )


                    }

                    Text(
                        text = "Camera",
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }


            }
        }
    }


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(15.dp))
        TopBar(
            title = "Produto",
            isBack = true,
            onBack = { navController.popBackStack() },
            onAction = { /*TODO*/ },
            icon = { }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Color(0XFF6A3F14))
                    .clickable {
                        showDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedProduct != null) {
                    if (url != null) {
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape),
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.cafe),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

            }
        }
        Text(
            text = ("Café " + selectedProduct?.typeOfGrains),
            color = Color.White,
            fontSize = TextUnit(30F, TextUnitType.Sp)
        )
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = "R$ " + selectedProduct?.price.toString(),
            color = Color.LightGray,
            fontSize = TextUnit(15F, TextUnitType.Sp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 20.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tipo de Torra",
                    modifier = Modifier.fillMaxWidth(0.5f),
                    color = Color.White,
                    fontSize = TextUnit(17F, TextUnitType.Sp)
                )
                Text(
                    text = selectedProduct?.roastingPoint.toString(),
                    color = Color.LightGray,
                    fontSize = TextUnit(17F, TextUnitType.Sp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Blend",
                    color = Color.White,
                    fontSize = TextUnit(17F, TextUnitType.Sp)
                )
                Text(
                    text = if (selectedProduct?.blend == true) "Sim" else "Não",
                    color = Color.LightGray,
                    fontSize = TextUnit(17F, TextUnitType.Sp)
                )
            }



        }

        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            Button(onClick = {
                navController.navigate("EditProduct")
            }) {
                Text(
                    text = "Editar",
                    color = Color.White,
                    fontSize = TextUnit(17F, TextUnitType.Sp)
                )
            }
            Button(onClick = {
                if (selectedProduct != null) {
                    productViewModel.deleteProduct(selectedProduct)
                }
                navController.navigate("Products")
            }) {
                Text(
                    text = "Excluir",
                    color = Color.White,
                    fontSize = TextUnit(17F, TextUnitType.Sp)
                )
            }
        }


    }
}