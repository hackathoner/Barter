package me.anuraag.barter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ManageListings extends ActionBarActivity {
    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private View mCustomView;
    private Query curUserQuery;
    private TextView mTitleTextView,plusView;
    private ParseUser myuser;
    private String curUserId = "";
    private Iterable<DataSnapshot> snapshots;
    private ArrayList<ChatObject> chatObjects;
    private ListView listview;
    private List<ParseObject> templist;
    private Button back,newListing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_listings);
        Firebase.setAndroidContext(this);
        Parse.initialize(this, "CZUQqTZcedP8pSmLPqHvBkxd41pmfTF7vyEch1xq", "f3aQ4TYpBwSZ4K8BkJpbkSrm1DrWKrxJH5LR3OK8");
        try{
            myuser = ParseUser.getCurrentUser();
            Log.i(myuser.getEmail(), myuser.getEmail());

        }catch (NullPointerException n){
            Log.d(n.toString(),n.toString());
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }
        android.app.ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.back_actionbar, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Chats");
        newListing = (Button)mCustomView.findViewById(R.id.button4);
        plusView = (TextView)mCustomView.findViewById(R.id.textView4);
        newListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateListing.class));
            }
        });
        newListing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    newListing.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    newListing.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
        back = (Button)mCustomView.findViewById(R.id.button3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    back.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    back.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_listings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
