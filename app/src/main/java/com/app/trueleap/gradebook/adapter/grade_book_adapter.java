package com.app.trueleap.gradebook.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.app.trueleap.R;
import com.app.trueleap.gradebook.model.GradeItem;
import com.app.trueleap.interfaces.recyclerviewClickListener;
import java.util.ArrayList;

public class grade_book_adapter extends RecyclerView.Adapter<grade_book_adapter.ViewHolder> {
    ArrayList<GradeItem> mValues;
    Context mContext;
    protected recyclerviewClickListener mListener;
    public String TAG = grade_book_adapter.class.getSimpleName();

    public grade_book_adapter(Context context, ArrayList<GradeItem> values, recyclerviewClickListener itemListener) {
        mValues = values;
        mContext = context;
        mListener= itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView grade_type, grade_weight ,grade_name, compulsary, compulsarypassmark, assessmentdate, outof, bestoutof, partofmidtermgrade;
        public LinearLayout grad_row;
        GradeItem item;
        int position_data;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            grade_type = v.findViewById(R.id.grade_type);
            grade_weight = v.findViewById(R.id.grade_weight);
            grade_name = v.findViewById(R.id.grade_name);
            compulsary = v.findViewById(R.id.compulsary);
            compulsarypassmark = v.findViewById(R.id.compulsarypassmark);
            assessmentdate = v.findViewById(R.id.date);
            outof = v.findViewById(R.id.out_of);
            bestoutof = v.findViewById(R.id.best_out_of);
            partofmidtermgrade = v.findViewById(R.id.part_ofmidterm_grade);
            grad_row = v.findViewById(R.id.grad_row);
            grad_row.setOnClickListener(this);
        }

        public void setData(GradeItem item) {
            this.item = item;
            grade_type.setText(item.getGradetype());
            grade_weight.setText(Double.toString(item.getGradeweight()));
            grade_name.setText(item.getGradename());
            compulsary.setText((item.isCompulsary()==true)? "Yes" : "No");
            compulsarypassmark.setText(Double.toString(item.getCompulsarypassmark()));
            assessmentdate.setText(item.getAssessmentdate());
            outof.setText(Double.toString(item.getOutof()));
            bestoutof.setText(Double.toString(item.getBestoutof()));
            partofmidtermgrade.setText((item.isPartofmidtermgrade()==true)? "Yes" : "No");
        }

        @Override
        public void onClick(View view) {
            /*if (mListener != null) {
                mListener.onClicked(position_data);
            }*/
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grade_item_row, parent, false);
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