package com.deya.hospital.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.deya.acaide.R;
import com.deya.hospital.base.img.PhotoNumsBean;
import com.deya.hospital.base.img.PictureUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * .ClassName: NewPhotoMultipleActivity(多张图片拍照管理类) <br/>
 * date: 2015年5月8日 上午11:40:43 <br/>
 * 
 * @author BMM
 */
public class NewPhotoMultipleActivity extends BaseActivity {
  public static final String MAX_UPLOAD_NUM = "maxUploadNum";
  public static int maxPhotoNums = 4;
  private static String mCurrentPhotoPath;
  private static final int REQUEST_TAKE_PHOTO = 0x0; // 拍照
  private static final int REQUEST_UPLOAD_PHOTO = 0x1; // 上传图片
  private static final int REQUEST_SELECT_LOCAL_PHOTO = 0x2; // 选择本地图片
  private static final int REQUEST_CUT_PHOTO = 0x3; // 剪切图片
  private static String imageId; // 图片id

  List<String> mimageIdList = new ArrayList<String>(); // 图片id
  List<String> msamllPathList = new ArrayList<String>(); // 图片路径

  private Uri mphotoUri; // 获取到的图片URI
  private int size = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_main_photo);
    size = getIntent().getIntExtra("size", 0);
    maxPhotoNums = getIntent().getIntExtra(MAX_UPLOAD_NUM, 4);
  }

  /**
   * onPhotographClick:【拍照按钮事件】. <br/>
   * .@param arg0.<br/>
   */
  public void onPhotographClick(View arg0) {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    try {
      // 指定存放拍摄照片的位置
      File file = createImageFile();
      takePictureIntent.putExtra("output", Uri.fromFile(file));
      startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void onCancleClick(View arg0) {
    finish();
  }

  /**
   * onLocalClick:【选择本地相册】. <br/>
   * .@param arg0.<br/>
   */
  public void onLocalClick(View arg0) {
    Intent intent = new Intent(this, SelectPhotoActivity.class);
    intent.putExtra("size", size);
    PhotoNumsBean.getInstant().setNumber(maxPhotoNums);
    startActivityForResult(intent, REQUEST_SELECT_LOCAL_PHOTO);
  }

  private static final int CODE_RESULT_CANCEL = -0x2; // 取消
  List<String> resullist = new ArrayList<String>();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    Log.i("test", "requestCode:" + requestCode);
    if (resultCode == CODE_RESULT_CANCEL) {
      // 返回结果异常
      finish();
    } else {
      switch (requestCode) {
        case REQUEST_TAKE_PHOTO:
          // 拍照返回结果
          if (resultCode == Activity.RESULT_OK) {
            startUploadActivity();
          }
          break;
        case REQUEST_SELECT_LOCAL_PHOTO:
          // 选择图片返回结果
          if (intent != null) {
            size += intent.getIntExtra("size", 0);
            if (intent.hasExtra("result")) {
              resullist = (List<String>) intent.getExtras().getSerializable("result");
              startUploadActivity();
            } else {
              finish();
            }
          }
          break;
        case REQUEST_CUT_PHOTO:
          // 没有调用剪切图片？？？
          // 剪切图片返回结果
          startUploadActivity();
          break;
        case REQUEST_UPLOAD_PHOTO:
          // 上传图片返回结果
          if (intent != null && RESULT_OK == resultCode) {
            mimageIdList = (List<String>) intent.getExtras().getSerializable(("imageId"));
            if (mimageIdList.size() > 0) {
              // 上传图片成功，返回图片路径
              msamllPathList = (List<String>) intent.getExtras().getSerializable("samllPath");
              Intent take = new Intent();
              Log.i("111", mimageIdList.size() + "a111");
              take.putExtra("imageId", (Serializable) mimageIdList);
              take.putExtra("samllPath", (Serializable) msamllPathList);
              take.putExtra("size", size);
              setResult(0x1, take);
              finish();
            }
          }
          break;
        default:
          break;
      }
    }
  }

  /**
   * startUploadActivity:【开启上传图片activity】. <br/>
   * ..<br/>
   */
  private void startUploadActivity() {
    Intent data = new Intent();
    data.putExtra("picList", (Serializable) resullist);
    NewPhotoMultipleActivity.this.setResult(RESULT_OK, data);
    NewPhotoMultipleActivity.this.finish();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return true;
  }

  /**
   * createImageFile:【把程序拍摄的照片放到 SD卡的 Pictures目录中 sheguantong 文件夹中
   * 照片的命名规则为：sheqing_20130125_173729.jpg】. <br/>
   * .@return .@throws IOException.<br/>
   */
  @SuppressLint("SimpleDateFormat")
  private File createImageFile() throws IOException {

    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
    String timeStamp = format.format(new Date());
    String imageFileName = "activity_" + timeStamp + ".jpg";

    File image = new File(PictureUtil.getAlbumDir(), imageFileName);
    mCurrentPhotoPath = image.getAbsolutePath();
    resullist.add(mCurrentPhotoPath);
    return image;
  }

  /**
   * getPath:【这里用一句话描述这个方法的作用】. <br/>
   * .@param context
   * .@param uri
   * .@return.<br/>
   */
  @SuppressLint("NewApi")
  public static String getPath(final Context context, final Uri uri) {
    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
          return Environment.getExternalStorageDirectory() + "/" + split[1];
        }
      } else if (isDownloadsDocument(uri)) {
        // DownloadsProvider

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri =
            ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                Long.valueOf(id));

        return getDataColumn(context, contentUri, null, null);
      } else if (isMediaDocument(uri)) {
        // MediaProvider
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[] {split[1]};

        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    } else if ("content".equalsIgnoreCase(uri.getScheme())) {
      // MediaStore (and general)

      // Return the remote address
      if (isGooglePhotosUri(uri)) {
        return uri.getLastPathSegment();
      }

      return getDataColumn(context, uri, null, null);
    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
      // File
      return uri.getPath();
    }

    return null;
  }

  /**
   * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other
   * file-based ContentProviders.
   * 
   * @param context The context.
   * @param uri The Uri to query.
   * @param selection (Optional) Filter used in the query.
   * @param selectionArgs (Optional) Selection arguments used in the query.
   * @return The value of the _data column, which is typically a file path.
   */
  public static String getDataColumn(Context context, Uri uri, String selection,
      String[] selectionArgs) {

    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = {column};

    try {
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
      if (cursor != null && cursor.moveToFirst()) {
        final int index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(index);
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return null;
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is ExternalStorageProvider.
   */
  public static boolean isExternalStorageDocument(Uri uri) {
    return "com.android.externalstorage.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is DownloadsProvider.
   */
  public static boolean isDownloadsDocument(Uri uri) {
    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is MediaProvider.
   */
  public static boolean isMediaDocument(Uri uri) {
    return "com.android.providers.media.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is Google Photos.
   */
  public static boolean isGooglePhotosUri(Uri uri) {
    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
  }
}
