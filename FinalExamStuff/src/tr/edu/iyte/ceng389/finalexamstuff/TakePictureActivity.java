package tr.edu.iyte.ceng389.finalexamstuff;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class TakePictureActivity extends Activity implements OnClickListener
{
	private Button startButton;
	private ImageView imageView;
	
	private Uri outputUri;
	
	private static final int TAKE_PICTURE_REQUEST = 1;
	private static final int TAKE_PICTURE_AND_SAVE_REQUEST = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_picture);
		
		startButton = (Button) findViewById(R.id.button_startCamera);
		imageView = (ImageView) findViewById(R.id.imageView_cameraResult);
		
		startButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view)
	{
		boolean savePicture = false;
		
		Bundle extras = getIntent().getExtras();
		if(extras != null && extras.containsKey("savePicture"))
		{
			savePicture = extras.getBoolean("savePicture", false);
		}
		
		startCamera(savePicture);
	}
	
	private void startCamera(boolean savePicture)
	{
		try
		{
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			
			if(savePicture)
			{
				File outputFile = new File(Environment.getExternalStorageDirectory(), "picture" + System.currentTimeMillis() + ".jpg");
				outputUri = Uri.fromFile(outputFile);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
				
				startActivityForResult(cameraIntent, TAKE_PICTURE_AND_SAVE_REQUEST);
			}
			else
			{
				startActivityForResult(cameraIntent, TAKE_PICTURE_REQUEST);
			}
		}
		catch(Exception e)
		{
			Log.e("FinalExamStuff", "Error occurred while taking a picture.", e);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK)
		{
			try
			{
				if(requestCode == TAKE_PICTURE_REQUEST)
				{
					Bitmap thumbnail = data.getParcelableExtra("data");
					imageView.setImageBitmap(thumbnail);
				}
				else if(requestCode == TAKE_PICTURE_AND_SAVE_REQUEST)
				{
					int width = imageView.getWidth();
					int height = imageView.getHeight();
					
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(outputUri.getPath(), options);
					
					int imageWidth = options.outWidth;
					int imageHeight = options.outHeight;
					int scaleFactor = Math.min(imageWidth / width, imageHeight / height);
					
					options.inJustDecodeBounds = false;
					options.inSampleSize = scaleFactor;
					options.inPurgeable = true;
					
					Bitmap scaledBitmap = BitmapFactory.decodeFile(outputUri.getPath(), options);
					imageView.setImageBitmap(scaledBitmap);
				}
			}
			catch(Exception e)
			{
				Log.e("FinalExamStuff", "Error occurred while reading taken picture.", e);
			}
		}
	}
}