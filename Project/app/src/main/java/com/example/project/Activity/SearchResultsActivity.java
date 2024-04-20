package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.SearchResultsAdapter;
import com.example.project.Decoration.ItemSpacingDecorationBottom;
import com.example.project.Domain.Barber;
import com.example.project.R;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView search_results;
    private RecyclerView.Adapter search_results_add;
    private TextView search_results_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        initSearchResults();

        search_results_text = findViewById(R.id.search_results_text);

        /*Intent intent = getIntent();
        search_results_text.setText(intent.getStringExtra("searchKey"));; // Example: retrieving a string value*/
    }

    private void initSearchResults()
    {
        // replace this with data from the db
        ArrayList<Barber> searchResults = new ArrayList<>();

        searchResults.add(new Barber("harel", "050-7870003", R.drawable.user_1, "Yish'i 10", "50₪"));
        searchResults.add(new Barber("harel2", "050-7870003", R.drawable.user_1, "Yish'i 10", "50₪"));
        searchResults.add(new Barber("harel3", "050-7870003", R.drawable.user_1, "Yish'i 10", "50₪"));
        searchResults.add(new Barber("harel4", "050-7870003", R.drawable.user_1, "Yish'i 10", "50₪"));
        searchResults.add(new Barber("harel4", "050-7870003", R.drawable.user_1, "Yish'i 10", "50₪"));
        searchResults.add(new Barber("harel4", "050-7870003", R.drawable.user_1, "Yish'i 10", "50₪"));

        // Initialize RecyclerView and set layout manager
        this.search_results = findViewById(R.id.search_results_list);
        this.search_results.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Create and set adapter
        search_results_add = new SearchResultsAdapter(searchResults);
        this.search_results.setAdapter(search_results_add);

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
        /*Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
        finish();*/
    }

    public void menuSettings(View view) {
        /*Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
        finish();*/
    }
}