package com.example.yt_player

import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(this@MainActivity, "请输入查找内容111！", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()

                    search(query.toString())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    Toast.makeText(this@MainActivity, "请输入查找内容222！", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, newText, Toast.LENGTH_SHORT).show()
                }
                return true
            }
        })

        listView = findViewById(R.id.youtube_list_view)

        search("")

    }

    private fun search(query: String)
    {
        //Get Youtube search data
        Thread {
            //這邊是背景thread在運作, 這邊可以處理比較長時間或大量的運算

            // Define and execute the API request
            val apiKey = getString(R.string.GOOGLE_API_KEY)
            println("apikey: $apiKey")

            val youtubeService = ApiExample.service
            // Define the API request for retrieving search results.
            val request = youtubeService.search().list(listOf("id", "snippet"))
            request.key = apiKey
            request.q = query
            request.type = listOf("video")
            request.fields = "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)"
            request.maxResults = 25

            val response = request.execute()
            println(response)

            val searchResultList = response.items
            val iteratorSearchResults = searchResultList.iterator()

            val listItems = ArrayList<YoutubeVideo>(searchResultList.size)

            while(iteratorSearchResults.hasNext())
            {
                val singleVideo = iteratorSearchResults.next()
                val rId = singleVideo.id

                // Confirm that the result represents a video. Otherwise, the
                // item will not contain a video ID.
                if (rId.kind == "youtube#video") {
                    val thumbnail = singleVideo.snippet.thumbnails.default

                    println(" Video Id" + rId.videoId)
                    println(" Title: " + singleVideo.snippet.title)
                    println(" Thumbnail: " + thumbnail.url)
                    println("\n-------------------------------------------------------------\n")

//                    YoutubeVideo.videoId = singleVideo.id.videoId
//                    YoutubeVideo.title = singleVideo.snippet.title
//                    YoutubeVideo.thumbnail = singleVideo.snippet.thumbnails.default.url
//                    listItems[index] = YoutubeVideo(singleVideo.id.videoId, singleVideo.snippet.title, singleVideo.snippet.thumbnails.default.url)
                    listItems.add(YoutubeVideo(singleVideo.id.videoId, singleVideo.snippet.title, singleVideo.snippet.thumbnails.default.url))
                }
            }

            runOnUiThread{
                //這邊是呼叫main thread handler幫我們處理UI部分
//                val adapter = ArrayAdapter(this, R.layout.youtube_list, listItems)
//                listView.adapter = adapter

                val adapter = YoutubeVideoAdapter(this, R.layout.youtube_list, listItems)
                listView.adapter = adapter
            }
        }.start()
    }

}
