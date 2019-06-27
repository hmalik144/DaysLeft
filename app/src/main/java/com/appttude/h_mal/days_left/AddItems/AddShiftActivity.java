package com.appttude.h_mal.days_left.AddItems;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appttude.h_mal.days_left.Abn.AbnObject;
import com.appttude.h_mal.days_left.Global.DateDialog;
import com.appttude.h_mal.days_left.Global.FirebaseClass;
import com.appttude.h_mal.days_left.Objects.ShiftObject;
import com.appttude.h_mal.days_left.Objects.TaskObject;
import com.appttude.h_mal.days_left.Objects.TimeObject;
import com.appttude.h_mal.days_left.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static com.appttude.h_mal.days_left.Global.FirebaseClass.EMPLOYER_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.PIECE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.SHIFT_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.SHIFT_ID;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.TASK_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.USER_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.auth;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.getDateTimeString;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.mDatabase;


public class AddShiftActivity extends AppCompatActivity {

    public static final int EMPLOYERREQUEST = 339;
    public static final int TASKREQUEST = 445;
    public static final String REQUEST = "request";
    public static final String EMPLOYER_CONSTANT = "employer";
    public static final String TASK_CONSTANT = "task";

    private TextView lableOne;
    private LinearLayout employerLayout;
    private LinearLayout taskLayout;
    private TextView lableTwo;
    private CardView employerCard;
    private TextView employerName;
    private TextView employerLocation;
    private CardView taskCard;
    private TextView taskName;
    private TextView taskSummary;
    private EditText units;
    private EditText date;

    private CardView selectTimesCardView;
    private LinearLayout timeLayoutLinearLayout;
    private TextView timeTextView;
    private TextView timeSummaryTextView;
    private LinearLayout breakHolderLinearLayout;
    private TextView breakSummaryTextView;
    private TextView lable3TextView;

