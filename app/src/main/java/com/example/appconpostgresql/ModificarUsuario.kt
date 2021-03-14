package com.example.appconpostgresql

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appconpostgresql.databinding.ActivityModificarUsuarioBinding
import org.json.JSONObject

class ModificarUsuario : AppCompatActivity() {
    private lateinit var binding: ActivityModificarUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recuperarData()
        binding.sendEdit.setOnClickListener {
            InsertarDatos(
                    nombre = binding.etNombre.text.toString(),
                    direccion = binding.etDireccion.text.toString(),
                    edad = toInteger(binding.etEdad.text.toString()) )
        }
    }
    fun recuperarData(){
        val nombre = getIntent().getStringExtra("nombre")
        val direccion = getIntent().getStringExtra("direccion")
        val edad = getIntent().getStringExtra("edad")
        binding.etNombre.setText(nombre)
        binding.etDireccion.setText(direccion)
        binding.etEdad.setText(edad)
    }

    fun toInteger(s: String): Int {
        try {
            val value = s.toInt()
            return value
        } catch (ex: NumberFormatException) {
            return  -6731
            println("The given string is non-numeric")
        }
    }
    fun InsertarDatos(nombre:String, direccion:String, edad: Int){
        if(edad !== -6731) {
            val url = "http://192.168.43.197/postgrest/modificar.php"
            val queue = Volley.newRequestQueue(this)
            val parametrosJson = JSONObject()
            val id_persona = getIntent().getStringExtra("id_persona")
            parametrosJson.put("id_persona",id_persona)
            parametrosJson.put("nombre",nombre)
            parametrosJson.put("direccion",direccion)
            parametrosJson.put("edad",edad.toString())

            val textview =  findViewById<TextView>(R.id.textView)
            val stringRequest = JsonObjectRequest(Request.Method.POST, url,parametrosJson,
                    Response.Listener {
                        response ->
                        run {
                            val intent: Intent = Intent(this, ListaUsuarios::class.java)
                            this.startActivity(intent)
                        }
                    }, Response.ErrorListener {
                error: VolleyError? -> textview.text = "Registrado con un pequeño error xd"
            })
            stringRequest.retryPolicy = DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    0,
                    1f
            )
            Singleton.getInstance(this).addToRequestQueue(stringRequest)
        }else {

        }

    }
}