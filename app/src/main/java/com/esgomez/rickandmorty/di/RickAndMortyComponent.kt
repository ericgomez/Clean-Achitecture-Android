package com.esgomez.rickandmorty.di

import android.app.Application
import com.esgomez.rickandmorty.data.RepositoryModule
import com.esgomez.rickandmorty.databasemanager.DatabaseModule
import com.esgomez.rickandmorty.requestmanager.APIModule
import com.esgomez.rickandmorty.usecases.UseCaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [APIModule::class, DatabaseModule::class, RepositoryModule::class, UseCaseModule::class])
interface RickAndMortyComponent {

    fun inject(module: CharacterListModule): CharacterListComponent
    fun inject(module: FavoriteListModule): FavoriteListComponent

    @Component.Factory
    interface Favorite {
        fun create(@BindsInstance app: Application) : RickAndMortyComponent
    }
}