package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.AppointmentChooseAdapter;
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
    TextView search_results_text, errorMsg;
    EditText searchBar;
    ArrayList<Barber> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();

        search_results_text = findViewById(R.id.search_results_text);
        errorMsg = findViewById(R.id.search_results_error);
        searchBar = findViewById(R.id.search_result_search_bar);

        String searchTerm = intent.getStringExtra("searchTerm");
        initSearchResults(searchTerm);

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            // function that detects when the user press enter on the search_bar EditText
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null &&
                        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                {
                    Intent intent = new Intent(SearchResultsActivity.this, SearchResultsActivity.class);
                    intent.putExtra("searchTerm", searchBar.getText().toString()); // Example: sending a string value
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initSearchResults(String searchTerm)
    {
        ArrayList<Barber> tempBarbers = new ArrayList<>(Helper.barbers);

        if (searchTerm.isEmpty() || searchTerm == "")
        {
            errorMsg.setText("You have to search something\n To get result enter something");
            errorMsg.setVisibility(View.VISIBLE);

            return;
        }

        for (Barber barber : tempBarbers)
        {
            if (barber.getName().toLowerCase().startsWith(searchTerm.toLowerCase()))
            {
                searchResults.add(barber);
            }
        }

        if (searchResults.isEmpty())
        {
            errorMsg.setText("There in no barber\n With such a name.");
            errorMsg.setVisibility(View.VISIBLE);

            return;
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

        search_results_text.setText("there is: " + searchResults.size() + " results.");
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