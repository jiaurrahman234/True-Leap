package com.app.trueleap.notification.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.app.trueleap.R;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import com.app.trueleap.notification.NotificationModel;
import java.util.ArrayList;
import static com.app.trueleap.external.CommonFunctions.parse_date_time;

public class notification_adapter extends RecyclerView.Adapter<notification_adapter.ViewHolder> {
    ArrayList<NotificationModel> mValues;
    Context mContext;
    protected recyclerviewClickListener mListener;
    public String TAG = notification_adapter.class.getSimpleName();

    public notification_adapter(Context context, ArrayList<NotificationModel> values, recyclerviewClickListener itemListener) {
        mValues = values;
        mContext = context;
        mListener= itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView notification_title, notification_date;
        public LinearLayout notification_row;
        NotificationModel item;
        int position_data;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            notification_title = v.findViewById(R.id.notification_title);
            notification_date = v.findViewById(R.id.notification_date);
            notification_row = v.findViewById(R.id.notification_row);
            notification_row.setOnClickListener(this);
        }

        public void setData(NotificationModel item) {
            this.item = item;
            notification_title.setText(item.get_note_exceprt());
            if(item.getViewed()){
                notification_title.setTextColor(mContext.getResources().getColor(R.color.text_color_grey,null));
            }
            notification_date.setText(parse_date_time(item.getDate()));
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_row_item, parent, false);
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