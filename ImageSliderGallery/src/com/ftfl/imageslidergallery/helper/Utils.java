package com.ftfl.imageslidergallery.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class Utils {

	private Context _context;
	int mColumnWidth = 0;

	// constructor
	public Utils(Context context) {
		this._context = context;
	}

	// Reading file paths from SDCard
	public ArrayList<String> getFilePaths() {
		ArrayList<String> filePaths = new ArrayList<String>();

		File directory = new File(
				android.os.Environment.getExternalStorageDirectory(),
				FTFLConstants.PHOTO_ALBUM);

		// check for directory
		if (directory.isDirectory()) {
			// getting list of file paths
			File[] listFiles = directory.listFiles();

			// Check for count
			if (listFiles.length > 0) {

				// loop through all files
				for (int i = 0; i < listFiles.length; i++) {

					// get file path
					String filePath = listFiles[i].getAbsolutePath();

					// check for supported file extension
					if (IsSupportedFile(filePath)) {
						// Add image path to array list
						filePaths.add(filePath);
					}
				}
			} else {
				// image directory is empty
				Toast.makeText(
						_context,
						FTFLConstants.PHOTO_ALBUM
								+ " is empty. Please load some images in it !",
						Toast.LENGTH_LONG).show();
			}

		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("Error!");
			alert.setMessage(FTFLConstants.PHOTO_ALBUM
					+ " directory path is not valid! Please create a folder by this name in your sd card");
			alert.setPositiveButton("OK", null);
			alert.show();
		}

		return filePaths;
	}

	// Check supported file extensions
	private boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
				filePath.length());

		if (FTFLConstants.FILE_EXTN.contains(ext.toLowerCase(Locale
				.getDefault())))
			return true;
		else
			return false;

	}

	/**
	 * getting screen width
	 */
	public int getScreenWidth() {

		DisplayMetrics metrics = _context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		// int height = metrics.heightPixels;

		mColumnWidth = width;
		return mColumnWidth;
	}

}
