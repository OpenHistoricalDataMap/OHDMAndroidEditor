package android.ohdm.de.editor.activities;

import android.app.Activity;
import android.content.Intent;
import android.ohdm.de.editor.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;


public class ValiddatepickerActivity extends Activity {

    private static final int MIN_DAY = 1;
    private static final int MIN_MONTH = 1;
    private static final int MIN_YEAR = 0;

    private static final int MAX_DAY = 31;
    private static final int MAX_MONTH = 12;
    private static final int MAX_YEAR = 3000;

    private int dateDay = MIN_DAY;
    private int dateMonth = MIN_MONTH;
    private int dateYear = MIN_YEAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_validdatepicker);

        if(getIntent() != null){
            dateDay = getIntent().getIntExtra(GetNearPolyObjectsActivity.EXTRA_DAY, MIN_DAY);
            dateMonth = getIntent().getIntExtra(GetNearPolyObjectsActivity.EXTRA_MONTH, MIN_MONTH);
            dateYear = getIntent().getIntExtra(GetNearPolyObjectsActivity.EXTRA_YEAR, MIN_YEAR);
        }

        NumberPicker numberPickerDay = (NumberPicker) findViewById(R.id.numberPickerDay);
        NumberPicker numberPickerMonth = (NumberPicker) findViewById(R.id.numberPickerMonth);
        NumberPicker numberPickerYear = (NumberPicker) findViewById(R.id.numberPickerYear);

        numberPickerDay.setMinValue(MIN_DAY);
        numberPickerDay.setMaxValue(MAX_DAY);

        numberPickerMonth.setMinValue(MIN_MONTH);
        numberPickerMonth.setMaxValue(MAX_MONTH);

        numberPickerYear.setMinValue(MIN_YEAR);
        numberPickerYear.setMaxValue(MAX_YEAR);

        numberPickerDay.setValue(dateDay);
        numberPickerMonth.setValue(dateMonth);
        numberPickerYear.setValue(dateYear);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_dialog_validdatepicker, menu);
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

    public void buttonDateOk(View view){

        NumberPicker numberPickerDay = (NumberPicker) findViewById(R.id.numberPickerDay);
        NumberPicker numberPickerMonth = (NumberPicker) findViewById(R.id.numberPickerMonth);
        NumberPicker numberPickerYear = (NumberPicker) findViewById(R.id.numberPickerYear);

        Intent resultData = new Intent();
        resultData.putExtra(GetNearPolyObjectsActivity.EXTRA_DAY, numberPickerDay.getValue());
        resultData.putExtra(GetNearPolyObjectsActivity.EXTRA_MONTH, numberPickerMonth.getValue());
        resultData.putExtra(GetNearPolyObjectsActivity.EXTRA_YEAR, numberPickerYear.getValue());

        setResult(Activity.RESULT_OK, resultData);

        finish();
    }

    public void buttonDateCancel(View view){
        finish();
    }
}
