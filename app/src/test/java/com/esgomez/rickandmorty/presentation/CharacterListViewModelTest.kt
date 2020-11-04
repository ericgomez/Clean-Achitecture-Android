package com.esgomez.rickandmorty.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.domain.Location
import com.esgomez.rickandmorty.domain.Origin
import com.esgomez.rickandmorty.presentation.CharacterListViewModel.CharacterListNavigation
import com.esgomez.rickandmorty.presentation.CharacterListViewModel.CharacterListNavigation.ShowCharacterList
import com.esgomez.rickandmorty.presentation.utils.Event
import com.esgomez.rickandmorty.usecases.GetAllCharactersUseCase
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterListViewModelTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getAllCharacterUseCase: GetAllCharactersUseCase

    @Mock
    lateinit var eventObserver: Observer<Event<CharacterListNavigation>>

    private lateinit var characterListViewModel: CharacterListViewModel

    @Before
    fun setUp(){
        characterListViewModel = CharacterListViewModel(getAllCharacterUseCase)
    }

    // Comenzando con los Test
    @Test
    fun `onGetAllCharacters should return an expected success list of characters`(){
        //GIVEN
        val expectedResult = listOf(mockedCharacter.copy(id = 1))
        given(getAllCharacterUseCase.invoke(any())).willReturn(Single.just(expectedResult))

        characterListViewModel.event.observeForever(eventObserver)

        //WHEN
        characterListViewModel.onGetAllCharacters()

        //THEN
        verify(eventObserver).onChanged(Event(ShowCharacterList(expectedResult)))
    }
}

val mockedOrigin = Origin(
    "",
    ""
)

val mockedLocation = Location(
    "",
    ""
)

val  mockedCharacter = Character(
    0,
    "",
    null,
    "",
    "",
    "",
    mockedOrigin,
    mockedLocation,
    listOf("")
)