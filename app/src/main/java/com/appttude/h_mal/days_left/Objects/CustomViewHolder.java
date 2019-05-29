package com.appttude.h_mal.days_left.Objects;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.appttude.h_mal.days_left.R;
import com.appttude.h_mal.days_left.bar.BarView;

public  class CustomViewHolder extends RecyclerView.ViewHolder {

        BarView barView;
        LinearLayout linearLayout;

        public CustomViewHolder(View itemView){
            super(itemView);

            barView = itemView.findViewById(R.id.bar);
            linearLayout = itemView.findViewById(R.id.lin);

        }
}