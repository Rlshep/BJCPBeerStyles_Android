package net.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;


public class MainActivity extends AppCompatActivity {

    TextView buckysText;
    BjcpDataHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buckysText = (TextView) findViewById(R.id.buckysText);

        dbHandler = new BjcpDataHelper(this, BjcpDataHelper.DATABASE_NAME, null, 1);
        dbHandler.onUpgrade(dbHandler.getWritableDatabase(), 1, 1);

        printDatabase();
    }

    public void addButtonClicked(View view) {
//        Products product = new Products(buckysInput.getText().toString());
//        dbHandler.addProduct(product);
//        printDatabase();
    }

    public void deleteButtonClicked(View view) {
//        String inputText = buckysInput.getText().toString();
//        dbHandler.deleteProduct(inputText);
//        printDatabase();
    }

    private void printDatabase() {
        String dbString = dbHandler.databaseToString();
        buckysText.setText(dbString);
    }


}
