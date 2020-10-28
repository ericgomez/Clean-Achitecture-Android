package com.esgomez.rickandmorty.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.esgomez.rickandmorty.R
import com.esgomez.rickandmorty.adapters.FavoriteListAdapter
import com.esgomez.rickandmorty.api.APIConstants.BASE_API_URL
import com.esgomez.rickandmorty.api.CharacterRequest
import com.esgomez.rickandmorty.database.CharacterDao
import com.esgomez.rickandmorty.database.CharacterDatabase
import com.esgomez.rickandmorty.database.CharacterEntity
import com.esgomez.rickandmorty.databinding.FragmentFavoriteListBinding
import com.esgomez.rickandmorty.presentation.FavoriteListViewModel
import com.esgomez.rickandmorty.utils.setItemDecorationSpacing
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favorite_list.*

class FavoriteListFragment : Fragment() {

    //region Fields

    private lateinit var favoriteListAdapter: FavoriteListAdapter
    private lateinit var listener: OnFavoriteListFragmentListener
    private lateinit var characterRequest: CharacterRequest
    private lateinit var characterDao: CharacterDao

    private val favoriteListViewModel: FavoriteListViewModel by lazy {
        FavoriteListViewModel(characterDao)//Le pasamos el caracter dao
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        characterRequest = CharacterRequest(BASE_API_URL)
        characterDao = CharacterDatabase.getDatabase(activity!!.applicationContext).characterDao()

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
        favoriteListViewModel.event.observe(this, Observer { events ->
            events?.getContentIfNotHandled()?.let { navigation ->
                when(navigation) {
                    is FavoriteListViewModel.FavoriteListNavigation.ShowCharacterList -> navigation.run {//En caso de que tenga que mostrar informacion
                        tvEmptyListMessage.isVisible = false
                        favoriteListAdapter.updateData(characterList)
                    }
                    FavoriteListViewModel.FavoriteListNavigation.ShowEmptyListMessage -> {
                        tvEmptyListMessage.isVisible = true
                        favoriteListAdapter.updateData(emptyList())
                    }
                }
            }
        })
    }

    //endregion

    //region Private Methods

    //endregion

    //region Inner Classes & Interfaces

    interface OnFavoriteListFragmentListener {
        fun openCharacterDetail(character: CharacterEntity)
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
