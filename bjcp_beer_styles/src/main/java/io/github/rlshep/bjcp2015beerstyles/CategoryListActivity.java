package io.github.rlshep.bjcp2015beerstyles;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.adapters.CategoriesListAdapter;
import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;
import io.github.rlshep.bjcp2015beerstyles.listeners.GestureListener;


public class CategoryListActivity extends BjcpActivity {
    private GestureDetector gestureDetector;
    private String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_category_list);
        String searchedText = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("CATEGORY") + " - " + extras.getString("CATEGORY_NAME");
            setupToolbar(R.id.sclToolbar, title, false, true);

            categoryId = extras.getString("CATEGORY_ID");
            searchedText = extras.getString("SEARCHED_TEXT");
        }

        gestureDetector = new GestureDetector(this, new GestureListener());
        setListView(categoryId, searchedText);
    }

    @SuppressWarnings("unchecked")
    private void setListView(String categoryId, String searchedText) {
        List listView = new ArrayList();

        listView.addAll(BjcpDataHelper.getInstance(this).getCategorySections(categoryId));
        listView.addAll(BjcpDataHelper.getInstance(this).getCategoriesByParent(categoryId));

        ListAdapter categoryAdapter = new CategoriesListAdapter(this, listView, searchedText);
        ListView categoryListView = (ListView) findViewById(R.id.categoryListView);
        categoryListView.setAdapter(categoryAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) instanceof Category) {
                    Category category = (Category) parent.getItemAtPosition(position);
                    BjcpController.loadCategoryBody((Activity) view.getContext(), category);
                }
            }
        });

        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                boolean consumed = false;
                parent.setFocusable(false);

                if (parent.getItemAtPosition(position) instanceof Category) {
                    addCategoryToBookmarked((Category) parent.getItemAtPosition(position));
                    consumed = true;
                }
                //TODO: CLEAN UP OR FIX
                else if (parent.getItemAtPosition(position) instanceof Section) {
                    TextView rowText = (TextView) findViewById(R.id.catSectionText);
                    rowText.setSelectAllOnFocus(true);
                    consumed = false;
//                   consumed = true;
                }

                return consumed;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        boolean eventReturn;
        boolean eventConsumed = gestureDetector.onTouchEvent(event);
        TextView rowText = (TextView) findViewById(R.id.catSectionText);

        if (eventConsumed) {
            if (GestureListener.SWIPE_LEFT.equals(GestureListener.currentGesture)) {
                changeCategory(-1);
            } else if (GestureListener.SWIPE_RIGHT.equals(GestureListener.currentGesture)) {
                changeCategory(1);
            }

            eventReturn = true;
        } else if (rowText.isSelected()) {
            if (event.equals(MotionEvent.ACTION_DOWN)) {
                eventReturn = true;
            } else {
                eventReturn = false;
            }
        }else {
            eventReturn = super.dispatchTouchEvent(event);
        }

        return eventReturn;
    }

    private void addCategoryToBookmarked(Category category) {
        category.setBookmarked(true);
        BjcpDataHelper.getInstance(this).updateCategoryBookmarked(category);
        Toast.makeText(getApplicationContext(), R.string.on_tap_success, Toast.LENGTH_SHORT).show();
    }

    private void changeCategory(int i) {
        List<Category> categories = BjcpDataHelper.getInstance(this).getAllCategories();
        Category category = BjcpDataHelper.getInstance(this).getCategory(categoryId);
        int newOrder = category.getOrderNumber() + i;

        for (Category c : categories) {
            if (newOrder == c.getOrderNumber()) {
                BjcpController.loadCategoryList(this, c);
            }
        }
    }

    //TODO: START
//    public void onInitializeMenu(Menu menu) {
//        // Start with a menu Item order value that is high enough
//        // so that your "PROCESS_TEXT" menu items appear after the
//        // standard selection menu items like Cut, Copy, Paste.
//        int menuItemOrder = 100;
//        for (ResolveInfo resolveInfo : getSupportedActivities()) {
//            menu.add(Menu.NONE, Menu.NONE,
//                    menuItemOrder++,
//                    getLabel(resolveInfo))
//
//                    .setIntent(createProcessTextIntentForResolveInfo(resolveInfo))
//                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        }
//    }
//
//    private Intent createProcessTextIntent() {
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        return new Intent()
//                .setAction(Intent.ACTION_PROCESS_TEXT)
//                .setType("text/plain");
////        }
//    }
//
//    private List getSupportedActivities() {
//        PackageManager packageManager = getApplicationContext().getPackageManager();
//        return packageManager.queryIntentActivities(createProcessTextIntent(), 0);
//    }
//
//    private Intent createProcessTextIntentForResolveInfo(ResolveInfo info) {
//        return createProcessTextIntent()
//                .putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, !
//                        mTextView.isTextEditable())
//                .setClassName(info.activityInfo.packageName,
//                        info.activityInfo.name);
//    }
}
