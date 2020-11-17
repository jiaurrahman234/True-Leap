package com.app.trueleap.MessagingModule.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.trueleap.MessagingModule.model.messageModel;
import com.app.trueleap.R;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import java.util.ArrayList;

public class chat_adapter extends RecyclerView.Adapter<chat_adapter.ViewHolder> {
    ArrayList<messageModel> mValues;
    Context mContext;
    protected recyclerviewClickListener mListener;
    public String TAG = chat_adapter.class.getSimpleName();

    public chat_adapter(Context context, ArrayList<messageModel> values, recyclerviewClickListener itemListener) {
        mValues = values;
        mContext = context;
        mListener= itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView user_name, chat_text;
        public LinearLayout message_row;
        messageModel item;
        int position_data;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            user_name = v.findViewById(R.id.user_name);
            chat_text = v.findViewById(R.id.chat_text);
            message_row = v.findViewById(R.id.message_row);
            message_row.setOnClickListener(this);
        }

        public void setData(messageModel item) {
            this.item = item;
            user_name.setText(item.getUser());
            chat_text.setText(item.getChatText());
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_row_item, parent, false);
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