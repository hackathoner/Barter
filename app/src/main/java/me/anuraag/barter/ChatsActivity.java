package me.anuraag.barter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;
import java.util.Map;


public class ChatsActivity extends Activity {
    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private View mCustomView;
    private Query curUserQuery;
    private TextView mTitleTextView;
    private ParseUser myuser;
    private String curUserId = "";
    private ChatsAdapter chatsAdapter;
    private Iterable<DataSnapshot> snapshots;
    private ArrayList<ChatObject> chatObjects;
    private ListView listview;
    private List<ParseObject> templist;
    private ImageView menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
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

        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Create Listing");
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
        drawerListView.setAdapter(new ArrayAdapter<String>(ChatsActivity.this,R.layout.drawer_listview_item, drawerListViewItems));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        populateListView();
    }
    public void populateListView(){
        chatObjects = new ArrayList<ChatObject>();
        Firebase firebaseRef = new Firebase("https://barter.firebaseio.com/Users/");
        curUserQuery = firebaseRef;
        curUserQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listview = (ListView)findViewById(R.id.lists);
                Iterable<DataSnapshot> myiterator = dataSnapshot.getChildren();
                for(DataSnapshot f: myiterator){

                    if(f.child("email").getValue()!=null) {
                        String swag = f.child("email").getValue().toString();
                        if(swag.equals(myuser.getEmail())){
                            curUserId = f.getKey();
                            snapshots = f.child("Chat").getChildren();
                            for(DataSnapshot snapshot: snapshots){
                                try {
                                    Map<String,Object> map = (Map<String,Object>)snapshot.getValue();

                                    Log.d("SNapshot",map.get("ListingName").toString());
                                    //TODO Make an oncclick for the listview items
                                    String chatTitle = map.get("ListingName").toString();
                                    String chatUserName = map.get("User2Name").toString();
                                    Log.d("Username",chatUserName);
                                    String chatEmail = map.get("User2").toString();
                                    chatObjects.add(new ChatObject(chatTitle, chatUserName, chatEmail));
                                }catch(NullPointerException n){
                                    Log.d(n.toString(),n.toString());
                                }
                            }
                            chatsAdapter = new ChatsAdapter(getApplicationContext(),chatObjects);
                            listview.setAdapter(chatsAdapter);
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    ChatObject chatObject = chatsAdapter.getItem(position);
                                    Intent myIntent = new Intent(getApplicationContext(),ChatScreen.class);
                                    myIntent.putExtra("ChatterName",chatObject.getChatterName());
                                    myIntent.putExtra("ChatterEmail", chatObject.getChatterEmail());
                                    myIntent.putExtra("Title",chatObject.getTitle());
                                    startActivity(myIntent);
                                }
                            });

                        }

                        Log.d("Child", f.child("email").getValue().toString());

                    }else{
                        Log.d("null","email is null hoe");
                    }

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
            mTitleTextView.setText("Chats");
        }
        else{
            drawerLayout.openDrawer(Gravity.START);
            mTitleTextView.setText("Menu");

        }
    }
    public static class ChatsAdapter extends ArrayAdapter<ChatObject> {
        private TextView title,name,address;
        public ChatsAdapter(Context context, ArrayList<ChatObject> chatObjects) {
            super(context, 0, chatObjects);
        }

        @Override
        public View getView(int position, View rootView, ViewGroup parent) {
            // Get the data item for this position
            ChatObject chatObject = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (rootView == null) {
                rootView = LayoutInflater.from(getContext()).inflate(R.layout.chats_listview_item, parent, false);
            }
            title = (TextView) rootView.findViewById(R.id.title);
            name = (TextView) rootView.findViewById(R.id.name);
            title.setText(chatObject.getTitle());
            name.setText(chatObject.getChatterName());
            return rootView;
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chats, menu);
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
