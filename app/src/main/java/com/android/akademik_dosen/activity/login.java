package com.android.akademik_dosen.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.akademik_dosen.MainActivity;
import com.android.akademik_dosen.R;
import com.android.akademik_dosen.koneksi.config;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    ProgressDialog PD;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    Button btnlogin,forget;
    TextView regis;
    private boolean loggedIn = false;
    EditText user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login Dosen");
        getSupportActionBar().getThemedContext();
        toolbar.setTitleTextColor(0xFFFFFFFF);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);

        btnlogin = (Button) findViewById(R.id.login);

        sp = this.getSharedPreferences("isi data", 0);
        spe = sp.edit();
        PD = new ProgressDialog(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = FirebaseInstanceId.getInstance().getToken();
                regis_user(token);
                login();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        }
    }


    private void gotohome() {
        Intent intent = new Intent(login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        if (!PD.isShowing())
            PD.show();
    }

    private void hideDialog() {
        if (PD.isShowing())
            PD.dismiss();
    }

    private void login() {
        //Getting values from edit texts
        final String input_user = user.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        PD.setMessage("Login Process...");
        showDialog();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //If we are getting success from server
                        if (response.contains(config.LOGIN_SUCCESS)) {
                            hideDialog();
                            sp = login.this.getSharedPreferences(config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            spe = sp.edit();

                            //Adding values to editor
                            spe.putBoolean(config.LOGGEDIN_SHARED_PREF, true);
                            spe.putString(config.EMAIL_SHARED_PREF, input_user);

                            spe.commit();
                            gotohome();

                        } else {
                            hideDialog();
                            Toast.makeText(login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        hideDialog();
                        Toast.makeText(login.this, "The server unreachable", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(config.KEY_NID, input_user);
                params.put(config.KEY_PASSWORD, password);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void regis_user(final String token) {
        PD.show();
        final String nid = user.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, config.UPDATE_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(config.KEY_TOKEN,token);
                params.put(config.KEY_NID,nid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(login.this);
        requestQueue.add(postRequest);
    }


}
