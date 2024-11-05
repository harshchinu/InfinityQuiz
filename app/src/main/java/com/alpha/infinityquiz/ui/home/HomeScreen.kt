package com.alpha.infinityquiz.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onStartTest: () -> Unit,
    onSolveBookmarks: () -> Unit
) {

    val countries by viewModel.countries.collectAsState()
    val selectedCountry by viewModel.selectedCountry.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    fun validateCountrySelected(function: () -> Unit){
        if(selectedCountry?.name!=null){
            function.invoke()
        }else {
            Toast.makeText(context, "Please Select Country", Toast.LENGTH_SHORT).show()

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Choose your country:", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown menu button
        Box {
            Button(onClick = { expanded = true }) {
                Text(text = selectedCountry?.name ?: "Select Country")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(onClick = {
                        viewModel.setSelectedCountry(country)
                        expanded = false
                    },
                        text = { Text(text = country.name) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { validateCountrySelected( onStartTest ) }) {
            Text(text = "Start")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {validateCountrySelected (onSolveBookmarks )}) {
            Text(text = "Solve Bookmarks")
        }


    }
}


