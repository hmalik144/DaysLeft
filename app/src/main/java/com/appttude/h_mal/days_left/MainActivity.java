package com.appttude.h_mal.days_left;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appttude.h_mal.days_left.AddItems.AddShiftActivity;
import com.appttude.h_mal.days_left.Login.ChangeUserDetailsActivity;
import com.appttude.h_mal.days_left.Login.FullscreenActivity;
import com.appttude.h_mal.days_left.Objects.ShiftObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.appttude.h_mal.days_left.Global.FirebaseClass.SHIFT_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.USER_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.auth;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.mDatabase;

public class MainActivity extends AppCompatActivity {

    public String currentFragment;
    private Toolbar toolbar;
    public static FragmentManager fragmentManager;
    private ProgressBar progressBar;
    public NavigationView navigationView;

    private DatabaseReference reference;
    public static List<ShiftObject> shiftObjectArrayList;
    public static Map<String,ShiftObject> shiftsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);

        shiftObjectArrayList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        FloatingActionButton fab = findViewById(R.id.fab);

        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(backStackChangedListener);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        reference = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child(SHIFT_FIREBASE);
        reference.addValueEventListener(new CustomValueEventListener(this,fragmentManager,progressBar));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddShiftActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // Handle navigation view item clicks here.
                int id = menuItem.getItemId();

                if (id == R.id.nav_camera) {
//                    // Handle the camera action
                    Intent ChangeDetailsIntent = new Intent(MainActivity.this,ChangeUserDetailsActivity.class);
                    startActivity(ChangeDetailsIntent);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });

        setupDrawer();

        parseXmlLayout();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.replace(R.id.container,new FragmentHome()).commit();
                    return true;
                case R.id.navigation_list:
                    fragmentTransaction.replace(R.id.container,new FragmentList()).commit();
                    return true;
                case R.id.navigation_tools:
                    fragmentTransaction.replace(R.id.container,new FragmentTools()).commit();
                    return true;
            }
            return false;
        }
    };

    public FragmentManager.OnBackStackChangedListener backStackChangedListener= new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            String fragmentString = fragmentManager.getFragments().get(0).getClass().getSimpleName();
            String title;

            switch (fragmentString){
                case "DriverProfileFragment":
                    title = "Driver Profile";
                    break;
                case "DriverLicenseFragment":
                    title = "Drivers License";
                    break;
                case "InsuranceFragment":
                    title = "Insurance";
                    break;
                default:
                    title = getResources().getString(R.string.app_name);
            }

            setTitle(title);
        }
    };

    public void setupDrawer(){
        View header = navigationView.getHeaderView(0);

        TextView driverEmail = header.findViewById(R.id.driver_email);
        TextView driverName = header.findViewById(R.id.driver_name);
        ImageView driverImage = header.findViewById(R.id.profileImage);

        if (auth != null){
            FirebaseUser user = auth.getCurrentUser();
            if (user.getEmail() != null){
                driverEmail.setText(user.getEmail());
            }
            if (user.getDisplayName() != null){
                driverName.setText(user.getDisplayName());
            }

            Picasso.get()
                    .load(user.getPhotoUrl())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(driverImage);
        }

        TextView textView = findViewById(R.id.logout);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(),FullscreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void setTitle(CharSequence title) {
        toolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStack();
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("Leave?")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                                System.exit(0);
                            }
                        }).create().show();
            }
        }
    }

    public static void printObjectAsJson(String TAG, Object o){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(new Gson().toJson(o));
        String prettyJsonString = gson.toJson(je);

        Log.i(TAG, "onBindViewHolder: object" + prettyJsonString);
    }

    private void parseXmlLayout(){
        final String ANDROID_ID = "android:id";
        String TAG = "parsingxml";
        boolean fragment = true;
        String preString = "";

        if (fragment){
            preString = "view.";
        }

        try {
            InputStream is = getResources().openRawResource(R.raw.fragment_add_employer);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("*");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    if (eElement.hasAttribute(ANDROID_ID)){
                        String view = eElement.getTagName();
                        String Id = eElement.getAttribute(ANDROID_ID).replace("@+id/","");

                        Log.i(TAG, view + " " + getFieldName(Id,view) + " = " + preString
                         + "findViewById(R.id." + Id + ");");
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "parseXmlLayout: ", e);
        }
    }

    private String getFieldName (String name,String view){
        if (name.contains("_")){
            int index = name.indexOf("_");
            name = name.replace("_","");

            char[] array = name.toCharArray();
            array[index] = Character.toUpperCase(array[index]);
            name = new String(array);
        }

        name = name + view;

        return name;
    }

    public static String Epoch2DateString(long epochSeconds, String formatString) {
        Date updatedate = new Date(epochSeconds * 1000);
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(updatedate);
    }

    public static int[] Epoch2DateTimes(int epochSeconds) {
        Date updatedate = new Date(epochSeconds);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        String time = format.format(updatedate);

        int[] array = new int[]{Integer.parseInt(time.split(":")[0]),Integer.parseInt(time.split(":")[1])};

        return array;
    }
}
