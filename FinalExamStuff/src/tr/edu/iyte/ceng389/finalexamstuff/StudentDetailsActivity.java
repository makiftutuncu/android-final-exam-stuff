package tr.edu.iyte.ceng389.finalexamstuff;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class StudentDetailsActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.containsKey(StudentProvider.COLUMN_ID))
		{
			long id = extras.getLong(StudentProvider.COLUMN_ID);
			
			Bundle args = new Bundle();
			args.putLong(StudentProvider.COLUMN_ID, id);
			StudentDetailsFragment detailsFragment = (StudentDetailsFragment) StudentDetailsFragment.instantiate(getApplicationContext(), StudentDetailsFragment.class.getName(), args);
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.frameLayout_studentDetails, detailsFragment);
			transaction.commit();
		}
	}
}