package com.app.trueleap.localization.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.app.trueleap.R;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import com.app.trueleap.localization.model.CountryModel;

import java.util.ArrayList;

public class country_adapter extends RecyclerView.Adapter<country_adapter.ViewHolder> {
    ArrayList<CountryModel> mValues;
    Context mContext;
    protected recyclerviewClickListener mListener;

    public country_adapter(Context context, ArrayList<CountryModel> values, recyclerviewClickListener itemListener) {
        mValues = values;
        mContext = context;
        mListener= itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public CardView card;
        CountryModel item;
        int position_data;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            title = v.findViewById(R.id.country_name);
            card = v.findViewById(R.id.country_card);
            card.setOnClickListener(this);
        }

        public void setData(CountryModel item) {
            this.item = item;
            title.setText(item.getName());
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onClicked(position_data);
            }
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.country_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));
        Vholder.position_data = position;
    }

    @Override
    public int getItemCount() {
        Log.d("fsdc","erefefefe:"+ mValues.size());
        return mValues.size();
    }
}