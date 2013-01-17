package pl.qbasso.pulltorefresh;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TestAdapter extends ArrayAdapter<String> {

	private List<String> items;
	private int resourceId;

	public TestAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		this.items = objects;
		this.resourceId = textViewResourceId;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			v = LayoutInflater.from(getContext()).inflate(resourceId, null);
		} else {
			v = convertView;
		}
		((TextView) v.findViewById(R.id.text1)).setText(items.get(position));
		return v;

	}

}
