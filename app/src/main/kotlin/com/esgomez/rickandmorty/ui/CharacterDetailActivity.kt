package com.esgomez.rickandmorty.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.esgomez.rickandmorty.R
import com.esgomez.rickandmorty.adapters.EpisodeListAdapter
import com.esgomez.rickandmorty.databinding.ActivityCharacterDetailBinding
import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.imagemanager.bindCircularImageUrl
import com.esgomez.rickandmorty.parcelables.CharacterParcelable
import com.esgomez.rickandmorty.parcelables.toCharacterDomain
import com.esgomez.rickandmorty.presentation.CharacterDetailViewModel
import com.esgomez.rickandmorty.presentation.CharacterDetailViewModel.CharacterDetailNavigation
import com.esgomez.rickandmorty.presentation.CharacterDetailViewModel.CharacterDetailNavigation.*
import com.esgomez.rickandmorty.presentation.utils.Event
import com.esgomez.rickandmorty.utils.Constants
import com.esgomez.rickandmorty.utils.app
import com.esgomez.rickandmorty.utils.getViewModel
import com.esgomez.rickandmorty.utils.showLongToast
import com.esgomez.rickandmorty.di.CharacterDetailComponent
import com.esgomez.rickandmorty.di.CharacterDetailModule
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity: AppCompatActivity() {

    //region Fields

    private lateinit var episodeListAdapter: EpisodeListAdapter
    private lateinit var binding: ActivityCharacterDetailBinding
    private lateinit var characterDetailComponent: CharacterDetailComponent

    private val characterDetailViewModel: CharacterDetailViewModel by lazy {
        getViewModel { characterDetailComponent.characterDetailViewModel }
    }

    //endregion

    //region Override Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        characterDetailComponent = app.component.inject(CharacterDetailModule(
                intent.getParcelableExtra<CharacterParcelable>(Constants.EXTRA_CHARACTER)?.toCharacterDomain()
        ))

        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail)
        binding.lifecycleOwner = this@CharacterDetailActivity

        episodeListAdapter = EpisodeListAdapter { episode ->
            this@CharacterDetailActivity.showLongToast("Episode -> $episode")
        }
        rvEpisodeList.adapter = episodeListAdapter

        characterFavorite.setOnClickListener { characterDetailViewModel.onUpdateFavoriteCharacterStatus() }

        characterDetailViewModel.characterValues.observe(this, Observer(this::loadCharacter))
        characterDetailViewModel.isFavorite.observe(this, Observer(this::updateFavoriteIcon))
        characterDetailViewModel.events.observe(this, Observer(this::validateEvents))

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

    private fun loadCharacter(character: Character){
        binding.characterImage.bindCircularImageUrl(
                url = character.image,
                placeholder = R.drawable.ic_camera_alt_black,
                errorPlaceholder = R.drawable.ic_broken_image_black
        )
        binding.characterDataName = character.name
        binding.characterDataStatus = character.status
        binding.characterDataSpecies = character.species
        binding.characterDataGender = character.gender
        binding.characterDataOriginName = character.origin.name
        binding.characterDataLocationName = character.location.name
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
