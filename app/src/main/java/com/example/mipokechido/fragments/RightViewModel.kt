package com.example.mipokechido.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mipokechido.database.DatabaseManager
import com.example.mipokechido.database.MyAppDataSource
import com.example.mipokechido.database.Pokemon
import kotlinx.coroutines.launch

class RightViewModel: ViewModel() {
    fun save(pokemon: Pokemon){
        viewModelScope.launch {
            val pokemonDao = DatabaseManager.instance.database.pokemonDao()
            MyAppDataSource(pokemonDao).save(pokemon)
        }
    }
}