package com.example.appconpostgresql

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nombre = findViewById<EditText>(R.id.editTextTextPersonName)
        val direccion = findViewById<EditText>(R.id.editTextTextPersonName2)
        val edad = findViewById<EditText>(R.id.editTextTextPersonName3)
        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)
        button.setOnClickListener {
            cambiarPantalla()
        }
        button2.setOnClickListener {
            InsertarDatos(
                nombre = nombre.text.toString(),
                direccion = direccion.text.toString(),
                edad = toInteger(edad.text.toString()) )
        }
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
            val url = "http://192.168.43.197/postgrest/registrar.php"
            val queue = Volley.newRequestQueue(this)
            val parametrosJson = JSONObject()
            parametrosJson.put("nombre",nombre)
            parametrosJson.put("direccion",direccion)
            parametrosJson.put("edad",edad.toString())

            val textview =  findViewById<TextView>(R.id.textView)
            val stringRequest = JsonObjectRequest(Request.Method.POST, url,parametrosJson,
                Response.Listener {
                        response -> textview.text = "Registrado con exito"
                },Response.ErrorListener {
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
    private fun cambiarPantalla(){
        val intent:Intent = Intent(this,ListaUsuarios::class.java)
        startActivity(intent)
    }
}