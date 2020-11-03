package com.esgomez.rickandmorty.databasemanager

import android.app.Application
import com.esgomez.rickandmorty.data.LocalCharacterDataSource
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