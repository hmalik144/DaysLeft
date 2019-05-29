package com.appttude.h_mal.days_left;

import android.content.Context;
import android.os.Build;
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
import com.appttude.h_mal.days_left.arc.CircleView;
import com.appttude.h_mal.days_left.bar.BarAnimation;
import com.appttude.h_mal.days_left.bar.BarView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class FireRecyclerAdapter extends FirebaseRecyclerAdapter<ShiftObject,RecyclerView.ViewHolder> {

    private String TAG = "FireRecyclerAdapter";
    private Context context;
    private List<ShiftObject> shiftObjectList;

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public FireRecyclerAdapter(Class<ShiftObject> modelClass, int modelLayout, Class<RecyclerView.ViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (i) {
            case 1:
                final View itemOne = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_one, viewGroup, false);
                return new FireRecyclerAdapter.ItemOne(itemOne);
            case 2:
                final View itemTwo = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_two, viewGroup, false);
                return new FireRecyclerAdapter.ItemTwo(itemTwo);
            case 3:
                final View itemThree = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_three, viewGroup, false);
                return new FireRecyclerAdapter.ItemThree(itemThree);
        }

        return null;
    }

    @Override
    protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, ShiftObject model, int i) {
        switch (viewHolder.getItemViewType()) {
            case 1:
                final FireRecyclerAdapter.ItemOne viewHolderCurrent = (FireRecyclerAdapter.ItemOne) viewHolder;

                CircleView arcView = viewHolderCurrent.arc;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    arcView.setPaintColor(context.getColor(R.color.two));
                }

                int days = 76;

                int complete = (360 * days) / 88;

                viewHolderCurrent.days.setText(String.valueOf(days));

                ArcAnimation animation = new ArcAnimation(arcView, complete);
                animation.setDuration(600);
                arcView.startAnimation(animation);

                break;
            case 2:
                final FireRecyclerAdapter.ItemTwo viewTwo = (FireRecyclerAdapter.ItemTwo) viewHolder;

                final BarView barView = viewTwo.barView;
                final LinearLayout linearLayout = viewTwo.linearLayout;

                barView.setCover(0.56f);

                barView.setColourOne(context.getResources().getColor(R.color.four));
                barView.setColourTwo(context.getResources().getColor(R.color.three));

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
                final FireRecyclerAdapter.ItemThree viewCounting = (FireRecyclerAdapter.ItemThree) viewHolder;

                TextView cardTitle = viewCounting.cardTitle;
                ImageView cardIcon = viewCounting.cardIcon;
                TextView units = viewCounting.units;
                TextView totalEarned = viewCounting.totalEarned;
                LinearLayout top = viewCounting.textholderTop;
                LinearLayout bottom = viewCounting.textholderBottom;

//                top.setPadding(60,0,60,0);
//                bottom.setPadding(60,0,60,0);
//                cardIcon.setPadding(0,0,10,0);

                if (i == 2) {
                    cardTitle.setText("Hourly");
                    cardIcon.setImageResource(R.drawable.clock_icon);
                    units.setText("296" + "Hours");
                    totalEarned.setText("$" + "907.53");
                }
                if (i == 3) {
                    cardTitle.setText("Piece");
                    cardTitle.setTextColor(context.getResources().getColor(R.color.three));
                    cardIcon.setImageResource(R.drawable.piece);
                    cardIcon.setRotation(270);
                    units.setText("180" + "Pcs");
                    totalEarned.setText("$" + "67.53");
                }
                if (i == 4) {
                    cardTitle.setVisibility(View.GONE);
                    cardIcon.setVisibility(View.GONE);

                    totalEarned.setText("$" + "974.53");

                    viewCounting.textholderTop.setVisibility(View.GONE);
                }

        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " +super.getItemCount());
        if (super.getItemCount() == 0){
            return super.getItemCount();
        }else {
            return 5;
        }

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

    @Override
    protected ShiftObject parseSnapshot(DataSnapshot snapshot) {
        shiftObjectList = new ArrayList<>();
        shiftObjectList.add(snapshot.getValue(ShiftObject.class));

        return super.parseSnapshot(snapshot);
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

        public ItemTwo(View itemView){
            super(itemView);

            barView = itemView.findViewById(R.id.bar);
            linearLayout = itemView.findViewById(R.id.lin);

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
}
