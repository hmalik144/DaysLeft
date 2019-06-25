package com.appttude.h_mal.days_left;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appttude.h_mal.days_left.Abn.AbnListAdapter;
import com.appttude.h_mal.days_left.Abn.AbnObject;
import com.appttude.h_mal.days_left.Objects.ShiftObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class FilterDialogClass extends AlertDialog {

    private FilterDialogClickListener listener;
    private TextView fromButton;
    private TextView toButton;
    private DatePicker datePicker;

    private String dateSelectionFrom;
    private String dateSelectionTo;

    private String currentTag = "from";

    protected FilterDialogClass(@NonNull Context context) {
        super(context);
    }

    protected FilterDialogClass(@NonNull Context context, int selection, FilterDialogClickListener listener) {
        super(context);
        init(selection);

        this.listener = listener;
    }


    public void init(int selection){
        View view;
        if (selection == 0){
            view = viewOne();
        }else {
            view = viewTwo();
        }

        setView(view);
        setButton(BUTTON_NEGATIVE, getContext().getResources().getString(android.R.string.cancel), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public View viewOne(){
        View view = View.inflate(getContext(),R.layout.dialog_previous_abns_used,null);

        final ListView listView = view.findViewById(R.id.list_item_list_dialog);
        view.findViewById(R.id.button_list_dialog).setVisibility(View.GONE);

        Map<String, AbnObject> mappedList = new HashMap<>();

        for(ShiftObject shiftObject : MainActivity.shiftObjectArrayList){
            mappedList.put(shiftObject.getAbnObject().getAbn(),shiftObject.getAbnObject());
        }

        final List<AbnObject> abnObjectList =  new ArrayList<>(mappedList.values());

        listView.setAdapter(new AbnListAdapter(getContext(),abnObjectList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentAbn = abnObjectList.get(position).getAbn();

                listener.applyAbn(currentAbn);
                dismiss();
            }
        });

        return view;
    }

    public View viewTwo(){
        View view = View.inflate(getContext(),R.layout.date_selector_dialog,null);

        fromButton = view.findViewById(R.id.from_date);
        toButton = view.findViewById(R.id.to_date);
        datePicker = view.findViewById(R.id.date_picker);

        fromButton.setOnClickListener(onClickListener);
        toButton.setOnClickListener(onClickListener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(dateChangedListener);
        }else{
            datePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        toggleTextButtons(true);

        Calendar calendar = Calendar.getInstance();
        String dateString = retrieveDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) +1, calendar.get(Calendar.DAY_OF_MONTH));

        dateSelectionFrom = dateString;
        dateSelectionTo = dateString;

        setButton(BUTTON_POSITIVE, getContext().getResources().getString(android.R.string.yes), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //interface results back
                if (!dateSelectionFrom.equals(dateSelectionTo)){
                    listener.applyDates(dateSelectionFrom,dateSelectionTo);
                }

                Toast.makeText(getContext(), dateSelectionFrom + " - " + dateSelectionTo, Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        return view;
    }

    public interface FilterDialogClickListener {
        void applyDates(String arg1,String arg2);

        void applyAbn(String abn);
    }

    DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            if (currentTag.equals("from")){
                dateSelectionFrom = retrieveDateString(year, monthOfYear, dayOfMonth);
            }else {
                dateSelectionTo = retrieveDateString(year, monthOfYear, dayOfMonth);
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentTag = (String) v.getTag();

            if (currentTag.equals("from")){
                toggleTextButtons(true);
                setDateOnDatePicker(dateSelectionFrom);

            }else {
                toggleTextButtons(false);
                setDateOnDatePicker(dateSelectionTo);
            }
        }
    };

    private String retrieveDateString(int year, int monthOfYear, int dayOfMonth){
        DecimalFormat mFormat= new DecimalFormat("00");

        String monthString = mFormat.format(monthOfYear );
        String dayString = mFormat.format(dayOfMonth);

        return year + "-" + monthString + "-" + dayString;
    }

    private void setDateOnDatePicker(String dateString){
        /* 2019-06-04 */
        int year = Integer.parseInt(dateString.substring(0,4));
        int month = Integer.parseInt(dateString.substring(5,7)) - 1;
        int day = Integer.parseInt(dateString.substring(8));

        datePicker.init(year,month,day,dateChangedListener);

    }

    private void toggleTextButtons(boolean top){
        setFadeAnimation(fromButton);
        setFadeAnimation(toButton);

        if (top){
            fromButton.setTypeface(null, Typeface.BOLD);
            toButton.setTypeface(null, Typeface.NORMAL);

            fromButton.setBackgroundColor(getContext().getResources().getColor(R.color.one));
            toButton.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
        }else {
            toButton.setTypeface(null, Typeface.BOLD);
            fromButton.setTypeface(null, Typeface.NORMAL);

            toButton.setBackgroundColor(getContext().getResources().getColor(R.color.one));
            fromButton.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
        }
    }

    private void setFadeAnimation(View view){
        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in);

        view.setAnimation(bottomUp);
    }
}
