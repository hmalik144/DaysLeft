package com.appttude.h_mal.days_left;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appttude.h_mal.days_left.Objects.ShiftObject;
import com.appttude.h_mal.days_left.arc.ArcAnimation;
import com.appttude.h_mal.days_left.bar.BarAnimation;

import com.appttude.h_mal.days_left.arc.CircleView;
import com.appttude.h_mal.days_left.bar.BarView;

import java.util.HashSet;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "something";

    private Context context;
    private List<ShiftObject> shiftObjectList;
    private int uniqueEntries;
    private int[] typeCount;

    public RecyclerViewAdapter(Context context, List<ShiftObject> shiftObjectList) {
        this.context = context;
        this.shiftObjectList = shiftObjectList;
        uniqueEntries = countDistinct();
        typeCount = countShiftType();
    }

    class ItemOne extends RecyclerView.ViewHolder {

        CircleView arc;
        TextView days;

        public ItemOne(View itemView){
            super(itemView);
            arc = itemView.findViewById(R.id.arc_view);
            days = itemView.findViewById(R.id.days_completed);
        }
    }

    class ItemTwo extends RecyclerView.ViewHolder {

        BarView barView;
        LinearLayout linearLayout;
        TextView pcText;
        TextView hrText;
//        LinearLayout textholder;
//        LinearLayout bottomTextholder;

        public ItemTwo(View itemView){
            super(itemView);

            barView = itemView.findViewById(R.id.bar);
            linearLayout = itemView.findViewById(R.id.lin);
            pcText = itemView.findViewById(R.id.pc_amount_text);
            hrText = itemView.findViewById(R.id.hr_amount_text);
//            textholder = itemView.findViewById(R.id.text_holder);
//            bottomTextholder = itemView.findViewById(R.id.bottom_text_holder);

        }
    }

    class ItemThree extends RecyclerView.ViewHolder {

        private TextView cardTitle;
        private ImageView cardIcon;
        private TextView units;
        private TextView totalEarned;
        private LinearLayout textholderTop;
        private LinearLayout textholderBottom;

        public ItemThree(View itemView){
            super(itemView);

            cardTitle = itemView.findViewById(R.id.card_title);
            cardIcon = itemView.findViewById(R.id.card_icon);
            units = itemView.findViewById(R.id.units);
            totalEarned = itemView.findViewById(R.id.total_earned);
            textholderTop = itemView.findViewById(R.id.text_holder);
            textholderBottom = itemView.findViewById(R.id.text_holder_two);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case 1:
                final View itemOne = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_one, viewGroup, false);
                return new ItemOne(itemOne);
            case 2:
                final View itemTwo = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_two, viewGroup, false);
                return new ItemTwo(itemTwo);
            case 3:
                final View itemThree = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_three, viewGroup, false);
                return new ItemThree(itemThree);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        switch (holder.getItemViewType()) {
            case 1:
                final ItemOne viewHolderCurrent = (ItemOne) holder;

                CircleView arcView = viewHolderCurrent.arc;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    arcView.setPaintColor(context.getColor(R.color.two));
                }

                int days = uniqueEntries;

                int complete = (360 * days)/88;

                viewHolderCurrent.days.setText(String.valueOf(days));

                ArcAnimation animation = new ArcAnimation(arcView, complete);
                animation.setDuration(600);
                arcView.startAnimation(animation);

                break;
            case 2:
                final ItemTwo viewTwo = (ItemTwo) holder;

                final BarView barView = viewTwo.barView;
                final LinearLayout linearLayout = viewTwo.linearLayout;

                float cover = (float) typeCount[1]/shiftObjectList.size();

                barView.setCover(cover);

                barView.setColourOne(context.getResources().getColor(R.color.four));
                barView.setColourTwo(context.getResources().getColor(R.color.three));

                viewTwo.pcText.setText(String.valueOf(typeCount[1]));
                viewTwo.hrText.setText(String.valueOf(typeCount[0]));

