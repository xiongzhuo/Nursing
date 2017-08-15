package com.deya.hospital.base;

import android.content.Intent;
import android.os.Bundle;

import com.deya.hospital.supervisor.AddDepartmentActivity;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.workspace.TaskUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public abstract class BaseDepartChooseActivity extends BaseActivity {
    List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();
    public DepartChoosePop departDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDepartPop();
    }

    public void initDepartPop(){
        TaskUtils.getDepartList(this, departlist);
        departDialog = new DepartChoosePop(this, departlist,
                new DepartChoosePop.OnDepartPopuClick() {
                    @Override
                    public void onChooseDepart(String name, String id) {
                        onChooseDepartList(name,id);
                    }

                    @Override
                    public void onAddDepart() {
                        Intent it = new Intent(mcontext,
                                AddDepartmentActivity.class);
                        it.putExtra("data", (Serializable) departlist);
                        startActivityForResult(it, 0x22);
                    }
                });
    }
    protected abstract void onChooseDepartList(String name, String id);

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
            onChooseDepartList(dv.getName(),dv.getId());

        } else if (resultCode == 0x23 && null != intent) {
            ChildsVo dv2 = (ChildsVo) intent.getSerializableExtra("data");
            onChooseDepartList(dv2.getName(),dv2.getId());
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
