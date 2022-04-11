package com.example.weatherforecastapplication.view.fav.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.view.fav.FavPojo

class FavAdapter(
    private val context : Context,
    private var favList:List<FavPojo>,
    private val onFavClickListener: FavOnClickListner

): RecyclerView.Adapter<FavAdapter.ViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_item,parent,false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val favLocation: TextView
            get() = itemView.findViewById(R.id.favPlaces)
         val linearLayout:LinearLayout
           get() =itemView.findViewById(R.id.linearr)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.favLocation.text = favList[position].locationName
        holder.linearLayout.setOnClickListener {
            onFavClickListener.onClick(favList[position])
        }
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    fun setList(favList: List<FavPojo>){
        this.favList= favList
    }


}