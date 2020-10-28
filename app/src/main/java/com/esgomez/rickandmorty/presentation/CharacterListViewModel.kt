package com.esgomez.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esgomez.rickandmorty.api.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterListViewModel (private val characterRequest: CharacterRequest) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<CharacterListNavigation>>()//Escucharemos los eventos
    val event: LiveData<Event<CharacterListNavigation>> get() = _events //Disparara los eventos

    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    //region Private Methods

    fun onLoadMoreItems(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int) {
        if (isLoading || isLastPage || !isInFooter(visibleItemCount, firstVisibleItemPosition, totalItemCount)) {
            return
        }

        currentPage += 1
        onGetAllCharacters()
    }

    private fun isInFooter(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        return visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE
    }

    fun onRetryGetAllCharacter(itemCount: Int) {
        if (itemCount > 0) {
            _events.value = Event(CharacterListNavigation.HideLoading)//Ocultamos el progres
            return
        }

        onGetAllCharacters()
    }

    fun onGetAllCharacters(){
        disposable.add(
            characterRequest
                .getService<CharacterService>()
                .getAllCharacters(currentPage)
                .map(CharacterResponseServer::toCharacterServerList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    _events.value = Event(CharacterListNavigation.ShowLoading)
                }
                .subscribe({ characterList ->
                    if (characterList.size < PAGE_SIZE) {
                        isLastPage = true
                    }

                    _events.value = Event(CharacterListNavigation.HideLoading)//Ocultamos el progres
                    _events.value = Event(CharacterListNavigation.ShowCharacterList(characterList))//Mandamos el listado de personajes
                }, { error ->
                    isLastPage = true
                    _events.value = Event(CharacterListNavigation.HideLoading)//Ocultamos el progres
                    _events.value = Event(CharacterListNavigation.ShowCharacterError(error))//Mandamos el error
                })
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

    //endregion

    //Esta clase indicara los estados que vamos a manejar
    sealed class CharacterListNavigation {
        //Como devuelven parametros son de tipo data class
        data class ShowCharacterError(val error: Throwable) : CharacterListNavigation()//Se va a utilizar en caso de que aya un error al momento de obtener la lista
        data class ShowCharacterList(val characterListt: List<CharacterServer>) : CharacterListNavigation()//Devolver la lista de personajes
        //los siguientes elemento no necesitan manejar ningun tipo de parametros por lo que los declararemos como object
        object HideLoading : CharacterListNavigation()//Indica cuando se oculte el proceso de carga de la aplicacion
        object ShowLoading: CharacterListNavigation()////Indica cuando se muestre el proceso de carga de la aplicacion
    }
}