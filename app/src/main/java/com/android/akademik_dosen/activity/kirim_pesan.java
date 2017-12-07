package com.android.akademik_dosen.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.akademik_dosen.R;
import com.android.akademik_dosen.koneksi.config;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class kirim_pesan extends AppCompatActivity {
    String id_matkul,matkul;
    RequestQueue requestQueue;
    EditText nama_matkul,keterangan;
    Button btn_kirim;
    ProgressDialog PD;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String nid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_pesan);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kirim Pesan");

        sp = getSharedPreferences(config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nid = sp.getString(config.EMAIL_SHARED_PREF, "Not Available");

        requestQueue = Volley.newRequestQueue(this);

        PD = new ProgressDialog(this);
        PD.setMessage("silahkan tunggu.....");
        PD.setCancelable(false);

        final Bundle bundle = getIntent().getExtras();
        id_matkul = bundle.getString("idmatkul");
        matkul = bundle.getString("matkul");
        Toast.makeText(this,id_matkul,Toast.LENGTH_LONG).show();

        nama_matkul = (EditText) findViewById(R.id.nama_matkul);
        keterangan = (EditText) findViewById(R.id.keterangan);
        nama_matkul.setText(matkul);
        btn_kirim = (Button) findViewById(R.id.btn_kirim);
        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                absen();
            }
        });
    }

    public void absen(){
        PD.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, config.ABSEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD.dismiss();
                        Toast.makeText(kirim_pesan.this,response.toString(),Toast.LENGTH_LONG).show();
                        Log.d("laporan ",response.toString());
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
                params.put("id_matkul", id_matkul);
                params.put("nid", nid);
                params.put("keterangan", keterangan.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(kirim_pesan.this);
        requestQueue.add(postRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        if(item.getItemId() == R.id.load){
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.load).setVisible(false);
        return true;
    }
}
