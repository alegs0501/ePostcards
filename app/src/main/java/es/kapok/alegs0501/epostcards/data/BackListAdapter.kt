package es.kapok.alegs0501.epostcards.data

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import es.kapok.alegs0501.epostcards.R


class BackListAdapter(private val list:ArrayList<Int>, private val context: Context):
        RecyclerView.Adapter<BackListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BackListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(es.kapok.alegs0501.epostcards.R.layout.back_card,
                parent, false)
        return ViewHolder(view)    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BackListAdapter.ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {


        fun bindItem(back: Int) {
            var image: ImageView = itemView.findViewById(R.id.back_image)
            image.setImageResource(back)

        }
    }

}