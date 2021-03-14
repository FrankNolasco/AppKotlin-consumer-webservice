package com.example.appconpostgresql

import android.app.Person
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appconpostgresql.databinding.ActivityListaUsuariosBinding
import org.json.JSONArray
import org.json.JSONObject

class ListaUsuarios : AppCompatActivity() {
    private lateinit var binding: ActivityListaUsuariosBinding
    val Personas = mutableListOf<Persona>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ListarDatos()
    }
    fun initRecycler(){
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = PersonAdapter(Personas)
        binding.recyclerview.adapter = adapter
    }
    fun ListarDatos(){
        val url = "http://192.168.43.197/postgrest/listar.php"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = JsonArrayRequest(Request.Method.GET, url,null,
            Response.Listener {
                    response : JSONArray ->
                run {
                    llenarLista(response)
                }
            }, Response.ErrorListener {
                    error: VolleyError? -> print("Error")
            })
        stringRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0,
            1f
        )
        Singleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    fun llenarLista(response : JSONArray){
        for (i in 0 until response.length()) {
            val item = response.getJSONObject(i)
            val id_persona = item.getInt("id_persona")
            val nombre = item.getString("nombre")
            val direccion = item.getString("direccion")
            val edad = item.getInt("edad")
            val p : Persona = Persona(id_persona,nombre,direccion,edad)
            Personas.add(p)
        }
        initRecycler()
    }
}