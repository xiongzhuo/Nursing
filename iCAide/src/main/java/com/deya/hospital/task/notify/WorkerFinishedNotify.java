package com.deya.hospital.task.notify;

import com.deya.hospital.vo.dbdata.TaskVo;

public interface WorkerFinishedNotify {
	void Finshed(TaskVo task);
	void RefreshNetwork();
	void workFinish();
}
