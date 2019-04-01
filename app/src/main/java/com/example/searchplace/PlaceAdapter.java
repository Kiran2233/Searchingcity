package com.example.searchplace;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyHolder> {
    private static final String TAG = "PlaceArrayAdapter";
    private final PlacesClient placesClient;
    private RectangularBounds mBounds;
    public ArrayList<PlaceAutocomplete> mResultList = new ArrayList<>();
    public Context context;


    public PlaceAdapter(RectangularBounds mBounds, Context context) {
        this.mBounds = mBounds;
        this.context = context;
        placesClient = com.google.android.libraries.places.api.Places.createClient(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        Log.d("ONcreate", "onCREATE");
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(MyHolder viewHolder, int i) {
        Log.d("ONBINDVIEW", "mResultList.get(i).toString()");
        viewHolder.textView.setText(mResultList.get(i).toString());

    }

    @Override
    public int getItemCount() {
        Log.d("onITemCOunt", String.valueOf(mResultList.size()));
        return mResultList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("Myholder", "Entered");
            textView = itemView.findViewById(R.id.address);
        }
    }

    public ArrayList<PlaceAutocomplete> getPredictions(CharSequence constraint) {
                         ArrayList<PlaceAutocomplete> results = new ArrayList<>();

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        Log.d("ONGETPREDICS2", "JGFKJDSFKDSHFDH");

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest.Builder request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                // .setLocationBias(bounds)
                .setLocationBias(mBounds)
                .setCountry("au")
                .setTypeFilter(TypeFilter.CITIES)
                .setSessionToken(token)
                .setQuery(constraint.toString());

        Task<FindAutocompletePredictionsResponse> autocompletePredictions = placesClient.findAutocompletePredictions(request.build());


        autocompletePredictions.addOnSuccessListener((response) -> {
            if (response != null) {

                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {

                    Log.i(TAG, prediction.getPlaceId());
                    Log.i(TAG, prediction.getPrimaryText(null).toString());

                    results.add(new PlaceAutocomplete( prediction.getPrimaryText(null).toString()));
                           Log.d("array",mResultList.get(0).toString());

                }
                    Log.d("array2",mResultList.get(0).toString());
                mResultList=results;
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.d(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });

         Log.d("to_find_size", String.valueOf(mResultList.size()));

             return mResultList;

    }

public class PlaceAutocomplete {


    public CharSequence name;

    PlaceAutocomplete( CharSequence name) {

        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
} }