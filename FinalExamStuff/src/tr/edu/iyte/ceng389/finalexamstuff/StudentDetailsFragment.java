package tr.edu.iyte.ceng389.finalexamstuff;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

public class StudentDetailsFragment extends Fragment
{
	private EditText studentName;
	private EditText studentNumber;
	private Spinner studentDepartment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View layout = inflater.inflate(R.layout.student_form, container, false);
		
		studentName = (EditText) layout.findViewById(R.id.editText_name);
		studentNumber = (EditText) layout.findViewById(R.id.editText_number);
		studentDepartment = (Spinner) layout.findViewById(R.id.spinner_department);
		
		Bundle args = getArguments();
		if(args != null)
		{
			long id = args.getLong(StudentProvider.COLUMN_ID);
			
			String[] projection = new String[]
			{
				StudentProvider.COLUMN_ID,
				StudentProvider.COLUMN_NAME,
				StudentProvider.COLUMN_NUMBER,
				StudentProvider.COLUMN_DEPARTMENT
			};
			
			Cursor cursor = getActivity().getContentResolver().query(Uri.withAppendedPath(StudentProvider.CONTENT_URI, "" + id), projection, null, null, null);
			
			Student student = StudentProvider.cursorToStudent(cursor);
			if(student != null)
			{
				studentName.setText(student.getName());
				studentNumber.setText("" + student.getNumber());
				studentDepartment.setSelection(student.getDepartment().ordinal());
			}
		}
		
		return layout;
	}
	
	public Student getStudent()
	{
		String name = null;
		int number = -1;
		Student.Departments department = null;
		
		if(studentName != null)
		{
			name = studentName.getText().toString();
		}
		if(studentNumber != null)
		{
			number = Integer.parseInt(studentNumber.getText().toString());
		}
		if(studentDepartment != null)
		{
			department = Student.Departments.values()[studentDepartment.getSelectedItemPosition()];
		}
		
		if(name != null && number != -1 && department != null)
			return new Student(-1, name, number, department);
		else
			return null;
	}
}