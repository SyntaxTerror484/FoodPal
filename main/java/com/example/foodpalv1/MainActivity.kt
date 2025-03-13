package com.example.foodpalv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.example.foodpalv1.data.model.DailyNutrients
import com.example.foodpalv1.data.model.Meal
import com.example.foodpalv1.data.model.NutrientProgress
import com.example.foodpalv1.ui.theme.FoodPalTheme
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import com.example.foodpalv1.ui.components.AddMealDialog
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.foodpalv1.ui.screens.CommunityScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Community : Screen("community", "Community", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodPalTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar {
                listOf(Screen.Home, Screen.Community).forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen }
                    )
                }
            }
        }
    ) { padding ->
        when (selectedScreen) {
            Screen.Home -> HomeScreen(padding)
            Screen.Community -> CommunityScreen()
        }
    }
}

@Composable
fun HomeScreen(padding: PaddingValues) {
    var meals by remember { mutableStateOf(listOf<Meal>()) }
    var dailyNutrients by remember { mutableStateOf(DailyNutrients()) }
    var showAddMealDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                NutrientsSection(dailyNutrients)
            }

            item {
                Text(
                    "Today's Meals",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(meals) { meal ->
                MealCard(meal)
            }
        }

        FloatingActionButton(
            onClick = { showAddMealDialog = true },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(26.dp)
                .padding(bottom = padding.calculateBottomPadding()),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Add, "Add meal")
        }

        if (showAddMealDialog) {
            AddMealDialog(
                onDismiss = { showAddMealDialog = false },
                onMealAdded = { meal ->
                    meals = meals + meal
                    // Update daily nutrients
                    dailyNutrients = DailyNutrients(
                        calories = NutrientProgress(
                            dailyNutrients.calories.current + meal.calories,
                            dailyNutrients.calories.target
                        ),
                        protein = NutrientProgress(
                            dailyNutrients.protein.current + meal.protein,
                            dailyNutrients.protein.target
                        ),
                        carbs = NutrientProgress(
                            dailyNutrients.carbs.current + meal.carbs,
                            dailyNutrients.carbs.target
                        ),
                        fat = NutrientProgress(
                            dailyNutrients.fat.current + meal.fat,
                            dailyNutrients.fat.target
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun NutrientsSection(nutrients: DailyNutrients) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Daily Progress",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .size(300.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Fat - outermost ring
            CircularNutrientProgress(
                current = nutrients.calories.current,
                target = nutrients.calories.target,
                radius = 140.dp,
                strokeWidth = 25.dp,
                color = Color(0xFFE91E63) // Bright Pink
            )
            
            // Carbs
            CircularNutrientProgress(
                current = nutrients.carbs.current,
                target = nutrients.carbs.target,
                radius = 106.dp,
                strokeWidth = 25.dp,
                color = Color(0xFF4CAF50) // Bright Green
            )
            
            // Protein
            CircularNutrientProgress(
                current = nutrients.protein.current,
                target = nutrients.protein.target,
                radius = 78.dp,
                strokeWidth = 25.dp,
                color = Color(0xFF2196F3) // Bright Blue
            )
            
            // Fat - innermost ring
            CircularNutrientProgress(
                current = nutrients.fat.current,
                target = nutrients.fat.target,
                radius = 50.dp,
                strokeWidth = 25.dp,
                color = Color(0xFFFF9800) // Bright Orange
            )

            // Center text showing total calories
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${nutrients.calories.current}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "kcal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Legend in 2x2 grid
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // First row: Calories and Protein
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientLegend(
                    "Calories",
                    nutrients.calories,
                    "kcal",
                    Color(0xFFE91E63),
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                )
                NutrientLegend(
                    "Protein",
                    nutrients.protein,
                    "g",
                    Color(0xFF2196F3),
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                )
            }
            
            // Second row: Carbs and Fat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientLegend(
                    "Carbs",
                    nutrients.carbs,
                    "g",
                    Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                )
                NutrientLegend(
                    "Fat",
                    nutrients.fat,
                    "g",
                    Color(0xFFFF9800),
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
fun CircularNutrientProgress(
    current: Int,
    target: Int,
    radius: Dp,
    strokeWidth: Dp,
    color: Color
) {
    Canvas(
        modifier = Modifier.size(radius * 2)
    ) {
        // Background circle
        drawArc(
            color = color.copy(alpha = 0.2f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
        
        val progress = current.toFloat() / target
        
        if (progress <= 1f) {
            // Normal progress arc
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        } else {
            // Complete circle in normal color
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            
            // Overshoot in darker color
            drawArc(
                color = color.copy(red = color.red * 0.7f, green = color.green * 0.7f, blue = color.blue * 0.7f),
                startAngle = -90f,
                sweepAngle = 360f * (progress - 1f),
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun NutrientLegend(
    label: String,
    nutrient: NutrientProgress,
    suffix: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, color)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${nutrient.current}/${nutrient.target}$suffix",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MealCard(meal: Meal) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color(0xFFD0BCFF).copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    meal.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD0BCFF)
                )
                Text(
                    formatTime(meal.timeAdded),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientInfo("Calories", meal.calories, "kcal", Color(0xFF967EE0))
                NutrientInfo("Protein", meal.protein, "g", Color(0xFF967EE0))
                NutrientInfo("Carbs", meal.carbs, "g", Color(0xFF967EE0))
                NutrientInfo("Fat", meal.fat, "g", Color(0xFF967EE0))
            }
        }
    }
}

@Composable
fun NutrientInfo(
    label: String, 
    value: Int, 
    suffix: String, 
    color: Color
) {
    Card(
        modifier = Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        border = BorderStroke(0.5.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$value$suffix",
                style = MaterialTheme.typography.bodyLarge,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = color.copy(alpha = 0.7f)
            )
        }
    }
}

private fun formatTime(timeMillis: Long): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date(timeMillis))
}