package tr.edu.iyte.ceng389.finalexamstuff;

import tr.edu.iyte.ceng389.finalexamstuff.StudentAdapter.Holder;
import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class StudentListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener
{
	private StudentAdapter adapter;
	
	private OnStudentSelectedListener listener;
	
	public interface OnStudentSelectedListener
	{
		public void onStudentSelected(long id);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		try
		{
			listener = (OnStudentSelectedListener) activity;
		}
		catch(Exception e)
		{
			throw new ClassCastException("Activity must implement OnStudentSelectedListener!");
		}
		
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		adapter = new StudentAdapter(getActivity());
		setListAdapter(adapter);
		getListView().setOnItemClickListener(this);
		
		getLoaderManager().initLoader(1, null, this);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		String[] projection = new String[]
		{
			StudentProvider.COLUMN_ID,
			StudentProvider.COLUMN_NAME,
			StudentProvider.COLUMN_NUMBER,
			StudentProvider.COLUMN_DEPARTMENT
		};
		
		return new CursorLoader(getActivity(), StudentProvider.CONTENT_URI, projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
		adapter.swapCursor(null);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
	{
		StudentAdapter.Holder holder = (Holder) view.getTag();
		listener.onStudentSelected(holder.id);
	}
}