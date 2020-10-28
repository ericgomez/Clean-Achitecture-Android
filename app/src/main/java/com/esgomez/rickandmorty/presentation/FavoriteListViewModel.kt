package com.esgomez.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esgomez.rickandmorty.database.CharacterDao
import com.esgomez.rickandmorty.database.CharacterEntity
import com.esgomez.rickandmorty.presentation.utils.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoriteListViewModel (private val characterDao: CharacterDao) : ViewModel() {

    private val disposable = CompositeDisposable()

    //Conponentes para manejas los eventos
    private val _events = MutableLiveData<Event<FavoriteListNavigation>>()
    val event: LiveData<Event<FavoriteListNavigation>> get() = _events
    //Listado para menejar el listado de personajes favoritos desde la base de datos
    private val _favoriteCharacterList: LiveData<List<CharacterEntity>>
        get() = LiveDataReactiveStreams.fromPublisher(//Nos ayuda a trabajar la informacion cuando aya un cambio en la base de datos
            characterDao
                .getAllFavoriteCharacters()//Nos regresa la lista de personajes
                .onErrorReturn { emptyList() }//Si hay un error que nos regrese una lista vacia
                .subscribeOn(Schedulers.io())//Indicamos donde se va trabajar esta informacion
        )

    val favoriteCharacterList: LiveData<List<CharacterEntity>>
        get() = _favoriteCharacterList

    /*
    disposable.add(
            characterDao.getAllFavoriteCharacters()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ characterList ->
                    if(characterList.isEmpty()) {
                        tvEmptyListMessage.isVisible = true
                        favoriteListAdapter.updateData(emptyList())
                    } else {
                        tvEmptyListMessage.isVisible = false
                        favoriteListAdapter.updateData(characterList)
                    }
                },{
                    tvEmptyListMessage.isVisible = true
                    favoriteListAdapter.updateData(emptyList())
                })
        )
    */

    override fun onCleared() {
        super.onCleared()
    }

    //Metodo para validar lo que recivamos de la base de datos
    fun onFavoriteCharacterList(list: List<CharacterEntity>) {
        if (list.isEmpty()) {
            _events.value = Event(FavoriteListNavigation.ShowEmptyListMessage)//Mandamo el evento de la lista vacia
            return
        }

        //En caso contrario que la lista No este vacia
        _events.value = Event(FavoriteListNavigation.ShowCharacterList(list))//Mandamos el evento de la lista con los personajes
    }

    //Clase sellada 'sealed'
    sealed class FavoriteListNavigation {
        //Manejaremos dos estados
        data class ShowCharacterList(val characterList: List<CharacterEntity>): FavoriteListNavigation()
        //El siguiente metodo no necesita parametros por lo que lo declaramos como object
        object ShowEmptyListMessage : FavoriteListNavigation()//Indica al evento cuando no hay nada que mostrar
    }

}