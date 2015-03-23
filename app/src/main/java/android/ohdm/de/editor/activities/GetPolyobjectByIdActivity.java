package android.ohdm.de.editor.activities;

import android.app.Activity;
import android.content.Intent;
import android.ohdm.de.editor.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class GetPolyobjectByIdActivity extends Activity {

    private static final String TAG = "GetPolyobjectByIdAct";
    private static final String EXTRA_POLYOBJECTID = "polyobjectid";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_getpolyobjectbyid);
    }

     /*
    Button-Listener
     */
    public void buttonIdOk(View view) {
        EditText editText = (EditText) findViewById(R.id.polyobjectid);
        String text = editText.getText().toString();

        Intent resultData = new Intent();

        if(text.length() > 0) {

            try {
                int polyObjectId = Integer.parseInt(text);

                Log.d(TAG,"polyObjectId: "+polyObjectId);

                resultData.putExtra(EXTRA_POLYOBJECTID, polyObjectId);
                setResult(Activity.RESULT_OK, resultData);

            }catch (NumberFormatException ex){

                Log.d(TAG,ex.toString());

                resultData.putExtra(EXTRA_POLYOBJECTID, -1);
                setResult(Activity.RESULT_CANCELED,resultData);
            }
        }else {

            resultData.putExtra(EXTRA_POLYOBJECTID, -1);
            setResult(Activity.RESULT_CANCELED,resultData);
        }

        finish();
    }

    public void buttonIdCancel(View view) {
        finish();
    }
}
