package net.victor.apktarpeya

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import net.victor.apktarpeya.adapter.CustomAdapter
import net.victor.apktarpeya.model.Tarea
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "**Victor**"
    }

    private lateinit var refTareas: DatabaseReference
    private lateinit var tareas: ArrayList<Tarea>
    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Referencia base de datos
        val database = FirebaseDatabase.getInstance()
        refTareas = database.getReference("tareas")

        //Iniciar arraylist
        tareas = ArrayList()

        //Pintar el adapter
        rvTareas.layoutManager = LinearLayoutManager(this)
        adapter = CustomAdapter(this, R.layout.row_tarea, tareas, refTareas)
        rvTareas.adapter = adapter


        // Read from the database
        refTareas.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e(TAG, dataSnapshot.childrenCount.toString())
                tareas.clear()
                for (dataSnapshothijo in dataSnapshot.children) {
                    val keyListener = dataSnapshothijo.key
                    val tarea = dataSnapshothijo.getValue(Tarea::class.java)
                    tarea!!.id = keyListener
                    tareas.add(tarea!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(TAG, "Error de lectura.", error.toException())
            }
        })

        fab.setOnClickListener { view ->
            addTarea()
        }


    }

    fun addTarea() {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        alert {
            title = "Nueva tarea"
            customView {
                verticalLayout {
                    lparams(width = wrapContent, height = wrapContent)
                    val etTarea = editText {
                        hint = "Tarea"
                        padding = dip(16)
                    }
                    val etDescripcion = editText {
                        hint = "Descripcion"
                        padding = dip(16)
                    }

                    positiveButton("Aceptar") {
                        if (etTarea.text.toString().length == 0)
                            toast("¡El campo Tarea es obligatorio!")
                        else {
                            val tarea = Tarea("", etTarea.text.toString(), etDescripcion.text.toString(), currentDate)
                            refTareas.push().setValue(tarea)
                        }
                    }
                }
            }
        }.show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

