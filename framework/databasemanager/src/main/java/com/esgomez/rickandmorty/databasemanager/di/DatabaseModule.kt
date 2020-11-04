package com.esgomez.rickandmorty.databasemanager.di

import android.app.Application
import com.esgomez.rickandmorty.data.LocalCharacterDataSource
import com.esgomez.rickandmorty.databasemanager.CharacterDatabase
import com.esgomez.rickandmorty.databasemanager.CharacterRoomDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun databaseProvider(app: Application) : CharacterDatabase = CharacterDatabase.getDatabase(app)

    @Provides
    fun localCharacterDataSourceProvider(
        database: CharacterDatabase
    ): LocalCharacterDataSource = CharacterRoomDataSource(database)
}