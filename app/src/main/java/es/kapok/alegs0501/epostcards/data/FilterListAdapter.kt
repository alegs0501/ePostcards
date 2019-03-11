package es.kapok.alegs0501.epostcards.data

import android.content.Context
import android.support.v7.cardview.R
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import es.kapok.alegs0501.epostcards.CameraActivity
import es.kapok.alegs0501.epostcards.models.CameraPreferences
import es.kapok.alegs0501.epostcards.models.Filter
import kotlinx.android.synthetic.main.filter_card.view.*


class FilterListAdapter(private val list:ArrayList<Filter>, private val context: Context):
                        RecyclerView.Adapter<FilterListAdapter.ViewHolder>(){

    private lateinit var mListener: OnItemClickListener


    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnclickListener(listener: OnItemClickListener){
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(es.kapok.alegs0501.epostcards.R.layout.filter_card,
                        parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position], mListener)
    }


    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindItem(filter: es.kapok.alegs0501.epostcards.models.Filter, listener: OnItemClickListener){
            var name: TextView = itemView.findViewById(es.kapok.alegs0501.epostcards.R.id.filter_name)
            name.text = filter.name_filter

            itemView.setOnClickListener{
                if (listener != null){
                    val position: Int = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position)
                    }
                }
            }
        }
    }

}

