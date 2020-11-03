package com.esgomez.rickandmorty

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.esgomez.rickandmorty.di.DaggerRickAndMortyComponent
import com.esgomez.rickandmorty.di.RickAndMortyComponent

class RickAndMortyApp: Application() {

    //region Override Methods & Callbacks

    lateinit var component: RickAndMortyComponent

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        component = initAppComponent()
    }

    //endregion

    private fun initAppComponent() = DaggerRickAndMortyComponent.factory().create(this)

}
