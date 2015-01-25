package me.anuraag.barter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ListingActivity extends Activity {
    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private View mCustomView;
    private ImageView menu;
    private Query curUserQuery;
    private String friendUserId,curUserId;
    private ListView listview;
    private Button startChat;
    private Firebase curUserRef,friendUserRef;
    private ParseUser myuser;
    private Firebase myref;
    private TextView title,address,description,creator;
    private TextView mTitleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_fragment_view);
        Firebase.setAndroidContext(this);
        Parse.initialize(this, "CZUQqTZcedP8pSmLPqHvBkxd41pmfTF7vyEch1xq", "f3aQ4TYpBwSZ4K8BkJpbkSrm1DrWKrxJH5LR3OK8");
        try{
            myuser = ParseUser.getCurrentUser();
            Log.i(myuser.getEmail(), myuser.getEmail());

        }catch (NullPointerException n){
            Log.d(n.toString(),n.toString());
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }
        myref = new Firebase("https://barter.firebaseio.com/");
        title = (TextView)findViewById(R.id.textView2);
        creator = (TextView)findViewById(R.id.poster);
        description = (TextView)findViewById(R.id.description);
        address = (TextView)findViewById(R.id.address);
        startChat = (Button)findViewById(R.id.chat);



        title.setText(getIntent().getStringExtra("title"));
        creator.setText(getIntent().getStringExtra("creator"));
        description.setText(getIntent().getStringExtra("description"));
        address.setText(getIntent().getStringExtra("address"));

        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  curUserId = "";
                 friendUserId = "";
                Firebase firebaseRef = new Firebase("https://barter.firebaseio.com/Users/");
                  curUserQuery = firebaseRef;
                curUserQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> myiterator = dataSnapshot.getChildren();
                        for(DataSnapshot f: myiterator){

                            if(f.child("email").getValue()!=null) {
                                String swag = f.child("email").getValue().toString();
                                if(swag.equals(myuser.getEmail())){
                                    curUserId = f.getKey();
                                }
                                if(swag.equals(creator.getText().toString()))
                                {
                                    friendUserId = f.getKey();
                                }
                                Log.d("Child", f.child("email").getValue().toString());

                            }else{
                                Log.d("null","email is null hoe");
                            }

                            }
//                        }
                        curUserQuery.removeEventListener(this);

                        Log.d("Me",curUserId);
                        Log.d("Firend",friendUserId);
                         curUserRef = new Firebase("https://barter.firebaseio.com/Users/" + curUserId).child("Chat").push();
                         friendUserRef = new Firebase("https://barter.firebaseio.com/Users/" + friendUserId).child("Chat").push();
                        Map<String,String> chatobj = new HashMap<String,String>();
                        chatobj.put("User1",myuser.getEmail());
                        chatobj.put("User2",creator.getText().toString());
                        try {
                            Log.i("Me",chatobj.toString());
                            curUserRef.setValue(chatobj);
//                            friendUserRef.setValue(chatobj);
                        }catch (FirebaseException j) {
                            Log.i("SOmething si happening", j.toString());
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.d("error",firebaseError.toString());
                    }


                });

                //TODO Add Chat objects under Firebase Users
                //TODO Create ListView Chat Page
                //TODO Create Chats

            }
        });
        android.app.ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Listing");
        menu = (ImageView)mCustomView.findViewById(R.id.imageView1);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doThis();
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
    }
    public void doThis(){
        if(drawerLayout.isDrawerOpen(Gravity.START))
        {
            drawerLayout.closeDrawer(Gravity.START);
            mTitleTextView.setText("Listing");
        }
        else{
            drawerLayout.openDrawer(Gravity.START);
            mTitleTextView.setText("Menu");

        }
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
            Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_LONG).show();
            doThis();

        }
    }
    private class DrawerItemDismissedListener implements View.OnFocusChangeListener {


        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                mTitleTextView.setText("Listing");
            }else{
                mTitleTextView.setText("Menu");
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listing, menu);
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
