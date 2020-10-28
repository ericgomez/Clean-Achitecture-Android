package com.esgomez.rickandmorty

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class RickAndMortyApp: Application() {

    //region Override Methods & Callbacks

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    //endregion

}
