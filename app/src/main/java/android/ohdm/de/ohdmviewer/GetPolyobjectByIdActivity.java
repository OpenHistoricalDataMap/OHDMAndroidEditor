package android.ohdm.de.ohdmviewer;

import android.app.Activity;
import android.content.Intent;
import android.ohdm.de.editor.R;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class GetPolyobjectByIdActivity extends Activity {

    private static final String TAG = "GetPolyobjectByIdActivity";
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

        if(text.length() > 0) {
            int polyObjectId = Integer.parseInt(text);

            Intent resultData = new Intent();
            resultData.putExtra(EXTRA_POLYOBJECTID, polyObjectId);
            setResult(Activity.RESULT_OK, resultData);
        }

        finish();
    }

    public void buttonIdCancel(View view) {
        finish();
    }
}
