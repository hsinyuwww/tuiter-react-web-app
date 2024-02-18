package edu.northeastern.numad24sp_group4;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import android.content.Intent;

import edu.northeastern.numad24sp_group4.EventsDisplay_RecyclerView.EventCard;
import edu.northeastern.numad24sp_group4.EventsDisplay_RecyclerView.ListOfEvents;

//public class Activity6 extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_a6);
//    }
//
//}


public class Activity6 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "WebServiceActivity";
    Spinner eventState;
    TextView mTitleTextView, stateResult, locationResult, performersResult, dateResult, urlResult;
    ProgressBar progressBar;
    String state, date ,jState ,jName, finalStringPerformers, eventurl, image;
    private static final String CLIENT_ID = "Mzk5MTQ0NzZ8MTcwODA1NTA3MC42MjY2NzIz";
    private static final String BASE_URL = "https://api.seatgeek.com/2/";
    private ArrayList<EventCard> eventList = new ArrayList<>();

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a6_display_events);

        Button button = findViewById(R.id.getdata);
        eventState = (Spinner) findViewById(R.id.stateSpinner);
        progressBar = findViewById(R.id.progressBar);
        dateResult = findViewById(R.id.DateCheck2);


        Button datePicker = findViewById(R.id.datepickerButton);

        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = dateObj.format(formatter);
        dateResult.setText(initDate);
        Log.v("CurrentDate:", dateResult.getText().toString());

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.states_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventState.setAdapter(adapter);

        eventState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            // do nothing
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // state = eventState.getText().toString();

                if(dateResult.getText().toString()!="" && state!=""){
                    progressBar.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });

                    /**
                     * Calling the ListOfEVents Activity and passing the selected date and state.
                     */
                    Intent intent = new Intent(Activity6.this, ListOfEvents.class);

                    intent.putExtra("state", state);
                    intent.putExtra("date", dateResult.getText().toString());

                    startActivity(intent);
                    progressBar.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Please input date and state", Toast.LENGTH_SHORT).show();
                }


            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // restore saved state if exists

    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        @SuppressLint("DefaultLocale") String monthf = String.format("%02d", month + 1);
        String data =  year + "-" + monthf + "-" + dayOfMonth;
        dateResult.setText(data);
    }





    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialogue = new DatePickerDialog(
                Activity6.this,
                Activity6.this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)


        );
        datePickerDialogue.show();
    }


}

