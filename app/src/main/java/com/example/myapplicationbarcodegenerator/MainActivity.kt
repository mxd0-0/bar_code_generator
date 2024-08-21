package com.example.myapplicationbarcodegenerator

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplicationbarcodegenerator.ui.theme.MyApplicationBarCodeGeneratorTheme
import com.example.myapplicationbarcodegenerator.ui.theme.buttonColor
import com.example.myapplicationbarcodegenerator.ui.theme.deepBlue
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                    BarcodeScreen()
        }
    }
}

fun generateBarcode(content: String, width: Int, height: Int): Bitmap {
    val bitMatrix: BitMatrix =
        MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, width, height)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(
                x,
                y,
                if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            )
        }
    }
    return bitmap
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScreen(modifier: Modifier = Modifier) {
    var content by remember { mutableStateOf("") }
    var barcodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(deepBlue)
            .padding(16.dp)
            ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
        painter = painterResource(id =R.drawable.logo ),
        contentDescription = null,
    )
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Enter content") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray,
                containerColor = Color.White,
                focusedPlaceholderColor = Color.White,
                focusedLabelColor = Color.White,
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            barcodeBitmap = generateBarcode(content, 600, 300)
            keyboardController?.hide()

        },            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text("Generate Barcode")
        }
        Spacer(modifier = Modifier.height(16.dp))
        barcodeBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Generated Barcode",
                modifier = Modifier.size(300.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BarcodeScreenPreview() {
    MyApplicationBarCodeGeneratorTheme {
        BarcodeScreen()
    }
}