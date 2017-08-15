package com.deya.hospital.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
	private static ExecutorService cachedThreadPool;

	public static ExecutorService getInstant() {
		if (null == cachedThreadPool) {
		//cachedThreadPool = Executors.newSingleThreadExecutor();
			cachedThreadPool = Executors.newCachedThreadPool();
		}
		return cachedThreadPool;
	}
}
