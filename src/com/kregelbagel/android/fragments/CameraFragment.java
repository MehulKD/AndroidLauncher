package com.kregelbagel.android.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.kregelbagel.android.core.Config;
import com.kregelbagel.android.drawer.MainActivity;
import com.kregelbagel.android.drawer.R;

public class CameraFragment extends Fragment {

	private Preview mPreview;
	Camera mCamera;
	int mNumberOfCameras;
	int mCurrentCamera; // Camera ID currently chosen
	int mCameraCurrentlyLocked; // Camera ID that's actually acquired
	int mDefaultCameraId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreview = new Preview(this.getActivity());
		mNumberOfCameras = Camera.getNumberOfCameras();
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < mNumberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK)
				mCurrentCamera = mDefaultCameraId = i;
		}
		setHasOptionsMenu(mNumberOfCameras > 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return mPreview;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.layout.cameralayout, menu);
	}

	@Override
	public void onResume() {
		super.onResume();
		mCamera = Camera.open(mCurrentCamera);
		mCameraCurrentlyLocked = mCurrentCamera;
		mPreview.setCamera(mCamera);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mCamera != null) {
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_switch_cam:
			if (mCamera != null) {
				mCamera.stopPreview();
				mPreview.setCamera(null);
				mCamera.release();
				mCamera = null;
			}
			mCurrentCamera = (mCameraCurrentlyLocked + 1) % mNumberOfCameras;
			mCamera = Camera.open(mCurrentCamera);
			mCameraCurrentlyLocked = mCurrentCamera;
			mPreview.switchCamera(mCamera);
			handleCamera(mCamera, mCurrentCamera);
			mCamera.startPreview();
			return true;
		case R.id.photo_capture:
			captureImage();
			Config.makeHotToast("Photo captured!");
			return true;
		case android.R.id.home:
			Intent intent = new Intent(this.getActivity(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void captureImage() {

		PictureCallback rawCallback = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				Log.d("Log", "onPictureTaken - raw");
			}
		};

		PictureCallback jpegCallback = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				Bitmap bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.length);
				FileOutputStream outStream = null;
				try {
					outStream = new FileOutputStream(createImageFile());
					outStream.write(data);
					outStream.close();
					Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}
				Log.d("Log", "onPictureTaken - jpeg");
			}
		};
		mCamera.takePicture(new ShutterCallback() {

			@Override
			public void onShutter() {
				Log.i("LOg", "Sutter!");
			}
		}, rawCallback, jpegCallback);
		Log.e("Picture Status: ", "picture taken successfully");
	}

	String mCurrentPhotoPath = Environment.DIRECTORY_DCIM;
	static final int REQUEST_TAKE_PHOTO = 1;

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "KB_" + timeStamp + "_";
		File s = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM
		    + "/KB Launcher");
		if (!s.isDirectory())
			s.mkdir();
		File storageDir = s;
		File image = File.createTempFile(imageFileName, /* prefix */
		    ".jpg", /* suffix */
		    storageDir /* directory */
		);
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		galleryAddPic(image.getAbsolutePath());
		return image;
	}

	private void galleryAddPic(String Location) {
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(Location);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		getActivity().sendBroadcast(mediaScanIntent);
	}

	private void handleCamera(Camera Cam, int currentCam) {
		Cam.stopPreview();
		CameraInfo cameraInfo = new CameraInfo();
		Camera.getCameraInfo(currentCam, cameraInfo);
		Camera.Parameters parameters = Cam.getParameters();
		if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK)
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		Cam.setParameters(parameters);
	}
}

class Preview extends ViewGroup implements SurfaceHolder.Callback {
	private final String TAG = "Preview";
	SurfaceView mSurfaceView;
	SurfaceHolder mHolder;
	Size mPreviewSize;
	List<Size> mSupportedPreviewSizes;
	Camera mCamera;
	boolean mSurfaceCreated = false;

	Preview(Context context) {
		super(context);
		mSurfaceView = new SurfaceView(context);
		addView(mSurfaceView);
		setFocusable(true);
		setFocusableInTouchMode(true);
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
	}

	public void setCamera(Camera camera) {
		mCamera = camera;
		if (mCamera != null) {
			mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
			if (mSurfaceCreated)
				requestLayout();
		}
	}

	public void switchCamera(Camera camera) {
		setCamera(camera);
		try {
			mHolder.setKeepScreenOn(true);
			camera.setPreviewDisplay(mHolder);
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		setMeasuredDimension(width, height);
		if (mSupportedPreviewSizes != null) {
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
		}
		if (mCamera != null) {
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			mCamera.setParameters(parameters);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (getChildCount() > 0) {
			final View child = getChildAt(0);
			mCamera.setDisplayOrientation(90);
			final int width = getWidth();
			final int height = getHeight();
			child.layout(0, 0, width, height);
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
				mCamera.stopPreview();
				Camera.Parameters p = mCamera.getParameters();
				p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
				mCamera.setParameters(p);
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
				mCamera.autoFocus(null);
			}
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
		if (mPreviewSize == null)
			forceLayout();

		mSurfaceCreated = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) h / w;
		if (sizes == null)
			return null;
		Camera.Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		int targetHeight = h;
		for (Camera.Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Camera.Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		forceLayout();
		mCamera.stopPreview();
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}
}