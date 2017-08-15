package com.deya.hospital.util;

public class EventManager {

    private static EventManager instance;
    private EventMgr<OnNotifyListener> listenerMrg;

    /**
     * 单例 . <br/>
     * 
     * @return 当前对象
     */
    public static EventManager getInstance() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if (instance == null) {
                    instance = new EventManager();
                }
            }
        }
        return instance;
    }

    private EventManager() {
        listenerMrg = new EventMgr<OnNotifyListener>();
    }

    public void registerListener(OnNotifyListener onNotifyListener) {
        listenerMrg.register(onNotifyListener);
    }

    /**
     * 注销对象 . <br/>
     * 
     * @param onNotifyListener 监听器
     */
    public void unRegisterListener(OnNotifyListener onNotifyListener) {
        listenerMrg.unregister(onNotifyListener);
    }

    /**
     * 接收到信息时的处理。 .<br/>
     * 
     * @param object 接收到的对象
     * @param eventTag 事件的标志
     */
    public void notify(final Object object, final String eventTag) {
        listenerMrg.startNotify(new EventMgr.INotifyCallback<OnNotifyListener>() {
            @Override
            public void onNotify(OnNotifyListener listener) {
                if (listener != null) {
                    listener.onNotify(object, eventTag);
                }
            }
        });
    }

    /**
     * 接收到信息时的处理。 .<br/>
     *
     * @param object 接收到的对象
     * @param eventTag 事件的标志
     */
    public void notify(final Object object,final String method, final String eventTag) {
        listenerMrg.startNotify(new EventMgr.INotifyCallback<OnNotifyListener>() {
            @Override
            public void onNotify(OnNotifyListener listener) {
                if (listener != null) {
                    listener.onNotify(object, eventTag);
                }
            }
        });
    }
    public interface OnNotifyListener {
        void onNotify(Object object, String eventTag);
    }

}
