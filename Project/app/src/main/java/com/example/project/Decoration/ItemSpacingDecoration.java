package com.example.project.Decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemSpacingDecoration extends RecyclerView.ItemDecoration
{
    private int spacing;

    // Constructor to initialize the ItemSpacingDecoration with the spacing value
    public ItemSpacingDecoration(Context context, int spacing)
    {
        this.spacing = spacing;
    }

    // Method to set spacing for each item in the RecyclerView
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);

        // Set spacing for left, right, and bottom of each item
        //outRect.left = spacing;
        outRect.right = spacing*2;
        //outRect.bottom = spacing;

        // Add top margin only for the first item to avoid double space between items
        // For other items, set top margin to 0
        /*if (parent.getChildAdapterPosition(view) == 0)
        {
            outRect.top = spacing;
        } else
        {
            outRect.top = 0;
        }*/
    }
}