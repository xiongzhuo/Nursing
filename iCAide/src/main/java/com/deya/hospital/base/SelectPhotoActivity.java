package com.deya.hospital.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.PhotoFolderFragment.OnPageLodingClickListener;
import com.deya.hospital.base.img.CheckImageLoaderConfiguration;
import com.deya.hospital.base.img.PhotoFragment;
import com.deya.hospital.base.img.PhotoFragment.OnPhotoSelectClickListener;
import com.deya.hospital.base.img.PhotoSerializable;
import com.deya.hospital.vo.PhotoInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * .ClassName: SelectPhotoActivity(选择图片的界面) <br/>
 * date: 2015年4月23日 下午6:01:33 <br/>
 * 
 * @author BMM
 */
public class SelectPhotoActivity extends FragmentActivity implements OnPageLodingClickListener,
        OnPhotoSelectClickListener {

  private PhotoFolderFragment photoFolderFragment;

  private Button btnOk;
  private ImageView imgBack;

  private TextView title;

  private List<PhotoInfo> hasList;

  private FragmentManager manager;
  private int backInt = 0;
  // 已选择图片数量
  private int count;
  private int size = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common_activity_selectphoto);

    getWindowManager().getDefaultDisplay().getMetrics(MyAppliaction.getDisplayMetrics());

    count = getIntent().getIntExtra("size", 0);
    manager = getSupportFragmentManager();

    hasList = new ArrayList<PhotoInfo>();

    imgBack = (ImageView) findViewById(R.id.img_back);
    btnOk = (Button) findViewById(R.id.btn_OK);
    title = (TextView) findViewById(R.id.title);
    imgBack.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO Auto-generated method stub
        if (backInt == 0) {
          finish();
        } else if (backInt == 1) {
          backInt--;
          hasList.clear();
          title.setText(getResources().getString(R.string.photo_folder_choice));
          FragmentTransaction transaction = manager.beginTransaction();
          transaction.show(photoFolderFragment).commit();
          manager.popBackStack(0, 0);
        }
      }
    });
    btnOk.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent data = new Intent();
        if (hasList.size() > 0) {
          List<String> list = new ArrayList<String>();
          for (PhotoInfo info : hasList) {
            list.add(info.getPath_absolute());
          }
          data.putExtra("result", (Serializable) list);
          data.putExtra("size", count);
          setResult(0x2, data);
          finish();
        } else {
          setResult(0x2, data);
          finish();
        }
      }
    });
    title.setText(getResources().getString(R.string.photo_folder_choice));
    photoFolderFragment = new PhotoFolderFragment();
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.add(R.id.body, photoFolderFragment);
    transaction.addToBackStack(null);
    // Commit the transaction
    transaction.commit();
  }

  @Override
  protected void onStart() {
    // TODO Auto-generated method stub
    super.onStart();
    CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);
  }

  @Override
  public void onPageLodingClickListener(List<PhotoInfo> list) {
    // TODO Auto-generated method stub
    title.setText(getResources().getString(R.string.photo_folder_choice_zero));
    Bundle args = new Bundle();
    PhotoSerializable photoSerializable = new PhotoSerializable();
    for (PhotoInfo photoInfoBean : list) {
      photoInfoBean.setChoose(false);
    }
    photoSerializable.setList(list);
    args.putInt("count", count);
    args.putSerializable("list", photoSerializable);
    FragmentTransaction transaction = manager.beginTransaction();
    PhotoFragment photoFragment = new PhotoFragment();
    photoFragment.setArguments(args);
    transaction = manager.beginTransaction();
    transaction.hide(photoFolderFragment).commit();
    transaction = manager.beginTransaction();
    transaction.add(R.id.body, photoFragment);
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    transaction.addToBackStack(null);
    // Commit the transaction
    transaction.commit();
    backInt++;
  }

  @Override
  public void onPhotoSelectClickListener(List<PhotoInfo> list) {
    // TODO Auto-generated method stub
    hasList.clear();
    for (PhotoInfo photoInfoBean : list) {
      if (photoInfoBean.isChoose()) {
        hasList.add(photoInfoBean);
      }
    }
    if (hasList.size() > 0) {
      btnOk.setText(getResources().getString(R.string.photo_folder_choice_success));
    } else {
      btnOk.setText(getResources().getString(R.string.photo_folder_choice_cancle));
    }
    title.setText(getResources().getString(R.string.photo_folder_choice_ready) + hasList.size()
        + getResources().getString(R.string.photo_folder_num));
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub
    if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 0) {
      finish();
    } else if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 1) {
      backInt--;
      hasList.clear();
      title.setText(getResources().getString(R.string.photo_folder_choice));
      FragmentTransaction transaction = manager.beginTransaction();
      transaction.show(photoFolderFragment).commit();
      manager.popBackStack(0, 0);
    }
    return false;
  }
}
