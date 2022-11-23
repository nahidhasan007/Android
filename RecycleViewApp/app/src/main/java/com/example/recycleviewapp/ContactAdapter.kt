package com.example.recycleviewapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(private val dataset:ArrayList<ContactModel>):RecyclerView.Adapter<ContactAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.contact,viewGroup,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text= dataset[position].name
        viewHolder.contact.text= dataset[position].contact
        viewHolder.address.text = dataset[position].address
        viewHolder.img.setImageResource(dataset[position].img)
        //viewHolder.mobile.text= dataset[position].mobile
       // viewHolder.adrs.text = dataset[position].adrs

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val img:ImageView
        val name: TextView
        val contact:TextView
        val address:TextView
        //val mobile:TextView
        //val adrs:TextView


        init {
            img = view.findViewById<ImageView>(R.id.image)
            name = view.findViewById<TextView>(R.id.name)
            contact = view.findViewById<TextView>(R.id.contact)
            address = view.findViewById<TextView>(R.id.address)
            //mobile = view.findViewById<TextView>(R.id.mobile)
            //adrs = view.findViewById<TextView>(R.id.Adrs)
        }


    }
}
