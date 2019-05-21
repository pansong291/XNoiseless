package pansong291.xposed.noiseless.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import pansong291.crash.ASControl;
import android.content.SharedPreferences;

public class Zactivity extends Activity
{
 private SharedPreferences sp;

 @Override
 protected void onResume()
 {
  super.onResume();
  
 }
 
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  ASControl.getASControl().addActivity(this);
  
 }

 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  ASControl.getASControl().removeActivity(this);
 }
 
 public SharedPreferences getPrefs()
 {
  if(sp == null)
   sp = getSharedPreferences(getPackageName() + "_preferences", MODE_WORLD_READABLE);
  return sp;
 }
 
 public void toast(String s)
 {
  toast(s, 0);
 }
 
 public void toast(String s, int i)
 {
  Toast.makeText(this, s, i).show();
 }
 
}
