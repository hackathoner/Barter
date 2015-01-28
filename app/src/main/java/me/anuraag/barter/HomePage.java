package me.anuraag.barter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.me.lewisdeane.lnavigationdrawer.NavigationItem;
import uk.me.lewisdeane.lnavigationdrawer.NavigationListView;


public class HomePage extends Activity {

    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private View mCustomView;
    private Button menu,newListing;
    private ListingAdapter listingAdapter;
    private ArrayList<ListingObject> listingObjects;
    private ListView listview;
    private List<ParseObject> templist;
    private ParseUser myuser;
    private TextView mTitleTextView,plusView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        // get list items from strings.xml
//        android.app.ActionBar action=this.getActionBar();
//        action.setBackgroundDrawable(new ColorDrawable(0xFFe74c3c));
//        action.setDisplayUseLogoEnabled(true);
        Parse.initialize(this, "CZUQqTZcedP8pSmLPqHvBkxd41pmfTF7vyEch1xq", "f3aQ4TYpBwSZ4K8BkJpbkSrm1DrWKrxJH5LR3OK8");
        try{
            myuser = ParseUser.getCurrentUser();
            Log.i(myuser.getEmail(),myuser.getEmail());

        }catch (NullPointerException n){
            Log.d(n.toString(),n.toString());
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }

        android.app.ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("HomePage");
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
        menu = (Button)mCustomView.findViewById(R.id.button3);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doThis();
            }
        });
        menu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    menu.setBackground(new ColorDrawable(0xFFc0392b));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    menu.setBackgroundColor(Color.TRANSPARENT);
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
        drawerListView.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_listview_item, drawerListViewItems));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        drawerListView.setOnFocusChangeListener(new DrawerItemDismissedListener());
        populateListView();

    }
    public void populateListView(){
        templist = new ArrayList<ParseObject>();
        listingObjects = new ArrayList<ListingObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listing");
        query.whereNotEqualTo("title", " ");
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
                        listingAdapter = new ListingAdapter(getApplicationContext(),listingObjects);
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
    public static class ListingAdapter extends ArrayAdapter<ListingObject> {
        private TextView title,description,address;
        private ImageView image;
        public ListingAdapter(Context context, ArrayList<ListingObject> notifs) {
            super(context, 0, notifs);
        }

        @Override
        public View getView(int position, View rootView, ViewGroup parent) {
            // Get the data item for this position
            ListingObject notif = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (rootView == null) {
                rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
            }
            title = (TextView) rootView.findViewById(R.id.title);
            image = (ImageView)rootView.findViewById(R.id.imageView);
            if(notif.getListingType() != null){
                String s = notif.getListingType();
                ArrayAdapter<String> itemsArrayList = new ArrayAdapter<String>(getContext(),   android.R.layout.simple_list_item_1);
                String[] itemNames = getContext().getResources().getStringArray(R.array.spinner_array);
                for(String names: itemNames){
                    if(s.equals(names)){
                        String swag = "R.drawable." + names.trim().replace(" ","");
                        Log.d("Swig",swag);
                        if(names.contains("/")){
                            names = names.replace("/","");
                        }
                        names = names.toLowerCase();
                        image.setImageResource(getContext().getResources().getIdentifier(names.replace(" ",""),"drawable",getContext().getPackageName()));
//                        getContext().getResources().getDrawable()
                    }
                }

            }
//            description = (TextView) rootView.findViewById(R.id.description);
//            address = (TextView) rootView.findViewById(R.id.address);

            title.setText(notif.getTitle());
//            description.setText(notif.getDescription());
//            address.setText(notif.getAddress());
            Log.i("Something happened","Something happened");
            return rootView;
        }
//        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
//        {
//            ListingObject myobj = getItem(position);
//            String name = myobj.getTitle();
//            String ad = myobj.getAddress();
//            String des = myobj.getDescription();
//            Intent myIntent = new Intent(getContext(),ListingActivity.class);
//            myIntent.putExtra("title",name);
//            myIntent.putExtra("address",ad);
//            myIntent.putExtra("description",des);
//            myIntent.putExtra("creator",myobj.getCreator());
//        }

    }
    public void doThis(){
        if(drawerLayout.isDrawerOpen(Gravity.START))
        {
        drawerLayout.closeDrawer(Gravity.START);
            mTitleTextView.setText("HomePage");
        }
        else{
            drawerLayout.openDrawer(Gravity.START);
            mTitleTextView.setText("Menu");

        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_page, menu);
        return super.onCreateOptionsMenu(menu);
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
            if(name.equals("Logout")){
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
            }
            if(name.equals("Manage Listings")){
                startActivity(new Intent(getApplicationContext(),ManageListings.class));
            }
            Toast.makeText(getApplicationContext(), ((TextView)view).getText(), Toast.LENGTH_LONG).show();
            doThis();

        }
    }
    private class DrawerItemDismissedListener implements View.OnFocusChangeListener {


        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                mTitleTextView.setText("HomePage");
            }else{
                mTitleTextView.setText("Menu");
            }
        }
    }
//    private class DrawerItemTouchListener implements ListView.OnTouchListener{
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if(event.getAction() == MotionEvent.ACTION_DOWN){
//                v.setBackgroundColor(0xFFD4D8D9);
//            }
//            if(event.getAction() == MotionEvent.ACTION_DOWN){
//                v.setBackgroundColor(0xFFecf0f1);
//            }
//
//            return true;
//        }
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.i("Id", id + "");
        if(id == R.id.icon){
            return true;
        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

}
