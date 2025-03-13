package com.example.foodpalv1.data.model

data class Meal(
    val id: Int,
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val timeAdded: Long = System.currentTimeMillis()
)

data class DailyNutrients(
    val calories: NutrientProgress = NutrientProgress(0, 2000),
    val protein: NutrientProgress = NutrientProgress(0, 150),
    val carbs: NutrientProgress = NutrientProgress(0, 250),
    val fat: NutrientProgress = NutrientProgress(0, 70)
)

data class NutrientProgress(
    val current: Int,
    val target: Int
) 