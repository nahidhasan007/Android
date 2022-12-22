package com.example.crud

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.data.User

class Adapter(private val clickHandler : ClickPostHandler): RecyclerView.Adapter<Adapter.Viewholder>() {

   private var userList = emptyList<User>()
   //private val clickHandler : ClickPostHandler()

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.userinfo,parent,false)
      return Viewholder(view)
   }

   override fun onBindViewHolder(holder: Viewholder, position: Int) {
      holder.itemView.rootView.setOnClickListener{
         clickHandler.userClicked(userList[position],position)
      }
      holder.name.text = userList[position].name
      holder.phone.text = userList[position].phone
      holder.address.text = userList[position].address
      holder.id.text = userList[position].id.toString()
   }

   override fun getItemCount(): Int {
      return userList.size
   }
   fun setData(user:List<User>)
   {
      this.userList = user
      notifyDataSetChanged()
   }

   inner class Viewholder(view: View):RecyclerView.ViewHolder(view){
      val id:TextView
      val name: TextView
      val phone:TextView
      val address:TextView

      init {
          id = view.findViewById<TextView>(R.id.uid)
          name = view.findViewById<TextView>(R.id.name)
          phone = view.findViewById<TextView>(R.id.phone)
          address = view.findViewById<TextView>(R.id.address)
      }
      }

   }

