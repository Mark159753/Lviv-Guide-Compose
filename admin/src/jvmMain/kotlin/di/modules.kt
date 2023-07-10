package di

import com.apollographql.apollo3.ApolloClient
import data.repository.CategoriesRepository
import data.repository.PlacesRepository
import org.koin.dsl.module
import ui.categories.CategoriesController
import ui.main.MainController
import ui.places.PlacesController
import ui.places.create.CreatePlaceController

val networkModule = module {
    factory {
        ApolloClient.Builder()
        .serverUrl("http://localhost:4654/graphql")
        .build()
    }

    factory { CategoriesRepository(get()) }
    factory { PlacesRepository(get()) }

    factory { MainController(get()) }

    factory { CategoriesController(get()) }

    factory { PlacesController(get()) }
    factory { CreatePlaceController(get()) }
}