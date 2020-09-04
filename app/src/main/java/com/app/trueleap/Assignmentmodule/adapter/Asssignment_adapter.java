package com.app.trueleap.Assignmentmodule.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.trueleap.Assignmentmodule.interfaces.assignmentClickListener;
import com.app.trueleap.Assignmentmodule.model.AssignmentModel;
import com.app.trueleap.Classnotemodule.model.ClassnoteModel;
import com.app.trueleap.R;
import java.util.ArrayList;

public class Asssignment_adapter extends RecyclerView.Adapter<Asssignment_adapter.ViewHolder> {
    ArrayList<ClassnoteModel> mValues;
    Context mContext;
    protected assignmentClickListener mListener;

    public Asssignment_adapter(Context context, ArrayList<ClassnoteModel> values, assignmentClickListener itemListener) {
        mValues = values;
        mContext = context;
        mListener= itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView note_title;
        public TextView note_text;
        public TextView note_date;
        public LinearLayout view_all_note;
        ClassnoteModel item;
        int position_data;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            note_text = v.findViewById(R.id.note_text_excerpt);
            note_title = v.findViewById(R.id.note_title);
            note_date = v.findViewById(R.id.note_date);
            view_all_note = v.findViewById(R.id.view_detail);
            view_all_note.setOnClickListener(this);
        }

        public void setData(ClassnoteModel item) {
            this.item = item;
            note_title.setText(item.getNote_title());
            note_text.setText(item.get_doucument_exceprt());
            note_date.setText(item.getUploaded_date());
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.assignment_note_row_layout, parent, false);
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