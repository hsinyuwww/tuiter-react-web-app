package edu.northeastern.numad24sp_group4.EventsDisplay_RecyclerView;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import edu.northeastern.numad24sp_group4.R;

public class ListOfEvents extends AppCompatActivity implements EventInterface{

    private static final String TAG = "ListOfEvents";
    private static final String CLIENT_ID = "Mzk5MTQ0NzZ8MTcwODA1NTA3MC42MjY2NzIz";
    private static final String BASE_URL = "https://api.seatgeek.com/2/";
    private ArrayList<EventCard> eventList = new ArrayList<>();

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    private RecyclerView recyclerView;

    private EventAdapter eventAdapter;
    private RecyclerView.LayoutManager rLayoutManger;

    private boolean flag=false;

    private String date, state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_events);


        Intent intent = getIntent();

        date = intent.getStringExtra("date");
        state =  intent.getStringExtra("state");


        //callWebserviceButtonHandler();
        init(savedInstanceState);
    }

    @Override
    public void onLinkClick(int position) {
        EventCard event = eventList.get(position);
        String url = event.getUrl();
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } else {
            // Handle the case where the URL is null or empty
            Toast.makeText(this, "URL is not available", Toast.LENGTH_SHORT).show();
        }
    }


    private void init(Bundle savedInstanceState) {

            createRecyclerView();

            if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
                if (eventList == null || eventList.size() == 0) {

                    int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);

                    // Retrieve keys we stored in the instance
                    for (int i = 0; i < size; i++) {
                        String location = savedInstanceState.getString(KEY_OF_INSTANCE + i + "0");
                        String dateTime = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                        String url = savedInstanceState.getString(KEY_OF_INSTANCE + i + "2");
                        String image = savedInstanceState.getString(KEY_OF_INSTANCE + i + "3");
                        String name = savedInstanceState.getString(KEY_OF_INSTANCE + i + "4");


                        // We need to make sure names such as "XXX(checked)" will not duplicate
                        // Use a tricky way to solve this problem, not the best though

                        EventCard eventCard = new EventCard(location, dateTime, url, image, name);

                        eventList.add(eventCard);

                    }
                    for(int i=0;i<eventList.size();i++){
                        eventAdapter.notifyItemInserted(i);
                    }
                }
                flag = savedInstanceState.getBoolean("FLAG");
            }

            if(!flag){
                callWebserviceButtonHandler();
                //createRecyclerView();
            }

    }


    /**
     * Method to call the WebService
     */
    public void callWebserviceButtonHandler(){
        ListOfEvents.PingWebServiceTask task = new ListOfEvents.PingWebServiceTask();
        new Thread(task).start();

    }

/**
     * This is class is responsible for establishing the HTTPS Connection
     * and returns data from the API call.
     * It implements Runnable to handle threading.
     */

private class PingWebServiceTask  implements Runnable{

    @Override
    public void run() {
        flag = true;

        URL urlFinal;
        String stringURL;
        // state = eventState.getText().toString();
        stringURL = (BASE_URL + "events?" + "client_id=" + CLIENT_ID
                + "&venue.state="+  state + "&datetime_utc.gt=" + date + "&callback=fireEvent" );

        try {
            urlFinal = new URL(stringURL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) urlFinal.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            urlConnection.connect();

            // Read response.
            InputStream inputStream = urlConnection.getInputStream();
            String resp = convertStreamToString(inputStream);
            //Log.v("resp:", resp.toString());
            resp = resp.substring(10,resp.length()-1);

            JSONObject jObject = new JSONObject(resp);
            JSONArray jEvent = jObject.getJSONArray("events");

            //Log.v("jEvent ", jEvent.toString());

            for(int i=0; i<jEvent.length(); i++){

                JSONObject event = jEvent.getJSONObject(i);
                String jImage="";
                String jAddress="";
                String jExtendedAddress = "";
                String jName = "";
                String jURL = "";
                String location = "";
                String jDateTime = "";

                try {
                    if (event.has("venue")) {
                        JSONObject obj = event.getJSONObject("venue");

                        if(obj.has("address")){
                            jAddress=obj.getString("address");
                        }
                        if(obj.has("extended_address")){
                            jExtendedAddress=obj.getString("extended_address");
                        }
                        if(obj.has("name")){
                            jName=obj.getString("name");
                        }
                        if(obj.has("url")){
                            jURL=obj.getString("url");
                        }

                        location = jAddress+", "+jExtendedAddress.trim();


                    }

                } catch (JSONException e) {
                    e.printStackTrace(); // Optionally, print the stack trace for debugging purposes
                }



                try {

                    if(event.has("datetime_local")){
                        jDateTime = convertDateFormat(event.getString("datetime_local"));

                    }

                    if (event.has("image")) {
                        jImage = event.getString("image");
                    } else if (event.has("performers")) {
                        JSONArray obj = event.getJSONArray("performers");
                        for(int j=0; j<obj.length(); j++){
                            JSONObject subObj = obj.getJSONObject(j);
                            if(subObj.has("image")){
                                jImage=subObj.getString("image");
                                break;
                            }
                        }

                    }



                } catch (JSONException e) {
                    e.printStackTrace(); // Optionally, print the stack trace for debugging purposes
                }


                EventCard eventItem = new EventCard(location, jDateTime, jURL, jImage, jName );

                eventList.add(eventItem);



            }

            for (EventCard obj : eventList) {
                Log.v("Event:  ", obj.toString());
            }

            for(int i=0;i<eventList.size();i++){
                eventAdapter.notifyItemInserted(i);
            }



        } catch (MalformedURLException e) {
            Log.e(TAG,"MalformedURLException");
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.e(TAG,"ProtocolException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG,"IOException");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG,"JSONException");
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();


        }


    }
}

    /**
     * Helper function to convert input stream.
     * @param is input stream.
     * @return String
     */
    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : ""; }


    private void createRecyclerView() {


        rLayoutManger = new GridLayoutManager(this, 2);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        eventAdapter = new EventAdapter(eventList);
        eventAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(rLayoutManger);


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        int size = eventList  == null ? 0 : eventList.size();
        outState.putInt(NUMBER_OF_ITEMS, size);

        // Need to generate unique key for each item
        // This is only a possible way to do, please find your own way to generate the key
        for (int i = 0; i < size; i++) {

            outState.putString(KEY_OF_INSTANCE + i + "0", eventList.get(i).getLocation());

            outState.putString(KEY_OF_INSTANCE + i + "1", eventList.get(i).getDateTime());

            outState.putString(KEY_OF_INSTANCE + i + "2", eventList.get(i).getUrl());

            outState.putString(KEY_OF_INSTANCE + i + "3", eventList.get(i).getImage());

            outState.putString(KEY_OF_INSTANCE + i + "4", eventList.get(i).getName());
        }

        outState.putBoolean("FLAG", flag);
        super.onSaveInstanceState(outState);

    }

    public String convertDateFormat(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, MMM dd, h:mm a", Locale.getDefault());
        String outputDate = "";

        try {
            Date date = inputFormat.parse(inputDate);
            outputDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDate;
    }
}