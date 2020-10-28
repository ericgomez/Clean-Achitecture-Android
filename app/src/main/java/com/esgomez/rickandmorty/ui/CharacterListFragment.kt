package com.esgomez.rickandmorty.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esgomez.rickandmorty.api.*
import com.esgomez.rickandmorty.R
import com.esgomez.rickandmorty.adapters.CharacterGridAdapter
import com.esgomez.rickandmorty.api.APIConstants.BASE_API_URL
import com.esgomez.rickandmorty.databinding.FragmentCharacterListBinding
import com.esgomez.rickandmorty.presentation.CharacterListViewModel
import com.esgomez.rickandmorty.utils.setItemDecorationSpacing
import com.esgomez.rickandmorty.utils.showLongToast
import kotlinx.android.synthetic.main.fragment_character_list.*


class CharacterListFragment : Fragment() {

    //region Fields

    private lateinit var characterGridAdapter: CharacterGridAdapter
    private lateinit var listener: OnCharacterListFragmentListener
    private lateinit var characterRequest: CharacterRequest

    //Intanciamos la clase CharacterListViewModel
    private val characterListViewModel: CharacterListViewModel by lazy {//Lo inicalizamos como tipo lazy
        CharacterListViewModel(characterRequest)
    }

    private val onScrollListener: RecyclerView.OnScrollListener by lazy {
        object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                characterListViewModel.onLoadMoreItems(visibleItemCount, firstVisibleItemPosition, totalItemCount)
            }
        }
    }

    //endregion

    //region Override Methods & Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as OnCharacterListFragmentListener
        }catch (e: ClassCastException){
            throw ClassCastException("$context must implement OnCharacterListFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        characterRequest = CharacterRequest(BASE_API_URL)

        return DataBindingUtil.inflate<FragmentCharacterListBinding>(
            inflater,
            R.layout.fragment_character_list,
            container,
            false
        ).apply {
            lifecycleOwner = this@CharacterListFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterGridAdapter = CharacterGridAdapter { character ->
            listener.openCharacterDetail(character)
        }
        characterGridAdapter.setHasStableIds(true)

        rvCharacterList.run{
            addOnScrollListener(onScrollListener)
            setItemDecorationSpacing(resources.getDimension(R.dimen.list_item_padding))

            adapter = characterGridAdapter
        }

        srwCharacterList.setOnRefreshListener {
            characterListViewModel.onRetryGetAllCharacter(rvCharacterList.adapter?.itemCount ?: 0)
        }

        characterListViewModel.event.observe(this, Observer { events ->
            events?.getContentIfNotHandled()?.let { navigation ->
                when (navigation) {
                    is CharacterListViewModel.CharacterListNavigation.ShowCharacterError -> {
                        context?.showLongToast("Error")//Cuando es un mostramos un Toast
                    }
                    is CharacterListViewModel.CharacterListNavigation.ShowCharacterList -> navigation.run {//Cuando es un listado cargamos la lista
                        characterGridAdapter.addData(characterListt)//Para poder trabajar con el characterListt agregamos el navigation.run
                    }
                    //Los siguientes elementos no reciben parametros
                    CharacterListViewModel.CharacterListNavigation.HideLoading -> {
                        srwCharacterList.isRefreshing = false//hacemos que se oculte el progres
                    }
                    CharacterListViewModel.CharacterListNavigation.ShowLoading -> {
                        srwCharacterList.isRefreshing = true//hacemos que se muestre el progres
                    }
                }
            }
        })
        //Escuchamos los eventos
        characterListViewModel.onGetAllCharacters()//Llamamos a los personajes
    }
    //endregion

    //region Inner Classes & Interfaces

    interface OnCharacterListFragmentListener {
        fun openCharacterDetail(character: CharacterServer)
    }

    //endregion

    //region Companion object

    companion object {

        private const val PAGE_SIZE = 20

        fun newInstance(args: Bundle? = Bundle()) = CharacterListFragment().apply {
            arguments = args
        }
    }

    //endregion
}
