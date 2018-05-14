package net.victor.apktarpeya.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.row_tarea.view.*
import net.victor.apktarpeya.model.Tarea
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


/**
 * Created by diurno on 19/02/2018.
 */
class CustomAdapter (val context: Context,
                     val layout: Int,
                     val dataList: ArrayList<Tarea>,
                     val receta: DatabaseReference
): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    companion object {
        private val REQUEST_DETALLE=0
    }

    //private lateinit var recetaException : DatabaseError
    //private lateinit var unit : Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item,position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {

        fun bind(dataItem: Tarea, position: Int){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            itemView.tvTarea.text = dataItem.nombreTarea
            itemView.tvDetalle.text = dataItem.detalleTarea
            itemView.tvFecha.text = dataItem.fechaTarea



            itemView.setOnLongClickListener({
                onLongItemClick(dataItem)
            })
        }


        private fun onLongItemClick(dataItem: Tarea): Boolean {
            context.alert("¿Se ha completado ya a ${dataItem.nombreTarea}?") {
                title = "Confirm"
                yesButton {
                    dataList.remove(dataItem)
                    notifyDataSetChanged()
                    //unit.equals(dataItem)
                    //receta.removeValue(recetaException, receta, unit)
                    receta.child(dataItem.id).removeValue()

                }
                noButton { }
            }.show()

            return true
        }

    }
}