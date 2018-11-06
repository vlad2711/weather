package com.kram.vlad.weather.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.kram.vlad.weather.R
import com.kram.vlad.weather.Utils
import com.kram.vlad.weather.activitys.MainActivity
import com.kram.vlad.weather.callbacks.CityChooseCallback
import com.kram.vlad.weather.callbacks.UpdateItemCallback
import com.kram.vlad.weather.settings.Preferences
import kotlinx.android.synthetic.main.item.view.*


/**
 * Created by Vlad Kramarenko on 23.01.2017.
 * Adapter for RecyclerView
 */
class CityNamesAdapter(mainActivity: MainActivity, private val mContext: Context) : RecyclerView.Adapter<CityNamesAdapter.CardViewHolder>() {

    private val mUpdateItemCallback: UpdateItemCallback
    private val mCityChooseCallback: CityChooseCallback

    init {
        mUpdateItemCallback = mainActivity
        mCityChooseCallback = mainActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityNamesAdapter.CardViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        return CardViewHolder(LayoutInflater.from(parent
                .context)
                .inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: CityNamesAdapter.CardViewHolder, position: Int) = holder.bind()
    override fun getItemCount(): Int {
        return Preferences.CITYS.size
    }


    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
        }

        fun bind(){
            Utils().setTypeface(mContext, "qontra", itemView.city)

            if (MainActivity.sRecyclerViewCityCurrentPosition != position) {
                itemView.marker!!.setImageResource(R.drawable.marker_yellow)
            } else {
                itemView.marker!!.setImageResource(R.drawable.marker_red)
            }

            itemView.delete!!.setOnClickListener { v ->
                Preferences.CITYS.removeAt(position)
                mUpdateItemCallback.updateItemCallback()
            }
            itemView.itemLayout !!.setOnClickListener { v ->

                //  holder.marker.setImageResource(R.drawable.marker_red);
                mCityChooseCallback.cityChooseCallback(Preferences.CITYS[position], position)
            }
            itemView.city.text = Preferences.CITYS[position].name
        }
    }

    companion object {

        val TAG = CityNamesAdapter::class.java.simpleName
    }
}
