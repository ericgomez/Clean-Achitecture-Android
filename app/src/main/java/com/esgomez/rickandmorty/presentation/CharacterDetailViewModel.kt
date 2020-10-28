package com.esgomez.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esgomez.rickandmorty.api.*
import com.esgomez.rickandmorty.database.CharacterDao
import com.esgomez.rickandmorty.database.CharacterEntity
import com.esgomez.rickandmorty.presentation.utils.Event
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// Paso 1: Pasar como parámetros "character" de tipo CharacterServer?, "characterDao" de tipo CharacterDao y "episodeRequest" de tipo EpisodeRequest
class CharacterDetailViewModel(
    private val character: CharacterServer? = null,
    private val characterDao: CharacterDao,
    private val episodeRequest: EpisodeRequest
) : ViewModel() {

    //region Fields

    //Paso 2: Declarar la variable "disposable" de tipo CompositeDisposable
    private val disposable = CompositeDisposable()
    //Paso 3: Crear las variables de tipo MutableLiveData y LiveData para manejar los valores del personaje (tipo sugerido: CharacterServer)
    private val _characterValues = MutableLiveData<CharacterServer>()//Creamos su valor privado
    val characterValues: LiveData<CharacterServer> get() = _characterValues//Creamos su valor publico que recivira el privado
    //Paso 4: Crear las variables de tipo MutableLiveData y LiveData para manejar el estado de favorito de un personaje (tipo sugerido: Boolean)
    private val _isFavorite = MutableLiveData<Boolean>()//Creamos su valor privado
    val isFavorite: LiveData<Boolean> get() = _isFavorite//Creamos su valor publico que recivira el privado
    //Paso 5: Crear las variables de tipo MutableLiveData y LiveData para manejar los eventos del view model (tipo sugerido: CharacterDetailNavigation)
    private val _event = MutableLiveData<Event<CharacterDetailNavigation>>()//Creamos su valor privado
    val event: LiveData<Event<CharacterDetailNavigation>> get() = _event//Creamos su valor publico que recivira el privado
    //endregion

    //region Override Methods & Callbacks

    override fun onCleared() {
        super.onCleared()
        //Paso 6: Limpiar la variable "disposable" cuando el view model borre los recursos
        disposable.clear()
    }

    //endregion

    //region Public Methods

    fun onCharacterValidation() {

        //Paso 7: Si el parámetro "character" es nulo, se debe disparar el evento de cerrar actividad y finalizar la ejecución de este método (Usar CharacterDetailNavigation)
        if (character == null) {
            _event.value = Event(CharacterDetailNavigation.CloseActivity)
            return
        }
        //Paso 8: Si el parámetro "character" no es nulo, se debe disparar el evento de cargar el personaje (Usar CharacterDetailNavigation)
        _characterValues.value = character
        //Paso 9: Si el parámetro "character" no es nulo, se debe llamar al método "validateFavoriteCharacterStatus" el cual valida el estatus del personaje favorito
        validateFavoriteCharacterStatus(character.id)
        //Paso 10: Si el parámetro "character" no es nulo, se debe llamar al método "requestShowEpisodeList" el cual hace una petición para devolver el listado de episodios del personaje
        requestShowEpisodeList(character.episodeList)
    }

    fun onUpdateFavoriteCharacterStatus() {
        val characterEntity: CharacterEntity = character!!.toCharacterEntity()
        disposable.add(
            characterDao.getCharacterById(characterEntity.id)
                .isEmpty
                .flatMapMaybe { isEmpty ->
                    if(isEmpty){
                        characterDao.insertCharacter(characterEntity)
                    }else{
                        characterDao.deleteCharacter(characterEntity)
                    }
                    Maybe.just(isEmpty)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { isFavorite ->
                    //Paso 11: Disparar la variable de tipo MutableLiveData que se implementó en el Paso 3 con la variable "isFavorite"
                    _isFavorite.value = isFavorite//Devuelve la peticion
                }
        )
    }

    //endregion

    //region Private Methods

    private fun validateFavoriteCharacterStatus(characterId: Int){
        disposable.add(
            characterDao.getCharacterById(characterId)
                .isEmpty
                .flatMapMaybe { isEmpty ->
                    Maybe.just(!isEmpty)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { isFavorite ->
                    //Paso 12: Disparar la variable de tipo MutableLiveData que se implementó en el Paso 3 con la variable "isFavorite"
                    _isFavorite.value = isFavorite//Devuelve la peticion
                }
        )
    }

    private fun requestShowEpisodeList(episodeUrlList: List<String>){
        disposable.add(
            Observable.fromIterable(episodeUrlList)
                .flatMap { episode: String ->
                    episodeRequest.baseUrl = episode
                    episodeRequest
                        .getService<EpisodeService>()
                        .getEpisode()
                        .toObservable()
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    //Paso 13: Disparar el evento de mostrar el progreso de carga de la lista de episodio (Usar CharacterDetailNavigation)
                    _event.value = Event(CharacterDetailNavigation.ShowEpisodeListLoading)
                }
                .subscribe(
                    { episodeList ->
                        //Paso 14: Disparar el evento de ocultar el progreso de carga de la lista de episodio (Usar CharacterDetailNavigation)
                        _event.value = Event(CharacterDetailNavigation.HideEpisodeListLoading)
                        //Paso 15: Disparar el evento de mostrar la lista de episodio (Usar CharacterDetailNavigation)
                        _event.value = Event(CharacterDetailNavigation.ShowEpisodeList(episodeList))
                    },
                    { error ->
                        //Paso 16: Disparar el evento de ocultar el progreso de carga de la lista de episodio (Usar CharacterDetailNavigation)
                        _event.value = Event(CharacterDetailNavigation.HideEpisodeListLoading)
                        //Paso 17: Disparar el evento de mostrar error al cargar los episodios (Usar CharacterDetailNavigation)
                        _event.value = Event(CharacterDetailNavigation.ShowEpisodeError(error))
                    })
        )
    }

    //endregion

    //region Inner Classes & Interfaces
    //Aqui se encuentran declarados todos los eventos que vamos a disparar
    sealed class CharacterDetailNavigation {
        data class ShowEpisodeError(val error: Throwable) : CharacterDetailNavigation()
        data class ShowEpisodeList(val episodeList: List<EpisodeServer>) : CharacterDetailNavigation()
        object CloseActivity : CharacterDetailNavigation()
        object HideEpisodeListLoading : CharacterDetailNavigation()
        object ShowEpisodeListLoading : CharacterDetailNavigation()
    }

    //endregion

}