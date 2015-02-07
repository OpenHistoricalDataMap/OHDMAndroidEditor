package android.ohdm.de.ohdmviewer;

import android.app.Activity;
import android.content.Intent;
import android.ohdm.de.editor.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class EditPolyObjectData extends Activity {

    private static final String DATA_KEY = "data_key";
    private static final String DATA_VALUE = "data_value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poly_object_data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_poly_object_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonEditOk(View view) {

        TextView textViewKey = (TextView)findViewById(R.id.polyobject_data_key);
        TextView textViewValue = (TextView)findViewById(R.id.polyobject_data_value);

        Intent resultData = new Intent();
        resultData.putExtra(DATA_KEY, textViewKey.getText().toString());
        resultData.putExtra(DATA_VALUE, textViewValue.getText().toString());
        setResult(Activity.RESULT_OK, resultData);

        finish();
    }

    public void buttonEditCancel(View view) {

        finish();

    }
}
