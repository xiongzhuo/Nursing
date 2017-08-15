package com.deya.hospital.util;

public class WebUrl {

    public static final String PUBLIC_SERVICE = "http://admin.gkgzj.com/gkgzjsys";// 本地服务器地址
    public static final String HOSPITAL_VERSION_PANXIAO = "http://demo.gkgzj.com/incontrol-intf/api";
    public static final String HOSPITAL_VERSION_TEST = "http://test.gkgzj.com/gkgzjsys";
    public static final String HOSPITAL_VERSION_ONLINE = "http://console.gkgzj.com/hospital";

  /**
   * 环境切换
   */
  public static boolean isTest = true;
  /**
   * 测试环境
   */
  public static final String LEFT_URL = isTest?HOSPITAL_VERSION_PANXIAO:HOSPITAL_VERSION_ONLINE;

   public static final String PUBLIC_SERVICE_URL =isTest?HOSPITAL_VERSION_TEST:PUBLIC_SERVICE;// 通用部分接口地址


    public WebUrl() {
        isTest = !(LEFT_URL.contains("admin.")||LEFT_URL.contains("console."));//LEFT_URL.equals(NATIVESERVE2);
    }

    private static WebUrl weUrl;

    /**
     * 数据库版本
     * 若有大的结构变得才更改版本号
     */
    private static int dataVersion = 20170109;

    public static int getDataVersionCode() {
        return dataVersion;
    }


    /**
     *APP的识别码
     */
    public static final String APP_CODE = "dygk_all";

    public static final String FILE_PDF_LOAD_URL = PUBLIC_SERVICE_URL + "/comm/download/";//pdf 下载链接地址用通用版的
    public static final String FILE_LOAD_URL = LEFT_URL + "/comm/download/";
    public static final String TASKDETAILURL = LEFT_URL
            + "/taskCount/fiveTimerCount?&task_id=";
    public static final String FILE_UPLOAD_URL = LEFT_URL + "/comm/upload";
    // public static final String FILE_UPLOAD_URL=NATIVESERVE+"/comm/upload";

    public static final String url = LEFT_URL + "/dyMagazine/app/map";
    /**
     * 获取短信验证码地址
     */
    public static final String CODE_URL = LEFT_URL + "/member/newVerifyCode";
    public static final String RESETPASSWORD = LEFT_URL + "/member/pwdmodify";

    public static final String DOCMENTURL = PUBLIC_SERVICE_URL
            + "/discover/linkClientShow?document_id=";



    public static final String WEB_SEARCH = PUBLIC_SERVICE_URL + "/gkgzj-work/search.html";//搜索
    public static final String WEB_ARTICAL_DETAIL = PUBLIC_SERVICE_URL + "/gkgzj-work/article_detail.html";//文章详情
    public static final String WEB_PDF = PUBLIC_SERVICE_URL + "/gkgzj-work/pdf.html";//PDF详情
    public static final String WEB_ARTICAL = PUBLIC_SERVICE_URL + "/gkgzj-work/article_detail.html?id=";//文章详情
    public static final String WEB_FORM_COUNT = LEFT_URL + "/gkgzj/gridCountList.html";//考核统计
    public static final String WEB_QUESTION_COUNT = LEFT_URL + "/gkgzj/gridSupervisorList.html";//问题汇总
    public static final String WEB_HAND_REPORT = LEFT_URL + "/reportCounts/reportNew?u=";//手卫生统计


    public static WebUrl webUrl;

    public static WebUrl getInstance() {
        if (null == webUrl) {
            webUrl = new WebUrl();
        }
        return webUrl;
    }

    public String web_url(String path) {
        return LEFT_URL + "/" + path;
    }

    /*
     * 获取联系人列表
     */
    public static final String CONTACTS_PATH = "iminfo/getFriendsList";
    /*
     * 联系人信息
     */
    public static final String CONTACT_PATH = "iminfo/searchFriend";

    /**
     * 查询并添加联系人信息 by phone
     */
    public static final String CONTACT_FIND_PATH = "iminfo/searchFriendNew";

    /**
     * 查询并添加联系人信息 by id
     */
    public static final String CONTACT_FIND_ID_PATH = "iminfo/addFriendByUid";
    /*
     * 修改联系人备注.
     */
    public static final String UPDATE_FRIEND_REMARK_PATH = "iminfo/setFriendRemark";

    public static final String CONTACT_PHONES = "iminfo/searchMobileList";

    public static final String GET_FORM_LIST = LEFT_URL
            + "/grid/queryTemplateInfos";
    public static final String SENDFORM = LEFT_URL + "/grid/submitGridInfos";

    public static final String GETAGREETEXT = "http://admin.gkgzj.com/gkgzjsys/public/agreement.txt";
    public static final String GETWEBFILES = "http://studio.gkgzj.com/ios/file.json";
    //现场反馈常规数据
    public static final String URL_PATH_QUERYREMARKPARAMS = "comm/queryRemarkParams";
}
