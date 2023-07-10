package com.example.data.repository.categories

import com.example.core.common.model.refresh.RefreshResult
import com.example.data.model.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    val categories: Flow<List<CategoryModel>>

    suspend fun refreshCategories(force:Boolean = false): RefreshResult
}