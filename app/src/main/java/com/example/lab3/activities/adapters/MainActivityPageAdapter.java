package com.example.lab3.activities.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.example.lab3.fragments.DetailsFragment;
import com.example.lab3.fragments.MainListFragment;
import com.example.lab3.presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPageAdapter extends FragmentStateAdapter {
    private final int PAGES_COUNT = 2;
    private final MainActivityPresenter presenter;
    private MainListFragment.OnOpenFileClickListener onOpenFileClickListener;
    private RecyclerView.AdapterDataObserver mObserver;
    private final Context context;
    private int selectedId = 0;
    private boolean isElementSelected = false;

    public void setOnOpenFileClickListener(MainListFragment.OnOpenFileClickListener onOpenFileClickListener) {
        this.onOpenFileClickListener = onOpenFileClickListener;
    }

    public MainActivityPageAdapter(@NonNull FragmentActivity fragmentActivity,
                                   MainActivityPresenter presenter) {
        super(fragmentActivity);
        this.context = fragmentActivity.getApplicationContext();
        this.presenter = presenter;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                MainListAdapter mainListAdapter = new MainListAdapter(context, new ArrayList<>());
                mainListAdapter.setOnItemSelected(this::onMainListItemClicked);
                mainListAdapter.setOnItemLongClick(this::onMainListItemLongClicked);
                MainListFragment fragment = MainListFragment.newInstance("","");
                fragment.setPresenter(presenter);
                fragment.setOnOpenFileClickListener(onOpenFileClickListener);
                presenter.setMainListAdapter(mainListAdapter);
                return fragment;
            case 1:
                Toast.makeText(this.context,
                        String.format("onBindViewHolder %d", 100),
                        Toast.LENGTH_SHORT).show();
                if (isElementSelected)
                    return DetailsFragment.newInstance(presenter.getById(selectedId));
        }
        return DetailsFragment.newInstance(null);
    }

    private void onMainListItemLongClicked(int elementId, View view) {

    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position,
                                 @NonNull List<Object> payloads) {

        Toast.makeText(holder.itemView.getContext(),
                String.format("onBindViewHolder %d", position),
                Toast.LENGTH_SHORT).show();
//        bindViewHolder(holder, position);
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return PAGES_COUNT;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        this.mObserver = observer;
    }

    public void onMainListItemClicked(int elementId){
        this.selectedId = elementId;
        isElementSelected = true;
        notifyItemChanged(1);
        //notifyItemRangeChanged(1, 1, null);
    }
}
