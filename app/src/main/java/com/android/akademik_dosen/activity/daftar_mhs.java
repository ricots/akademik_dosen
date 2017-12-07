package com.android.akademik_dosen.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.akademik_dosen.R;
import com.android.akademik_dosen.adapter.adapter_daftar_kuliah;
import com.android.akademik_dosen.adapter.adapter_daftar_kuliah_mhs;
import com.android.akademik_dosen.adapter.adapter_daftar_mahasiswa;
import com.android.akademik_dosen.koneksi.config;
import com.android.akademik_dosen.oop.Item;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.akademik_dosen.R.id.id_matkul;

public class daftar_mhs extends AppCompatActivity {
    String id_matkul;
    RecyclerView list_daftar;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adp_daftar;
    RequestQueue requestQueue;
    List<Item> datanya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_mhs);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Mahsiswa");

        final Bundle bundle = getIntent().getExtras();
        id_matkul = bundle.getString("idmatkul");
        Toast.makeText(this,id_matkul,Toast.LENGTH_LONG).show();

        list_daftar = (RecyclerView) findViewById(R.id.list_mhs);
        list_daftar.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        list_daftar.setLayoutManager(layoutManager);

        datanya = new ArrayList<Item>();
        requestQueue = Volley.newRequestQueue(this);
        getData();
        adp_daftar = new adapter_daftar_mahasiswa(datanya, this);
        list_daftar.setAdapter(adp_daftar);
    }

    public void getData() {
        final ProgressDialog loading = ProgressDialog.show(daftar_mhs.this,"Loading Data", "Please wait...",false,false);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, config.DAFTAR_MHS + id_matkul,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loading.dismiss();
                        //Log.d("hasilnya ", response.toString());
                        try {

                            JSONArray data_trans = response.getJSONArray("list_mhs");

                            for (int a = 0; a < data_trans.length(); a++) {
                                Item data = new Item();
                                JSONObject json = data_trans.getJSONObject(a);
                                data.setNpm(json.getString(config.KEY_NPM));
                                data.setNama_mhs(json.getString(config.KEY_NAMA));
                                datanya.add(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("ini kesalahannya " + e.getMessage());
                        }
                        adp_daftar.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.d("ini kesalahannya",error.toString());
                    }
                });

        requestQueue.add(jsonRequest);
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
