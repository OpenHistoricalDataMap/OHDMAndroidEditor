package android.ohdm.de.editor.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.ohdm.de.editor.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GetNearPolyObjectsActivity extends Activity {

    public static final String EXTRA_YEAR = "extra_year";
    public static final String EXTRA_MONTH = "extra_month";
    public static final String EXTRA_DAY = "extra_day";

    public static final int SINCE_DAY_START = 1;
    public static final int SINCE_MONTH_START = 1;
    public static final int SINCE_YEAR_START = 0;

    public static final int UNTIL_DAY_START = 1;
    public static final int UNTIL_MONTH_START = 1;
    public static final int UNTIL_YEAR_START = 3000;

    private static final String TAG = "GetNearPolyObjectsActiv";

    private static final String BUNDLE_DATE_SINCE_DAY = "bundle_date_since_day";
    private static final String BUNDLE_DATE_SINCE_MONTH = "bundle_date_since_month";
    private static final String BUNDLE_DATE_SINCE_YEAR = "bundle_date_since_year";
    private static final String BUNDLE_DATE_UNTIL_DAY = "bundle_date_until_day";
    private static final String BUNDLE_DATE_UNTIL_MONTH = "bundle_date_until_month";
    private static final String BUNDLE_DATE_UNTIL_YEAR = "bundle_date_until_year";
    private static final String BUNDLE_LONGITUDE = "longitude";
    private static final String BUNDLE_LATITUDE = "latitude";

    private static final int DATE_SINCE_REQUEST_CODE = 1445;
    private static final int DATE_UNTIL_REQUEST_CODE = 1446;

    private int dateSinceDay = SINCE_DAY_START;
    private int dateSinceMonth = SINCE_MONTH_START;
    private int dateSinceYear = SINCE_YEAR_START;

    private int dateUntilDay = UNTIL_DAY_START;
    private int dateUntilMonth = UNTIL_MONTH_START;
    private int dateUntilYear = UNTIL_YEAR_START;

    private double longitude = 0;
    private double latitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_near_poly_objects);

        if (savedInstanceState != null) {
            dateSinceDay = savedInstanceState.getInt(BUNDLE_DATE_SINCE_DAY);
            dateSinceMonth = savedInstanceState.getInt(BUNDLE_DATE_SINCE_MONTH);
            dateSinceYear = savedInstanceState.getInt(BUNDLE_DATE_SINCE_YEAR);

            dateUntilDay = savedInstanceState.getInt(BUNDLE_DATE_UNTIL_DAY);
            dateUntilMonth = savedInstanceState.getInt(BUNDLE_DATE_UNTIL_MONTH);
            dateUntilYear = savedInstanceState.getInt(BUNDLE_DATE_UNTIL_YEAR);

            longitude = savedInstanceState.getDouble(BUNDLE_LONGITUDE);
            latitude = savedInstanceState.getDouble(BUNDLE_LATITUDE);
        }

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            longitude = bundle.getDouble(MainActivity.EXTRA_NEAR_GEOPOINT_LONG);
            latitude = bundle.getDouble(MainActivity.EXTRA_NEAR_GEOPOINT_LAT);
        }

        dateSinceChanged(dateSinceYear, dateSinceMonth, dateSinceDay);
        dateUntilChanged(dateUntilYear, dateUntilMonth, dateUntilDay);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {

        state.putInt(BUNDLE_DATE_SINCE_DAY, dateSinceDay);
        state.putInt(BUNDLE_DATE_SINCE_MONTH, dateSinceMonth);
        state.putInt(BUNDLE_DATE_SINCE_YEAR, dateSinceYear);

        state.putInt(BUNDLE_DATE_UNTIL_DAY, dateUntilDay);
        state.putInt(BUNDLE_DATE_UNTIL_MONTH, dateUntilMonth);
        state.putInt(BUNDLE_DATE_UNTIL_YEAR, dateUntilYear);

        state.putDouble(BUNDLE_LONGITUDE, longitude);
        state.putDouble(BUNDLE_LATITUDE, latitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_near_poly_objects, menu);
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

    public void buttonNearObjectsOk(View view) {

        Intent resultData = new Intent();

        String valid_since = String.format("%04d", dateSinceYear) + "-" + dateSinceMonth + "-" + dateSinceDay;
        String valid_until = String.format("%04d", dateUntilYear) + "-" + dateUntilMonth + "-" + dateUntilDay;

        EditText editTextDistance = (EditText) findViewById(R.id.editText);
        int distance = Integer.parseInt(editTextDistance.getText().toString());

        Log.d(TAG,"Distance: "+distance);

        resultData.putExtra(MainActivity.EXTRA_NEAR_DISTANCE, distance);
        resultData.putExtra(MainActivity.EXTRA_NEAR_VALID_SINCE, valid_since);
        resultData.putExtra(MainActivity.EXTRA_NEAR_VALID_UNTIL, valid_until);
        resultData.putExtra(MainActivity.EXTRA_NEAR_GEOPOINT_LAT, latitude);
        resultData.putExtra(MainActivity.EXTRA_NEAR_GEOPOINT_LONG, longitude);

        setResult(Activity.RESULT_OK, resultData);

        finish();
    }

    public void buttonNearObjectsCancel(View view) {
        finish();
    }

    public void buttonSelectDateSince(View view) {
        Intent selectDateIntent = new Intent(this, ValiddatepickerActivity.class);

        Bundle extras = new Bundle();

        extras.putInt(EXTRA_DAY, dateSinceDay);
        extras.putInt(EXTRA_MONTH, dateSinceMonth);
        extras.putInt(EXTRA_YEAR, dateSinceYear);

        selectDateIntent.putExtras(extras);

        startActivityForResult(selectDateIntent, DATE_SINCE_REQUEST_CODE);
    }

    public void buttonSelectDateUntil(View view) {
        Intent selectDateIntent = new Intent(this, ValiddatepickerActivity.class);

        Bundle extras = new Bundle();

        extras.putInt(EXTRA_DAY, dateUntilDay);
        extras.putInt(EXTRA_MONTH, dateUntilMonth);
        extras.putInt(EXTRA_YEAR, dateUntilYear);

        selectDateIntent.putExtras(extras);

        startActivityForResult(selectDateIntent, DATE_UNTIL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == DATE_SINCE_REQUEST_CODE) {

            dateSinceYear = data.getIntExtra(EXTRA_YEAR, 0);
            dateSinceMonth = data.getIntExtra(EXTRA_MONTH, 0);
            dateSinceDay = data.getIntExtra(EXTRA_DAY, 0);

            dateSinceChanged(dateSinceYear, dateSinceMonth, dateSinceDay);

        } else if (resultCode == Activity.RESULT_OK && requestCode == DATE_UNTIL_REQUEST_CODE) {

            dateUntilYear = data.getIntExtra(EXTRA_YEAR, 0);
            dateUntilMonth = data.getIntExtra(EXTRA_MONTH, 0);
            dateUntilDay = data.getIntExtra(EXTRA_DAY, 0);

            dateUntilChanged(dateUntilYear, dateUntilMonth, dateUntilDay);
        }
    }

    private void dateSinceChanged(int year, int month, int day) {
        Button dateSinceButton = (Button) findViewById(R.id.buttonDateSince);
        dateSinceButton.setText(year + "-" + month + "-" + day);
    }

    private void dateUntilChanged(int year, int month, int day) {
        Button buttonDateUntil = (Button) findViewById(R.id.buttonDateUntil);
        buttonDateUntil.setText(year + "-" + month + "-" + day);
    }
}
