package com.appttude.h_mal.days_left.Abn;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.appttude.h_mal.days_left.R;

import java.util.List;
import java.util.zip.Inflater;

public class AbnListAdapter extends ArrayAdapter<AbnObject> {

    private Context context;

    public AbnListAdapter(@NonNull Context context, @NonNull List<AbnObject> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.abn_list_item,parent, false);
        }

        AbnObject currentFarm = getItem(position);

        TextView farmNameTextView = view.findViewById(R.id.farm_name);
        TextView abnTextview = view.findViewById(R.id.abn_text);
        TextView postCodeTextView = view.findViewById(R.id.postcode_text);

        farmNameTextView.setText(currentFarm.getCompanyName());
        abnTextview.setText(currentFarm.getAbn());
        postCodeTextView.setText(currentFarm.getState() + " " + currentFarm.getPostCode());

        return view;
    }


}
