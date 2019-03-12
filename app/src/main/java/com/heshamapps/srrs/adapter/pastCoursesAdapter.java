package com.heshamapps.srrs.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.heshamapps.srrs.R;
import com.heshamapps.srrs.models.Courses;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class pastCoursesAdapter extends RecyclerView.Adapter<pastCoursesAdapter.ViewHolder> {

    Activity mContext;
    private ArrayList<Courses> itemsData;
    public pastCoursesAdapter(ArrayList<Courses> itemsData, Activity context) {
        this.itemsData = itemsData;
        this.mContext=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public pastCoursesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData


switch (itemsData.get(position).getStatus()){


    case "notTaken":  viewHolder.indicatorColor.setBackground(mContext.getDrawable(R.drawable.circle_red));
        break;
        case  "inProgress": viewHolder.indicatorColor.setBackground(mContext.getDrawable(R.drawable.circle_yellow));
            break;
            case  "passed": viewHolder.indicatorColor.setBackground(mContext.getDrawable(R.drawable.circle_green));
                break;
}
      viewHolder.courseCodeTV.setText(itemsData.get(position).getCourseCode());
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.indicatorColor)
         ImageButton indicatorColor;

        @BindView(R.id.courseCodeTV)
        TextView courseCodeTV;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this,itemLayoutView);

        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}
