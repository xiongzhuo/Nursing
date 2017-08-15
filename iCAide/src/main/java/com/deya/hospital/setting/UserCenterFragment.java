package com.deya.hospital.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.account.UserUtis;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseDataInitReciever;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.TabBaseFragment;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.UploadBizImpl;
import com.deya.hospital.descover.SortByMineFragment;
import com.deya.hospital.descover.SurroundFragemtsAdapter;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.services.BaseDataInitService;
import com.deya.hospital.shop.PrizListActivity;
import com.deya.hospital.shop.ShopGoodsListFragment;
import com.deya.hospital.shop.VipTipsActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.CircleImageView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.PagerTab;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.im.sdk.dy.ui.SDKCoreHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuntongxun.ecsdk.ECDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心
 */
public class UserCenterFragment extends TabBaseFragment implements
        OnClickListener ,ViewPager.OnPageChangeListener{
    private static final int UPDATE_USERINFO = 9010;
    private static final int UPDATE_USERINFO_FAIL =9011 ;
    private LayoutInflater inflater;
    private View view;
    private CircleImageView headImg;
    public int uploadStatus = 0;
    private MyHandler myHandler;
    public static final int ADD_PRITRUE_CODE = 9009;
    // 压缩图片的msg的what

    public static final int COMPRESS_IMAGE = 0x17;
    private TextView nichengTv;
    private TextView hospitalTv;
    private TextView scoreTv;
    private Tools tools;
    private ImageView sexImg;
    private ImageView vip_img;
    DisplayImageOptions optionsSquare1, optionsSquare2;
    private ImageView img_state;

    protected static final int GET_SUCESS = 0x3007;
    protected static final int GET_FAILE = 0x3008;
    BaseDataInitReciever reciever;
    private TextView jobs;
    Intent serviceIntent;
    private DisplayImageOptions optionsSquare;
    private SurroundFragemtsAdapter myadapter;
    private ViewPager fragmentPager;
    List<Fragment> listfragment;
    private PagerTab mIndicator;

    ImageView my_set;
    private RelativeLayout dealLay;
    BaseFragment fragment1,fragment2,fragment3,fragment4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        tools = new Tools(getActivity(), Constants.AC);
        this.inflater = inflater;
        if (view == null) {
            view = inflater.inflate(R.layout.settting_fragment_new, container,
                    false);

            String sexId=tools.getValue(Constants.SEX);
            optionsSquare = AbViewUtil.getOptions(sexId.equals("0")?R.drawable.men_defult:R.drawable.women_defult);
            initMyHandler();
            initView();
            initReciever();
            serviceIntent=new Intent(getActivity(),BaseDataInitService.class);
            serviceIntent.putExtra(BaseDataInitService.INTENTCODE, BaseDataInitService.GET_USER_INFO);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + (int) 0.5f);
    }

    private void initView() {
        optionsSquare1 = AbViewUtil.getOptions(R.drawable.men_defult);
        optionsSquare2 = AbViewUtil.getOptions(R.drawable.women_defult);
        img_state = (ImageView) view.findViewById(R.id.img_state);
        nichengTv = (TextView) view.findViewById(R.id.nicheng);
        headImg = (CircleImageView) view.findViewById(R.id.owner_photo);
        hospitalTv = (TextView) view.findViewById(R.id.hospital);
        hospitalTv.setOnClickListener(this);



        jobs = (TextView) view.findViewById(R.id.jobs);

        nichengTv.setText(tools.getValue(Constants.NAME));
        sexImg = (ImageView) view.findViewById(R.id.sexImg);
        scoreTv = (TextView) view.findViewById(R.id.score);

        vip_img = (ImageView) view.findViewById(R.id.vip_img);
        vip_img.setOnClickListener(this);
        headImg.setOnClickListener(this);
        if(tools.getValue(Constants.STATE).equals("5")){
            img_state.setImageResource(R.drawable.not_authented);
        }
         fragmentPager = (ViewPager) view.findViewById(R.id.viewpager);
        fragmentPager.setSaveEnabled(false);
        listfragment=new ArrayList<>();
        if(null==fragment1){
            fragment1=new MyCircleCollectionFragment();
            listfragment.add(fragment1);
        }
        if(null==fragment2){
            fragment2=new SortByMineFragment();

            listfragment.add(fragment2);
        }
        if(null==fragment4){
            fragment4=new ShopGoodsListFragment();
            listfragment.add(fragment4);
        }
        if(null==fragment3){
            fragment3=new MyAcToolsFragment();
            listfragment.add(fragment3);
        }



        myadapter = new MyfragementAdapter(getChildFragmentManager(),
                listfragment);
        fragmentPager.setAdapter(myadapter);
        mIndicator = (PagerTab) view.findViewById(R.id.indicator);
		mIndicator.setViewPager(fragmentPager);
        my_set= (ImageView) view.findViewById(R.id.my_set);
        my_set.setOnClickListener(this);
        dealLay = (RelativeLayout) view.findViewById(R.id.dealLay);
        dealLay.setOnClickListener(this);
		mIndicator.selectTab(0);
		mIndicator.setOnPageChangeListener(this);
        setEditorRes();
    }


    void initReciever() {
        reciever = new BaseDataInitReciever() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);

                if (intent.getAction().equals(BaseDataInitReciever.GET_USER_INFO_SC)) {
                    try {
                        JSONObject jsonObject = new JSONObject(intent.getStringExtra("data"));
                        Log.i("SystemSettingFragment",jsonObject.toString());
                        setInfoRes(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseDataInitReciever.GET_USER_INFO_SC);
        getActivity().registerReceiver(reciever, intentFilter);
    }
    public void tokephote() {
        Intent takePictureIntent = new Intent(getActivity(),
                NewPhotoMultipleActivity.class);
        takePictureIntent.putExtra(NewPhotoMultipleActivity.MAX_UPLOAD_NUM, 1);
        takePictureIntent.putExtra("type", "1");
        takePictureIntent.putExtra("size", "0");
        startActivityForResult(takePictureIntent, ADD_PRITRUE_CODE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.owner_photo:
                tokephote();
                break;
            case R.id.hospital:
            case R.id.vip_img:
                String isVip = tools.getValue(Constants.IS_VIP_HOSPITAL);
                if (!AbStrUtil.isEmpty(isVip) && isVip.equals("1")) {
                    Intent hospitalTip = new Intent(getActivity(),
                            VipTipsActivity.class);
                    startActivity(hospitalTip);
                }

                break;
            case R.id.dealLay:
                Intent it1 = new Intent(getActivity(), PrizListActivity.class);
                startActivity(it1);
                break;
            case R.id.my_set:
                Intent it = new Intent(getActivity(),
                        SettingActivity.class);
                it.putExtra("isEditor", true);
                startActivity(it);
                break;
            default:
                break;
        }

    }
    /**
     * .本来所有消息的接收对象
     */
    private void initMyHandler() {
        myHandler = new MyHandler(getActivity()) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case UploadMessage.SHOP_UPLOAD_PICTURE_SUCCESS:// 上传图片成功
                            try {
                                setHeadImg(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case UploadMessage.SHOP_UPLOAD_PICTURE_FAIL:
                            uploadStatus = 0;
                            break;
                        case COMPRESS_IMAGE:// 压缩从选择图片界面返回的图片
                            uploadStatus = 1;
                            Log.i("1111", msg.obj + "");
                            File file = new File(msg.obj + "");
                            Log.i("1111", file.exists() + "");
                            if (null != msg && null != msg.obj) {
                                UploadBizImpl.getInstance().propertyUploadPicture(
                                        myHandler, msg.obj.toString(),
                                        UploadMessage.SHOP_UPLOAD_PICTURE_SUCCESS,
                                        UploadMessage.SHOP_UPLOAD_PICTURE_FAIL);
                            }
                            break;
                        case UPDATE_USERINFO:
                            ToastUtil.showMessage("用户头像修改成功!");
                            dismissdialog();
                            break;
                        case UPDATE_USERINFO_FAIL:
                            dismissdialog();
                            break;
                        default:
                            break;
                    }
                }
            }

        };
    }
    private void setHeadImg(JSONObject jsonObject) {
        if(jsonObject.has("fileId")){
            ImageLoader.getInstance().displayImage(
                    WebUrl.FILE_LOAD_URL
                            + jsonObject.optString("fileId").toString(),
                    headImg, optionsSquare);
            tools.putValue(Constants.HEAD_PIC,
                    jsonObject.optString("fileId").toString());
            CCPAppManager.getClientUser().setAvatar(jsonObject.optString("fileId").toString());
        }

    }

    public void UpdateAvata(String avata) {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("avatar", avata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, getActivity(),
                UPDATE_USERINFO,UPDATE_USERINFO_FAIL, job, "user/updateUser");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PRITRUE_CODE && null != data) {
            Log.i("1111111111", data.getExtras() + "");
            uploadStatus = 1;
            for (int i = 0; i < data.getStringArrayListExtra("picList").size(); i++) {
                CompressImageUtil.getCompressImageUtilInstance()
                        .startCompressImage(myHandler, COMPRESS_IMAGE,
                                data.getStringArrayListExtra("picList").get(i));
                Log.i("1111111111", data.getStringArrayListExtra("picList")
                        .get(i));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String integral = "";
        try {

            integral = tools.getValue(Constants.INTEGRAL);
        } catch (Exception e) {
            // TODO: handle exception
        }
        getActivity().startService(serviceIntent);

        int integra = 0;
        if (!AbStrUtil.isEmpty(integral)) {
            scoreTv.setText(integral + "橄榄");
            integra = Integer.parseInt(integral);
        } else {
            scoreTv.setText("0");
        }

        String localSex = tools.getValue(Constants.SEX);
        if (!AbStrUtil.isEmpty(localSex) && localSex.equals("0")) {
            sexImg.setImageResource(R.drawable.ico_man);
            ImageLoader.getInstance().displayImage(
                    WebUrl.FILE_LOAD_URL + tools.getValue(Constants.HEAD_PIC),
                    headImg, optionsSquare1);
        } else {
            sexImg.setImageResource(R.drawable.ico_women);
            ImageLoader.getInstance().displayImage(
                    WebUrl.FILE_LOAD_URL + tools.getValue(Constants.HEAD_PIC),
                    headImg, optionsSquare2);
        }

        nichengTv.setText(tools.getValue(Constants.NAME));

    }



    /**
     * .本来所有消息的接收对象
     */

    protected void setEditorRes() {
        String hospitalName = tools.getValue(Constants.HOSPITAL_NAME);
        if (!AbStrUtil.isEmpty(hospitalName)) {
            hospitalTv.setText(hospitalName);
        } else {
            hospitalTv.setText("");
        }
        String hospitalJob = tools.getValue(Constants.JOB);
        if (!AbStrUtil.isEmpty(hospitalJob)) {
            if (hospitalJob.equals("1")) {
                jobs.setText("感控科主任");
            } else if (hospitalJob.equals("2")) {
                jobs.setText("专职感控人员");
            } else if (hospitalJob.equals("3")) {
                jobs.setText("兼职感控人员");
            } else if (hospitalJob.equals("4")) {
                jobs.setText("其他");
            }else if (hospitalJob.equals("5")) {
                jobs.setText("暗访人员");
            }
        } else {
            jobs.setText("其他");
        }


    }


    protected void setInfoRes(JSONObject jsonObject) {
        Log.i("111111111", jsonObject.toString());

            JSONObject job = jsonObject;
            UserUtis.setEditorRes(tools, job);//保存用户信息
            int integra = 0;
            String integral = "0";
            if (!AbStrUtil.isEmpty(job.optString("integral"))) {
                integral = job.optString("integral");
            }

            if (!AbStrUtil.isEmpty(integral)) {
                scoreTv.setText(integral + "橄榄");
                tools.putValue(Constants.INTEGRAL, integral + "");
                integra = Integer.parseInt(integral);
            } else {
                scoreTv.setText("0橄榄");
            }
            if (!AbStrUtil.isEmpty(job.optString("recommend_user_mobile"))) {
                tools.putValue(Constants.VSITIRCODE,
                        job.optString("recommend_user_mobile"));
            } else {
                tools.putValue(Constants.VSITIRCODE, "");
            }
            String isVip = job.optString("is_sign");
            if (!AbStrUtil.isEmpty(isVip) && isVip.equals("0")) {
                vip_img.setBackgroundResource(R.drawable.is_vip);
                tools.putValue(Constants.IS_VIP_HOSPITAL, "0");

            } else {
                vip_img.setBackgroundResource(R.drawable.not_vip);
                tools.putValue(Constants.IS_VIP_HOSPITAL, "1");
            }
            String recommend_user_code = job.optString("recommend_user_code");
            if (!AbStrUtil.isEmpty(recommend_user_code)) {
                tools.putValue(Constants.VSITIRCODE, recommend_user_code);
            }
            if (!AbStrUtil.isEmpty(job.optString("invitation_code"))) {
                tools.putValue(Constants.INVITATION_CODE,
                        job.optString("invitation_code"));
            } else {
                tools.putValue(Constants.INVITATION_CODE, job.optString(""));
            }

            int imgId = 0;
            if(job.optString("state").equals("0")){
                if (ECDevice.isInitialized())
                    try {
                        ECDevice.unInitial();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                SDKCoreHelper.logout(false);

                CCPAppManager.setClientUser(null);

                MainActivity.mInit = false;
                tools.putValue(Constants.AUTHENT,"");
                Intent intent = new Intent(MyAppliaction.getContext(),
                        LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
            if (!job.optString("state").equals("5")) {
                imgId = 0;
            } else {
                imgId = R.drawable.is_authented;
            }
            img_state.setImageResource(imgId);
    }

    @Override
    public void onDestroy() {
        if(null!=serviceIntent){
            getActivity().stopService(serviceIntent);
        }
        if(null!=reciever){
            getActivity().unregisterReceiver(reciever);
        }
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    String [] tabTitle={"我的收藏","问答","兑换","服务"};
    private class MyfragementAdapter extends SurroundFragemtsAdapter{

        public MyfragementAdapter(FragmentManager fm, List<Fragment> listfragment) {
            super(fm, listfragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }
    }


}
