package com.appttude.h_mal.days_left.AddItems;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appttude.h_mal.days_left.Abn.AbnObject;
import com.appttude.h_mal.days_left.Objects.TaskObject;
import com.appttude.h_mal.days_left.R;

import java.util.List;

public class DialogListAdapter extends ArrayAdapter<TaskObject> {

    public DialogListAdapter(@NonNull Context context, @NonNull List<TaskObject> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item,parent, false);
        }

        TaskObject currentTask = getItem(position);

        TextView taskTextView = view.findViewById(R.id.task);
        TextView summaryTextview = view.findViewById(R.id.task_summary);
        ImageView taskImageView = view.findViewById(R.id.task_image);

        taskTextView.setText(currentTask.getTask());

        String summary = currentTask.getWorkType() + " - $" + currentTask.getRate()+ " /" ;
        if (currentTask.getWorkType().equals("Piece Rate")){
            summary = summary + "Unit";
            taskImageView.setImageResource(R.drawable.piece);
        }else {
            summary = summary + "Hour";
            taskImageView.setImageResource(R.drawable.task);
        }

        summaryTextview.setText(summary);

        return view;
    }
}
