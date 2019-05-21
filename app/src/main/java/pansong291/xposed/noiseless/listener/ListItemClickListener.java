package pansong291.xposed.noiseless.listener;

import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import pansong291.xposed.noiseless.adapter.AppListAdapter.ViewHolder;
import pansong291.xposed.noiseless.hook.HookConfig;
import pansong291.xposed.noiseless.adapter.AppInfo;

public class ListItemClickListener implements OnItemClickListener
{

 @Override
 public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
 {
  ViewHolder vh = (ViewHolder)p2.getTag();
  AppInfo tmpInfo = (AppInfo)p1.getAdapter().getItem(p3);
  if(tmpInfo.isChecked)
  {
   HookConfig.pkgs.remove(tmpInfo.pkgName);
  }else
  {
   HookConfig.pkgs.add(tmpInfo.pkgName);
  }
  tmpInfo.isChecked = !tmpInfo.isChecked;
  vh.switch_pkg.setChecked(tmpInfo.isChecked);
 }
 
}
