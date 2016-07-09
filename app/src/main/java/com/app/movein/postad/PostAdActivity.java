package com.app.movein.postad;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.app.movein.R;
import com.app.movein.postad.AndroidMultiPartEntity.ProgressListener;
import com.app.movein.postad.utils.AudioPlay;
import com.app.movein.postad.utils.IRadioAdapterData;
import com.app.movein.postad.utils.RadioAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

public class PostAdActivity extends Activity implements View.OnClickListener,
		IRadioAdapterData {
	private Spinner mPreferenceSpinner; 
	private TextView mArea, mMainArea;
	private String[] mFlat = { "Television", "Fridge", "Wi-Fi",
			"Washing Machine", "AC", "Geyser", "Bed", "Mattress", "Wardrobe",
			"Cook" };
	private String[] mBuilding = { "Elevator", "Security Guard", "Parking",
			"Gym", "Pool", "Club", "Power Backup", "24/7 Water Supply" };
	private String[] mRadioFinalValues, mFlatAminitiesFinalValues,
			mBuildingAminitiesFinalValues;
	private TextView mPhotos, mTakePhotos, mDatePicker, mAudioAlbumTitle;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	private static final int CAMERA_CAPTURE_AUDIO_REQUEST_CODE = 300;
	private static final int SELECT_IMAGE = 101;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int MEDIA_TYPE_AUDIO = 3;
	public static final int IMAGE_MULTIPLE_PICS = 102;
	public static final int IMAGE_PIC_ARRAY = 103;
	public static final int AUDIO_FILE_CHOOSER = 104;
	public static final int VIDEO_FILE_CHOOSER = 105;
	private Uri fileUri;
	String mRentVal;
	private String mDepositVal;
	private CheckBox mRentNegotiable, mDepositNegotiable, mContactNumberCheck;
	private EditText mRent, mDeposit, mDescription, mFlatAminitiesOthers,
			mBuildingAminitiesOthers, mContactNumber,mAminitiesFlat,mAminitiesBuilding;
	@SuppressWarnings("deprecation")
	private Gallery mGallery;
	String[] images;
	Context mContext;
	public ArrayList<String> mImgPaths = new ArrayList();
	RadioGroup mRadioLook, mRadioContactNumber;
	private static String mContactNumberRadio;
	RadioGroup mRadioNo;
	int lastSelected;
	private PicAdapter mPicAdapter;
	private Button mAudioUpload, mAudioRecord, mVideoUpload, mVideoRecord,
			mBtnSubmit, mAddFlatOthers, mAddBuildingOthers, mAddedTextOthers1,
			mDeleteOthers1, mAddedTextOthers2, mDeleteOthers2,
			mAddedTextOthers3, mDeleteOthers3, mAddedTextOthers4,
			mDeleteOthers4, mAddedTextOthers5, mDeleteOthers5,
			mAddedTextOthers6, mDeleteOthers6, mAddedTextOthers7,
			mDeleteOthers7, mAddedTextOthers8, mDeleteOthers8;
	private ImageView mAudioPreview, mVideoPreview;
	private Thread mVideoThumbnail, mAudioThumbnail;
	private VideoView mVideo;
	private String mAudioFilePath, mVideoFilePath;
	private static String mToggleLookValue, mToggleFlatNoValue;
	private int count;
	private boolean doubleBackToExitPressedOnce = false;
	private String mAddressString, mMainAreaString, mRadioAdapterFinal;
	private View mAudioFragmentView;
	private AudioPlay mAudioFragment;
	private ProgressBar progressBar;
	private long totalSize = 0;
	private FrameLayout mFrameOthers1, mFrameOthers2, mFrameOthers3,
			mFrameOthers4, mFrameOthers5, mFrameOthers6, mFrameOthers7,
			mFrameOthers8;
	private AsyncHttpClient mAsyncHttpClient;
	private StringBuilder mFlatValueOthers = new StringBuilder();
	private StringBuilder mBuildingValueOthers = new StringBuilder();
	private StringBuilder mBuildingAminitiesValue;
	private StringBuilder mFlatAminitiesValue;
	private AlertDialog mFlatDialog,mBuildingDialog;
	 private  AlertDialog.Builder mFlatbuilder,mBuildingBuilder;
	 private String  tv="0",fridge="0",ac="0",washing_machine="0",geyser="0",wifi="0",beds="0",wardrobes="0",cook="0";
	 private String  elevator="0",security="0",parking="0",gym="0",sports_club="0",pool="0",power_backup="0",water_supply="0";
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.postads);
		mAsyncHttpClient = new AsyncHttpClient();
		mContext = this;
		// Start of area and main area
		Bundle mBundle = getIntent().getExtras();
		String mLocality = mBundle.getString("locality");
		String mSubLocality = mBundle.getString("sub_locality");

		// final String mMainAreaString=mSubLocality + mLocality;
		mAddressString = mBundle.getString("address");
		mArea = (TextView) findViewById(R.id.postaddress);
		mArea.setText(mAddressString);
		mArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// i.putExtra("main_area", mMainAreaString);
				i.putExtra("area", mAddressString);
				setResult(101, i);
				finish();
				// TODO Auto-generated method stub

			}
		});
		mMainArea = (TextView) findViewById(R.id.postlocality);
		mMainArea.setText(mSubLocality + "," + mLocality);

		mMainArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// i.putExtra("main_area", mMainAreaString);
				i.putExtra("area", mAddressString);
				setResult(101, i);
				finish();

				// TODO Auto-generated method stub

			}
		});
		// End of area and main area
		// Start ofspinner for preference and aminities
		mPreferenceSpinner = (Spinner) findViewById(R.id.mpreferencespinner);
		mPreferenceSpinner.setAdapter(new RadioAdapter(this, this));
	/*	mAminitiesFlat=(EditText) findViewById(R.id.maminitiesflat);
		 		mAminitiesFlat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 final ArrayList<Integer> seletedItems=new ArrayList<Integer>();
			       
				 mFlatbuilder = new AlertDialog.Builder(PostAdActivity.this);

			        mFlatbuilder.setTitle("Select Flat Aminities");
			        mFlatbuilder.setMultiChoiceItems(mFlat, null,
			                new DialogInterface.OnMultiChoiceClickListener() {
			         // indexSelected contains the index of item (of which checkbox checked)
			         @SuppressWarnings("unchecked")
					@Override
			         public void onClick(DialogInterface dialog, int indexSelected,
			                 boolean isChecked) {
			             if (isChecked) {
			               
			                 seletedItems.add(indexSelected);
			              
			                 
			             } else if (seletedItems.contains(indexSelected)) {
			                 // Else, if the item is already in the array, remove it 
			                 // write your code when user Uchecked the checkbox 
			                 seletedItems.remove(Integer.valueOf(indexSelected));
			             }
			         }
			     })
			      // Set the action buttons
			     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			         @Override
			         public void onClick(DialogInterface dialog, int id) {
			        	ListView list=((AlertDialog)mFlatDialog).getListView();
			        	 mFlatAminitiesValue = new StringBuilder();
			        	 for(int i=0;i<list.getCount();i++){
			        	 boolean checked=list.isItemChecked(i);
			        	 if(checked)
			        	 {
			        		if(mFlatAminitiesValue.length() >0)
			        			 mFlatAminitiesValue.append(",");
			        		 mFlatAminitiesValue.append(list.getItemAtPosition(i));
			        		 if(list.getItemAtPosition(i).toString().equalsIgnoreCase("tv"))
			        		 {
			        			 tv="1";
			        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("fridge"))
			        		 {
			        			 fridge="1";
			        			 
			        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("ac"))
			        		 {
			        			 ac="1";
			        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("washing machine"))
			        		 {
			        			 washing_machine="1";
			        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("geyser"))
			        		 {
			        			geyser="1"; 
			        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("wifi"))
			        		 {
			        			 wifi="1";
			        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("beds"))
			        		 {
			        			 beds="1";
			        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("wardrobes"))
			        		 {
			        			 wardrobes="1";
			        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("cook"))
			        		 {
			        			 cook="1";
			        		 }
			        		 
			        	 }
			        	 }
			        	 
			        	 mAminitiesFlat.setText(mFlatAminitiesValue.toString());
			             //  Your code when user clicked on OK
			             //  You can write the code  to save the selected item here
			            
			         }
			     })
			     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			         @Override
			         public void onClick(DialogInterface dialog, int id) {
			        	 dialog.dismiss();
			            //  Your code when user clicked on Cancel
			           
			         }
			     });

			        mFlatDialog = mFlatbuilder.create();//AlertDialog dialog; create like this outside onClick
			        mFlatDialog.show();
				// TODO Auto-generated method stub
				
			}
			
		});*/
		   
		/*mAminitiesBuilding=(EditText) findViewById(R.id.maminitiesbuilding);
		 
		mAminitiesBuilding.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 final ArrayList<Integer> seletedItems=new ArrayList<Integer>();
			       
				 mBuildingBuilder = new AlertDialog.Builder(PostAdActivity.this);
			        mBuildingBuilder.setTitle("Select The Difficulty Level");
			        mBuildingBuilder.setMultiChoiceItems(mBuilding, null,
			                new DialogInterface.OnMultiChoiceClickListener() {
			         // indexSelected contains the index of item (of which checkbox checked)
			         @SuppressWarnings("unchecked")
					@Override
			         public void onClick(DialogInterface dialog, int indexSelected,
			                 boolean isChecked) {
			             if (isChecked) {
			                 // If the user checked the item, add it to the selected items
			                 // write your code when user checked the checkbox 
			                 seletedItems.add(indexSelected);
			                // mBuildingValues.append(seletedItems.get(indexSelected));
			                 
			             } else if (seletedItems.contains(indexSelected)) {
			                 // Else, if the item is already in the array, remove it 
			                 // write your code when user Uchecked the checkbox 
			                 seletedItems.remove(Integer.valueOf(indexSelected));
			             }
			         }
			     })
			      // Set the action buttons
			     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			         @Override
			         public void onClick(DialogInterface dialog, int id) {

				        	ListView list=((AlertDialog)mBuildingDialog).getListView();
				        	 mBuildingAminitiesValue = new StringBuilder();
				        	 for(int i=0;i<list.getCount();i++){
				        	 boolean checked=list.isItemChecked(i);
				        	 if(checked)
				        	 {
				        		 if(mBuildingAminitiesValue.length() >0)
				        			 mBuildingAminitiesValue.append(",");
				        		 mBuildingAminitiesValue.append(list.getItemAtPosition(i));
				        		 if(list.getItemAtPosition(i).toString().equalsIgnoreCase("elevator"))
				        		 {
				        			 elevator="1";
				        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("security"))
				        		 {
				        			 security="1";
				        			 
				        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("parking"))
				        		 {
				        			 parking="1";
				        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("gym"))
				        		 {
				        			 gym="1";
				        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("sports_club"))
				        		 {
				        			 sports_club="1";
				        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("pool"))
				        		 {
				        			 pool="1";
				        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("power_backup"))
				        		 {
				        			 power_backup="1";
				        		 }else if(list.getItemAtPosition(i).toString().equalsIgnoreCase("water_supply"))
				        		 {
				        			 water_supply="1";
				        		 }
				        		 
				        	 }
				        	 }
				        	 
				        	 mAminitiesBuilding.setText(mBuildingAminitiesValue.toString());
				             //  Your code when user clicked on OK
				             //  You can write the code  to save the selected item here
				            
				         
			            
			         }
			     })
			     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			         @Override
			         public void onClick(DialogInterface dialog, int id) {
			        	 dialog.dismiss();
			            //  Your code when user clicked on Cancel
			           
			         }
			     });

			        mBuildingDialog = mBuildingBuilder.create();//AlertDialog dialog; create like this outside onClick
			        mBuildingDialog.show();   
				// TODO Auto-generated method stub
				
			}
		});
		
*/

		
		// End of spinner for preference nd aminities
		// Start of toggle button(looking for)
		mRadioLook = (RadioGroup) findViewById(R.id.toggleGrouplooking);
		ToggleButton mToggleLook = (ToggleButton) this.mRadioLook.getChildAt(0);
		onToggleLook(mToggleLook);
		mToggleLook.setChecked(true);

		mRadioLook.setOnCheckedChangeListener(ToggleListenerLook);

		// End of toggle button(looking for)
		// Start of Flatmates needed
		mRadioNo = (RadioGroup) findViewById(R.id.toggleGroupfaltmatesno_);
		ToggleButton mToggleNum = (ToggleButton) this.mRadioNo.getChildAt(0);
		onToggleNum(mToggleNum);
		mToggleNum.setChecked(true);

		mRadioNo.setOnCheckedChangeListener(ToggleListenerFlatNo);

		// End of Flatmates needed
		// start of photos
		mPhotos = (TextView) findViewById(R.id.add_picsfromgallery);

		mPhotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(photoPickerIntent, SELECT_IMAGE);

				// TODO Auto-generated method stub

			}
		});
		mTakePhotos = (TextView) findViewById(R.id.takepic);
		mTakePhotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				captureImage();

				// TODO Auto-generated method stub

			}
		});
		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			finish();
		}

		// end of photos
		// start of deposit and rent values
		mRent = (EditText) findViewById(R.id.rent_val);

		mDeposit = (EditText) findViewById(R.id.deposit_val);
		mRentVal = mRent.getText().toString();
		mDepositVal = mDeposit.getText().toString();
		// end of deposit and rent values
		mDescription = (EditText) findViewById(R.id.description);
		mDatePicker = (TextView) findViewById(R.id.date_text);
		mDatePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);
				System.out.println("the selected " + mDay);
				DatePickerDialog dialog = new DatePickerDialog(
						PostAdActivity.this, new mDateSetListener(), mYear,
						mMonth, mDay);
				dialog.show();
				// TODO Auto-generated method stub

			}
		});
		// Start of image gallery
		this.mPicAdapter = new PicAdapter(this);

		for (int m = 0; m < 1; m++)
			this.mImgPaths.add(m, "");
		this.mPicAdapter.notifyDataSetChanged();
		mGallery = (Gallery) findViewById(R.id.gallery);
		mGallery.setAdapter(this.mPicAdapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(getBaseContext(),
						"pic" + (position + 1) + " selected",
						Toast.LENGTH_SHORT).show();
				if (mImgPaths.get(position).equals("")) {
					Intent photoPickerIntent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

					startActivityForResult(photoPickerIntent, SELECT_IMAGE);
				} else {
					Log.i("MI", "in else mGallery " + mImgPaths.get(position));
					Intent intent = new Intent(
							PostAdActivity.this,
							com.app.movein.postad.utils.ImageViewerActivity.class);
					intent.putExtra("FilePath", mImgPaths.get(position));
					startActivity(intent);

				}

			}
		});
		mGallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mImgPaths.remove(arg2);
				mPicAdapter.notifyDataSetChanged();
				// TODO Auto-generated method stub
				return false;
			}

		});
		// End of image gallery
		// Start of audio upload
		mAudioRecord = (Button) findViewById(R.id.postaudiorecord);
		mAudioRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				recordAudio();
				// TODO Auto-generated method stub

			}
		});
		mAudioUpload = (Button) findViewById(R.id.postaudioupload);
		mAudioUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(
						Intent.createChooser(intent, "Complete action using"),
						AUDIO_FILE_CHOOSER);
				// TODO Auto-generated method stub

			}
		});
		mAudioAlbumTitle = (TextView) findViewById(R.id.postaudioalbumtitle);
		mAudioAlbumTitle.setText("No Title");
		mAudioPreview = (ImageView) findViewById(R.id.postaudiopreview);

		mAudioPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAudioFilePath == null) {
					Toast.makeText(getApplicationContext(),
							"PLease upload an audio", 0).show();

				} else {
					Intent intent = new Intent(PostAdActivity.this,
							com.app.movein.postad.utils.AudioPlay.class);
					intent.putExtra("FilePath", mAudioFilePath);
					startActivity(intent);

				}
				// TODO Auto-generated method stub

			}
		});

		// end of audio upload
		// Start of video upload

		mVideoRecord = (Button) findViewById(R.id.postvideorecord);
		mVideoRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				recordVideo();
				// TODO Auto-generated method stub

			}
		});
		mVideoUpload = (Button) findViewById(R.id.postvideoupload);
		mVideoUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(
						Intent.createChooser(intent, "Complete action using"),
						VIDEO_FILE_CHOOSER);

				// TODO Auto-generated method stub

			}
		});
		mVideoPreview = (ImageView) findViewById(R.id.postvideopreview);
		mVideoPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mVideoFilePath == null) {
					Toast.makeText(getApplicationContext(),
							"PLease upload a video", 0).show();
				} else {
					Intent i = new Intent(PostAdActivity.this,
							com.app.movein.postad.utils.VideoPlay.class);
					i.putExtra("VideoPath", mVideoFilePath);
					startActivity(i);
				}
				// TODO Auto-generated method stub

			}
		});
		// end of video upload
		// Start of submit button
		mBtnSubmit = (Button) findViewById(R.id.postsubmit);
		progressBar = (ProgressBar) findViewById(R.id.postprogressBar);
		mBtnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (mAddressString == null) {
					Toast.makeText(mContext, "Address can't be null", 0).show();
				} else if (mRentVal == null) {
					Toast.makeText(mContext, "Please enter rent value", 0).show();
						
				} else if (mDepositVal == null) {
					Toast.makeText(mContext, "Please enter deposit value", 0).show();
						

				} else if (mContactNumber.getText().toString() == null) {
					Toast.makeText(mContext, "Mobile Number can't be null", 0).show();
							
				} else if (mToggleLookValue == null) {
					Toast.makeText(mContext, "Please specify gender", 0).show();
				} else if (mToggleFlatNoValue == null) {
					Toast.makeText(mContext,
							"Please choose number of flatmates", 0).show();
				} else if (mDatePicker.getText().toString() == null) {
					Toast.makeText(mContext, "Date can't be null", 0).show();
				}else if(mContactNumberRadio==null)
				{
					Toast.makeText(mContext, "Slect the visibility mode for contact number", 0).show();
					
				}else {
				

					new UploadFileToServer().execute();
				}

				// TODO Auto-generated method stub

			}
		});

		// End of Submit Button
		// Start of Flat Others
		mFrameOthers1 = (FrameLayout) findViewById(R.id.postframeothers1);
		mAddedTextOthers1 = (Button) findViewById(R.id.postaddedothers1);
		mDeleteOthers1 = (Button) findViewById(R.id.postdeleteothers1);
		mFrameOthers2 = (FrameLayout) findViewById(R.id.postframeothers2);
		mAddedTextOthers2 = (Button) findViewById(R.id.postaddedothers2);
		mDeleteOthers2 = (Button) findViewById(R.id.postdeleteothers2);
		mFrameOthers3 = (FrameLayout) findViewById(R.id.postframeothers3);
		mAddedTextOthers3 = (Button) findViewById(R.id.postaddedothers3);
		mDeleteOthers3 = (Button) findViewById(R.id.postdeleteothers3);
		mFrameOthers4 = (FrameLayout) findViewById(R.id.postframeothers4);
		mAddedTextOthers4 = (Button) findViewById(R.id.postaddedothers4);
		mDeleteOthers4 = (Button) findViewById(R.id.postdeleteothers4);
		mFlatAminitiesOthers = (EditText) findViewById(R.id.postaminitiesflatothers);

		mAddFlatOthers = (Button) findViewById(R.id.postaminitiesflataddothers);
		mAddFlatOthers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mFlatAminitiesOthers.getText().toString() == null) {
					Toast.makeText(mContext, "Enter a Text", 0).show();
				} else {
					if (mFrameOthers1.getVisibility() == View.GONE) {
						mFrameOthers1.setVisibility(View.VISIBLE);
						mAddedTextOthers1.setText(mFlatAminitiesOthers
								.getText().toString());
						mFlatValueOthers.append(mFlatAminitiesOthers.getText()
								.toString());
					} else if (mFrameOthers2.getVisibility() == View.GONE) {
						mFrameOthers2.setVisibility(View.VISIBLE);
						mAddedTextOthers2.setText(mFlatAminitiesOthers
								.getText().toString());
						mFlatValueOthers.append(mFlatAminitiesOthers.getText()
								.toString() + ",");
					} else if (mFrameOthers3.getVisibility() == View.GONE) {
						mFrameOthers3.setVisibility(View.VISIBLE);
						mAddedTextOthers3.setText(mFlatAminitiesOthers
								.getText().toString());
						mFlatValueOthers.append(mFlatAminitiesOthers.getText()
								.toString() + ",");
					} else if (mFrameOthers4.getVisibility() == View.GONE) {
						mFrameOthers4.setVisibility(View.VISIBLE);
						mAddedTextOthers4.setText(mFlatAminitiesOthers
								.getText().toString());
						mFlatValueOthers.append(mFlatAminitiesOthers.getText()
								.toString());
					} else {
						Toast.makeText(mContext, "Max 4 are allowed", 0).show();
					}

				}

				// TODO Auto-generated method stub

			}
		});
		mDeleteOthers1.setOnClickListener(this);
		mDeleteOthers2.setOnClickListener(this);
		mDeleteOthers3.setOnClickListener(this);
		mDeleteOthers4.setOnClickListener(this);

		// End of Flat Others
		// Start of building others
		mFrameOthers5 = (FrameLayout) findViewById(R.id.postframeothers5);
		mAddedTextOthers5 = (Button) findViewById(R.id.postaddedothers5);
		mDeleteOthers5 = (Button) findViewById(R.id.postdeleteothers5);
		mFrameOthers6 = (FrameLayout) findViewById(R.id.postframeothers6);
		mAddedTextOthers6 = (Button) findViewById(R.id.postaddedothers6);
		mDeleteOthers6 = (Button) findViewById(R.id.postdeleteothers6);
		mFrameOthers7 = (FrameLayout) findViewById(R.id.postframeothers7);
		mAddedTextOthers7 = (Button) findViewById(R.id.postaddedothers7);
		mDeleteOthers7 = (Button) findViewById(R.id.postdeleteothers7);
		mFrameOthers8 = (FrameLayout) findViewById(R.id.postframeothers8);
		mAddedTextOthers8 = (Button) findViewById(R.id.postaddedothers8);
		mDeleteOthers8 = (Button) findViewById(R.id.postdeleteothers8);
		mBuildingAminitiesOthers = (EditText) findViewById(R.id.postaminitiesbuildingothers);

		mAddBuildingOthers = (Button) findViewById(R.id.postaminitiesbuildingaddothers);
		mAddBuildingOthers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mBuildingAminitiesOthers.getText().toString() == null) {
					Toast.makeText(mContext, "Enter a Text", 0).show();
				} else {
					if (mFrameOthers5.getVisibility() == View.GONE) {
						mFrameOthers5.setVisibility(View.VISIBLE);
						mAddedTextOthers5.setText(mBuildingAminitiesOthers
								.getText().toString());
						mBuildingValueOthers.append(mBuildingAminitiesOthers
								.getText().toString() + ",");
					} else if (mFrameOthers6.getVisibility() == View.GONE) {
						mFrameOthers6.setVisibility(View.VISIBLE);
						mAddedTextOthers6.setText(mBuildingAminitiesOthers
								.getText().toString());
						mBuildingValueOthers.append(mBuildingAminitiesOthers
								.getText().toString() + ",");
					} else if (mFrameOthers7.getVisibility() == View.GONE) {
						mFrameOthers7.setVisibility(View.VISIBLE);
						mAddedTextOthers7.setText(mBuildingAminitiesOthers
								.getText().toString());
						mBuildingValueOthers.append(mBuildingAminitiesOthers
								.getText().toString() + ",");
					} else if (mFrameOthers8.getVisibility() == View.GONE) {
						mFrameOthers8.setVisibility(View.VISIBLE);
						mAddedTextOthers8.setText(mBuildingAminitiesOthers
								.getText().toString());
						mBuildingValueOthers.append(mBuildingAminitiesOthers
								.getText().toString());
					} else {
						Toast.makeText(mContext, "Max 4 are allowed", 0).show();
					}

				}

				// TODO Auto-generated method stub

			}
		});
		mDeleteOthers5.setOnClickListener(this);
		mDeleteOthers6.setOnClickListener(this);
		mDeleteOthers7.setOnClickListener(this);
		mDeleteOthers8.setOnClickListener(this);
		// End of building others
		// Start of Contact Number
		mContactNumberCheck = (CheckBox) findViewById(R.id.contact_numbercheck);
		mContactNumber = (EditText) findViewById(R.id.postcontact_number);
		mContactNumberCheck
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mAsyncHttpClient.get(
									Config.CONTACT_NUMBER_FETCHING,
									new AsyncHttpResponseHandler() {
										JSONObject obj;

										@Override
										public void onSuccess(int arg0,
												Header[] arg1, byte[] arg2) {
											try {
												obj = new JSONObject(
														new String(arg2,
																"UTF-8"));
												System.out.println(obj);
												JSONObject number = obj
														.getJSONObject("data");

												mContactNumber.setText(number
														.getString("contact_no"));
											} catch (
													UnsupportedEncodingException
													| JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											// TODO Auto-generated method stub

										}

										@Override
										public void onFailure(int arg0,
												Header[] arg1, byte[] arg2,
												Throwable arg3) {
											// TODO Auto-generated method stub

										}
									});
						}
						// TODO Auto-generated method stub

					}
				});
		mRadioContactNumber = (RadioGroup) findViewById(R.id.radioGroupcontact_number);
		mRadioContactNumber
				.setOnCheckedChangeListener(ContactNumberRadioListener);

		// End of Contact Number

	}

	static final RadioGroup.OnCheckedChangeListener ToggleListenerLook = new RadioGroup.OnCheckedChangeListener() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
			for (int j = 0; j < radioGroup.getChildCount(); j++) {
				final ToggleButton mToggleLook = (ToggleButton) radioGroup
						.getChildAt(j);

				if (mToggleLook.getId() == i) {
					mToggleLook.setChecked(true);
					mToggleLook.setTextColor(R.color.app_color);
					mToggleLookValue = mToggleLook.getText().toString();
					
				}

				
			}
		}
	};
	static final RadioGroup.OnCheckedChangeListener ToggleListenerFlatNo = new RadioGroup.OnCheckedChangeListener() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
			for (int j = 0; j < radioGroup.getChildCount(); j++) {
				final ToggleButton mToggleFlatNo = (ToggleButton) radioGroup
						.getChildAt(j);
				if (mToggleFlatNo.getId() == i) {
					mToggleFlatNo.setChecked(true);
					mToggleFlatNo.setTextColor(R.color.app_color);
					mToggleFlatNoValue = mToggleFlatNo.getText().toString();
					// Log.i("MI", "val=" + val);
				}

			}
		}
	};
	static final RadioGroup.OnCheckedChangeListener ContactNumberRadioListener = new RadioGroup.OnCheckedChangeListener() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
			for (int j = 0; j < radioGroup.getChildCount(); j++) {
				final RadioButton mRadioContact = (RadioButton) radioGroup
						.getChildAt(j);
				if (mRadioContact.getId() == i) {
					mRadioContact.setChecked(true);
					mRadioContact.setTextColor(R.color.app_color);
					mContactNumberRadio = mRadioContact.getText().toString();
					// Log.i("MI", "val=" + val);
				}

			}
		}
	};

	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on screen orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				String mpath = fileUri.getPath();
				Toast.makeText(getApplicationContext(),
						"image file path" + mpath, Toast.LENGTH_SHORT).show();
				/*
				 * mImgPaths.add(mpath); mPicAdapter.notifyDataSetChanged();
				 */
				mImgPaths.add(mpath);
				if (count == 0) {
					count = 1;
					Log.i("MI", "inside if" + mImgPaths.get(0));
					mImgPaths.remove(0);
					mPicAdapter.notifyDataSetChanged();
				} else {
					Log.i("MI", "inside else" + mImgPaths.get(0));
					mPicAdapter.notifyDataSetChanged();
				}

			} else if (resultCode == RESULT_CANCELED) {

				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();

			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}

		} else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				// video successfully recorded
				// launching upload activity
				/* launchUploadActivity(false,true); */
				mVideoFilePath = fileUri.getPath();
				Toast.makeText(getApplicationContext(),
						"video file path" + mVideoFilePath, Toast.LENGTH_SHORT)
						.show();
				Bitmap ThumbVideo = ThumbnailUtils.createVideoThumbnail(
						mVideoFilePath, MediaStore.Video.Thumbnails.MINI_KIND);

				mVideoPreview.setImageBitmap(ThumbVideo);

			} else if (resultCode == RESULT_CANCELED) {

				// user cancelled recording
				Toast.makeText(getApplicationContext(),
						"User cancelled video recording", Toast.LENGTH_SHORT)
						.show();

			} else {
				// failed to record video
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to record video", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (requestCode == CAMERA_CAPTURE_AUDIO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				mAudioFilePath = fileUri.getPath();
				Toast.makeText(getApplicationContext(),
						"audio file path" + mAudioFilePath, Toast.LENGTH_SHORT)
						.show();
				String mAudioTitle = getAudioTitle(mAudioFilePath);
				mAudioAlbumTitle.setText(mAudioTitle);

			} else if (resultCode == RESULT_CANCELED) {

				// user cancelled recording
				Toast.makeText(getApplicationContext(),
						"User cancelled audio recording", Toast.LENGTH_SHORT)
						.show();

			} else {
				// failed to record video
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to record audio", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (requestCode == SELECT_IMAGE) {
			if (resultCode == RESULT_OK) {

				String mpath = getPathImage(this, data.getData());
				Toast.makeText(getApplicationContext(),
						"image file path" + mpath, Toast.LENGTH_SHORT).show();
				// Bitmap
				// mThumbImage=ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mpath),
				// 64, 64);
				mImgPaths.add(mpath);
				if (count == 0) {
					count = 1;
					Log.i("MI", "inside if" + mImgPaths.get(0));
					mImgPaths.remove(0);
					mPicAdapter.notifyDataSetChanged();
				} else {
					Log.i("MI", "inside else" + mImgPaths.get(0));
					mPicAdapter.notifyDataSetChanged();
				}
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(),
						"User cancelled image multiple pics",
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to load image multiple pics",
						Toast.LENGTH_SHORT).show();

			}
		} else if (requestCode == AUDIO_FILE_CHOOSER) {
			if (resultCode == RESULT_OK) {
				mAudioFilePath = getPathAudio(this, data.getData());
				Toast.makeText(getApplicationContext(),
						"audio file path" + mAudioFilePath, Toast.LENGTH_SHORT)
						.show();
				String mAudioTitle = getAudioTitle(mAudioFilePath);
				mAudioAlbumTitle.setText(mAudioTitle);

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(),
						"User cancelled image multiple-array pics",
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to load image multiple-array pics",
						Toast.LENGTH_SHORT).show();

			}

		} else if (requestCode == VIDEO_FILE_CHOOSER) {
			if (resultCode == RESULT_OK) {
				mVideoFilePath = getPathVideo(this, data.getData());
				Toast.makeText(getApplicationContext(),
						"video file path" + mVideoFilePath, Toast.LENGTH_SHORT)
						.show();
				Bitmap ThumbVideo = ThumbnailUtils.createVideoThumbnail(
						mVideoFilePath, MediaStore.Video.Thumbnails.MINI_KIND);

				mVideoPreview.setImageBitmap(ThumbVideo);

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(),
						"User cancelled image multiple-array pics",
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to load image multiple-array pics",
						Toast.LENGTH_SHORT).show();

			}

		}

	}

	private void recordVideo() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

		// set video quality
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
															// name

		// start the video capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
	}

	private void recordAudio() {

		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_AUDIO);

		// set video quality
		intent.putExtra(MediaStore.Audio.Media.CONTENT_TYPE, 1);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
															// name

		// start the video capture Intent
			startActivityForResult(intent, CAMERA_CAPTURE_AUDIO_REQUEST_CODE);
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				Config.IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				/*
				 * Log.d(TAG, "Oops! Failed create " +
				 * Config.IMAGE_DIRECTORY_NAME + " directory");
				 */
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else if (type == MEDIA_TYPE_AUDIO) {

			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "AUD_" + timeStamp + ".mp3");

		} else {
			return null;
		}

		return mediaFile;
	}

	class mDateSetListener implements DatePickerDialog.OnDateSetListener {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TextView v = new TextView(getApplicationContext());
			// TODO Auto-generated method stub
			// getCalender();
			int mYear = year;
			int mMonth = monthOfYear;
			int mDay = dayOfMonth;
			mDatePicker.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(mMonth + 1).append("/").append(mDay).append("/")
					.append(mYear).append(" "));
			System.out.println(mDatePicker.getText().toString());

		}
	}

	public class PicAdapter extends BaseAdapter {
		int defaultItemBackground;
		private Context galleryContext;

		public PicAdapter(Context arg2) {

			this.galleryContext = arg2;
			TypedArray localTypedArray = this.galleryContext
					.obtainStyledAttributes(R.styleable.PicGallery);
			this.defaultItemBackground = localTypedArray.getResourceId(0, 0);
			localTypedArray.recycle();
		}

		public int getCount() {
			/*
			 * if (CreateHavePlaceProfileActivity.this.mProfile == null) return
			 * 0;
			 */
			Log.i("MI", "size=" + mImgPaths.size());
			return mImgPaths.size();
		}

		public Object getItem(int paramInt) {
			return Integer.valueOf(paramInt);
		}

		public long getItemId(int paramInt) {
			return paramInt;
		}

		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			ImageView localImageView1 = null;
			if (paramView == null) {
				localImageView1 = new ImageView(this.galleryContext);
				localImageView1.setLayoutParams(new Gallery.LayoutParams(300,
						200));
				localImageView1.setScaleType(ImageView.ScaleType.FIT_XY);
				localImageView1
						.setBackgroundResource(this.defaultItemBackground);
			}
			String str;
			for (ImageView localImageView2 = localImageView1;; localImageView2 = (ImageView) paramView) {
				str = (String) mImgPaths.get(paramInt);
				Log.d("pic", paramInt + " - " + str);
				if (!str.isEmpty())
					break;
				Picasso.with(PostAdActivity.this.mContext)
						.load(R.id.gallery_img).resize(300, 200).centerCrop()
						.into(localImageView2);
				return localImageView2;
			}
			Picasso.with(PostAdActivity.this.mContext).load(new File(str))
					.resize(300, 200).centerCrop().into(localImageView1);
			return localImageView1;
		}
	}

	public void onToggleLook(View paramView) {
		if (this.lastSelected == paramView.getId())
			return;
		Log.d("tt", "on toggled list");
		((RadioGroup) paramView.getParent()).check(paramView.getId());
		View localView = findViewById(this.lastSelected);
		if (localView != null) {
			localView.setBackgroundResource(R.drawable.bhk_button_normal);
			((ToggleButton) localView).setTextColor(getResources().getColor(
					R.color.divider));
		}
		paramView.setBackgroundResource(R.drawable.bhk_button_pressed);
		this.lastSelected = paramView.getId();
	}

	public void onToggleNum(View paramView) {
		if (this.lastSelected == paramView.getId())
			return;
		Log.d("tt", "on toggled list");
		((RadioGroup) paramView.getParent()).check(paramView.getId());
		View localView = findViewById(this.lastSelected);
		if (localView != null) {
			localView.setBackgroundResource(R.drawable.bhk_button_normal);
			((ToggleButton) localView).setTextColor(getResources().getColor(
					R.color.divider));
		}
		paramView.setBackgroundResource(R.drawable.bhk_button_pressed);
		this.lastSelected = paramView.getId();
	}

	private String getPathAudio(Context c, Uri u) {
		String[] proj = { MediaStore.Audio.Media.DATA };
		CursorLoader loader = new CursorLoader(c, u, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private String getPathVideo(Context c, Uri u) {
		String[] proj = { MediaStore.Video.Media.DATA };
		CursorLoader loader = new CursorLoader(c, u, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private String getPathImage(Context c, Uri u) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(c, u, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	protected void onDestroy() {

		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {

		if (doubleBackToExitPressedOnce) {
			Intent i = new Intent();
			// i.putExtra("main_area", mMainAreaString);
			i.putExtra("area", mAddressString);
			setResult(101, i);
			finish();
			super.onBackPressed();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click BACK again to exit",
				Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
		// TODO Auto-generated method stub

	}

	private String getAudioTitle(String filePath) {
		MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
		File mp3File = new File(filePath);
		Uri uri = (Uri) Uri.fromFile(mp3File);
		mediaMetadataRetriever.setDataSource(this, uri);
		String title = (String) mediaMetadataRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		if (title.equals("null")) {
			return "UnTitled";
		} else {
			return title;
		}
	}

	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			progressBar.setProgress(0);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// Making progress bar visible
			progressBar.setVisibility(View.VISIBLE);

			// updating progress bar value
			progressBar.setProgress(progress[0]);

			// updating percentage value
			// txtPercentage.setText(String.valueOf(progress[0]) + "%");
		}

		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}

		@SuppressWarnings("deprecation")
		private String uploadFile() {
			String responseString = null;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new ProgressListener() {

							@Override
							public void transferred(long num) {
								publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});
				if (mAudioFilePath != null) {
					File mAudioFile = new File(mAudioFilePath);
					entity.addPart("audio", new FileBody(mAudioFile));
				}
				if (mVideoFilePath != null) {
					File mVideoFile = new File(mVideoFilePath);
					entity.addPart("video", new FileBody(mVideoFile));
				}
				
				entity.addPart("main_location", new StringBody(mAddressString));

				mMainAreaString = mMainArea.getText().toString();
				entity.addPart("main_area", new StringBody(mMainAreaString));

				entity.addPart("rent", new StringBody(mRentVal));

				entity.addPart("deposit", new StringBody(mDepositVal));
				//File files;
				File[] files=new File[mImgPaths.size()];
				for(int i=0;i<mImgPaths.size();i++)
				{
					files[i]=new File(mImgPaths.get(i));
				
				entity.addPart("image"+(i+1),new FileBody(files[i]));
				}

				// Extra parameters if you want to pass to server
				// entity.addPart("flat_aminities_others",
				// new StringBody(mFlatValueOthers.toString()));
				// entity.addPart("building_minities_others", new
				// StringBody(mBuildingValueOthers.toString()));

				entity.addPart("mobile_no", new StringBody(mContactNumber
						.getText().toString()));
				entity.addPart("mobile_no_rdio_button", new StringBody(
						mContactNumberRadio));

				entity.addPart("looking_for", new StringBody(mToggleLookValue));

				entity.addPart("flat_no", new StringBody(mToggleFlatNoValue));

				entity.addPart("available_from", new StringBody(mDatePicker
						.getText().toString()));

				entity.addPart("email", new StringBody("rgupta993@gmail.com"));
				/*entity.addPart("flat_amenities",new StringBody(mFlatAminitiesValue.toString()));
				entity.addPart("building_aminities",new StringBody(mBuildingAminitiesValue.toString()));*/
				entity.addPart("television",new StringBody(tv));
				entity.addPart("fridge",new StringBody(fridge));
				entity.addPart("ac",new StringBody(ac));
				entity.addPart("washing_machine",new StringBody(washing_machine));
				entity.addPart("geyser",new StringBody(geyser));
				entity.addPart("wifi",new StringBody(wifi));
				entity.addPart("beds",new StringBody(beds));
				entity.addPart("wardrobes",new StringBody(wardrobes));
				entity.addPart("cook",new StringBody(cook));
				entity.addPart("elevator",new StringBody(elevator));
				entity.addPart("security",new StringBody(security));
				entity.addPart("parking",new StringBody(parking));
				entity.addPart("gym",new StringBody(gym));
				entity.addPart("sports_club",new StringBody(sports_club));
				entity.addPart("pool",new StringBody(pool));
				entity.addPart("power_backup",new StringBody(power_backup));
				entity.addPart("water_supply",new StringBody(water_supply));
			
				
				
				
				totalSize = entity.getContentLength();
				httppost.setEntity(entity);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			Log.e("MI", "Response from server: " + result);

			// showing the server response in an alert dialog
			showAlert(result);

			super.onPostExecute(result);
		}

	}

	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setTitle("Response from Servers")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.postdeleteothers1:
			mFrameOthers1.setVisibility(View.GONE);
			break;
		case R.id.postdeleteothers2:
			mFrameOthers2.setVisibility(View.GONE);
			break;
		case R.id.postdeleteothers3:
			mFrameOthers3.setVisibility(View.GONE);
			break;
		case R.id.postdeleteothers4:
			mFrameOthers4.setVisibility(View.GONE);
			break;
		case R.id.postdeleteothers5:
			mFrameOthers5.setVisibility(View.GONE);
			break;
		case R.id.postdeleteothers6:
			mFrameOthers6.setVisibility(View.GONE);
			break;
		case R.id.postdeleteothers7:
			mFrameOthers7.setVisibility(View.GONE);
			break;
		case R.id.postdeleteothers8:
			mFrameOthers8.setVisibility(View.GONE);
			break;
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void setRadioAdapterValue(String[] value) {
		mRadioFinalValues = value;
		for (int i = 0; i < mRadioFinalValues.length; i++) {
			Toast.makeText(mContext, "" + mRadioFinalValues[i].toString(), 0)
					.show();
			mRadioAdapterFinal += mRadioFinalValues[i].toString();
		}// TODO Auto-generated method stub

	}



}
