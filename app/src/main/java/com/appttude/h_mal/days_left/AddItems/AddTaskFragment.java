package com.appttude.h_mal.days_left.AddItems;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appttude.h_mal.days_left.Objects.TaskObject;
import com.appttude.h_mal.days_left.R;

import static com.appttude.h_mal.days_left.AddItems.AddShiftActivity.TASK_CONSTANT;

public class AddTaskFragment extends Fragment {

    private CardView cardoneCardView;
    private CardView cardtwoCardView;
    private CardView cardthreeCardView;
    private Spinner spinneroneSpinner;
    private Spinner spinnerTwoSpinner;
    private EditText payrateEditText;
    private TextView unittextTextView;
    private ImageButton searchbuttonImageButton;
    private Boolean dialogActive = false;

    private String product = "";
    String[] strings;
    
    String TAG = "AddTaskFragment";
    private String[] taskList;
    private String[] workTypeArray;
    private String current;
    private String previous;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseArrayString();
        previous = taskList[0];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        cardoneCardView = view.findViewById(R.id.card_one);
        cardtwoCardView = view.findViewById(R.id.card_two);
        cardthreeCardView = view.findViewById(R.id.card_three);

        spinneroneSpinner = view.findViewById(R.id.spinner_one);
        spinnerTwoSpinner = view.findViewById(R.id.spinner_Two);

        payrateEditText = view.findViewById(R.id.pay_rate);
        unittextTextView = view.findViewById(R.id.unit_text);

        searchbuttonImageButton = view.findViewById(R.id.search_button);
        searchbuttonImageButton.setOnClickListener(submit);

        if (getArguments() != null){
            Bundle bundle = getArguments();
            TaskObject taskObject = bundle.getParcelable(TASK_CONSTANT);
            spinneroneSpinner.setSelection(getSpinnerOneSelection(taskObject.getWorkType()));
            payrateEditText.setText(String.valueOf(taskObject.getRate()));
            spinnerTwoSpinner.setSelection(getSpinnerTwoSelection(taskObject.getTask()));
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSpinnerOne();
        setupSpinnerTwo();

    }

    private int getSpinnerOneSelection(String s){
        switch (s){
            case "Hourly":
                return 1;
            case "Piece Rate":
                return 2;
        }
        return 0;
    }

    private int getSpinnerTwoSelection(String s){
        String[] strings = getContext().getResources().getStringArray(R.array.task_list);
        int i = 1;
        for (String string: strings){

            if (s.contains(string)){
                return i;
            }
            i++;
        }

        return 0;
    }

    View.OnClickListener submit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String workType = (String) spinneroneSpinner.getSelectedItem();
            String rate = payrateEditText.getText().toString().trim();
            String task = (String) spinnerTwoSpinner.getSelectedItem();
            if (!workType.equals(strings[0]) &&
                    !TextUtils.isEmpty(rate) &&
                    !task.equals(taskList[0])){
                if (((String) spinnerTwoSpinner.getSelectedItem()).contains(workTypeArray[1]) && task.equals(workTypeArray[1])){
                    Toast.makeText(getContext(), "Insert A product Harvested", Toast.LENGTH_SHORT).show();
                }else {
                    rate = String.format("%.2f",Float.valueOf(rate));
                    TaskObject taskObject = new TaskObject(workType,Float.valueOf(rate),task,null,null);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("TaskObject",taskObject);
                    getActivity().setResult(Activity.RESULT_OK,returnIntent);
                    getActivity().finish();
                }

            }else {
                Toast.makeText(getContext(), "Insert All Required data", Toast.LENGTH_SHORT).show();


            }
        }
    };

    private void setupSpinnerOne(){
        workTypeArray = getContext().getResources().getStringArray(R.array.work_type);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        workTypeArray){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView,  @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinneroneSpinner.setAdapter(spinnerArrayAdapter);
        spinneroneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        unittextTextView.setText("per Unit");
                        break;
                    case 2:
                        unittextTextView.setText("per Hour");
                    default:

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupSpinnerTwo(){
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        strings) {
            @Override
            public boolean isEnabled(int position) {
                if(position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView,  @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerTwoSpinner.setAdapter(spinnerArrayAdapter);

        SpinnerInteractionListener listener = new SpinnerInteractionListener();

        spinnerTwoSpinner.setOnItemSelectedListener(listener);
        spinnerTwoSpinner.setOnTouchListener(listener);
    }

    private void initialiseArrayString(){
        taskList = getContext().getResources().getStringArray(R.array.task_list);
        strings = new String[]{taskList[0], taskList[1]+product, taskList[2], taskList[3]};
    }


    public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (userSelect) {
                Toast.makeText(getContext(), "current = " + pos, Toast.LENGTH_SHORT).show();

                previous = current;
                current = (String) parent.getItemAtPosition(pos);

                if (pos == 1){
                        final EditText edittext = new EditText(getContext());
                        edittext.setHint("Product Harvested?");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setView(edittext);
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                spinnerTwoSpinner.setSelection(getPosition(previous));
                                dialog.dismiss();
                            }
                        }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String text = edittext.getText().toString();
                                if (!TextUtils.isEmpty(text)){
                                    product = " " + text;
                                    initialiseArrayString();
                                    setupSpinnerTwo();
                                    spinnerTwoSpinner.setSelection(1);
                                }
                                dialog.dismiss();
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialogActive = false;
                            }
                        });
                        builder.setCancelable(false).create().show();
                    }

                userSelect = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    private int getPosition(String previous){
        int i = 0;
        if (previous != null){
            for (String s: strings){
                if (previous.contains(s)){
                    break;
                }
                i++;

            }
        }

        return i;
    }
}
