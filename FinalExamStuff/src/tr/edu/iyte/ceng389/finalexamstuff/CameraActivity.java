package tr.edu.iyte.ceng389.finalexamstuff;

import android.app.Activity;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class CameraActivity extends Activity
{
	private Camera camera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		if(camera != null)
		{
			camera.release();
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		new CameraOpenHelper(camera).execute();
	}
	
	class CameraOpenHelper extends AsyncTask<Void, Void, Void>
	{
		private Camera camera;
		
		public CameraOpenHelper(Camera camera)
		{
			this.camera = camera;
		}
		
		@Override
		protected Void doInBackground(Void... args)
		{
			camera = Camera.open();
			
			Camera.Parameters parameters = camera.getParameters();
			
			if(parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
			{
				camera.autoFocus(new Camera.AutoFocusCallback()
				{
					@Override
					public void onAutoFocus(boolean success, Camera camera)
					{
						Log.d("FinalExamStuff", "Camera auto focus status: " + success);
					}
				});
			}
			
			return null;
		}		
	}
}