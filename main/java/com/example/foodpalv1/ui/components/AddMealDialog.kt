package com.example.foodpalv1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodpalv1.data.model.Meal

@Composable
fun AddMealDialog(
    onDismiss: () -> Unit,
    onMealAdded: (Meal) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Meal") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Meal Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it.filter { it.isDigit() } },
                    label = { Text("Calories (kcal)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it.filter { it.isDigit() } },
                    label = { Text("Protein (g)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it.filter { it.isDigit() } },
                    label = { Text("Carbs (g)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it.filter { it.isDigit() } },
                    label = { Text("Fat (g)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && calories.isNotBlank()) {
                        onMealAdded(
                            Meal(
                                id = System.currentTimeMillis().toInt(),
                                name = name,
                                calories = calories.toIntOrNull() ?: 0,
                                protein = protein.toIntOrNull() ?: 0,
                                carbs = carbs.toIntOrNull() ?: 0,
                                fat = fat.toIntOrNull() ?: 0
                            )
                        )
                        onDismiss()
                    }
                }
            ) {
                Text("Add Meal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 