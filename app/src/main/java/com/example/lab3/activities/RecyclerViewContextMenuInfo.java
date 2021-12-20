package com.example.lab3.activities;

import android.view.ContextMenu;
import android.view.View;

public class RecyclerViewContextMenuInfo implements ContextMenu.ContextMenuInfo{
    private View view;
    public RecyclerViewContextMenuInfo(View v) {
        this.view = v;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
