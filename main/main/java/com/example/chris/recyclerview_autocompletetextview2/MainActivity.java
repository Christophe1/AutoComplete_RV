package com.example.chris.recyclerview_autocompletetextview2;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
/*
import android.support.v7.widget.DividerItemDecoration;
*/
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CategoriesAdapter.CategoriesAdapterListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static RecyclerView recyclerView;
    private SearchView searchView;

    ArrayList<String> items;

    //in onCreate, we will be showing reviews
    private List<Review> reviewList = new ArrayList<Review>();
    //this is the adapter for reviews
    public PopulistoListAdapter pAdapter;
    //this is the url for loading the reviews
    private static final String AllReviews_URL = "http://www.populisto.com/AllReviews.php";

    //we are posting phoneNoofUser, the key is phonenumberofuser, which we see in php
    public static final String KEY_PHONENUMBER_USER = "phonenumberofuser";

    //when searchView has focus and user types, we will be showing/filtering
    //categories
    private List<Category> categoryList = new ArrayList<Category>();
    //this is the adapter for categories
    private CategoriesAdapter mAdapter;
    //this is the url for loading the categories
    //private static final String AllCategories_URL = "http://www.populisto.com/AllCategories.php";
    private static final String AllCategories_URL = "http://www.populisto.com/CategorySearch.php";


    String phoneNoofUser = "+353872934480";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //the adapter for reviews
        pAdapter = new PopulistoListAdapter(reviewList, this);

        //the adapter for filtering categories
        mAdapter = new CategoriesAdapter(this, categoryList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        //****************
        //HOW DO I SET mAdapter WHEN searchView HAS THE FOCUS??
        //****************

        recyclerView.setAdapter(pAdapter);

        JsonArrayRequest request = new JsonArrayRequest(AllReviews_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the results! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Review> items = new Gson().fromJson(response.toString(), new TypeToken<List<Review>>() {
                        }.getType());

                        // adding contacts to contacts list
                        reviewList.clear();
                        reviewList.addAll(items);

                        // refreshing recycler view
                        pAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "...4.php", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }


    //this is the function for filtering categories in the searchView
    //it is called onQueryTextChange
    private void fetchContacts() {

        //still crashes the app, with this here
        //recyclerView.setAdapter(mAdapter);

        //still crashes the app, with this here
   /*   if  (mAdapter!=null)
      {recyclerView.setAdapter(mAdapter);}
*/
        StringRequest request = new StringRequest(Request.Method.POST,AllCategories_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Do something with response

                            try {

                            //not sure exactly what is happening here
                            //something to do with converting the response to a list and setting it to
                            //our Category object...whatever that means
                            List<Category> items = new Gson().fromJson(response.toString(), new TypeToken<List<Category>>() {
                            }.getType());

                                //clear the list
                                categoryList.clear();

                                // adding contacts to contacts list
                                categoryList.addAll(items);

                                //app not crashing as much with this here
                                recyclerView.setAdapter(mAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // refreshing recycler view
                            mAdapter.notifyDataSetChanged();
                        }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            //post info to php
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //phoneNoofUser is the value we get from Android, the user's phonenumber.
                //the key,KEY_PHONENUMBER_USER, is "phonenumberofuser". When we see "phonenumberofuser" in our php,
                //put in phoneNoofUser
                params.put(KEY_PHONENUMBER_USER, phoneNoofUser);
                return params;

            }
        };
        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        //Clear query
        searchView.setQuery("", false);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {

                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                //WILL CRASH IF UNCOMMENTED
                //recyclerView.setAdapter(mAdapter);
                //reviewList.clear();

                //if the searchView is empty
                if (searchView.getQuery().length() == 0) {

                    //show the reviews, not the searched categories
                     recyclerView.setAdapter(pAdapter);
                     pAdapter.notifyDataSetChanged();
                } else {
                    //if there's text in the search box
                    fetchContacts();
                    // filter recycler view when text is changed
                    mAdapter.getFilter().filter(query);
                }
                return false;

            }});

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(Category category) {
        Toast.makeText(getApplicationContext(), "Selected: " + category.getName(), Toast.LENGTH_LONG).show();

        //   Toast.makeText(getApplicationContext(), "Selected: " + category.getName() + ", " + category.getPhone(), Toast.LENGTH_LONG).show();
    }
}