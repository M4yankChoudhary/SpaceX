package me.mayankchoudhary.spacex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.mayankchoudhary.spacex.Room.Crew;
import me.mayankchoudhary.spacex.Room.CrewDatabase;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    private FloatingActionButton refreshButton, deleteAllbtn;
    private List<Crew> mCrewList;
    private CrewAdapter mCrewAdapter;
    private RecyclerView mCrewRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteAllbtn = findViewById(R.id.deleteButton);
        refreshButton = findViewById(R.id.refreshButton);
        mCrewRecycler = findViewById(R.id.recyclerView);


        // if the crew table is empty fill it.
        if(setUpDB().dao().checkCrew() == 0) {
            fetchAndInsert();
        }
        setUpRecycler();


        // refresh
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // disable button for 3000 millis
                refreshButton.setEnabled(false);
                refreshButton.postDelayed(() -> refreshButton.setEnabled(true), 3000);

                // if the crew table is empty fill it.
                if(setUpDB().dao().checkCrew() == 0) {
                    fetchAndInsert();
                } else {
                    Toast.makeText(MainActivity.this, "Already Up-To-Date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // delete all data from room db
        deleteAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // disable button for 3000 millis
                deleteAllbtn.setEnabled(false);
                deleteAllbtn.postDelayed(() -> deleteAllbtn.setEnabled(true), 3000);

                // delete data from room
                setUpDB().dao().deleteAll();
                // clear crew list
                mCrewList.clear();
                mCrewAdapter.notifyDataSetChanged();
            }
        });

    }


    // setup the database
    public CrewDatabase setUpDB() {
        CrewDatabase database = Room.databaseBuilder(
                MainActivity.this, CrewDatabase.class, "CrewDatabase")
                .allowMainThreadQueries().build();

        return database;
    }


    // fetch data from API and insert it into the room database. (Using Volley)
    public void fetchAndInsert() {

        // API URL
        String URL = "https://api.spacexdata.com/v4/crew";

        requestQueue = Volley.newRequestQueue(this);

        // get JSON response from API
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // insert data into room db
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);

                                String name = object.get("name").toString();
                                String agency = object.get("agency").toString();
                                String image = object.get("image").toString();
                                String wikipedia = object.get("wikipedia").toString();
                                String status = object.get("status").toString();
                                Crew crew = new Crew(name, agency, image, wikipedia, status);

                                setUpDB().dao().crewInsertion(crew);
                                setUpRecycler();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },// handle error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SpaceXResponse", "Something went wrong");
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    // setup the recyclerview
    // get the data from crew table
    public void setUpRecycler() {
        mCrewList = new ArrayList<>();
        mCrewList = setUpDB().dao().getAllCrew();
        mCrewAdapter = new CrewAdapter(MainActivity.this, mCrewList);
        mCrewRecycler.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        mCrewRecycler.setAdapter(mCrewAdapter);
        mCrewAdapter.notifyDataSetChanged();
    }
}