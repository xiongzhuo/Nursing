package com.deya.hospital.descover;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * ClassName:. SurroundFragemtsAdapter【周边主界面的adapter】 <br/>
 */
public class SurroundFragemtsAdapter extends FragmentStatePagerAdapter {
  private List<Fragment> listfragment;

  public SurroundFragemtsAdapter(FragmentManager fm, List<Fragment> listfragment) {
    super(fm);
    this.listfragment = listfragment;
  }

  public void setData(List<Fragment> listfragment){
    this.listfragment=listfragment;
    notifyDataSetChanged();
  }
  @Override
  public int getCount() {
    return listfragment == null ? 0 : listfragment.size();
  }

  @Override
  public Fragment getItem(int arg0) {
    return listfragment.get(arg0);
  }
  	@Override
	public CharSequence getPageTitle(int position)
	{
		return listfragment.get(position).getTag()+"";
	}
  @Override
  public int getItemPosition(Object object) {
    return PagerAdapter.POSITION_NONE;
  }
}
