package com.deya.hospital.base;


import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.base.img.PhotoFolderAdapter;
import com.deya.hospital.base.img.ThumbnailsUtil;
import com.deya.hospital.vo.AlbumInfo;
import com.deya.hospital.vo.PhotoInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName:. PhotoFolderFragment【本地相册的文件夹目录】 <br/>
 */
public class PhotoFolderFragment extends BaseFragment {
  ExecutorService cachedThreadPool;

  public interface OnPageLodingClickListener {
    public void onPageLodingClickListener(List<PhotoInfo> list);

  }

  private OnPageLodingClickListener onPageLodingClickListener;

  private ListView listView;

  private ContentResolver cr;

  private List<AlbumInfo> listImageInfo = new ArrayList<AlbumInfo>();

  private PhotoFolderAdapter listAdapter;

  private LinearLayout loadingLay;

  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      loadingLay.setVisibility(View.GONE);
      if (getActivity() != null) {
        listAdapter = new PhotoFolderAdapter(getActivity(), listImageInfo);
        listView.setAdapter(listAdapter);
      }
    }
  };

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    if (onPageLodingClickListener == null) {
      onPageLodingClickListener = (OnPageLodingClickListener) activity;
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    cachedThreadPool = Executors.newSingleThreadExecutor();
    return inflater.inflate(R.layout.common_fragment_photofolder, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    listView = (ListView) getView().findViewById(R.id.listView);
    listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
    loadingLay = (LinearLayout) getView().findViewById(R.id.loadingLay);

    cr = getActivity().getContentResolver();
    listImageInfo.clear();

    cachedThreadPool.execute(new Runnable() {
      @Override
      public void run() {
        // TODO Auto-generated method stub
        // 获取缩略图
        ThumbnailsUtil.clear();
        String[] projection = {BaseColumns._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);

        if (cur != null && cur.moveToFirst()) {
          int imageId;
          String imagePath;
          int imageIdColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
          int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
          do {
            imageId = cur.getInt(imageIdColumn);
            imagePath = cur.getString(dataColumn);
            ThumbnailsUtil.put(imageId, "file://" + imagePath);
          } while (cur.moveToNext());
        }
        if (cur != null && !cur.isClosed()) {
          cur.close();
        }

        // 获取原图
        Cursor cursor =
            cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                "date_modified DESC");

        String myPath = "_data";
        String myAlbum = "bucket_display_name";

        HashMap<String, AlbumInfo> myhash = new HashMap<String, AlbumInfo>();
        AlbumInfo albumInfo = null;
        PhotoInfo photoInfo = null;
        if (cursor != null && cursor.moveToFirst()) {
          do {
            int index = 0;
            int theId = cursor.getInt(cursor.getColumnIndex("_id"));
            String path = cursor.getString(cursor.getColumnIndex(myPath));
            String album = cursor.getString(cursor.getColumnIndex(myAlbum));
            List<PhotoInfo> stringList = new ArrayList<PhotoInfo>();
            photoInfo = new PhotoInfo();
            if (myhash.containsKey(album)) {
              albumInfo = myhash.remove(album);
              if (listImageInfo.contains(albumInfo)) {
                index = listImageInfo.indexOf(albumInfo);
              }
              photoInfo.setImage_id(theId);
              photoInfo.setPath_file("file://" + path);
              photoInfo.setPath_absolute(path);
              albumInfo.getList().add(photoInfo);
              listImageInfo.set(index, albumInfo);
              myhash.put(album, albumInfo);
            } else {
              albumInfo = new AlbumInfo();
              stringList.clear();
              photoInfo.setImage_id(theId);
              photoInfo.setPath_file("file://" + path);
              photoInfo.setPath_absolute(path);
              stringList.add(photoInfo);
              albumInfo.setImage_id(theId);
              albumInfo.setPath_file("file://" + path);
              albumInfo.setPath_absolute(path);
              albumInfo.setName_album(album);
              albumInfo.setList(stringList);
              listImageInfo.add(albumInfo);
              myhash.put(album, albumInfo);
            }
          } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
          cursor.close();
        }
        handler.sendEmptyMessage(0);
      }
    });

    listView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        onPageLodingClickListener.onPageLodingClickListener(listImageInfo.get(arg2).getList());
      }
    });
  }
}
