package com.deya.hospital.workspace.workspacemain;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public interface TasktypeChooseListener<T> {
    public void onChooseType(int type);
    public void onChooseSetting();
    public void onSelectType(List<T> list);
}
