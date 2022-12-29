package com.example.retrofitapi.ui.list
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapi.R
import com.example.retrofitapi.model.Model

class Adapter(private val clickListener: ClickHandler) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private var post = emptyList<Model>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.rootView.setOnClickListener()
        {
            clickListener.postClicked(post[position],position)
        }
        holder.postBody.text = post[position].title
    }

    override fun getItemCount(): Int {
        return post.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData(post:List<Model>)
    {
        this.post= post
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
       val postBody:TextView
       init {
           postBody = view.findViewById<TextView>(R.id.testData)
       }
    }

}