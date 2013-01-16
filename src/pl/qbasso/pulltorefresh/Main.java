package pl.qbasso.pulltorefresh;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

public class Main extends Activity {

	private PullToRefreshListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mListView = (PullToRefreshListView) findViewById(R.id.list_view);
		mListView
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, new String[] {
								"a", "b", "c", "d", "e", "f", "g", "h", "j",
								"i", "k", "m", "a" }));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
