package com.deya.hospital.workcircle;//package com.deya.acaide.workcircle;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import com.deya.acaide.util.AbStrUtil;
//import com.deya.acaide.vo.HotVo;
//import com.deya.acaide.workcircle.TabFragment.PageChangeInter;
//import com.deya.acaide.workcircle.fragment.CircleDocumentFragment;
//import com.deya.acaide.workcircle.fragment.CircleExpertdFragment;
//import com.deya.acaide.workcircle.fragment.CircleOrganizeFragment;
//import com.deya.acaide.workcircle.fragment.CircleRecommendFragment;
//import com.deya.acaide.workcircle.fragment.CircleTabFragment;
//import com.im.sdk.dy.common.utils.LogUtil;
//
//import java.util.List;
//
//public class
//TabAdapter extends FragmentPagerAdapter
//{
//	List<HotVo> list;
//	PageChangeInter pageInter;
//	public TabAdapter(FragmentManager fm,List<HotVo> list)
//	{
//		super(fm);
//		this.list=list;
//	}
//
//	@Override
//	public Fragment getItem(int arg0) {
//		if (list.get(arg0).getId() != null && !AbStrUtil.isEmpty(list.get(arg0).getId())) {
//			try {
//				int id = Integer.parseInt(list.get(arg0).getId());
//				LogUtil.i("fragmentID",id+"===============");
//				switch (id) {
//					case 0:
//						//推荐
//						return CircleRecommendFragment.newInstance(id);
////					break;
//					case 10086:
//						//专家
//						return CircleExpertdFragment.newInstance(id);
////					break;
//					case 10087:
//						//机构
//						return CircleOrganizeFragment.newInstance(id);
////					break;
//					case 10088:
//						//文献
//						return CircleDocumentFragment.newInstance(id);
////					break;
//					default:
//						//其他频道
//						return CircleTabFragment.newInstance(id);
////					break;
//				}
//
//			} catch (NumberFormatException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return CircleRecommendFragment.newInstance(0);
////		return TabFragment.newInstance(list.get(arg0).getId());
//	}
//
//	@Override
//	public CharSequence getPageTitle(int position)
//	{
//		return list.get(position).getName();
//	}
//
//	@Override
//	public int getCount()
//	{
//		return list.size();
//	}
//
//}
