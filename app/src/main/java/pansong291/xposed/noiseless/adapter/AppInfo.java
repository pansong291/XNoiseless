package pansong291.xposed.noiseless.adapter;

import android.graphics.drawable.Drawable;

public class AppInfo implements Comparable<AppInfo>
{
 public Drawable icon;
 public String name,
  pkgName,
  verName,
  verCode;
 public boolean isSystem;
 public boolean isChecked;
 
 @Override
 public int compareTo(AppInfo p)
 {
  if(isChecked && !p.isChecked)
   return -1;
  else if(p.isChecked && !isChecked)
   return 1;
  else if(isSystem && !p.isSystem)
   return 1;
  else if(p.isSystem && !isSystem)
   return -1;
  else
   return name.compareTo(p.name);
 }
}
