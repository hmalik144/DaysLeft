package com.appttude.h_mal.days_left.AddItems;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.appttude.h_mal.days_left.Objects.TimeObject;
import com.appttude.h_mal.days_left.R;

import java.util.Calendar;

import static com.appttude.h_mal.days_left.MainActivity.Epoch2DateString;
import static com.appttude.h_mal.days_left.MainActivity.Epoch2DateTimes;

public class InsertTimeDialog{

    Context context;
    TimeObject timeObject;
    OnDialogClickListener listener;

    public interface OnDialogClickListener {
        void onDialogImageRunClick(TimeObject timeObject);

        void onClick(DialogInterface dialog, int which);
    }

    public InsertTimeDialog(Context context, @Nullable TimeObject timeObject, OnDialogClickListener listener) {
        this.context = context;
        this.timeObject = timeObject;
        this.listener = listener;

        DialogTime dialogTime = new DialogTime(context);

    }

    class DialogTime extends AlertDialog.Builder{


        private TimePicker timePickerTimePicker;
        private TextView startTimeTextView;
        private TextView finishTimeTextView;
        private EditText breakEditText;

        public DialogTime(Context context) {
            super(context);
            init();
        }

        public DialogTime(Context context, int themeResId) {
            super(context, themeResId);
        }

        private void init(){
            setView(null);
            setNegativeButton(null,null);
            setPositiveButton(null,null);


            AlertDialog alertDialog = create();
        }

        @Override
        public AlertDialog.Builder setView(View view) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_search_employer, null);

            startTimeTextView = view.findViewById(R.id.start_time);
            finishTimeTextView = view.findViewById(R.id.finish_time);
            timePickerTimePicker = view.findViewById(R.id.time_picker);
//            breakEditText = view.findViewById(R.id.breaktime);

            startTimeTextView.setTag("start");
            finishTimeTextView.setTag("finish");

            startTimeTextView.setOnClickListener(timeSelect);
            finishTimeTextView.setOnClickListener(timeSelect);

            return super.setView(view);
        }

        View.OnClickListener timeSelect = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentTag  = (String) v.getTag();
            }
        };

        @Override
        public AlertDialog create() {
            return super.create();
        }

        @Override
        public AlertDialog.Builder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
            return super.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        @Override
        public AlertDialog.Builder setPositiveButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            return super.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
    }


}
