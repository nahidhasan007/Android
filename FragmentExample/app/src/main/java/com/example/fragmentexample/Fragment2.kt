package com.example.fragmentexample

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "Fragment2"
class Fragment2() : Fragment(R.layout.fragment2), PostClickHandler{
    var contact = ArrayList<ContactInfo>()
    lateinit var fav : MenuItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val rootView = layoutInflater.inflate(R.layout.fragment2,container,false)
        var inputText = arguments?.getString("Input_text")
        //val outText = rootView.findViewById<TextView>(R.id.textView)
       // val btn2 = rootView.findViewById<Button>(R.id.fragment2btn)
       // btn2.setOnClickListener(){ replaceFragment(Fragment2())}
        //val btn = rootView.findViewById<Button>(R.id.inputText)
        var input = inputText
        val pref = activity?.getSharedPreferences("Login",MODE_PRIVATE)
        val msg = pref?.getString("appName","Default message shown!")
        /*btn2.setOnClickListener(){
            val msg = editText.text.toString()
            val sharedPref = getSharedPreferences(
                getString(R.string.demo), Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("Message",msg)
            editor.apply()
            textView.text = msg



        }

        val getshared = getSharedPreferences(getString(R.string.demo), Context.MODE_PRIVATE)
        val getmsg = getshared.getString("Message","Default msg shown because of NUll! value")
        textView.text = getmsg

         */
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.contactInfo)
        contact.add(ContactInfo(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contact.add(ContactInfo(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contact.add(ContactInfo(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contact.add(ContactInfo(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contact.add(ContactInfo(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contact.add(ContactInfo(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contact.add(ContactInfo(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contact.add(ContactInfo(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contact.add(ContactInfo(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contact.add(ContactInfo(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        val mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = Adapter(contact,this)
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.title = input
        //outText.text = inputText
        input = ""
        return rootView
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        fav = menu.add("Prev")
        inflater.inflate(R.menu.menu_xml,menu)
        super.onCreateOptionsMenu(menu, inflater)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.menu_item -> {
            //if (parentFragmentManager.backStackEntryCount > 0) {
            val pref = activity?.getSharedPreferences("Login", MODE_PRIVATE)
            pref?.edit()?.putString("Login", null)?.apply()
            //parentFragmentManager.popBackStack();
            // }
            val fm = requireActivity().supportFragmentManager
            val ft = fm.beginTransaction()
            val frag1 = Fragment1()
            ft.replace(R.id.fragmentContainer,frag1)
            ft.commit()
            true
        }
        else ->{
            super.onOptionsItemSelected(item)
        }
    }






    /* override fun onResume() {
         val btn = findViewById<Button>(R.id.inputText)
         val editText = findViewById<EditText>(R.id.editText)
         btn.setOnClickListener(){
             val msg = editText.text.toString()
             val sharedPref = getSharedPreferences(
                 getString(R.string.demo), Context.MODE_PRIVATE)
             val editor = sharedPref.edit()
             editor.putString("Message",msg)
             editor.apply()
             textView.text = msg



         }
         val getshared = getSharedPreferences(getString(R.string.demo), Context.MODE_PRIVATE)
         val getmsg = getshared.getString("Message","Default msg shown because of NUll! value")
         textView.text = getmsg
         super.onResume()
         (requireActivity() as MainActivity).supportActionBar?.title = "Deals"
     }

     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
    }

    override fun clickedPosTItem(post: ContactInfo,pos:Int) {
        //Log.i(TAG,post.name+post.contact+"pos: "+pos)
        val bundle = bundleOf("Data" to post)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        val frag = DataFragment()
        frag.arguments = bundle
        transaction.replace(R.id.fragmentContainer,frag)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}

