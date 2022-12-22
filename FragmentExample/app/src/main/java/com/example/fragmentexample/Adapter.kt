package com.example.fragmentexample

import android.icu.text.Transliterator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val dataset: ArrayList<ContactInfo>,
private val clickHandler: PostClickHandler):RecyclerView.Adapter<Adapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_contact,viewGroup,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.rootView.setOnClickListener{clickHandler.clickedPosTItem(dataset[position],position)}
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
           // view.setOnClickListener(this)


        }




    }
}