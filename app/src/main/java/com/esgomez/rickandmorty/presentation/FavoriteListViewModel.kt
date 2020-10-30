package com.esgomez.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esgomez.rickandmorty.database.CharacterDao
import com.esgomez.rickandmorty.database.CharacterEntity
import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.presentation.utils.Event
import com.esgomez.rickandmorty.usecases.GetAllFavoriteCharactersUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoriteListViewModel (private val getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase) : ViewModel() {

    private val disposable = CompositeDisposable()

    //Conponentes para manejas los eventos
    private val _events = MutableLiveData<Event<FavoriteListNavigation>>()
    val event: LiveData<Event<FavoriteListNavigation>> get() = _events
    //Listado para menejar el listado de personajes favoritos desde la base de datos
    val favoriteCharacterList: LiveData<List<Character>>
        get() = LiveDataReactiveStreams.fromPublisher(//Nos ayuda a trabajar la informacion cuando aya un cambio en la base de datos
                getAllFavoriteCharactersUseCase//mandamos llamar a la clase getAllFavoriteCharactersUseCase
                        .invoke()//junto con su funcion invoke
        )

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    //Metodo para validar lo que recivamos de la base de datos
    fun onFavoriteCharacterList(list: List<Character>) {
        if (list.isEmpty()) {
            _events.value = Event(FavoriteListNavigation.ShowCharacterList(emptyList()))
            _events.value = Event(FavoriteListNavigation.ShowEmptyListMessage)//Mandamo el evento de la lista vacia
            return
        }

        //En caso contrario que la lista No este vacia
        _events.value = Event(FavoriteListNavigation.ShowCharacterList(list))//Mandamos el evento de la lista con los personajes
    }

    //Clase sellada 'sealed'
    sealed class FavoriteListNavigation {
        //Manejaremos dos estados
        data class ShowCharacterList(val characterList: List<Character>): FavoriteListNavigation()
        //El siguiente metodo no necesita parametros por lo que lo declaramos como object
        object ShowEmptyListMessage : FavoriteListNavigation()//Indica al evento cuando no hay nada que mostrar
    }

}