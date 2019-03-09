package es.kapok.alegs0501.epostcards.data

import android.content.Context
import android.support.v7.cardview.R
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import es.kapok.alegs0501.epostcards.models.Filter
import kotlinx.android.synthetic.main.filter_card.view.*


class FilterListAdapter(private val list:ArrayList<Filter>, private val context: Context):
                        RecyclerView.Adapter<FilterListAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(es.kapok.alegs0501.epostcards.R.layout.filter_card,
                        parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }



    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindItem(filter: es.kapok.alegs0501.epostcards.models.Filter){
            var name: TextView = itemView.findViewById(es.kapok.alegs0501.epostcards.R.id.filter_name)
            name.text = filter.name_filter
        }
    }

}