    private TimeObject timeObject = new TimeObject();
    public AbnObject abnObject;
    public TaskObject taskObject;
    private FirebaseClass firebaseClass;
    private ProgressBar progressBar;
    private String ShiftID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);

        employerCard = findViewById(R.id.select_employer_card);
        employerLayout = findViewById(R.id.employer_layout);
        employerName = findViewById(R.id.employer_name);
        employerLocation = findViewById(R.id.employer_location);
        lableOne = findViewById(R.id.lable_1);

        employerCard.setOnClickListener(employerListener);

        taskCard = findViewById(R.id.select_task_card);
        taskLayout = findViewById(R.id.task_layout);
        taskName = findViewById(R.id.task);
        taskSummary = findViewById(R.id.task_summary);
        lableTwo = findViewById(R.id.lable_2);

        taskCard.setOnClickListener(taskListener);

        selectTimesCardView = findViewById(R.id.select_times_card);
        timeLayoutLinearLayout = findViewById(R.id.time_layout);
        timeTextView = findViewById(R.id.time);
        timeSummaryTextView = findViewById(R.id.time_summary);
        breakHolderLinearLayout = findViewById(R.id.break_holder);
        breakSummaryTextView = findViewById(R.id.break_summary);
        lable3TextView = findViewById(R.id.lable_3);

        units = findViewById(R.id.units);
        date = findViewById(R.id.date);

        ImageButton submit = findViewById(R.id.search_button);
        submit.setOnClickListener(submitListener);

        Intent intent = getIntent();
        ShiftID = intent.getStringExtra(SHIFT_ID);
        if (ShiftID != null){
            DatabaseReference reference = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child(SHIFT_FIREBASE).child(ShiftID);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ShiftObject shiftObject = dataSnapshot.getValue(ShiftObject.class);
                    abnObject = shiftObject.getAbnObject();
                    taskObject = shiftObject.getTaskObject();
                    if (shiftObject.timeObject != null){
                        timeObject = shiftObject.getTimeObject();
                        setTimeSummary();
                    }

                    if (shiftObject.getTaskObject().getWorkType().equals(PIECE)){
                        units.setText(String.valueOf(shiftObject.getUnitsCount()));
                    }
                    setTaskCard();
                    setEmployerCard();
                    date.setText(shiftObject.getShiftDate());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        progressBar = findViewById(R.id.progress_bar);

        Log.i("DateTime", "onCreate: " + getDateTimeString());

        selectTimesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDialogClass timeDialogClass = new TimeDialogClass(AddShiftActivity.this);
                timeDialogClass.create().show();
            }
        });

        date.setFocusable(false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dateDialog;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    dateDialog = new DateDialog(AddShiftActivity.this);
                }else {
                    Calendar calendar = Calendar.getInstance();
                    final int selectedYear = calendar.get(Calendar.YEAR);
                    final int selectedMonth = calendar.get(Calendar.MONTH);
                    final int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

                    dateDialog = new DateDialog(AddShiftActivity.this,
                            null,
                            selectedYear,
                            selectedMonth,
                            selectedDay);
                }
                dateDialog.init(date);
                dateDialog.show();
            }
        });

        firebaseClass = new FirebaseClass();

    }

    View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String dateString = date.getText().toString();
            progressBar.setVisibility(View.VISIBLE);

            if (abnObject != null && taskObject != null && !TextUtils.isEmpty(dateString)){
                String workType = taskObject.getWorkType();
                ShiftObject shiftObject = null;

                if (workType.equals("Piece Rate")){
                    String unitString = units.getText().toString();

                    if (!TextUtils.isEmpty(unitString)){
                        float unitFloat = Float.parseFloat(unitString);
                        shiftObject = new ShiftObject(dateString,getDateTimeString(),abnObject,taskObject,unitFloat,timeObject);
                    }else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddShiftActivity.this, "Mandatory information missing", Toast.LENGTH_SHORT).show();
                    }

                }else if(workType.equals("Hourly")){
                    shiftObject = new ShiftObject(dateString,getDateTimeString(),abnObject,taskObject,null,timeObject);
                }else {
                    Toast.makeText(AddShiftActivity.this, "Insert Times", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (shiftObject != null){
                    DatabaseReference shiftReference;

                    if (ShiftID != null){
                        shiftReference = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child(SHIFT_FIREBASE).child(ShiftID);
                        //Updating a shift
                    }else {
                        shiftReference = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child(SHIFT_FIREBASE).push();
                        //Pushing a brand new shift
                    }

                    shiftReference.setValue(shiftObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("Firebase", "onComplete: " + task.getResult());
                            if (task.isSuccessful()){


                                firebaseClass.UpdateListOfPreviouslyUsedAbns(abnObject.getAbn(), new FirebaseClass.Complete() {
                                    @Override
                                    public void taskCompleted(Boolean success) {
                                        if (success){
                                            firebaseClass.PushAbnObject(abnObject,taskObject,progressBar,AddShiftActivity.this);
                                            Log.i("PushPhase", "taskCompleted: UpdateListOfPreviouslyUsedAbns");
                                        }else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(AddShiftActivity.this, "Could not upload Task", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AddShiftActivity.this, "Could not publish shift", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddShiftActivity.this, "Could not publish shift", Toast.LENGTH_SHORT).show();
                }


            }else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddShiftActivity.this, "Mandatory information missing", Toast.LENGTH_SHORT).show();
            }

        }
    };

    View.OnClickListener employerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);

            PreviouslyUsedItemsClass previouslyUsedItemsClass = new PreviouslyUsedItemsClass(progressBar,AddShiftActivity.this);
            DatabaseReference databaseReference = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child("recent"+EMPLOYER_FIREBASE);

            databaseReference.addListenerForSingleValueEvent(previouslyUsedItemsClass);
        }
    };

    View.OnClickListener taskListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);

            if (abnObject != null){
                String abn = abnObject.getAbn();
                AssociatedTasksClass associatedTasks = new AssociatedTasksClass(AddShiftActivity.this, progressBar);
                DatabaseReference taskReference = mDatabase.child(EMPLOYER_FIREBASE).child(abn).child(TASK_FIREBASE);
                taskReference.addListenerForSingleValueEvent(associatedTasks);

            }else {
                Intent intent = new Intent(AddShiftActivity.this,AddItemActivity.class);
                intent.putExtra(REQUEST,TASKREQUEST);
                if (taskObject != null){
                    intent.putExtra(TASK_CONSTANT,taskObject);
                }
                startActivityForResult(intent,TASKREQUEST);
            }
        }
    };

    private void setTimeSummary(){
        if (lable3TextView.getVisibility() == View.VISIBLE){
            toggleViewVisibility(selectTimesCardView);
        }

        timeSummaryTextView.setText(timeObject.getTimeIn() + " - " + timeObject.getTimeOut());
        if (timeObject.getBreakEpoch() > 0){
            breakHolderLinearLayout.setVisibility(View.VISIBLE);
            breakSummaryTextView.setText(timeObject.getBreakEpoch() + " minutes");
        }else {
            breakHolderLinearLayout.setVisibility(View.GONE);
        }

        timeTextView.setText(convertTimeFloat(timeObject.getHours()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EMPLOYERREQUEST) {
            if(resultCode == Activity.RESULT_OK){
                abnObject = data.getParcelableExtra("AbnObject");
                setEmployerCard();
                
                TaskObject taskObjectRel = data.getParcelableExtra("TaskObject"); 
				
				if(taskObjectRel != null){
					taskObject = taskObjectRel;
					
					setTaskCard();
				}
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }else if (requestCode == TASKREQUEST) {
            if(resultCode == Activity.RESULT_OK){
                taskObject = data.getParcelableExtra("TaskObject");
                setTaskCard();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void setEmployerCard(){
        if (lableOne.getVisibility() == View.VISIBLE){
            toggleViewVisibility(employerCard);
        }

        employerName.setText(abnObject.getCompanyName());
        employerLocation.setText(abnObject.getState() + " " + abnObject.getPostCode());
    }

    public void setTaskCard(){
        if (lableTwo.getVisibility() == View.VISIBLE){
            toggleViewVisibility(taskCard);
        }

        taskName.setText(taskObject.getTask());

        String summary = taskObject.getWorkType() + " - $" + taskObject.getRate()+ " /" ;
        if (taskObject.getWorkType().equals("Piece Rate")){
            summary = summary + "Unit";

            units.setVisibility(View.VISIBLE);
            selectTimesCardView.setVisibility(View.VISIBLE);
        }else {
            summary = summary + "Hour";

            units.setVisibility(View.GONE);
            selectTimesCardView.setVisibility(View.VISIBLE);
        }

        taskSummary.setText(summary);
    }

    private void toggleViewVisibility(CardView cardView){
        switch (cardView.getId()){
            case R.id.select_employer_card:
                setVisibility(employerLayout);
                setVisibility(lableOne);
                break;
            case R.id.select_task_card:
                setVisibility(taskLayout);
                setVisibility(lableTwo);
                break;
            case R.id.select_times_card:
                setVisibility(timeLayoutLinearLayout);
                setVisibility(lable3TextView);
        }

    }

    private void setVisibility(View view){
        int vis;
        if (view.getVisibility() == View.VISIBLE){
            vis = View.GONE;
        }else{
            vis = View.VISIBLE;
        }

        view.setVisibility(vis);
    }

    private String convertTimeFloat(float timeIn){
        int hour = (int) timeIn;
        int minutes = (int) (timeIn - hour)*60;

        return hour + "hours " + minutes + "mins";
    }

    class TimeDialogClass extends AlertDialog.Builder{

        private TimePicker timePickerTimePicker;
        private TextView startTimeTextView;
        private TextView finishTimeTextView;
        private EditText breakEditText;

        private String currentTag;
        private AlertDialog alertDialog;
        private int breakInt;

        public TimeDialogClass(Context context) {
            super(context);
            init();
        }

        public TimeDialogClass(Context context, int themeResId) {
            super(context, themeResId);
            init();
        }

        private void init(){
            setView(null);
        }

        @Override
        public AlertDialog.Builder setView(View view) {
            view = View.inflate(getContext(),R.layout.dialog_add_times, null);

            currentTag = "start";

            startTimeTextView = view.findViewById(R.id.from_date);
            finishTimeTextView = view.findViewById(R.id.to_date);
            timePickerTimePicker = view.findViewById(R.id.time_picker);
            breakEditText = view.findViewById(R.id.breaktime);
            TextView okText = view.findViewById(R.id.ok);

            timePickerTimePicker.setIs24HourView(true);

            okText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (timeObject.getTimeIn() != null && timeObject.getTimeOut() != null){
                        timeObject.setHours(calculateDuration());
                        timeObject.setBreakEpoch(breakInt);


                        if (lable3TextView.getVisibility() == View.VISIBLE){
                            toggleViewVisibility(selectTimesCardView);
                        }

                        timeTextView.setText(convertTimeFloat(timeObject.getHours()));

                        setTimeSummary();

                    }

                    alertDialog.dismiss();
                }
            });

            startTimeTextView.setTag("start");
            finishTimeTextView.setTag("finish");

            startTimeTextView.setOnClickListener(timeSelect);
            finishTimeTextView.setOnClickListener(timeSelect);

            timePickerTimePicker.setOnTimeChangedListener(timeChangedListener);
            toggleTextButtons(true);

            return super.setView(view);
        }



        TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String ddTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
                if (currentTag.equals("start")){
                    timeObject.setTimeIn(ddTime);
                }else {
                    timeObject.setTimeOut(ddTime);
                }
            }
        };

        View.OnClickListener timeSelect = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTag = (String) v.getTag();
                String timeString;

                if (currentTag.equals("start")){
                    timeString = timeObject.getTimeIn();
                    toggleTextButtons(true);

                }else {
                    timeString = timeObject.getTimeOut();
                    toggleTextButtons(false);
                }

                if (timeString != null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        timePickerTimePicker.setHour(getTime(timeString)[0]);
                        timePickerTimePicker.setMinute(getTime(timeString)[1]);
                    }
                }else {
                    Calendar calendar = Calendar.getInstance();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        timePickerTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                        timePickerTimePicker.setMinute(calendar.get(Calendar.MINUTE));
                    }

                }
            }
        };

        @Override
        public AlertDialog create() {
            alertDialog = super.create();

            return alertDialog;
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

        private int[] getTime(String s){

            return new int[]{Integer.parseInt(s.split(":")[0]),Integer.parseInt(s.split(":")[1])};
        }

        private float calculateDuration(){
            String startTime = timeObject.getTimeIn();
            String finishTime = timeObject.getTimeOut();
            String breakText = breakEditText.getText().toString().trim();

            int hoursIn = getTime(startTime)[0];
            int hoursOut = getTime(finishTime)[0];
            int minutesIn = getTime(startTime)[1];
            int minutesOut = getTime(finishTime)[1];

            breakInt = 0;
            if (!TextUtils.isEmpty(breakText)){
                breakInt =  Integer.parseInt(breakText);
            }
            float duration;

            if (hoursOut > hoursIn){
                duration = (((float)hoursOut + ((float)minutesOut/60)) - ((float) hoursIn + ((float)minutesIn/60))) - ((float) breakInt / 60);
            }else{
                duration = ((((float)hoursOut + ((float)minutesOut/60)) - ((float)hoursIn + ((float)minutesIn/60))) - ((float) breakInt / 60) + 24);
            }

            String s = String.format("%.2f",duration);
            return Float.parseFloat(s);
        }

        private void toggleTextButtons(boolean top){
            setFadeAnimation(startTimeTextView);
            setFadeAnimation(finishTimeTextView);

            if (top){
                startTimeTextView.setTypeface(null, Typeface.BOLD);
                finishTimeTextView.setTypeface(null, Typeface.NORMAL);

                startTimeTextView.setBackgroundColor(getContext().getResources().getColor(R.color.one));
                finishTimeTextView.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
            }else {
                finishTimeTextView.setTypeface(null, Typeface.BOLD);
                startTimeTextView.setTypeface(null, Typeface.NORMAL);

                finishTimeTextView.setBackgroundColor(getContext().getResources().getColor(R.color.one));
                startTimeTextView.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
            }
        }

        private void setFadeAnimation(View view){
            Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                    R.anim.fade_in);

            view.setAnimation(bottomUp);
        }


    }
}
