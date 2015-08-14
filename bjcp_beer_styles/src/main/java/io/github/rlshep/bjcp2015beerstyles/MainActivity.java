package io.github.rlshep.bjcp2015beerstyles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.github.rlshep.bjcp2015beerstyles.adapters.ViewPagerAdapter;
import io.github.rlshep.bjcp2015beerstyles.tabs.SlidingTabLayout;


public class MainActivity extends AppCompatActivity {
    private static final int NUM_OF_TABS = 2;
    private static final int ON_TAP_TAB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CharSequence Titles[]={getString(R.string.cat_tab_header), getString(R.string.on_tap_tab_header)};
        setupToolbar();

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        final ViewPagerAdapter adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles, NUM_OF_TABS);

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
                if (ON_TAP_TAB == position) {
                    OnTapTab fragment = (OnTapTab) adapter.instantiateItem(pager, position);
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
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
//TODO: FIX
        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
//                Toast.makeText(getApplicationContext(), "Search not implemented.", Toast.LENGTH_SHORT).show();
//                return true;
                return super.onOptionsItemSelected(item);
            case R.id.action_info:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.action_icon);
    }
}