//                viewTwo.textholder.setPadding(60,0,60,0);
//                viewTwo.bottomTextholder.setPadding(60,0,60,0);

                linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Log.i(TAG, "onGlobalLayout: " + linearLayout.getWidth());

                        BarAnimation barAnimation = new BarAnimation(barView, linearLayout.getWidth(), 0);
                        barAnimation.setDuration(600);
                        barView.setAnimation(barAnimation);
                        linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

                break;

            case 3:
                final ItemThree viewCounting = (ItemThree) holder;

                TextView cardTitle = viewCounting.cardTitle;
                ImageView cardIcon = viewCounting.cardIcon;
                TextView units = viewCounting.units;
                TextView totalEarned = viewCounting.totalEarned;
                LinearLayout top = viewCounting.textholderTop;
                LinearLayout bottom = viewCounting.textholderBottom;

//                top.setPadding(60,0,60,0);
//                bottom.setPadding(60,0,60,0);
//                cardIcon.setPadding(0,0,10,0);

                if (i == 2){
                    cardTitle.setText("Hourly");
                    cardIcon.setImageResource(R.drawable.clock_icon);
                    String hours = String.format("%.2f", calculateHours());
                    units.setText(hours + " Hours");

                    String total = String.format("%.2f", calculateAccumulatedPay(0));
                    totalEarned.setText("$" + total);
                }
                if (i == 3){
                    cardTitle.setText("Piece");
                    cardTitle.setTextColor(context.getResources().getColor(R.color.three));
                    cardIcon.setImageResource(R.drawable.piece);
                    cardIcon.setRotation(270);
                    String pieces = String.format("%.2f", calculateUnits());
                    units.setText(pieces + " Units");
                    String total = String.format("%.2f", calculateAccumulatedPay(1));
                    totalEarned.setText("$" + total);
                }
                if (i == 4){
                    cardTitle.setVisibility(View.GONE);
                    cardIcon.setVisibility(View.GONE);

                    String total = String.format("%.2f", calculateAccumulatedPay(3));
                    totalEarned.setText("$" + total);

                    viewCounting.textholderTop.setVisibility(View.GONE);
                }
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;

        switch (position){
            case 0:
                type = 1;
                break;
            case 1:
                type = 2;
                break;
            case 2:
                type = 3;
                break;
            case 3:
                type = 3;
                break;
            case 4:
                type = 3;
                break;
        }

        return type;
    }

    private int countDistinct() {
        HashSet<String> hs = new HashSet<>();

        for(int i = 0; i < shiftObjectList.size(); i++) {

            hs.add(shiftObjectList.get(i).getShiftDate());
        }

        return hs.size();
    }

    private int[] countShiftType(){
        int i = 0;
        int j = 0;

        for (ShiftObject shiftObject : shiftObjectList){
            if (shiftObject.getTaskObject().getWorkType().equals("Hourly")){
                i++;
            }else {
                j++;
            }
        }

        return new int[]{i,j};
    }

    private float calculateHours(){
        float hours = 0;
        for (ShiftObject shiftObject : shiftObjectList){
            if (shiftObject.getTaskObject().getWorkType().equals("Hourly")){
                hours = hours + shiftObject.getTimeObject().getHours() - shiftObject.getTimeObject().getBreakEpoch();
            }
        }

        return hours;
    }

    private float calculateUnits(){
        float units = 0;
        for (ShiftObject shiftObject : shiftObjectList){
            if (shiftObject.getTaskObject().getWorkType().equals("Piece Rate")){
                units = units + shiftObject.getUnitsCount();
            }
        }

        return units;
    }

    private float calculateAccumulatedPay(int type){
        float pay = 0;

        for (ShiftObject shiftObject : shiftObjectList){
            if (type == 0){
                if (shiftObject.getTaskObject().getWorkType().equals("Hourly")){
                    pay = pay + (shiftObject.getTaskObject().getRate() *
                            (shiftObject.getTimeObject().getHours() - shiftObject.getTimeObject().getBreakEpoch()));
                }
            }else if (type == 1){
                if (shiftObject.getTaskObject().getWorkType().equals("Piece Rate")){
                    pay = pay + (shiftObject.getTaskObject().getRate() * shiftObject.getUnitsCount());
                }
            }else {
                if (shiftObject.getTaskObject().getWorkType().equals("Hourly")){
                    pay = pay + (shiftObject.getTaskObject().getRate() *
                            (shiftObject.getTimeObject().getHours() - shiftObject.getTimeObject().getBreakEpoch()));
                }else {
                    pay = pay + (shiftObject.getTaskObject().getRate() * shiftObject.getUnitsCount());
                }
            }
        }

        return pay;
    }
}
