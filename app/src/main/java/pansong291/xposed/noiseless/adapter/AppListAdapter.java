package pansong291.xposed.noiseless.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import pansong291.xposed.noiseless.R;
import android.widget.Switch;

public class AppListAdapter extends BaseAdapter
{
 Context context;
 List<AppInfo> list;
 
 public AppListAdapter(Context c, List<AppInfo> l)
 {
  context = c;
  list = l;
 }

 @Override
 public int getCount()
 {
  return list.size();
 }

 @Override
 public Object getItem(int p1)
 {
  return list.get(p1);
 }

 @Override
 public long getItemId(int p1)
 {
  return p1;
 }

 @Override
 public View getView(int p1, View p2, ViewGroup p3)
 {
  ViewHolder vh = null;
  if(p2 != null)
  {
   vh = (ViewHolder)p2.getTag();
  }else
  {
   vh = new ViewHolder();
   LayoutInflater vi = (LayoutInflater)context
   .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   p2 = vi.inflate(R.layout.list_item_app, null);
   vh.img_icon = p2.findViewById(R.id.img_icon);
   vh.txt_name = p2.findViewById(R.id.txt_name);
   vh.txt_vercode = p2.findViewById(R.id.txt_vercode);
   vh.txt_vername = p2.findViewById(R.id.txt_vername);
   vh.switch_pkg = p2.findViewById(R.id.switch_pkg);
   p2.setTag(vh);
  }
  
  AppInfo appInfo = list.get(p1);
  vh.img_icon.setImageDrawable(appInfo.icon);
  vh.txt_name.setText(appInfo.name);
  vh.txt_vercode.setText(appInfo.verCode);
  vh.txt_vername.setText(appInfo.verName);
  vh.switch_pkg.setText(appInfo.pkgName);
  vh.switch_pkg.setChecked(appInfo.isChecked);
  return p2;
 }
  
 public class ViewHolder
 {
  ImageView img_icon;
  TextView txt_name,
  txt_vername,
  txt_vercode;
  public Switch switch_pkg;
 }
 
}
