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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.lab_week_09.ui.theme.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

// =======================================================
// MODUL 9
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
// HomeScreen – convert list to JSON using Moshi
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

    var inputField by remember { mutableStateOf(Student("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        HomeContent(
            listData = listData,
            inputField = inputField,
            onInputValueChange = { inputField = Student(it) },
            onButtonClick = {
                if (inputField.name.isNotBlank()) {
                    listData.add(inputField)
                    inputField = Student("")
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Please enter a name first!")
                    }
                }
            },
            onFinishClick = {
                // 🔹 Konversi list ke JSON pakai Moshi
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val adapter = moshi.adapter(List::class.java)
                val jsonString = adapter.toJson(listData)

                // 🔹 Encode JSON agar aman di URL
                val encodedJson = URLEncoder.encode(jsonString, StandardCharsets.UTF_8.toString())

                // 🔹 Navigasi ke summary dengan JSON di route
                navController.navigate("summary/$encodedJson")
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// =======================================================
// SummaryScreen – decode JSON and display list
// =======================================================
@Composable
fun SummaryScreen(navController: androidx.navigation.NavHostController, jsonData: String) {
    // 🔹 Decode JSON dari URL
    val decodedJson = URLDecoder.decode(jsonData, StandardCharsets.UTF_8.toString())

    // 🔹 Parse JSON ke List<Student> pakai Moshi
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val adapter = moshi.adapter<List<Map<String, String>>>(List::class.java)
    val studentList = adapter.fromJson(decodedJson)?.map { Student(it["name"] ?: "") } ?: emptyList()

    // 🔹 Tampilkan data hasil parsing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnBackgroundTitleText(text = stringResource(id = R.string.list_title))

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(studentList) { student ->
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
// HomeContent (UI reuse from previous parts)
// =======================================================
@Composable
fun HomeContent(
    listData: List<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onFinishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
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
