package com.esgomez.rickandmorty.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.esgomez.rickandmorty.R
import com.esgomez.rickandmorty.databinding.ActivityCharacterDetailBinding
import com.esgomez.rickandmorty.adapters.EpisodeListAdapter
import com.esgomez.rickandmorty.api.APIConstants.BASE_API_URL
import com.esgomez.rickandmorty.api.CharacterServer
import com.esgomez.rickandmorty.api.EpisodeRequest
import com.esgomez.rickandmorty.database.CharacterDao
import com.esgomez.rickandmorty.database.CharacterDatabase
import com.esgomez.rickandmorty.presentation.CharacterDetailViewModel.CharacterDetailNavigation
import com.esgomez.rickandmorty.presentation.CharacterDetailViewModel.CharacterDetailNavigation.*
import com.esgomez.rickandmorty.presentation.utils.Event
import com.esgomez.rickandmorty.presentation.CharacterDetailViewModel
import com.esgomez.rickandmorty.utils.Constants
import com.esgomez.rickandmorty.utils.bindCircularImageUrl
import com.esgomez.rickandmorty.utils.getViewModel
import com.esgomez.rickandmorty.utils.showLongToast
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity: AppCompatActivity() {

    //region Fields

    private lateinit var episodeListAdapter: EpisodeListAdapter
    private lateinit var binding: ActivityCharacterDetailBinding
    //Paso 19: Modificar la inicialización de la variable "episodeRequest" usando la función lazy
    private val episodeRequest: EpisodeRequest by lazy {
        EpisodeRequest(BASE_API_URL)//Inicializamos el EpisodeRequest con la BASE_API_URL
    }

    //Paso 20: Modificar la inicialización de la variable "characterDao" usando la función lazy
    private val characterDao: CharacterDao by lazy {
        CharacterDatabase.getDatabase(application).characterDao()
    }

    //Paso 21: Crear la variable "characterDetailViewModel" de tipo CharacterDetailViewModel usando la función lazy
    private val characterDetailViewModel: CharacterDetailViewModel by lazy {
        getViewModel {
            CharacterDetailViewModel(
                //Paso 22: Agregar parámetro "intent.getParcelableExtra(Constants.EXTRA_CHARACTER)"
                intent.getParcelableExtra(Constants.EXTRA_CHARACTER),
                //Paso 23: Agregar parámetro "characterDao"
                characterDao,
                //Paso 24: Agregar parámetro "episodeRequest"
                episodeRequest
            )
        }
    }

    //endregion

    //region Override Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail)
        binding.lifecycleOwner = this@CharacterDetailActivity

        episodeListAdapter = EpisodeListAdapter { episode ->
            this@CharacterDetailActivity.showLongToast("Episode -> $episode")
        }
        rvEpisodeList.adapter = episodeListAdapter

        //Paso 32: Usar el método "onUpdateFavoriteCharacterStatus" implementado en el view model
        characterFavorite.setOnClickListener { characterDetailViewModel.onUpdateFavoriteCharacterStatus() }

        //Nota: Para trabajar el resultado de cada observer se sugiere llamar al método mediante referencia (Ejemplo -> this::callMethod)
        //Paso 33: Implementar el método observer del LiveData implementado en el Paso 3 y trabajar el resultado del observer en el método "loadCharacter"
        characterDetailViewModel.characterValues.observe(this, Observer (this::loadCharacter))
        //Paso 34: Implementar el método observer del LiveData implementado en el Paso 4 y trabajar el resultado del observer en el método "updateFavoriteIcon"
        characterDetailViewModel.isFavorite.observe(this, Observer (this::updateFavoriteIcon))
        //Paso 35: Implementar el método observer del LiveData implementado en el Paso 5 y trabajar el resultado del observer en el método "validateEvents"
        characterDetailViewModel.event.observe(this, Observer (this::validateEvents))
        //Paso 36: Usar el método "onCharacterValidation" implementado en el view model
        characterDetailViewModel.onCharacterValidation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    //endregion

    //region Private Methods

    //Paso 41: Inicializar los valores de la variable "binding" utilizando de referencia el paso 30
    private fun loadCharacter(character: CharacterServer){
        binding.characterImage.bindCircularImageUrl(
            url = character!!.image,
            placeholder = R.drawable.ic_camera_alt_black,
            errorPlaceholder = R.drawable.ic_broken_image_black
        )
        binding.characterDataName = character!!.name
        binding.characterDataStatus = character!!.status
        binding.characterDataSpecies = character!!.species
        binding.characterDataGender = character!!.gender
        binding.characterDataOriginName = character!!.origin.name
        binding.characterDataLocationName = character!!.location.name
    }

    private fun updateFavoriteIcon(isFavorite: Boolean?){
        characterFavorite.setImageResource(
            if (isFavorite != null && isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_border
            }
        )
    }

    private fun validateEvents(event: Event<CharacterDetailNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowEpisodeError -> navigation.run {
                    this@CharacterDetailActivity.showLongToast("Error -> ${error.message}")
                }
                is ShowEpisodeList -> navigation.run {
                    episodeListAdapter.updateData(episodeList)
                }
                CloseActivity -> {
                    this@CharacterDetailActivity.showLongToast(R.string.error_no_character_data)
                    finish()
                }
                HideEpisodeListLoading -> {
                    episodeProgressBar.isVisible = false
                }
                ShowEpisodeListLoading -> {
                    episodeProgressBar.isVisible = true
                }
            }
        }
    }


    //endregion
}
