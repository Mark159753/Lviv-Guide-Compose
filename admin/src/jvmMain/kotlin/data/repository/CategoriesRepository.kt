package data.repository

import com.apollographql.apollo3.ApolloClient
import com.example.AllCategoriesQuery
import com.example.CreateCategoryMutation
import com.example.RemoveCategoryMutation
import com.example.type.CreateCategoryInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoriesRepository(
    private val client: ApolloClient
) {

    suspend fun fetchAllCategories() = withContext(Dispatchers.IO){
            client.query(AllCategoriesQuery()).execute().asResult()
        }

    suspend fun removeCategory(id:Int) = withContext(Dispatchers.IO){
        client.mutation(RemoveCategoryMutation(id)).execute().asResult()
    }

    suspend fun createCategory(name:String) = withContext(Dispatchers.IO){
        val input = CreateCategoryInput(name = name)
        client.mutation(CreateCategoryMutation(input)).execute().asResult()
    }
}