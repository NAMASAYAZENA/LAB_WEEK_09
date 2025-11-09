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
import androidx.navigation.compose.rememberNavController
import com.example.lab_week_09.ui.theme.*

// =======================================================
// MODUL 9 – PART 4
// Navigation
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
                    val navController = rememberNavController()
                    NavigationGraph(navController)
                }
            }
        }
    }
}

// =======================================================
// Data Model
// =======================================================
data class Student(var name: String)

// =======================================================
// HomeScreen – Input data dan navigasi ke Summary
// =======================================================
@Composable
fun HomeScreen(navController: androidx.navigation.NavHostController) {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    var inputField = remember { mutableStateOf(Student("")) }

    HomeContent(
        listData,
        inputField.value,
        { input -> inputField.value = Student(input) },
        {
            if (inputField.value.name.isNotBlank()) {
                listData.add(inputField.value)
                inputField.value = Student("")
            }
        },
        onFinishClick = {
            // Navigasi ke Summary sambil kirim data (disimpan global dulu)
            GlobalStudentData.students = listData
            navController.navigate("summary")
        }
    )
}

// =======================================================
// SummaryScreen – Menampilkan daftar nama
// =======================================================
@Composable
fun SummaryScreen(navController: androidx.navigation.NavHostController) {
    val listData = GlobalStudentData.students

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnBackgroundTitleText(text = stringResource(id = R.string.list_title))
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(listData) { student ->
                OnBackgroundItemText(text = student.name)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryTextButton(text = "Back") {
            navController.popBackStack()
        }
    }
}

// =======================================================
// Object untuk menyimpan data global sementara
// =======================================================
object GlobalStudentData {
    var students: SnapshotStateList<Student> = mutableStateListOf()
}

// =======================================================
// HomeContent – pakai tombol Finish untuk navigasi
// =======================================================
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onFinishClick: () -> Unit
) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundTitleText(text = stringResource(id = R.string.enter_item))

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = inputField.name,
                    onValueChange = { onInputValueChange(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PrimaryTextButton(text = stringResource(id = R.string.button_click)) {
                        onButtonClick()
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    PrimaryTextButton(text = stringResource(id = R.string.button_navigate)) {
                        onFinishClick()
                    }
                }
            }
        }

        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

// =======================================================
// Preview
// =======================================================
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        val navController = rememberNavController()
        HomeScreen(navController)
    }
}
