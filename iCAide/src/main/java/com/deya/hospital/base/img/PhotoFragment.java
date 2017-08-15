package com.deya.hospital.base.img;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.vo.PhotoInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.List;

public class PhotoFragment extends BaseFragment {

  public interface OnPhotoSelectClickListener {
    public void onPhotoSelectClickListener(List<PhotoInfo> list);

  }

  private OnPhotoSelectClickListener onPhotoSelectClickListener;

  private GridView gridView;
  private PhotoAdapter photoAdapter;

  private List<PhotoInfo> list;

  private int hasSelect = 1;

  private int count;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    if (onPhotoSelectClickListener == null) {
      onPhotoSelectClickListener = (OnPhotoSelectClickListener) activity;
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.common_fragment_photoselect, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    gridView = (GridView) getView().findViewById(R.id.gridview);
    gridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
    Bundle args = getArguments();

    PhotoSerializable photoSerializable = (PhotoSerializable) args.getSerializable("list");
    list = new ArrayList<PhotoInfo>();
    list.addAll(photoSerializable.getList());
    photoAdapter = new PhotoAdapter(getActivity(), list, gridView);
    gridView.setAdapter(photoAdapter);
    count = args.getInt("count");
    gridView.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (list.get(position).isChoose() && hasSelect > 1) {
          list.get(position).setChoose(false);
          hasSelect--;
        } else if (hasSelect + count < PhotoNumsBean.getInstant().getNumber() + 1) {
          list.get(position).setChoose(true);
          hasSelect++;
        } else {
        /*  Toast.makeText(
              activity,
              CommonUtils.getString(R.string.photo_folder_max_num)
                  + PhotoNumsBean.getInstant().getNumber()
                  + CommonUtils.getString(R.string.photo_folder_max_num_pic), Toast.LENGTH_SHORT)
              .show();*/
        }
        photoAdapter.refreshView(position);
        onPhotoSelectClickListener.onPhotoSelectClickListener(list);
      }
    });
  }
}
