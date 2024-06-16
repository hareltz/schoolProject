package com.example.project.Decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemSpacingDecorationRight extends RecyclerView.ItemDecoration
{
    private final int spacing;

    // Constructor to initialize the ItemSpacingDecoration with the spacing value
    public ItemSpacingDecorationRight(Context context, int spacing)
    {
        this.spacing = spacing;
    }

    // Method to set spacing for each item in the RecyclerView
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);

        // Set spacing right
        outRect.right = spacing;
    }
}