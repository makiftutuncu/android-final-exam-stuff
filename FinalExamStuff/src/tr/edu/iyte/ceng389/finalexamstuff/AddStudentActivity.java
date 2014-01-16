package tr.edu.iyte.ceng389.finalexamstuff;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddStudentActivity extends Activity
{
	private StudentDetailsFragment detailsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		detailsFragment = (StudentDetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_studentDetails);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.save_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.save_student:
				if(detailsFragment != null)
				{
					Student student = detailsFragment.getStudent();
					
					if(student != null)
					{
						// Save to database
						Uri newStudent = getContentResolver().insert(StudentProvider.CONTENT_URI, StudentProvider.studentToContentValues(student));
						
						finish();
						
						return true;
					}
				}
		}
		
		return false;
	}
}