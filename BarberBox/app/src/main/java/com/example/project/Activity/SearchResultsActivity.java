package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.BarberAdapter;
import com.example.project.Adapter.SearchResultsAdapter;
import com.example.project.Decoration.ItemSpacingDecorationBottom;
import com.example.project.Domain.Barber;
import com.example.project.Helper;
import com.example.project.Interfaces.IRecyclerViewOnBarberClick;
import com.example.project.R;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity implements IRecyclerViewOnBarberClick {

    private RecyclerView search_results;
    private RecyclerView.Adapter search_results_add;
    private TextView search_results_text;

    ArrayList<Barber> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();

        String searchTerm = intent.getStringExtra("searchTerm");
        initSearchResults(searchTerm);

        search_results_text = findViewById(R.id.search_results_text);
    }

    private void initSearchResults(String searchTerm)
    {
        // replace this with data from the db
        ArrayList<Barber> tempBarbers = new ArrayList<>(Helper.barbers_);

        for (Barber barber : tempBarbers)
        {
            if (barber.getName().toLowerCase().startsWith(searchTerm.toLowerCase()))
            {
                searchResults.add(barber);
            }
        }

        if (searchResults.isEmpty())
        {
            // show message
        }

        // Initialize RecyclerView and set layout manager
        this.search_results = findViewById(R.id.search_results_list);
        this.search_results.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Create and set adapter
        this.search_results_add = new SearchResultsAdapter(searchResults, this);
        this.search_results.setAdapter(this.search_results_add);

        // Apply ItemSpacingDecoration to add spacing between items
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        this.search_results.addItemDecoration(new ItemSpacingDecorationBottom(this, spacingInPixels));
    }

    public void ArrowBack(View view)
    {
        Intent intent = new Intent(this, MainPage.class); // run the main class
        startActivity(intent);
        finish();
    }

    public void menuHome(View view) {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
        finish();
    }

    public void menuSearch(View view) {
        // we here already
    }

    public void menuUser(View view) {
        Intent intent = new Intent(this, AccountSettings.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBarberClick(int position, int type)
    {
        Barber barber = ((SearchResultsAdapter)this.search_results_add).GetBarberByPosition(position);

        Intent intent = new Intent(this, BarberInfo.class);
        assert barber != null; // check that "barber" is not null
        intent.putExtra("barberId", barber.get_id());

        startActivity(intent);
        finish();
    }
}