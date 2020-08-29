package com.app.trueleap.home.studentsubject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.app.trueleap.R;
import com.app.trueleap.home.studentsubject.ClassModel;
import com.app.trueleap.home.studentsubject.interfaces.subjectlickListener;

import java.util.ArrayList;

public class subject_adapter extends RecyclerView.Adapter<subject_adapter.ViewHolder> {
    ArrayList<ClassModel> mValues;
    Context mContext;
    protected subjectlickListener mListener;

    public subject_adapter(Context context, ArrayList<ClassModel> values, subjectlickListener itemListener) {
        mValues = values;
        mContext = context;
        mListener= itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subject_title;
        public CardView subject_card;
        ClassModel item;
        int position_data;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            subject_title = v.findViewById(R.id.subject_name);
            subject_card = v.findViewById(R.id.sub_card);
            subject_card.setOnClickListener(this);
        }

        public void setData(ClassModel item) {
            this.item = item;
            subject_title.setText(item.getSubject());
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.subject_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));
        Vholder.position_data = position;
    }

    @Override
    public int getItemCount() {
        Log.d("fsdc","erefefefe"+mValues.size());
        return mValues.size();
    }
}