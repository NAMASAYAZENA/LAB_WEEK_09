package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

// =======================================================
// Part 1 – Building a Simple Jetpack Compose UI
// =======================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Compose menggunakan setContent, bukan setContentView
        setContent {
            LAB_WEEK_09Theme {
                // Surface = container utama
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // data statis untuk Part 1
                    val list = listOf("Tanu", "Tina", "Tono")
                    Home(list)
                }
            }
        }
    }
}

// =======================================================
// Composable Function – Home
// =======================================================
@Composable
fun Home(items: List<String>) {
    // LazyColumn = pengganti RecyclerView
    LazyColumn {
        // item pertama berisi input dan tombol
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // judul daftar
                Text(text = stringResource(id = R.string.list_title))

                Spacer(modifier = Modifier.height(8.dp))

                // TextField (belum berfungsi di Part 1)
                TextField(
                    value = "",
                    onValueChange = {},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tombol Submit (belum berfungsi di Part 1)
                Button(onClick = { }) {
                    Text(text = stringResource(id = R.string.button_click))
                }
            }
        }

        // item berikutnya = daftar nama statis
        items(items) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item)
            }
        }
    }
}

// =======================================================
// Preview untuk Android Studio
// =======================================================
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        Home(listOf("Tanu", "Tina", "Tono"))
    }
}
