package com.example.weatherapplication

import android.net.Uri
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


/**
 * These utilities will be used to communicate with the network.
 */
object NetworkUtils {
    val GITHUB_BASE_URL = "https://api.github.com/search/repositories"
    val PARAM_QUERY = "q"

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    private val PARAM_SORT = "sort"
    private val sortBy = "stars"

    /**
     * Builds the URL used to query GitHub.
     *
     * @param githubSearchQuery The keyword that will be queried for.
     * @return The URL to use to query the GitHub server.
     */
    fun buildUrl(githubSearchQuery: String?): URL? {
        // TODO (1) Fill in this method to build the proper GitHub query URL
        val buildUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
            .appendQueryParameter(PARAM_QUERY,githubSearchQuery)
            .appendQueryParameter(PARAM_SORT, sortBy)
            .build()
        var url = URL(null)
        try {
            url = URL(buildUri.toString());}
        catch (e:MalformedURLException){
        }

        return url
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    @Throws(IOException::class)
    public fun getResponseFromHttpUrl(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
        return try {
            val `in` = urlConnection.inputStream
            val scanner = Scanner(`in`)
            scanner.useDelimiter("\\A")
            val hasInput = scanner.hasNext()
            if (hasInput) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}