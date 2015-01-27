package me.anuraag.barter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class CreateListing extends Activity {

    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private View mCustomView;
    private EditText title,description,date,address;
    private Button submit;
    private ParseUser myuser;
    private Button menu;
    private Button newListing;
    private TextView plusView;
    private TextView mTitleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

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
        mTitleTextView.setText("Create Listing");
        newListing = (Button)mCustomView.findViewById(R.id.button4);
        plusView = (TextView)mCustomView.findViewById(R.id.textView4);
        plusView.setVisibility(View.GONE);
        newListing.setVisibility(View.GONE);
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


        title = (EditText)findViewById(R.id.editText);
        description = (EditText)findViewById(R.id.editText2);
        address = (EditText)findViewById(R.id.editText4);

        submit = (Button)findViewById(R.id.button2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().isEmpty() || description.getText().toString().isEmpty()|| address.getText().toString().isEmpty()){
                    issueAlert("Please make sure you fill out all the fields properly");
                }else {
                    createListing();
                    Toast.makeText(getApplicationContext(), "Listing Created", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                }
            }
        });

    }

    public void createListing(){
        ParseObject listing = new ParseObject("Listing");
        listing.put("title",title.getText().toString());
        listing.put("description",description.getText().toString());
        listing.put("address",address.getText().toString());
        Log.d(myuser.getEmail(),myuser.getEmail());
        listing.put("creator",myuser.getEmail());
        listing.put("creatorName",myuser.getString("Name"));
        Log.d("CreatorName",myuser.getString("Name"));
        listing.saveInBackground();
        title.setText("");
        description.setText("");
        address.setText("");
    }
    private void issueAlert(String issue)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(issue)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
            Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_LONG).show();
            doThis();

        }
    }
    public void doThis(){
        if(drawerLayout.isDrawerOpen(Gravity.START))
        {
            drawerLayout.closeDrawer(Gravity.START);
            mTitleTextView.setText("Create Listing");
        }
        else{
            drawerLayout.openDrawer(Gravity.START);
            mTitleTextView.setText("Menu");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_listing, menu);
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
