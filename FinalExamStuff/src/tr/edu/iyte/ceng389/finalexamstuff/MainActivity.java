package tr.edu.iyte.ceng389.finalexamstuff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener
{
	private Button takePictureButton;
	private Button takePictureAndSaveButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		takePictureButton = (Button) findViewById(R.id.button_takePicture);
		takePictureAndSaveButton = (Button) findViewById(R.id.button_takePictureAndSave);
		
		takePictureButton.setOnClickListener(this);
		takePictureAndSaveButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.button_takePicture:
				startActivity(new Intent(this, TakePictureActivity.class));
				break;
				
			case R.id.button_takePictureAndSave:
				startActivity(new Intent(this, TakePictureActivity.class).putExtra("savePicture", true));
				break;
		}
	}
}