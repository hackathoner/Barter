package me.anuraag.barter;

import android.app.Activity;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;


public class ChatScreen extends Activity {
    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private View mCustomView;
    private Query curUserQuery,query2;
    private TextView mTitleTextView,plusView;
    private ParseUser myuser;
    private Button submit;
    private Firebase firebaseRef,firebaseRef2;
    private Map<String,String> messageTemp;
    private String myusermessageKey,myUserKey,secondUserKey,secondUserMessageKey;
    private EditText messageString;
    private MessageAdapter messageAdapter;
    private String curUserId = "";
    private Iterable<DataSnapshot> snapshots;
    private ArrayList<MessageObject> messageObjects;
    private ListView listview;
    private List<ParseObject> templist;
    private Button back,newListing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        Firebase.setAndroidContext(this);
        Parse.initialize(this, "CZUQqTZcedP8pSmLPqHvBkxd41pmfTF7vyEch1xq", "f3aQ4TYpBwSZ4K8BkJpbkSrm1DrWKrxJH5LR3OK8");
        try{
            myuser = ParseUser.getCurrentUser();
            Log.i(myuser.getEmail(), myuser.getEmail());

        }catch (NullPointerException n){
            Log.d(n.toString(),n.toString());
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }
        messageString = (EditText)findViewById(R.id.messageString);
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!messageString.getText().toString().isEmpty()) {
                    String mymessage = messageString.getText().toString();
                    messageString.setText("");
                    messageTemp = new HashMap<String,String>();
                    android.text.format.Time now = new android.text.format.Time();
                    messageTemp.put("TimeStamp",now.toString());
                    messageTemp.put("MessageText",mymessage);
                    messageTemp.put("Sender",myuser.getEmail());
                    Firebase headRef = new Firebase("https://barter.firebaseio.com/Users");
                    firebaseRef = new Firebase("https://barter.firebaseio.com/Users/" + myUserKey + "/Chat/" + myusermessageKey);
                    query2 = headRef;
                    query2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                //TODO push chat stuff to users too
//                                Log.d("LOL", dataSnapshot.getValue().toString());
                                Iterable<DataSnapshot> myIter = dataSnapshot.getChildren();
                                for(DataSnapshot datasnapshot: myIter){
//                                    Log.d("Swag",datasnapshot.getValue().toString());
//                                    Map<String,Object> map = (Map<String,Object>)datasnapshot.getValue();
                                    try {

//                                        Log.d("wat", datasnapshot.child("email").toString());
                                        if (datasnapshot.child("email").getValue().toString().equals(getIntent().getStringExtra("ChatterEmail"))) {
                                            secondUserKey = datasnapshot.getKey();
                                            Log.d("userkey",secondUserKey);
                                            Iterable<DataSnapshot> messageSnapshot = datasnapshot.child("Chat").getChildren();
                                            for (DataSnapshot d : messageSnapshot) {
                                                if(d.child("User2").getValue().equals(myuser.getEmail())){
                                                    secondUserMessageKey = d.getKey();
                                                    Log.d("Message Key", secondUserMessageKey);
                                                }
//                                                Log.d("Snap", d.getValue().toString());
                                            }
                                        }
                                    }catch (NullPointerException n){

                                    }
                                }
                                query2.removeEventListener(this);
                                firebaseRef.child("Messages").push().setValue(messageTemp);
                                firebaseRef2 = new Firebase("https://barter.firebaseio.com/Users/" + secondUserKey + "/Chat/" + secondUserMessageKey);
                                firebaseRef2.child("Messages").push().setValue(messageTemp);
                            }catch(NullPointerException n){
                                Log.d(n.toString(),n.toString());
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    //TODO ADD PERSON PUSHINGTOO
                }


            }
        });
        Log.d("Wow",getIntent().getStringExtra("Title"));
        android.app.ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.back_actionbar, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText(getIntent().getStringExtra("ChatterName"));
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
        drawerListView.setAdapter(new ArrayAdapter<String>(ChatScreen.this,R.layout.drawer_listview_item, drawerListViewItems));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        populateListview();
    }
    public void populateListview(){
        listview = (ListView)findViewById(R.id.lists);
        messageObjects = new ArrayList<>();
        Firebase firebaseRef = new Firebase("https://barter.firebaseio.com/Users/");
        curUserQuery = firebaseRef;
        curUserQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listview = (ListView) findViewById(R.id.lists);

                Iterable<DataSnapshot> myiterator = dataSnapshot.getChildren();
                for (DataSnapshot f : myiterator) {

                    if (f.child("email").getValue() != null) {
                        String swag = f.child("email").getValue().toString();
                        if (swag.equals(myuser.getEmail())) {
                            myUserKey = f.getKey();
                            curUserId = f.getKey();
                            snapshots = f.child("Chat").getChildren();
//                            Log.d("Snapshot",f.child("Chat").toString());
//                            Log.d("Child", f.child("email").getValue().toString());
                            for(DataSnapshot snapshot: snapshots){
//                                Log.d("Mini Snap",snapshot.getValue().toString());
                                Map<String,Object> map = (Map<String,Object>)snapshot.getValue();
                                if(map.get("User2").toString().equals(getIntent().getStringExtra("ChatterEmail"))){
                                    messageObjects = new ArrayList<MessageObject>();
                                    myusermessageKey = snapshot.getKey();
                                    try{
                                        Iterable<DataSnapshot> messageSnapshots = snapshot.child("Messages").getChildren();
                                        for(DataSnapshot d: messageSnapshots){
                                            Map<String,Object> messageMap = (Map<String,Object>)d.getValue();
                                            messageObjects.add(new MessageObject(messageMap.get("TimeStamp").toString(),messageMap.get("Sender").toString(),messageMap.get("MessageText").toString()));
                                        }
                                    }catch (NullPointerException n){
//                                        Log.d("Null",n.toString());
                                    }
                                    messageAdapter = new MessageAdapter(getApplicationContext(),messageObjects);
                                    listview.setAdapter(messageAdapter);
                                }
                            }

                        } else {
                            Log.d("null", "email is null hoe");
                        }

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("error",firebaseError.toString());
            }


        });


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
    public static class MessageAdapter extends ArrayAdapter<MessageObject> {
        private TextView message,timestamp,address;
        private ParseUser myuser = ParseUser.getCurrentUser();
        public MessageAdapter(Context context, ArrayList<MessageObject> messageObjects) {
            super(context, 0, messageObjects);
        }

        @Override
        public View getView(int position, View rootView, ViewGroup parent) {
            // Get the data item for this position
            MessageObject messageObject = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (rootView == null) {
                rootView = LayoutInflater.from(getContext()).inflate(R.layout.message_listview_item, parent, false);
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            message = (TextView)rootView.findViewById(R.id.message);
//            timestamp = (TextView)rootView.findViewById(R.id.timestamp);
            message.setText(messageObject.getMessageText());
//            timestamp.setText(messageObject.getTimeStamp());
            if(messageObject.getSenderEmail().equals(myuser.getEmail())){
                message.setLayoutParams(layoutParams);
                layoutParams.addRule(RelativeLayout.BELOW,R.id.message);
//                timestamp.setLayoutParams(layoutParams);
//                rootView.setBackgroundColor(0xFF419fdd);
//                timestamp.setLayoutParams(layoutParams);
            }else{
                message.setLayoutParams(layoutParams2);
                layoutParams2.addRule(RelativeLayout.BELOW,R.id.message);
//                timestamp.setLayoutParams(layoutParams2);
//                rootView.setBackgroundColor(0xFFe74c3c);

//                timestamp.setLayoutParams(layoutParams2);
            }
            return rootView;
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_screen, menu);
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
