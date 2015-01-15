package me.anuraag.barter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;
import java.util.Map;


public class SignIn extends Activity {
    private ParseUser myuser;
    private Button signin,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Parse.initialize(this, "CZUQqTZcedP8pSmLPqHvBkxd41pmfTF7vyEch1xq", "f3aQ4TYpBwSZ4K8BkJpbkSrm1DrWKrxJH5LR3OK8");
        Firebase.setAndroidContext(this);
        signin = (Button)findViewById(R.id.button2);
        login = (Button)findViewById(R.id.button);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                signup();
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new SignUpFragment())
                        .commit();
//                startActivity(myintent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new LoginFragment())
                        .commit();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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
    public static class SignUpFragment extends Fragment {
        private Button submit;
        private EditText name,email,pass,repass;
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.signuplayout, container, false);

            return rootView;
        }
        public void onViewCreated(View view, Bundle savedInstanceState) {
            name = (EditText)view.findViewById(R.id.editText);
            email = (EditText)view.findViewById(R.id.editText2);
            pass = (EditText)view.findViewById(R.id.editText3);
            repass = (EditText)view.findViewById(R.id.editText4);

            submit = (Button)view.findViewById(R.id.button2);
            submit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signup();
                }
            });

        }
//        public void firebaseSignUp(String emails){
//            final String emailed = emails;
//            RequestQueue queue = Volley.newRequestQueue(getActivity());
//            String url ="https://barter.firebaseio.com/.json";
//
//            StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.i("Repsponse",response);
//                }
//            },new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                   Log.i("Error.Response", error.toString());
//
//                }
//            }
//            ) {
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("email", emailed);
//                    return params;
//                }
//            };
//            queue.add(postRequest);
//        }
        public void firebaseSignUp(String emailID){
            Firebase ref = new Firebase("https://barter.firebaseio.com/Users");
            Map<String,String> user = new HashMap<String,String>();
            user.put("email",emailID);
            ref.push().setValue(user);
        }
        public void signup() {
            final Intent myintent = new Intent(getActivity(),HomePage.class);
            if (!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()  && !repass.getText().toString().isEmpty() && pass.getText().toString().equals(repass.getText().toString()) && pass.getText().toString().length() >= 6)
            {
                ParseUser user = new ParseUser();
                user.setUsername(email.getText().toString());
                user.setPassword(pass.getText().toString());
                user.setEmail(email.getText().toString());
                user.put("Name",name.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            showSuccess("Please verify your email, then click the login button");
                            firebaseSignUp(email.getText().toString());
                        } else {
                            showIssue(e.toString());
                        }
                    }
                });
            }else if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()  || repass.getText().toString().isEmpty()){
                showIssue("Please makes sure you fill out all the fields properly!");
            }else if(!pass.getText().toString().equals(repass.getText().toString())){
                showIssue("Oops your passwords do not match, please try again");
            }else if(pass.getText().toString().length()<6){
                showIssue("Please make sure your password is at least six characters long");
            }
        }
        private void showSuccess(String issue)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(issue)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().finish();
                            startActivity(getActivity().getIntent());
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        private void showIssue(String issue)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(issue)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
    public static class LoginFragment extends Fragment {
        private Button submit;
        private ParseUser myuser;
        private EditText name,email,pass,repass;
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.loginlayout, container, false);

            return rootView;
        }
        public void onViewCreated(View view, Bundle savedInstanceState) {
            email = (EditText)view.findViewById(R.id.editText2);
            pass = (EditText)view.findViewById(R.id.editText3);

            submit = (Button)view.findViewById(R.id.button2);
            submit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });

        }
        public void login(){
            final Intent myintent = new Intent(getActivity(),HomePage.class);

            ParseUser.logInInBackground(email.getText().toString(), pass.getText().toString(), new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Log.i("User", user.getEmail());
                        boolean worked = user.getBoolean("emailVerified");
                        if(!worked) {
                            showIssue("Please make sure you have verified your email");

                        }else {
                            myuser = ParseUser.getCurrentUser();
                            startActivity(myintent);
                        }
                    } else {

                        showIssue(e.toString());


                    }
                }
            });
        }
        private void showSuccess(String issue)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(issue)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().finish();
                            startActivity(getActivity().getIntent());
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        private void showIssue(String issue)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(issue)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
}
