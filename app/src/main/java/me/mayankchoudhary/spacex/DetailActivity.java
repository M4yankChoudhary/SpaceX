package me.mayankchoudhary.spacex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import me.mayankchoudhary.spacex.Room.Crew;

public class DetailActivity extends AppCompatActivity {

    private ImageView mCrewImage;
    private TextView mCrewName, mCrewAgency, mCrewStatus, mCrewWikipedia;
    private Crew crew = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mCrewImage = findViewById(R.id.detailCrewImage);
        mCrewName = findViewById(R.id.detailCrewName);
        mCrewAgency = findViewById(R.id.detailCrewAgency);
        mCrewStatus = findViewById(R.id.detailCrewStatus);
        mCrewWikipedia = findViewById(R.id.detailWikipedia);

        Object obj = getIntent().getSerializableExtra("detail");
        if(obj instanceof Crew){
            crew = (Crew) obj;
        }
        Glide.with(getApplicationContext()).load(crew.getImage()).into(mCrewImage);
        mCrewName.setText(crew.getName());
        mCrewAgency.setText(crew.getAgency());
        mCrewStatus.setText(crew.getStatus());


    }

    public void openWebsite(View view) {
        String url = crew.getWikipedia();
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }
}