package com.esgomez.rickandmorty.presentation.utils

data class Event <out T>(private val content: T) {

    //Variable que nos permite manejar si el contenido ya a sido manejado previamente
    private var hasBeenHandled = false

    //Metodo que nos permite verifivar si el contenido a sido utilizado
    fun getContentIfNotHandled(): T? = if (hasBeenHandled) {
        null//Si a sido manejado devolvemos un null
    } else {
        hasBeenHandled = true//Actualizamos la bandera a true
        content//regresamos en contenido
    }

}