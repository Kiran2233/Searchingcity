package com.example.searchplace;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.model.RectangularBounds;

import com.google.android.libraries.places.api.net.PlacesClient;



public class MainActivity extends AppCompatActivity {
    private String apiKey ="AIzaSyB6_TfYv6WT-sdlBmxmMQAhJV81VWfuqcQ";
    private EditText editText;
    private PlacesClient placesClient;

    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.cityname);

        recyclerView = findViewById(R.id.rv1);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),apiKey);
            Log.d("INITIALIZED","JGFKJDSFKDSHFDH");
        }
        else{
            Log.d("on else","already initialized");
        }
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(23.63936, 68.14712),
                new LatLng(28.20453, 97.34466));
        placeAdapter = new PlaceAdapter(bounds, MainActivity.this, new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PlaceAdapter.PlaceAutocomplete item, int position) {
                editText.setText(item.toString());
                recyclerView.setVisibility(View.GONE);
            }
        });
        PlacesClient placesClient = Places.createClient(this);
        this.placesClient = placesClient;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


               placeAdapter.getPredictions(s);
                Log.d("ONTEXTCHANGED","JGFKJDSFKDSHFDH");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        recyclerView.setAdapter(placeAdapter);
                    }
                }, 1000);   //5 seconds

                Log.d("AFTERSETADPTR","JGFKJDSFKDSHFDH");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
