package com.example.fragmentexample

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView




class DataFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_data, container, false)
        val heading = rootView.findViewById<TextView>(R.id.Heading)
        val pic = rootView.findViewById<ImageView>(R.id.pic)
        val subHeading = rootView.findViewById<TextView>(R.id.SubHeading)
        val address = rootView.findViewById<TextView>(R.id.address)
        val data = arguments?.getParcelable<ContactInfo>("Data")
        if (data != null) {
            heading.text = data.name
            pic.setImageResource(data.img)
            subHeading.text = data.contact
            address.text = data.address
        }
        return rootView
    }


}