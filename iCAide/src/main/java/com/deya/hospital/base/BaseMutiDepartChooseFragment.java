package com.deya.hospital.base;

import android.app.Activity;
import android.content.Intent;

import com.deya.hospital.supervisor.AddDepartmentActivity;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.workspace.TaskUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class BaseMutiDepartChooseFragment extends TabBaseFragment {
   public  List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
    public MutiDepartChoosePop departDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initDepartPop();
    }

    public void initDepartPop(){
        TaskUtils.getDepartList(getActivity(), departlist);
        departDialog = new MutiDepartChoosePop(getActivity(), departlist,
                new MutiDepartChoosePop.OnDepartPopuClick() {

                    @Override
                    public void onChooseDepart(Map<String, ChildsVo> map) {
                        onChooseDepartList(map);
                    }

                    @Override
                    public void onAddDepart() {
                        Intent it = new Intent(getActivity(),
                                AddDepartmentActivity.class);
                        it.putExtra("data", (Serializable) departlist);
                        startActivityForResult(it, 0x22);
                    }
                });
    }
    protected abstract void onChooseDepartList(Map<String, ChildsVo> map);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == 0x22 && null != intent) {
            ChildsVo dv = (ChildsVo) intent.getSerializableExtra("data");
            String rooId = dv.getParent();
            for (DepartLevelsVo dlv : departlist) {
                if (rooId.equals("1") && dlv.getRoot().getId().equals("0")) {
                    // dlv.setChooseNum(dlv.getChooseNum() + 1);
                    dlv.getChilds().add(0, dv);
                } else if (dlv.getRoot().getId().equals(rooId)) {
                    // dlv.setChooseNum(dlv.getChooseNum() + 1);
                    dlv.getChilds().add(0, dv);
                }
            }
            departDialog.setdata(departlist);
            //onChooseDepartList(dv.getName(),dv.getId());

        } else if (resultCode == 0x23 && null != intent) {
            ChildsVo dv2 = (ChildsVo) intent.getSerializableExtra("data");
            //onChooseDepartList(dv2.getName(),dv2.getId());
        }

    }

    @Override
    public void onDestroy() {
        if(null!=departDialog&&departDialog.isShowing()){
            departDialog.dismiss();
        }
        super.onDestroy();
    }
}
