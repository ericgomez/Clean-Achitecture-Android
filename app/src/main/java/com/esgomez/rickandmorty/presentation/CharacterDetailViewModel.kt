package com.esgomez.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esgomez.rickandmorty.api.*
import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.domain.Episode
import com.esgomez.rickandmorty.presentation.utils.Event
import com.esgomez.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.esgomez.rickandmorty.usecases.GetFavoriteCharacterStatusUseCase
import com.esgomez.rickandmorty.usecases.UpdateFavoriteCharacterStatusUseCase
import io.reactivex.disposables.CompositeDisposable

// Paso 1: Pasar como parámetros "character" de tipo CharacterServer?, "characterDao" de tipo CharacterDao y "episodeRequest" de tipo EpisodeRequest
class CharacterDetailViewModel(
    private val character: Character? = null,
    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase,
    private val getFavoriteCharacterStatusUseCase: GetFavoriteCharacterStatusUseCase,
    private val updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase
) : ViewModel() {

    //region Fields

    //Paso 2: Declarar la variable "disposable" de tipo CompositeDisposable
    private val disposable = CompositeDisposable()
    //Paso 3: Crear las variables de tipo MutableLiveData y LiveData para manejar los valores del personaje (tipo sugerido: CharacterServer)
    private val _characterValues = MutableLiveData<Character>()//Creamos su valor privado
    val characterValues: LiveData<Character> get() = _characterValues//Creamos su valor publico que recivira el privado
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
        disposable.add(
            updateFavoriteCharacterStatusUseCase//Implementar variable "updateFavoriteCharacterStatusUseCase"
                .invoke(character!!)//Pasar la variable "characterEntity" al método "invoke" del caso de uso
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
            getFavoriteCharacterStatusUseCase//Implementar variable "getFavoriteCharacterStatusUseCase"
                .invoke(characterId)//Pasar la variable "characterId" al método "invoke" del caso de uso
                .subscribe { isFavorite ->
                    //Paso 12: Disparar la variable de tipo MutableLiveData que se implementó en el Paso 3 con la variable "isFavorite"
                    _isFavorite.value = isFavorite//Devuelve la peticion
                }
        )
    }

    private fun requestShowEpisodeList(episodeUrlList: List<String>){
        disposable.add(
            getEpisodeFromCharacterUseCase//mandamos llamar a la clase getEpisodeFromCharacterUseCase
                .invoke(episodeUrlList)//junto con su funcion invoke con la lista de URL
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
        data class ShowEpisodeList(val episodeList: List<Episode>) : CharacterDetailNavigation()
        object CloseActivity : CharacterDetailNavigation()
        object HideEpisodeListLoading : CharacterDetailNavigation()
        object ShowEpisodeListLoading : CharacterDetailNavigation()
    }

    //endregion

}