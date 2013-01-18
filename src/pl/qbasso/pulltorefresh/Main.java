package pl.qbasso.pulltorefresh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.qbasso.pulltorefresh.PullToRefreshListView.PullToRefreshListner;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Channels;
import com.google.api.services.youtube.YouTube.Videos;
import com.google.api.services.youtube.model.SearchListResponse;

public class Main extends Activity {

	private List<String> items = new ArrayList<String>(
			Arrays.asList(new String[] { "b", "c", "d", "e", "f", "g", "h",
					"j", "i", "k", "m" }));
	private PullToRefreshListView mListView;
	private TestAdapter adapter;
	private PullToRefreshListner mListener = new PullToRefreshListner() {

		@Override
		public void onRefreshTriggered() {
			adapter = new TestAdapter(Main.this, R.layout.item, items);
			mListView.setAdapter(adapter);
			mListView.refreshDone();
		}

		@Override
		public void onItemRemoved(int pos) {
			items.remove(pos);
			adapter.notifyDataSetChanged();
		}
	};
	private Object response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					YouTube.Builder ytb = new YouTube.Builder(
//							new NetHttpTransport(), new AndroidJsonFactory(),
//							new BasicAuthentication("lujmen", "qpmz1@QPMZ"));
//					ytb.setApplicationName("qbasso-yt-test");
//					YouTube yt = ytb.build();
//					com.google.api.services.youtube.YouTube.Search.List videos;
//					videos = yt.search().list("parkour");
//					response = videos.execute();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		}).start();
		mListView = (PullToRefreshListView) findViewById(R.id.list_view);
		adapter = new TestAdapter(this, R.layout.item, items);
		mListView.setAdapter(adapter);
		mListView.setPullToRefreshListener(mListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
