package pansong291.xposed.noiseless.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pansong291.xposed.noiseless.R;
import pansong291.xposed.noiseless.adapter.AppInfo;
import pansong291.xposed.noiseless.adapter.AppListAdapter;
import pansong291.xposed.noiseless.hook.HookConfig;
import pansong291.xposed.noiseless.listener.ListItemClickListener;
import pansong291.xposed.noiseless.listener.SwitchListener;

public class MainActivity extends Zactivity 
{
 Switch[] ss = new Switch[HookConfig.noiseless.length];
 TextView txt_no_apps;
 ListView listView;
 List<AppInfo> list;
 AppListAdapter appListAdapter;
 Handler handler = new Handler()
 {
  @Override
  public void handleMessage(Message msg)
  {
   if(msg.what == 1)
   {
    listView.setAdapter(appListAdapter);
    if(list == null || list.size() <= 0)
     txt_no_apps.setVisibility(View.VISIBLE);
    else
     txt_no_apps.setVisibility(View.GONE);
   }
  }
 };
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  
  HookConfig.init();
  SwitchListener sl = new SwitchListener();
  for(int i = 0; i < ss.length; i++)
  {
   ss[i] = findViewById(getResources().getIdentifier("switch" + i,"id",getPackageName()));
   ss[i].setChecked(HookConfig.noiseless[i]);
   ss[i].setOnCheckedChangeListener(sl);
  }
  txt_no_apps = findViewById(R.id.txt_no_apps);
  listView = findViewById(R.id.list_app);
  listView.setOnItemClickListener(new ListItemClickListener());
  new Thread(new Runnable()
  {
    @Override
    public void run()
    {
     List<PackageInfo> packages = getPackageManager()
     .getInstalledPackages(0);
     list = new ArrayList<>();
     AppInfo tmpInfo = null;
     for(int i = 0; i < packages.size(); i++)
     {
      PackageInfo packageInfo = packages.get(i);
      tmpInfo = new AppInfo();
      tmpInfo.icon = packageInfo.applicationInfo
      .loadIcon(MainActivity.this.getPackageManager());
      tmpInfo.name = packageInfo.applicationInfo
      .loadLabel(MainActivity.this.getPackageManager()).toString();
      tmpInfo.pkgName = packageInfo.packageName;
      tmpInfo.verCode = String.valueOf(packageInfo.versionCode);
      tmpInfo.verName = packageInfo.versionName;
      tmpInfo.isSystem = (packageInfo.applicationInfo.flags &
                          ApplicationInfo.FLAG_SYSTEM) != 0;
      tmpInfo.isChecked = HookConfig.pkgs.contains(tmpInfo.pkgName);
      list.add(tmpInfo);
     }
     Collections.sort(list);
     appListAdapter = new AppListAdapter(MainActivity.this, list);
     Message msg = new Message();
     msg.what = 1;
     handler.sendMessage(msg);
    }
   }).start();
 }

 @Override
 protected void onStop()
 {
  HookConfig.save(this);
  super.onStop();
 }
 
}
