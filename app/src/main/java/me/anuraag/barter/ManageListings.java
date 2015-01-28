package me.anuraag.barter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ManageListings extends Activity {
    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private View mCustomView;
    private Query curUserQuery;
    private TextView mTitleTextView,plusView;
    private ParseUser myuser;
    private HomePage.ListingAdapter listingAdapter;
    private String curUserId = "";
    private Iterable<DataSnapshot> snapshots;
    private ArrayList<ChatObject> chatObjects;
    private ArrayList<ListingObject> listingObjects;
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
        mTitleTextView.setText("Manage Listings");
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
                finish();
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

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        drawerListViewItems = getResources().getStringArray(R.array.Items);

        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(ManageListings.this,R.layout.drawer_listview_item, drawerListViewItems));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        populateListView();
    }
    public void populateListView(){
        templist = new ArrayList<ParseObject>();
        listingObjects = new ArrayList<ListingObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listing");
        query.whereEqualTo("creator", myuser.getEmail());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + parseObjects.size() + " scores");
                    templist = parseObjects;
                    for(int x = 0; x < parseObjects.size(); x++){
                        ListingObject l = new ListingObject(parseObjects.get(x).getString("title"),parseObjects.get(x).getString("address"),parseObjects.get(x).getString("description"),parseObjects.get(x).getString("creator"),parseObjects.get(x).getString("creatorName"));
                        try {
                            l.setListingType(parseObjects.get(x).getString("serviceType"));
                        }catch(NullPointerException n){

                        }
                        listingObjects.add(l);
                        Log.i("ObjectCreated",new ListingObject(parseObjects.get(x).getString("title"),parseObjects.get(x).getString("address"),parseObjects.get(x).getString("description"),parseObjects.get(x).getString("creator"),parseObjects.get(x).getString("creatorName")).toString());
                        listingAdapter = new HomePage.ListingAdapter(getApplicationContext(),listingObjects);
                        listview = (ListView)findViewById(R.id.lists);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ListingObject myobj = listingAdapter.getItem(position);
                                String name = myobj.getTitle();
                                String ad = myobj.getAddress();
                                String des = myobj.getDescription();
                                Intent myIntent = new Intent(getApplicationContext(),ListingActivity.class);
                                myIntent.putExtra("title",name);
                                myIntent.putExtra("address",ad);
                                myIntent.putExtra("description",des);
                                myIntent.putExtra("creator",myobj.getCreator());
                                myIntent.putExtra("creatorName",myobj.getCreatorName());
                                Log.d("CreatorName",myobj.getCreatorName());
                                startActivity(myIntent);
                            }
                        });
                        listview.setAdapter(listingAdapter);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }

            }
        });
        Log.i("Listview",listingObjects.toString());
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            String name = ((TextView)view).getText().toString();
            if(name.equals("Create Listing")){
                Intent myintent = new Intent(getApplicationContext(),CreateListing.class);
                startActivity(myintent);
            }
            if(name.equals("Home")){
                Intent myintent = new Intent(getApplicationContext(),HomePage.class);
                startActivity(myintent);
            }
            if(name.equals("Chats")){
                startActivity(new Intent(getApplicationContext(),ChatsActivity.class));
            }
            if(name.equals("Manage Listings")){
                startActivity(new Intent(getApplicationContext(),ManageListings.class));
            }
            Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_LONG).show();
            doThis();

        }
    }
    public void doThis(){
        if(drawerLayout.isDrawerOpen(Gravity.START))
        {
            drawerLayout.closeDrawer(Gravity.START);
            mTitleTextView.setText("Manage Listings");
        }
        else{
            drawerLayout.openDrawer(Gravity.START);
            mTitleTextView.setText("Menu");

        }
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


        return super.onOptionsItemSelected(item);
    }
}
