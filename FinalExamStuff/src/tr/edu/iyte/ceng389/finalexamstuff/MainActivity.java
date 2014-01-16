package tr.edu.iyte.ceng389.finalexamstuff;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity implements StudentListFragment.OnStudentSelectedListener
{
	private boolean isTwoPane = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		View detailView = findViewById(R.id.frameLayout_studentDetails);
		isTwoPane = (detailView != null && detailView.getVisibility() == View.VISIBLE);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.add_student:
				startActivity(new Intent(this, AddStudentActivity.class));
				return true;
		}
		
		return false;
	}

	@Override
	public void onStudentSelected(long id)
	{
		if(isTwoPane)
		{
			Bundle args = new Bundle();
			args.putLong(StudentProvider.COLUMN_ID, id);
			StudentDetailsFragment detailsFragment = (StudentDetailsFragment) StudentDetailsFragment.instantiate(getApplicationContext(), StudentDetailsFragment.class.getName(), args);
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.frameLayout_studentDetails, detailsFragment);
			transaction.commit();
		}
		else
		{
			Intent intent = new Intent(this, StudentDetailsActivity.class);
			intent.putExtra(StudentProvider.COLUMN_ID, id);
			startActivity(intent);
		}
	}
}