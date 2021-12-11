package com.example.mipokechido.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mipokechido.database.Pokemon

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import mipokechido.databinding.FragmentRightBinding

import org.json.JSONObject

private lateinit var database: DatabaseReference
class RightFragment : Fragment() {
    private lateinit var poke_image: String
    private lateinit var binding: FragmentRightBinding
    private val rightViewModel: RightViewModel by viewModels()
    private lateinit var queue: RequestQueue
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myDB = FirebaseDatabase.getInstance()
        database = myDB.reference
        // Inflate the layout for this fragment
        binding= FragmentRightBinding.inflate(inflater,container,false)
        queue = Volley.newRequestQueue(context)
        binding.btnCatchPkmn.isVisible=false
        binding.ivPkmnSprite.isVisible=false
        binding.btnSearch.setOnClickListener { response ->
            if(binding.etPokemonToSearchFor.text.toString() != ""){
                getPkmn()
            }
        }
        binding.btnCatchPkmn.setOnClickListener {
            if(binding.tvID.text.toString() != ""){
                val user="1"
                database.child("usuarios").child(user).get().addOnSuccessListener { record->
                    val json = JSONObject(record.value.toString())
                    val numPkmn = json.getInt("capturados")
                    Log.d("updtNum", "Pokemon totales: $numPkmn")
                    val updtNum = hashMapOf<String, Any>(
                        "usuarios/${user}/capturados" to numPkmn +1
                    )
                    Log.d("updtNum", "Pokemon actuali: $updtNum")
                    database.updateChildren(updtNum)
                }
                val destination = RightFragmentDirections.actionRightFragmentToLeftFragment(binding.tvPokemonInfo.text.toString())
                NavHostFragment.findNavController(this).navigate(destination)
                rightViewModel.save(Pokemon(
                    binding.tvID.text.toString().toInt(),
                    binding.tvPokemonInfo.text.toString(),
                    binding.tvHP.text.toString(),
                    binding.tvAtk.text.toString(),
                    binding.tvAtkSpc.text.toString(),
                    binding.tvDef.text.toString(),
                    binding.tvDefSpc.text.toString(),
                    binding.tvSpd.text.toString(),
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${binding.tvID.text.toString()}.png"))
            }

        }
        return binding.root
    }
    fun getPkmn(){
        binding.btnCatchPkmn.isVisible=true

        val url = "https://pokeapi.co/api/v2/pokemon/${binding.etPokemonToSearchFor.text.toString().lowercase()}"
        val jsonRequest = JsonObjectRequest(url, Response.Listener<JSONObject>{response->
            binding.etPokemonToSearchFor.text.clear()
            binding.tvPokemonInfo.setText(response.getString("name").replaceFirstChar { it.uppercaseChar() })
            binding.tvID.setText((response.getString("id")))
            binding.labelType.setText("Tipo: ")
            binding.tvType.setText(response.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name").replaceFirstChar { it.uppercaseChar() })
            binding.labelHP.setText("HP: ")
            binding.tvHP.setText(response.getJSONArray("stats").getJSONObject(0).getString("base_stat"))
            binding.labelAtk.setText("Ataque: ")
            binding.tvAtk.setText(response.getJSONArray("stats").getJSONObject(1).getString("base_stat"))
            binding.labelAtkSpc.setText("Ataque Especial: ")
            binding.tvAtkSpc.setText(response.getJSONArray("stats").getJSONObject(3).getString("base_stat"))
            binding.labelDef.setText("Defensa: ")
            binding.tvDef.setText(response.getJSONArray("stats").getJSONObject(2).getString("base_stat"))
            binding.labelDefSpc.setText("Defensa Especial: ")
            binding.tvDefSpc.setText(response.getJSONArray("stats").getJSONObject(4).getString("base_stat"))
            binding.labelSpd.setText("Velocidad: ")
            binding.tvSpd.setText(response.getJSONArray("stats").getJSONObject(5).getString("base_stat"))
            binding.labelPeso.setText("Peso: ")
            binding.tvPeso.setText(response.getString("weight"))
            if(binding.tvID.text.length==1){
                val aidi="00${binding.tvID.text.toString()}"
                val img ="https://assets.pokemon.com/assets/cms2/img/pokedex/full/${aidi}.png"
                Picasso.get().load(img).into(binding.ivPkmnSprite)
            } else if (binding.tvID.text.length==2){
                val aidi="0${binding.tvID.text.toString()}"
                val img ="https://assets.pokemon.com/assets/cms2/img/pokedex/full/${aidi}.png"
                Picasso.get().load(img).into(binding.ivPkmnSprite)
            } else if(binding.tvID.text.length==3){
                val img ="https://assets.pokemon.com/assets/cms2/img/pokedex/full/${binding.tvID.text.toString()}.png"
                Picasso.get().load(img).into(binding.ivPkmnSprite)
            }
            binding.ivPkmnSprite.isVisible=true
            //val img = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${binding.tvID.text.toString()}.png"
        },
            Response.ErrorListener { errorMessage ->
                binding.etPokemonToSearchFor.text.clear()
                binding.tvPokemonInfo.setText("404 Pokemon Not Found")
                binding.btnCatchPkmn.isVisible=false
                binding.ivPkmnSprite.isVisible=false

                binding.tvID.setText("")
                binding.tvType.setText("")
                binding.tvHP.setText("")
                binding.tvAtk.setText("")
                binding.tvAtkSpc.setText("")
                binding.tvDef.setText("")
                binding.tvDefSpc.setText("")
                binding.tvSpd.setText("")
                binding.tvPeso.setText("")
                binding.labelType.setText("")
                binding.labelHP.setText("")
                binding.labelAtk.setText("")
                binding.labelAtkSpc.setText("")
                binding.labelDef.setText("")
                binding.labelDefSpc.setText("")
                binding.labelSpd.setText("")
                binding.labelPeso.setText("")
                Log.d("JSONResponse", "error: $errorMessage")
            }
        )
        queue.add(jsonRequest)
    }
    override fun onStop() {
        super.onStop()
        queue.cancelAll("stopped")
    }
}