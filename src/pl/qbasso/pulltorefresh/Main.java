package pl.qbasso.pulltorefresh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.qbasso.pulltorefresh.PullToRefreshListView.PullToRefreshListner;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;

public class Main extends Activity {
	String authToken;
	private static JsonFactory factory = new JacksonFactory();
	private static NetHttpTransport transport = new NetHttpTransport();
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
	protected String accountName;
//	private AccountManagerCallback<Bundle> mCallback = new AccountManagerCallback<Bundle>() {
//
//		@Override
//		public void run(AccountManagerFuture<Bundle> arg0) {
//			Bundle b = null;
//			try {
//				b = arg0.getResult();
//			} catch (OperationCanceledException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (AuthenticatorException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if (b.containsKey(AccountManager.KEY_AUTHTOKEN)) {
//				authToken = b.getString(AccountManager.KEY_AUTHTOKEN);
//				accountName = b.getString(AccountManager.KEY_ACCOUNT_NAME);
//			}
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					try {
//						GoogleCredential cr = new GoogleCredential()
//								.setAccessToken(authToken);
//						YouTube.Builder ytb = new YouTube.Builder(transport,
//								factory, null);
//						ytb.setApplicationName("qbasso-yt-test")
//								.setYouTubeRequestInitializer(
//										new YouTubeRequestInitializer("AIzaSyDh9s5_FPdAptP895z2ffNP-zQUgWOSgbc"));// {
////
////											@Override
////											protected void initializeYouTubeRequest(
////													YouTubeRequest<?> arg0)
////													throws IOException {
////												arg0.setKey("924735525603-j6tkqahovdm044k6er77dces44chubaj.apps.googleusercontent.com");
////												arg0.setOauthToken(authToken);
////											}
////										});
//						YouTube yt = ytb.build();
//						com.google.api.services.youtube.YouTube.Search.List videos;
////						GoogleHeaders headers = new GoogleHeaders();
//						videos = yt.search().list("parkour");
////						 videos.setOauthToken(authToken);
////						headers.setAuthorization(authToken);
////						headers.setGDataKey("AIzaSyDh9s5_FPdAptP895z2ffNP-zQUgWOSgbc");
////						headers.setApplicationName("qbasso-yt-test");
////						videos.setRequestHeaders(headers);
//						HttpRequest request = videos.buildHttpRequest();
//						response = videos.execute();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//			}).start();
//
//		}
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		AccountManager am = (AccountManager) getSystemService(Activity.ACCOUNT_SERVICE);
//		Account[] accounts = am.getAccountsByType("com.google");
//		am.getAuthToken(accounts[0], "youtube", null, this, mCallback, null);
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
