package com.esgomez.rickandmorty.di

import android.app.Application
import com.esgomez.rickandmorty.data.di.RepositoryModule
import com.esgomez.rickandmorty.databasemanager.di.DatabaseModule
import com.esgomez.rickandmorty.requestmanager.di.APIModule
import com.esgomez.rickandmorty.usecases.di.UseCaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    APIModule::class,
    DatabaseModule::class,
    RepositoryModule::class,
    UseCaseModule::class
])
interface RickAndMortyComponent {

    fun inject(module: CharacterListModule): CharacterListComponent
    fun inject(module: FavoriteListModule): FavoriteListComponent
    fun inject(module: CharacterDetailModule): CharacterDetailComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): RickAndMortyComponent
    }
}
