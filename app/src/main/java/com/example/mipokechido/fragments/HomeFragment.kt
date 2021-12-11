package com.example.mipokechido.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mipokechido.databinding.FragmentHomeBinding
import org.json.JSONObject

private lateinit var database: DatabaseReference
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        val myDB = FirebaseDatabase.getInstance()
        database = myDB.reference

        database.child("usuarios").child("1").get().addOnSuccessListener { record ->
            val json = JSONObject(record.value.toString())
            Log.d("ValoresFirebase", "got value ${record.value}")
            binding.tvName.setText(json.getString("usuario").toString())
          //binding.tvLastname.setText(json.getString("apellido").toString())
            binding.tvnickName.setText(json.getString("mote").toString())
            binding.tvNivel.setText(json.getString("nivel"))
            binding.tvNumPkmn.setText(json.getString("capturados"))
        }
        return binding.root
    }

}