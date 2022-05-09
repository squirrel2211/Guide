package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class ToursActivity : AppCompatActivity() {
    private val cities = listOf<String>("Барнаул", "Новосибирск", "Бийск")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tours)
        val listView = findViewById<ListView>(R.id.toursView)
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cities)
        listView.setOnItemClickListener { adapterView, view, i, l ->
            intent.putExtra("city", cities[i])
            setResult(RESULT_OK, intent)
            finish()
        }



    }
}