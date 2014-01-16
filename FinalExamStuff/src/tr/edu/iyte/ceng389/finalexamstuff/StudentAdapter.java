package tr.edu.iyte.ceng389.finalexamstuff;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class StudentAdapter extends CursorAdapter
{
	public static class Holder
	{
		public long id;
		public TextView name;
	}
	
	private LayoutInflater inflater;
	
	public StudentAdapter(Context context)
	{
		super(context, null, false);
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		Holder holder = (Holder) view.getTag();
		
		int idColumnIndex = cursor.getColumnIndex(StudentProvider.COLUMN_ID);
		int nameColumnIndex = cursor.getColumnIndex(StudentProvider.COLUMN_NAME);
		
		long id = cursor.getLong(idColumnIndex);
		String name = cursor.getString(nameColumnIndex);
		
		holder.id = id;
		holder.name.setText(name);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		Holder holder = new Holder();
		
		holder.name = (TextView) view.findViewById(android.R.id.text1);
		
		view.setTag(holder);
		
		return view;
	}
}