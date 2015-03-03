package android.ohdm.de.ohdmviewer;

import android.app.Activity;
import android.content.Intent;
import android.ohdm.de.editor.R;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ShowPolyObjectDataActivity extends Activity {

    private static final String TAG = "EditPolyObjectData";
    private static final int EDIT_DATA_REQUEST_CODE = 1749;

    public static final String DATA_KEY = "data_key";
    public static final String DATA_VALUE = "data_value";

    private HashMap<String, String> polyObjectData = new HashMap<String, String>();
    private UUID polyObjectInternId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_poly_object_data);

        ListView listView = (ListView) findViewById(R.id.listView);
        View footer = getLayoutInflater().inflate(R.layout.activity_show_poly_object_data_footer, null);
        listView.addFooterView(footer);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            polyObjectData = (HashMap) bundle.getSerializable(MainActivity.MAP_DATA);
            HashMapAdapter adapter = new HashMapAdapter(polyObjectData);
            listView.setAdapter(adapter);

            polyObjectInternId = (UUID) bundle.getSerializable(MainActivity.EXTRA_SELECTED_POLYOBJECT_INTERNID);
        }
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

    public void buttonShowOk(View view) {

        Intent resultData = new Intent();

        for(Map.Entry<String,String> entry : polyObjectData.entrySet()){
            Log.d(TAG, entry.getKey() + " : " + entry.getValue());
        }

        resultData.putExtra(MainActivity.MAP_DATA, polyObjectData);

        Log.d(TAG,"selected polybject intern id: "+polyObjectInternId.toString());

        resultData.putExtra(MainActivity.EXTRA_SELECTED_POLYOBJECT_INTERNID,polyObjectInternId);
        setResult(Activity.RESULT_OK, resultData);

        finish();

    }

    public void buttonShowCancel(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == EDIT_DATA_REQUEST_CODE) {

            String dataKey = data.getStringExtra(DATA_KEY);
            String dataValue = data.getStringExtra(DATA_VALUE);

            polyObjectData.put(dataKey, dataValue);
            updateAdapterAndRefreshView();
        }
    }

    public void buttonShowAdd(View view) {

        Intent intent = new Intent(this, EditPolyObjectDataActivity.class);
        startActivityForResult(intent, EDIT_DATA_REQUEST_CODE);
    }

    public void buttonEditDataRowDelete(View view){

        Map.Entry<String,String> item = getItemFromRow(view);

        if(item != null) {
            polyObjectData.remove(item.getKey());
            updateAdapterAndRefreshView();
        }
    }

    private void updateAdapterAndRefreshView(){
        ListView listView = (ListView) findViewById(R.id.listView);
        HashMapAdapter adapter = new HashMapAdapter(polyObjectData);

        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        listView.invalidate();
    }

    public void buttonEditDataRow(View view){
        Map.Entry<String,String> item = getItemFromRow(view);

        Intent intent = new Intent(this, EditPolyObjectDataActivity.class);
        intent.putExtra(DATA_KEY,item.getKey().toString());
        intent.putExtra(DATA_VALUE,item.getValue());
        startActivityForResult(intent, EDIT_DATA_REQUEST_CODE);
    }

    private Map.Entry<String,String> getItemFromRow(View rowView){
        ListView listView = (ListView) findViewById(R.id.listView);

        for(int i=0; i< listView.getChildCount();i++){
            if(listView.getChildAt(i).equals(rowView.getParent())){
                int offset = listView.getFirstVisiblePosition();
                Map.Entry<String,String> item = (Map.Entry)listView.getItemAtPosition(i + offset);

                return item;
            }
        }

        return null;
    }
}
