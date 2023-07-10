package data.repository

import com.apollographql.apollo3.ApolloClient
import com.example.AllPlacesQuery
import com.example.CreatePlaceMutation
import com.example.RemovePlaceMutation
import com.example.type.CreatePlaceInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlacesRepository(
    private val client: ApolloClient
) {

    suspend fun fetchAll() = withContext(Dispatchers.IO){
        client.query(AllPlacesQuery()).execute().asResult()
    }

    suspend fun createPlace(input: CreatePlaceInput) = withContext(Dispatchers.IO){
        client.mutation(CreatePlaceMutation(input)).execute().asResult()
    }

    suspend fun removePlace(id:Int) = withContext(Dispatchers.IO){
        client.mutation(RemovePlaceMutation(id)).execute().asResult()
    }
}