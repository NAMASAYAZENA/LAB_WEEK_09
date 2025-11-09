package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

// =======================================================
// MODUL 9 – PART 2
// States and Adding Event Handlers
// =======================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home()
                }
            }
        }
    }
}

// =======================================================
// Data Model
// =======================================================
data class Student(
    var name: String
)

// =======================================================
// Composable Function – Home (pakai State)
// =======================================================
@Composable
fun Home() {
    // Membuat list mahasiswa dengan state (agar bisa berubah)
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    // State untuk input text
    var inputField = remember { mutableStateOf(Student("")) }

    // Panggil HomeContent untuk menampilkan isi halaman
    HomeContent(
        listData,
        inputField.value,
        { input -> inputField.value = Student(input) },
        {
            if (inputField.value.name.isNotBlank()) {
                listData.add(inputField.value)
                inputField.value = Student("")
            }
        }
    )
}

// =======================================================
// Composable Function – HomeContent
// =======================================================
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit
) {
    LazyColumn {
        // Bagian input dan tombol
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.enter_item))

                TextField(
                    value = inputField.name,
                    onValueChange = { onInputValueChange(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { onButtonClick() }) {
                    Text(text = stringResource(id = R.string.button_click))
                }
            }
        }

        // Menampilkan list mahasiswa
        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item.name)
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
        // preview dengan data dummy
        val listData = mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
        HomeContent(
            listData = listData,
            inputField = Student(""),
            onInputValueChange = {},
            onButtonClick = {}
        )
    }
}
