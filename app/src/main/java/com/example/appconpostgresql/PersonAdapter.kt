package com.example.appconpostgresql

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appconpostgresql.databinding.ItemPersonaBinding
import org.json.JSONObject

class PersonAdapter(val persona:List<Persona>): RecyclerView.Adapter<PersonAdapter.PersonHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        val layoutInflater = LayoutInflater.from(parent.context );
        return PersonHolder(layoutInflater.inflate(R.layout.item_persona,parent,false),parent);
    }

    override fun getItemCount(): Int = persona.size

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        holder.bind(persona[position])
    }
    class PersonHolder(val view: View,val parent: ViewGroup):RecyclerView.ViewHolder(view){
        val binding = ItemPersonaBinding.bind(view)
        fun bind(person : Persona){
            binding.tvName.text = person.nombre
            binding.tvDireccion.text = person.direccion
            binding.tvEdad.text = person.edad.toString() + " Años"
            binding.btnModificar.setOnClickListener {
                handleModificar(person)
            }
            binding.btnEliminar.setOnClickListener {
                handleDelete(person)
            }
        }
        private fun handleModificar(p:Persona){
            val intent: Intent = Intent(parent.context,ModificarUsuario::class.java)
            intent.putExtra("id_persona",p.id_persona.toString())
            intent.putExtra("nombre",p.nombre)
            intent.putExtra("direccion",p.direccion)
            intent.putExtra("edad",p.edad.toString())
            parent.context.startActivity(intent)
        }
        private fun handleDelete(p: Persona) {
            val url = "http://192.168.43.197/postgrest/eliminar.php"
            val queue = Volley.newRequestQueue(parent.context)
            val parametrosJson = JSONObject()
            parametrosJson.put("id_persona",p.id_persona)
            parametrosJson.put("nombre",p.nombre)
            parametrosJson.put("direccion",p.direccion)
            parametrosJson.put("edad",p.edad.toString())
            val stringRequest = JsonObjectRequest(Request.Method.POST, url,parametrosJson,
                Response.Listener {
                        response ->
                    run {
                        val intent: Intent = Intent(parent.context, ListaUsuarios::class.java)
                        parent.context.startActivity(intent)
                    }
                }, Response.ErrorListener {
                        error: VolleyError? -> print("Ocurrio un error")
                })
            stringRequest.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                0,
                1f
            )
            Singleton.getInstance(parent.context).addToRequestQueue(stringRequest)
        }
    }
}