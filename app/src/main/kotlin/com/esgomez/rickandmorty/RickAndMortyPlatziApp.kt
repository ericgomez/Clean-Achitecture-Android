package com.esgomez.rickandmorty

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.esgomez.rickandmorty.di.DaggerRickAndMortyComponent
import com.esgomez.rickandmorty.di.RickAndMortyComponent

class RickAndMortyApp: Application() {

    //region Fields

    lateinit var component: RickAndMortyComponent
        private set

    //endregion

    //region Override Methods & Callbacks

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        component = initAppComponent()
    }

    //endregion

    //region Private Methods

    private fun initAppComponent() = DaggerRickAndMortyComponent.factory().create(this)

    //endregion
}
