package com.kram.vlad.weather.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kram.vlad.weather.R;
import com.kram.vlad.weather.Utils;
import com.kram.vlad.weather.activitys.MainActivity;
import com.kram.vlad.weather.callbacks.ForecastDateCallback;
import com.kram.vlad.weather.models.Weather;
import com.kram.vlad.weather.settings.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vlad on 17.08.17.
 * Adapter For date recycler view
 */

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.CardViewHolder> {

    public static final String TAG = DateAdapter.class.getSimpleName();

    private Weather[] mWeather;
    private ForecastDateCallback mForecastDateCallback;
    private Context mContext;

    public DateAdapter(Weather[] weather, ForecastDateCallback forecastDateCallback, Context context) {
        mWeather = weather;
        mForecastDateCallback = forecastDateCallback;
    }

    @Override
    public DateAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent
                .getContext())
                .inflate(R.layout.date_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, final int position) {

        new Utils().setTypeface(mContext, "qontra", holder.date);

        if (MainActivity.sRecyclerViewDateCurrentPosition != position) {
            holder.dateLayout.setBackgroundResource(R.color.white);
        } else {
            holder.dateLayout.setBackgroundResource(R.color.mikadoyellow);
        }

        holder.dateLayout.setOnClickListener(v -> {
            mForecastDateCallback.forecastDateCallback(mWeather, position);
            holder.dateLayout.setBackgroundResource(R.color.mikadoyellow);

            MainActivity.sRecyclerViewDateCurrentPosition = position;
        });

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(Preferences.startDateFormat);
            Date date = formatter.parse(mWeather[position].getDate());
            formatter = new SimpleDateFormat(Preferences.dateFormat);
            holder.date.setText(formatter.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mWeather.length;
    }


    class CardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date) TextView date;
        @BindView(R.id.date_layout) RelativeLayout dateLayout;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            new Utils().setTypeface(mContext, "qontra", date);
        }
    }
}