package com.esgomez.rickandmorty.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.esgomez.rickandmorty.R
import com.esgomez.rickandmorty.adapters.FavoriteListAdapter
import com.esgomez.rickandmorty.requestmanager.APIConstants
import com.esgomez.rickandmorty.requestmanager.CharacterRequest
import com.esgomez.rickandmorty.requestmanager.CharacterRetrofitDataSource
import com.esgomez.rickandmorty.data.CharacterRepository
import com.esgomez.rickandmorty.data.LocalCharacterDataSource
import com.esgomez.rickandmorty.data.RemoteCharacterDataSource
import com.esgomez.rickandmorty.databasemanager.CharacterDatabase
import com.esgomez.rickandmorty.databasemanager.CharacterRoomDataSource
import com.esgomez.rickandmorty.databinding.FragmentFavoriteListBinding
import com.esgomez.rickandmorty.di.FavoriteListComponent
import com.esgomez.rickandmorty.di.FavoriteListModule
import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.presentation.FavoriteListViewModel
import com.esgomez.rickandmorty.presentation.FavoriteListViewModel.FavoriteListNavigation
import com.esgomez.rickandmorty.presentation.FavoriteListViewModel.FavoriteListNavigation.ShowCharacterList
import com.esgomez.rickandmorty.presentation.FavoriteListViewModel.FavoriteListNavigation.ShowEmptyListMessage
import com.esgomez.rickandmorty.presentation.utils.Event
import com.esgomez.rickandmorty.usecases.GetAllFavoriteCharactersUseCase
import com.esgomez.rickandmorty.utils.app
import com.esgomez.rickandmorty.utils.getViewModel
import com.esgomez.rickandmorty.utils.setItemDecorationSpacing
import kotlinx.android.synthetic.main.fragment_favorite_list.*

class FavoriteListFragment : Fragment() {

    //region Fields

    private lateinit var favoriteListAdapter: FavoriteListAdapter
    private lateinit var listener: OnFavoriteListFragmentListener

    private lateinit var favoriteListComponent: FavoriteListComponent

    private val favoriteListViewModel: FavoriteListViewModel by lazy {
        getViewModel { favoriteListComponent.favoriteListViewModel }//Le pasamos el caracter getAllFavoriteCharactersUseCase
    }

    //endregion

    //region Override Methods & Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as OnFavoriteListFragmentListener
        }catch (e: ClassCastException){
            throw ClassCastException("$context must implement OnFavoriteListFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteListComponent = context!!.app.component.inject(FavoriteListModule())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //characterRequest = CharacterRequest(BASE_API_URL)
        //characterDao = CharacterDatabase.getDatabase(activity!!.applicationContext).characterDao()

        return DataBindingUtil.inflate<FragmentFavoriteListBinding>(
            inflater,
            R.layout.fragment_favorite_list,
            container,
            false
        ).apply {
            lifecycleOwner = this@FavoriteListFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteListAdapter = FavoriteListAdapter { character ->
            listener.openCharacterDetail(character)
        }
        favoriteListAdapter.setHasStableIds(true)

        rvFavoriteList.run {
            setItemDecorationSpacing(resources.getDimension(R.dimen.list_item_padding))
            adapter = favoriteListAdapter
        }

        favoriteListViewModel.favoriteCharacterList.observe(this, Observer (favoriteListViewModel::onFavoriteCharacterList))
        //Lo que obtengamos de favoriteListViewModel lo vamos a volver a volver a mandar al ViewModel para que valide en contenido
        favoriteListViewModel.event.observe(this, Observer(this::validateEvents))

    }

    //endregion

    //region Private Methods

    private fun validateEvents(event: Event<FavoriteListNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowCharacterList -> navigation.run {//En caso de que tenga que mostrar informacion
                    tvEmptyListMessage.isVisible = false
                    favoriteListAdapter.updateData(characterList)
                }
                ShowEmptyListMessage -> {
                    tvEmptyListMessage.isVisible = true
                    favoriteListAdapter.updateData(emptyList())
                }
            }
        }
    }

    //endregion

    //region Inner Classes & Interfaces

    interface OnFavoriteListFragmentListener {
        fun openCharacterDetail(character: Character)
    }

    //endregion

    //region Companion object

    companion object {

        fun newInstance(args: Bundle? = Bundle()) = FavoriteListFragment().apply {
            arguments = args
        }
    }

    //endregion

}
