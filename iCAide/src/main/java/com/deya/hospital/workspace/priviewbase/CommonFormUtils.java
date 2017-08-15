package com.deya.hospital.workspace.priviewbase;

import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.IdAndResultsVo;
import com.deya.hospital.vo.RisistantVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class CommonFormUtils {
    public  static void getresult(RisistantVo rv, List<IdAndResultsVo> resultlist) {
        for (RisistantVo.TempListBean rt : rv.getTemp_list()) {
            if (rt == null) {
                continue;
            }
            IdAndResultsVo idAndResultsVo = new IdAndResultsVo();
            idAndResultsVo.setResult(new ArrayList<IdAndResultsVo.ItemBean>());
            idAndResultsVo.setTemp_id(rt.getId() + "");
            for (RisistantVo.TempListBean.ItemListBean rti : rt.getItem_list()) {

                switch (rti.getType()) {
                    case 1:
                        break;
                    case 2:
                        IdAndResultsVo.ItemBean resultItems = new IdAndResultsVo.ItemBean();
                        resultItems.setResult(rti.getChildren().get(0).getResult());
                        resultItems.setSelect_id(rti.getChildren().get(0).getId() + "");
                        idAndResultsVo.getResult().add(resultItems);
                        break;
                    case 3:
                        IdAndResultsVo.ItemBean itemBean2 = new IdAndResultsVo.ItemBean();
                        itemBean2.setResult(rti.getChildren().get(0).getResult());
                        itemBean2.setSelect_id((rti.getChildren().get(0).getId() + ""));
                        idAndResultsVo.getResult().add(itemBean2);
                        break;
                    case 4:
                        IdAndResultsVo.ItemBean itemBean3 = new IdAndResultsVo.ItemBean();
                        itemBean3.setResult(rti.getChildren().get(0).getResult());
                        itemBean3.setSelect_id(rti.getChildren().get(0).getId() + "");
                        IdAndResultsVo.ItemBean itemBean4 = new IdAndResultsVo.ItemBean();
                        itemBean4.setResult(rti.getChildren().get(1).getResult());//积分
                        itemBean4.setSelect_id(rti.getChildren().get(1).getId() + "");
                        idAndResultsVo.getResult().add(itemBean3);
                        idAndResultsVo.getResult().add(itemBean4);
                        break;
                    case 5:

                    case 6:
                        for (RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean : rti.getChildren()) {
                            IdAndResultsVo.ItemBean itemBean5 = new IdAndResultsVo.ItemBean();
                            itemBean5.setSelect_id(childrenBean.getId() + "");
                            if (AbStrUtil.isEmpty(childrenBean.getResult())) {
                                itemBean5.setResult("0");
                            } else {
                                itemBean5.setResult(childrenBean.getResult());
                            }
                            idAndResultsVo.getResult().add(itemBean5);
                        }
                        break;


                }


            }
            resultlist.add(idAndResultsVo);

        }
    }

}
