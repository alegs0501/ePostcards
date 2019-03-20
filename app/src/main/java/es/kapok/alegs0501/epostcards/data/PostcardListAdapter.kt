package es.kapok.alegs0501.epostcards.data

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import es.kapok.alegs0501.epostcards.R
import es.kapok.alegs0501.epostcards.models.Postcard

class PostcardListAdapter(private val list:ArrayList<Postcard>, private val context: Context):
        RecyclerView.Adapter<PostcardListAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnclickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(es.kapok.alegs0501.epostcards.R.layout.front_card,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position], mListener)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(postcard: es.kapok.alegs0501.epostcards.models.Postcard, listener: OnItemClickListener?) {
            var image: ImageView = itemView.findViewById(R.id.front_image)
            val bitmap = BitmapFactory.decodeByteArray(postcard.front, 0, postcard.front.size)
            image.setImageBitmap(bitmap)

            itemView.setOnClickListener {
                if (listener != null) {
                    val position: Int = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position)
                    }
                }
            }
        }
    }

}