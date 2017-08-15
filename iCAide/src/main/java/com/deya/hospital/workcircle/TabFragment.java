package com.deya.hospital.workcircle;//package com.deya.acaide.workcircle;
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.LinearLayout;
//
//import com.artifex.mupdfdemo.PdfPreviewActivity;
//import com.deya.acaide.R;
//import com.deya.acaide.base.BaseFragment;
//import com.deya.acaide.descover.QuestionSortActivity;
//import com.deya.acaide.util.Constants;
//import com.deya.acaide.util.LoadingView;
//import com.deya.acaide.util.LoadingView.LoadingStateInter;
//import com.deya.acaide.util.NetWorkUtils;
//import com.deya.acaide.util.Tools;
//import com.deya.acaide.util.WebUrl;
//
//@SuppressLint({ "ValidFragment", "SetJavaScriptEnabled" })
//public class TabFragment extends BaseFragment {
//
//	Tools tools;
//	String id = "";
//	WebView webView;
//	private LinearLayout networkView;
//	private ProgressDialog progDailog;
//	LoadingView loadingView;
//	private String urlweb="";
//
//	//PageChangeInter pageInter;
////	public TabFragment(String url) {
////		this.id = url;
////	}
//
//	/**
//	 * 传入需要的参数，设置给arguments
//	 * @param id
//	 * @return
//	 */
//	public static TabFragment newInstance(String id)
//	{
//		Bundle bundle = new Bundle();
//		bundle.putString("tabfragment", id);
//		TabFragment contentFragment = new TabFragment();
//		contentFragment.setArguments(bundle);
//		return contentFragment;
//	}
//
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//	}
//
//	@SuppressLint("JavascriptInterface")
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//							 Bundle savedInstanceState) {
//		tools = new Tools(getActivity(), Constants.AC);
//		Bundle bundle = getArguments();
//		if (bundle != null){
//			id = bundle.getString("tabfragment");
//		}
//		View view = inflater.inflate(R.layout.circle_tab_web, null);
//		webView = (WebView) view.findViewById(R.id.webview);
//		loadingView = (LoadingView) view.findViewById(R.id.loadingView);
//		// progDailog = ProgressDialog.show(activity, "正在加载", "努力加载中。。。", true);
//		// progDailog.setCancelable(true);
//		loadingView.setLoadingListener(new LoadingStateInter() {
//
//			@Override
//			public void onloadingStart() {
//				loadingView.setVisibility(View.VISIBLE);
//
//			}
//
//			@Override
//			public void onloadingFinish() {
//				loadingView.setVisibility(View.GONE);
//
//			}
//		});
//		loadingView.setVisibility(View.VISIBLE);
//		loadingView.startAnimition();
//		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setLoadWithOverviewMode(true);
//		webView.getSettings().setUseWideViewPort(true);
//		webView.setWebChromeClient(new WebChromeClient());
//		webView.addJavascriptInterface(new InJavaScript(), "init");
//		networkView = (LinearLayout) view.findViewById(R.id.networkView);
//		webView.setWebViewClient(new WebViewClient() {
//
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				// view.loadUrl(url);
//				Intent it = new Intent();
//				it.putExtra("url", url);
//				urlweb = url;
//				if (id.equals("10086")) {
//					it.setClass(getActivity(), WebViewExpertDtail.class);
//				} else if(id.equals("10087")){
//					it.setClass(getActivity(), OrganizeActivity.class);
//					it.putExtra("organization_id", url.substring(url.indexOf("id=")+3));
//				} else {
//					if (url.contains("pdfid")) {
//						it.putExtra("articleid", url.substring(url.indexOf("id=")+3,url.indexOf("&pdfid=")));
//						it.setClass(getActivity(), PdfPreviewActivity.class);
//					} else if(url.contains("question")){
//						it.setClass(getActivity(), QuestionSortActivity.class);
//					}
//					else if(url.contains("literat_detail")){
//						it.putExtra("category_id",url.substring(url.indexOf("id=")+3));
//						it.putExtra("channelId",id);
//						it.putExtra("channelName","文献");
//						it.setClass(getActivity(), DocumentActivity.class);
//					}
//					else {
//						it.setClass(getActivity(), WebViewDtail.class);
//					}
//				}
//				startActivity(it);
//				return true;
//			}
//
//			@Override
//			public void onPageFinished(WebView view, final String url) {
////				if (id.equals("10086")) {// 专家列表
////					webView.loadUrl("javascript:setInitData(" + ")");
////				} else {// 普通列表
//				webView.loadUrl("javascript:setInitData(" + "'" + id + "'"
//						+ ")");
////				}
//
//				loadingView.stopAnimition();
//				loadingView.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				super.onPageStarted(view, url, favicon);
//				checkNetWork();
//			}
//		});
////		 webView.loadUrl("javascript:setInitData("
////		 + tools.getValue(Constants.AUTHENT)+","+ id+ ")");
//		// webView.loadUrl("javascript:javacalljswithargs("
//		// + tools.getValue(Constants.AUTHENT)+","+ url+ ")");
////		webView.clearCache(true);
//		if (id.equals("10086")) {
//			webView.loadUrl(WebUrl.WEB_EXTPERTLIST);
//		} else if (id.equals("10087")) {
//			webView.loadUrl(WebUrl.WEB_ORGANIZE);
//		} else if (id.equals("10088")) {
//			webView.loadUrl(WebUrl.WEB_LITERAT);
//		} else {
//			webView.loadUrl(WebUrl.WEB_DOUCMENTLIST);
//		}
////		webView.reload();
//		return view;
//	}
//
//	void checkNetWork() {
//		if (NetWorkUtils.isConnect(getActivity())) {
//			networkView.setVisibility(View.GONE);
//		} else {
//			networkView.setVisibility(View.VISIBLE);
//		}
//	}
//
//	final class InJavaScript {
//		@JavascriptInterface
//		public void handleSwipe(final String str) {
//
////			getActivity().runOnUiThread(new Runnable() {
////
////				@Override
////				public void run() {
////					ToastUtils.showToast(getActivity(),str );
////				if(!AbStrUtil.isEmpty(str)){
////					if(str.equals("right")){
////						pageInter.onScrollLeft();
////					}else{
////						pageInter.onScrollRight();;
////					}
////				}
////				}
////			});
//		}
//	}
//
//	@Override
//	public void onDestroy() {
//		if (null != loadingView) {
//			loadingView.stopAnimition();
//		}
//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			dealJavascriptLeak();
//		}
//		super.onDestroy();
//	}
//
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	private void dealJavascriptLeak() {
//		webView.removeJavascriptInterface("searchBoxJavaBridge_");
//		webView.removeJavascriptInterface("accessibility");
//		webView.removeJavascriptInterface("accessibilityTraversal");
//	}
//
//	public interface PageChangeInter {
//		 void onScrollLeft();
//		 void onScrollRight();
//
//	}
//}
