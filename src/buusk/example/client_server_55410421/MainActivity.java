package buusk.example.client_server_55410421;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.ClipData.Item;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
//import app.example.testone_55410421.AddActivity421;
//import app.example.testone_55410421.MainActivity421;

public class MainActivity extends Activity {
	private ProgressDialog dialog;
	private ListView listview1;
	JSONPaser jPaser = new JSONPaser();
	ArrayList<HashMap<String, String>> studentList;
	private static String url_all_student = "http://www.sawasdeemall.com/android/show_student.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_STUDENT = "student";
	private static final String TAG_ID = "id";
	private static final String TAG_STUID = "stu_id";
	private static final String TAG_NAME = "name";
	private static final String TAG_TEL = "tel";
	JSONArray student = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview1 = (ListView) findViewById(R.id.listView1);

		studentList = new ArrayList<HashMap<String, String>>();
		new loadAllStudent().execute();
	}

	class loadAllStudent extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Loadting student. Plase wait... ");
			dialog.setIndeterminate(false);
			dialog.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			JSONObject json = jPaser.makeHttpRequest(url_all_student, "GET", pairs);
			Log.d("All Student", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					student = json.getJSONArray(TAG_STUDENT);
					for (int i = 0; i < student.length(); i++) {
						JSONObject c = student.getJSONObject(i);
						String id = c.getString(TAG_ID);
						String stu_id = c.getString(TAG_STUID);
						String name = c.getString(TAG_NAME);
						String tel = c.getString(TAG_TEL);

						HashMap<String, String> map = new HashMap<String, String>();
						map.put(TAG_ID, id);
						map.put(TAG_STUID, stu_id);
						map.put(TAG_NAME, name);
						map.put(TAG_TEL, tel);

						studentList.add(map);

					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ListAdapter adapter = new SimpleAdapter(MainActivity.this,
							studentList, R.layout.itemshow, new String[] {
									TAG_ID, TAG_STUID, TAG_NAME, TAG_TEL },
							new int[] { R.id.ColID, R.id.ColStuID,
									    R.id.ColName, R.id.ColTel});
					listview1.setAdapter(adapter);

				}

			});
		}
	}
}
