package com.app.trueleap.documentSearch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.trueleap.R;
import com.app.trueleap.documentSearch.model.SresultItem;
import com.app.trueleap.home.studentsubject.interfaces.subjectlickListener;
import com.app.trueleap.home.studentsubject.model.ClassModel;
import com.app.trueleap.interfaces.recyclerviewClickListener;

import java.util.ArrayList;

public class search_result_adapter extends RecyclerView.Adapter<search_result_adapter.ViewHolder> {
    ArrayList<SresultItem> mValues;
    Context mContext;
    protected recyclerviewClickListener mListener;
    public String TAG = search_result_adapter.class.getSimpleName();

    public search_result_adapter(Context context, ArrayList<SresultItem> values, recyclerviewClickListener itemListener) {
        mValues = values;
        mContext = context;
        mListener= itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView filename;
        public TextView content_excerpt;
        public LinearLayout result_item;
        SresultItem item;
        int position_data;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            filename = v.findViewById(R.id.file_name);
            content_excerpt = v.findViewById(R.id.content);
            result_item = v.findViewById(R.id.result_item);
            result_item.setOnClickListener(this);
        }

        public void setData(SresultItem item) {
            this.item = item;
            filename.setText(item.getFilename());
            content_excerpt.setText(item.get_content_exceprt());
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));
        Vholder.position_data = position;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"size "+ mValues.size());
        return mValues.size();
    }
}