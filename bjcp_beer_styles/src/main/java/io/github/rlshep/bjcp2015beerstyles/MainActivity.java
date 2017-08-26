package io.github.rlshep.bjcp2015beerstyles;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import io.github.rlshep.bjcp2015beerstyles.adapters.ViewPagerAdapter;
import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;
import io.github.rlshep.bjcp2015beerstyles.tabs.SlidingTabLayout;
import io.github.rlshep.bjcp2015beerstyles.view.ArrayAdapterSearchView;


public class MainActivity extends BjcpActivity implements SearchView.OnQueryTextListener {
    private static final int BOOKMARKED_TAB = 1;
    private static final int COLOR_TAB = 2;
    private static final int MAX_SEARCH_CHARS = 3;

    private static final String[] COUNTRIES = new String[]{"Belgium",
            "France", "France_", "Italy", "Germany", "Spain"};
    private ArrayAdapter<String> adapter;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_main);

        CharSequence Titles[] = {getString(R.string.cat_tab_header), getString(R.string.on_tap_tab_header), "Color Chart"};
        setupToolbar(R.id.tool_bar, "", true, false);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles);
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Added trigger On Tap reload when switching between tabs.
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (BOOKMARKED_TAB == position) {
                    BookmarkedTab fragment = (BookmarkedTab) adapter.instantiateItem(pager, position);
                    if (fragment != null) {
                        fragment.onResume();
                    }
                } else if (COLOR_TAB == position) {
                    ColorTableTab fragment = (ColorTableTab) adapter.instantiateItem(pager, position);
                    if (fragment != null) {
                        fragment.onResume();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        // Set search text color
        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.WHITE);

        // Set adapter to get search suggestions.
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        searchView.setAdapter(adapter);

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setText(adapter.getItem(position).toString());
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                return super.onOptionsItemSelected(item);
            case R.id.action_info:
                BjcpController.startAboutActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String keyword) {
        if (MAX_SEARCH_CHARS <= keyword.length()) {
            BjcpController.startSearchResultsActivity(this, keyword);
        } else {
            Toast.makeText(this.getApplicationContext(), R.string.not_enough_chars, Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Associate searchable configuration with the SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.collapseActionView(searchItem);
    }
}
