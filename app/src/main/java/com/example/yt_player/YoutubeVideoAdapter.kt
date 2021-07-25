package com.example.yt_player

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso

class YoutubeVideoAdapter(context: Context, val resourceId: Int, data: List<YoutubeVideo>) : ArrayAdapter<YoutubeVideo>(context, resourceId, data){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId,parent,false)
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        val title: TextView = view.findViewById(R.id.title)
        val youtubeVideo = getItem(position)
        if (youtubeVideo != null){
            title.text = youtubeVideo.title
            Picasso.get().load(youtubeVideo.thumbnail).into(thumbnail)

            thumbnail.setOnClickListener {
                Toast.makeText(context, "Go to play", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, YoutubePlayerActivity::class.java)
                intent.putExtra("videoId", youtubeVideo.videoId)
                startActivity(context, intent, null)
            }

            title.setOnClickListener {
                Toast.makeText(context, "Go to play", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, YoutubePlayerActivity::class.java)
                intent.putExtra("videoId", youtubeVideo.videoId)
                startActivity(context, intent, null)
            }
        }
        return  view
    }
}