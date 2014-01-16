package tr.edu.iyte.ceng389.finalexamstuff;

import java.util.Arrays;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class StudentProvider extends ContentProvider
{
	// ------ DATABASE STUFF ----------
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_DEPARTMENT = "department";
	
	public static final String DATABASE_NAME = "student_db";
	public static final String TABLE_NAME = "student_table";
	public static final int DATABASE_VERSION = 1;
	
	private static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ COLUMN_NAME + " TEXT NOT NULL,"
			+ COLUMN_NUMBER + " INTEGER NOT NULL,"
			+ COLUMN_DEPARTMENT + " INTEGER NOT NULL);";
	
	public class StudentSQLOpenHelper extends SQLiteOpenHelper
	{
		public StudentSQLOpenHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(CREATE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
			db.execSQL(CREATE_SQL);
		}	
	}
	
	// ------- CONTENT PROVIDER STUFF ----------
	private static final String AUTHORITY = "com.foo.bar";
	private static final String CONTENT = "students";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT);
	
	private StudentSQLOpenHelper dbOpenHelper;
	
	// URI match codes for different types
	private static final int MULTIPLE_ROWS = 1;
	private static final int SINGLE_ROW = 2;

	// Static initialization of the UriMatcher to match the possible types of queries
	private static final UriMatcher uriMatcher;
	static
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, CONTENT, MULTIPLE_ROWS);
		uriMatcher.addURI(AUTHORITY, CONTENT + "/#", SINGLE_ROW);
	}
	
	@Override
	public boolean onCreate()
	{
		dbOpenHelper = new StudentSQLOpenHelper(getContext());
		
		return true;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		switch(uriMatcher.match(uri))
		{
			case MULTIPLE_ROWS:
				SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
				
				long id = db.insert(TABLE_NAME, null, values);
				
				if(id > -1)
				{
					Uri insertedUri = ContentUris.withAppendedId(CONTENT_URI, id);
					
					getContext().getContentResolver().notifyChange(uri, null, false);
					
					return insertedUri;
				}
				else
				{
					return null;
				}
			
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		
		switch(uriMatcher.match(uri))
		{
			case SINGLE_ROW:
				String newWhere = COLUMN_ID + "=?";
				selection = !TextUtils.isEmpty(selection) ? selection + " AND (" + newWhere + ")" : newWhere;
				
				String rowId = uri.getPathSegments().get(1);
				if(selectionArgs != null)
				{
					String[] newWhereArgs = Arrays.copyOf(selectionArgs, selectionArgs.length);
					newWhereArgs[newWhereArgs.length - 1] = rowId;
					selectionArgs = newWhereArgs;
				}
				else
				{
					selectionArgs = new String[] {rowId};
				}
				
				Cursor singleCursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
				singleCursor.setNotificationUri(getContext().getContentResolver(), uri);
				
				return singleCursor;
			
			case MULTIPLE_ROWS:
				Cursor allCursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
				allCursor.setNotificationUri(getContext().getContentResolver(), uri);
				
				return allCursor;
			
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		switch(uriMatcher.match(uri))
		{
			case SINGLE_ROW:
				SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
				
				String rowID = uri.getPathSegments().get(1);
				selection = COLUMN_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
				
				int updateCount = db.update(TABLE_NAME, values, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				
				return updateCount;
			
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int deleteCount = 0;
		
		switch(uriMatcher.match(uri))
		{
			case SINGLE_ROW:
				String rowID = uri.getPathSegments().get(1);
				selection = COLUMN_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
				
				deleteCount = db.delete(TABLE_NAME, selection, selectionArgs);
				break;
			
			case MULTIPLE_ROWS:
				deleteCount = db.delete(TABLE_NAME, "1=1", null);
				break;
			
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return deleteCount;
	}
	
	@Override
	public String getType(Uri uri)
	{
		switch(uriMatcher.match(uri))
		{
			case MULTIPLE_ROWS:
				return ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.foo.bar.students";
			
			case SINGLE_ROW:
				return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.foo.bar.student";
			
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
	
	public static ContentValues studentToContentValues(Student student)
	{
		if(student == null)
		{
			return null;
		}
		
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_NAME, student.getName());
		values.put(COLUMN_NUMBER, student.getNumber());
		values.put(COLUMN_DEPARTMENT, student.getDepartment().ordinal());
		
		return values;
	}
	
	public static Student cursorToStudent(Cursor cursor)
	{
		if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst())
		{
			long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
			String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
			int number = cursor.getInt(cursor.getColumnIndex(COLUMN_NUMBER));
			Student.Departments department = Student.Departments.values()[cursor.getInt(cursor.getColumnIndex(COLUMN_DEPARTMENT))];
			
			return new Student(id, name, number, department);
		}
		
		return null;
	}
}