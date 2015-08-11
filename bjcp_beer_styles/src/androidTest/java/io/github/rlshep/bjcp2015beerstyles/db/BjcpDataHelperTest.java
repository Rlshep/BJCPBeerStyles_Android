package io.github.rlshep.bjcp2015beerstyles.db;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class BjcpDataHelperTest extends ApplicationTestCase<Application> {
    private BjcpDataHelper dbHelper;

    public BjcpDataHelperTest() {
        super(Application.class);
    }

    @SmallTest
    public void testCanaryIsAlive(){
        assertTrue(true);
    }


//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
////        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
//        dbHelper = new BjcpDataHelper(getContext(), BjcpDataHelper.DATABASE_NAME, null, 1);
//    }
//
//    @SmallTest
//    public void testDropDB(){
//        assertTrue(mContext.deleteDatabase(BjcpDataHelper.DATABASE_NAME));
//    }
//
//    @SmallTest
//    public void testCreateDB(){
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        assertTrue(db.isOpen());
//        db.close();
//    }
}
