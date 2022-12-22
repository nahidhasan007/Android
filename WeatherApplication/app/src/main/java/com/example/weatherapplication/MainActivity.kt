package com.example.weatherapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast



class MainActivity : AppCompatActivity() {
    private lateinit var mSearchEditTextView : EditText
    private lateinit var mDisplayTextView : TextView
    private lateinit var mSearchResults : TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get layout views by Id
         mSearchEditTextView = findViewById<EditText>(R.id.et_search_box)
         mDisplayTextView = findViewById<TextView>(R.id.tv_url_display)
         mSearchResults = findViewById<TextView>(R.id.tv_github_search_results_json)

    }
    private fun makeGithubSearchQuery():Unit{
        val githubQuery = mSearchEditTextView.text.toString()
        val githubSearchUrl = NetworkUtils.buildUrl(githubQuery)
        mDisplayTextView.text = githubSearchUrl.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuItemThatWasSelected = item.itemId
        if(menuItemThatWasSelected==R.id.action_search)
        {
            //Toast.makeText(this, "Search Clicked!", Toast.LENGTH_SHORT).show()
            //display searchQuery
            makeGithubSearchQuery()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}