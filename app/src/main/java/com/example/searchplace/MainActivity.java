package com.example.searchplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String apiKey ="AIzaSyC6QUQSzSFDaKwKCj5IjY3lwuu6AIK8V2g";
    private EditText editText;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> resultsList;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.cityname);
        recyclerView = findViewById(R.id.rv1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),apiKey);
        }
        else{
            Log.d("on else","already initialized");
        }
        PlacesClient placesClient = Places.createClient(this);
        this.placesClient = placesClient;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getCities(s.toString());
                    searchAdapter = new SearchAdapter(resultsList,MainActivity.this);
                    recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public List<AutocompletePrediction> getCities(String query){
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596));
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setCountry("au")
                .setTypeFilter(TypeFilter.CITIES)
                .setSessionToken(token)
                .setQuery(query)
                .build();
        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            resultsList=response.getAutocompletePredictions();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
               Log.d("Check", prediction.getPlaceId());
                Log.i("He", prediction.getPrimaryText(null).toString());
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("Hey", "Place not found: " + apiException.getStatusCode());
            }
        });
        return  resultsList;
    }
}
