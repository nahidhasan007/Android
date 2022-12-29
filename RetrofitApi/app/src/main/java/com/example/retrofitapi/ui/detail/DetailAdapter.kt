package com.example.retrofitapi.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.retrofitapi.R
import com.example.retrofitapi.model.Model
import org.w3c.dom.Text

class DetailAdapter: RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    private var detailPost = ArrayList<Model>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = detailPost[position].id.toString()
        holder.email.text = detailPost[position].email
        holder.body.text = detailPost[position].body
    }

    override fun getItemCount(): Int {
        return detailPost.size
    }
    fun setData(detailPost:List<Model>)
    {
        this.detailPost= detailPost as ArrayList<Model>
        notifyDataSetChanged()
    }
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val id:TextView
        val email:TextView
        val body:TextView
        init {
            id = view.findViewById<TextView>(R.id.Id)
            email = view.findViewById<TextView>(R.id.email)
            body = view.findViewById<TextView>(R.id.body)
        }

    }
}