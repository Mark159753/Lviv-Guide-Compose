package data.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation

fun <R : Operation.Data>ApolloResponse<R>.asResult():Result<R>{
    return if (hasErrors()){
        Result.Error(errors = errors ?: emptyList())
    }else{
        Result.Success(data = data)
    }
}

sealed interface Result<out T : Any>{
    data class Success<T : Any>(val data:T?):Result<T>
    data class Error(val errors:List<com.apollographql.apollo3.api.Error>):Result<Nothing>
}