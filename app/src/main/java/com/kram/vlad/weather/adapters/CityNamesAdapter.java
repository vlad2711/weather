package com.kram.vlad.weather.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kram.vlad.weather.R;
import com.kram.vlad.weather.Utils;
import com.kram.vlad.weather.activitys.MainActivity;
import com.kram.vlad.weather.callbacks.CityChooseCallback;
import com.kram.vlad.weather.callbacks.UpdateItemCallback;
import com.kram.vlad.weather.settings.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vlad Kramarenko on 23.01.2017.
 * Adapter for RecyclerView
 */
public class CityNamesAdapter extends RecyclerView.Adapter<CityNamesAdapter.CardViewHolder> {

    public static final String TAG = CityNamesAdapter.class.getSimpleName();

    private UpdateItemCallback mUpdateItemCallback;
    private CityChooseCallback mCityChooseCallback;
    private Context mContext;
    public CityNamesAdapter(MainActivity mainActivity, Context context) {
        mUpdateItemCallback = mainActivity;
        mCityChooseCallback = mainActivity;
        mContext = context;
    }

    @Override
    public CityNamesAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        return new CardViewHolder(LayoutInflater.from(parent
                .getContext())
                .inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CityNamesAdapter.CardViewHolder holder, final int position) {

        new Utils().setTypeface(mContext, "qontra", holder.textView);

        if (MainActivity.sRecyclerViewCityCurrentPosition != position) {
            holder.marker.setImageResource(R.drawable.marker_yellow);
        }
        else {
            holder.marker.setImageResource(R.drawable.marker_red);
        }

        holder.delete.setOnClickListener(v -> {
            Preferences.CITYS.remove(position);
            mUpdateItemCallback.updateItemCallback();
        });
        holder.relativeLayout.setOnClickListener(v -> {

          //  holder.marker.setImageResource(R.drawable.marker_red);
            mCityChooseCallback.cityChooseCallback(Preferences.CITYS.get(position), position);
        });
        holder.textView.setText(Preferences.CITYS.get(position).name);
    }

    @Override
    public int getItemCount() {
        return Preferences.CITYS.size();
    }


    class CardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView) ImageView delete;
        @BindView(R.id.city) TextView textView;
        @BindView(R.id.itemLayout) RelativeLayout relativeLayout;
        @BindView(R.id.marker) ImageView marker;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            new Utils().setTypeface(mContext, "qontra", textView);
        }
    }
}
