package com.example.lab3.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.lab3.R;
import com.example.lab3.activities.adapters.MainActivityPageAdapter;
import com.example.lab3.fragments.SearchDialog;
import com.example.lab3.fragments.SplashScreen;
import com.example.lab3.presenter.MainActivityPresenter;
import com.example.lab3.repository.DatabaseRepository;
import com.example.lab3.repository.RepositoryHolder;
import com.example.lab3.services.DatabaseService;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private MainActivityPageAdapter mainAdapter;
    private MainActivityPresenter presenter;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getApplicationContext(), DatabaseService.class));
        RepositoryHolder.setMainRepository(new DatabaseRepository());
        RepositoryHolder.setApplicationContext(getApplicationContext());
        presenter = new MainActivityPresenter(RepositoryHolder.getMainRepository());
        mainAdapter = new MainActivityPageAdapter(this, presenter);
        presenter.setPagerAdapter(mainAdapter);

        mainAdapter.setOnOpenFileClickListener(() -> {
            presenter.setConnectionString("mainDb.sqlite3");

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Загрузка данных...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.show();
            Thread thread = new Thread(() -> {
                try {
                    for (int i = 1; i < 11; i++) {
                        TimeUnit.MILLISECONDS.sleep(200);
                        int finalI = i;
                        this.runOnUiThread(() -> progressDialog.setProgress(finalI * 10));
                    }
                    TimeUnit.MILLISECONDS.sleep(300);
                    this.runOnUiThread(progressDialog::hide);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        });
        ViewPager2 vp = ((ViewPager2) findViewById(R.id.mainViewPager));
        vp.setAdapter(mainAdapter);
        Thread splt = new Thread(() -> {

            try {
//                TimeUnit.MILLISECONDS.sleep(50);
                Fragment fragment = new SplashScreen();
                runOnUiThread(() -> {
                    findViewById(R.id.splashFrame).setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.splashFrame, fragment, "SPLASH_SCREEN_OWN").commit();
                });
                TimeUnit.SECONDS.sleep(3);
                runOnUiThread(() ->{
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    findViewById(R.id.splashFrame).setVisibility(View.INVISIBLE);
                });
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e.getCause());
            }
        });
        splt.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toast.makeText(this, "onCreateOptionsMenu", Toast.LENGTH_SHORT).show();
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu, menu);
        mainAdapter.notifyItemRangeChanged(1, 1);
        return true;
    }

    @Override
    protected void onResume() {
        presenter.redrawFragments();
        Log.d("D0011", "MainActivity.onResume()");
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_add_action) {
            Log.d("TAG", "onOptionsItemSelected: R.id.menu_add_action");
            Intent intent = new Intent(this, AddEditActivity.class);
            intent.putExtra(AddEditActivity.ACTIVITY_MODE_FLAG_TAG, false);
            this.startActivityForResult(intent, R.id.element_vh_tag);
            //this.presenter.redrawFragments();
        } else if (itemId == R.id.menu_save_action) {
            Log.d("TAG", "onOptionsItemSelected: R.id.menu_save_action");
            presenter.onSaveMenuItemClick();
        } else if (itemId == R.id.menu_refresh_action) {
            ((ViewPager2) findViewById(R.id.mainViewPager)).setAdapter(mainAdapter);
        } else if (itemId == R.id.menu_search_action) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            LayoutInflater inflater = this.getLayoutInflater();
//            builder.setTitle("Search record...").setView(inflater.inflate(R.layout.fragment_search_dialog, null))
//
//                    .setPositiveButton("Search", (dialog, id) -> {
//                        // sign in the user ...
//                    })
//                    .setNegativeButton("Cancel",
//                            (dialog, id) -> dialog
//                                    .cancel());
//            builder.create().show();
            SearchDialog dialog = new SearchDialog(this);
            dialog.show(getSupportFragmentManager(), SearchDialog.TAG);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == R.id.element_vh_tag && resultCode == RESULT_OK) {
            presenter.notifyDataChanged();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        RecyclerViewContextMenuInfo info = (RecyclerViewContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.removeMenuAction:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?")
                        .setIcon(android.R.drawable.stat_notify_error)
                        .setPositiveButton("Yes!", (dialog, which) -> {
                            int id = presenter.getMainListAdapter().getContextualSelectedId();
                            presenter.requestForDelete(id);
                        })
                        .setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()));
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.togleColorActionMenu:
                View view = presenter.getMainListAdapter().getContextSelectedView();
                int color = getResources().getColor(R.color.secondaryLightColor);
                view.findViewById(R.id.cardItem).setBackgroundColor(color);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}