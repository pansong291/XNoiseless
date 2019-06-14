package pansong291.xposed.noiseless.hook;

import android.media.AudioManager;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import android.app.Activity;
import android.os.Bundle;

public class MainHook implements IXposedHookLoadPackage
{
 private static final boolean DEBUG = true;
 public static final String TAG = "LOG_E_TAG";
 public static String pkgN = "unknown";

 @Override
 public void handleLoadPackage(LoadPackageParam p1) throws Throwable
 {
  if(p1.packageName.equals("pansong291.xposed.noiseless"))
  {
   log("try to hook self");
   /*
    * hook自身时，如果直接用MainActivity.class会没有任何效果
    * 使用类名字符串加classLoader才有效果
    * 不清楚是什么原因
    */
   XposedHelpers.findAndHookMethod(
   "pansong291.xposed.noiseless.activity.MainActivity",p1.classLoader,
   "setModuleActive",boolean.class,
   new XC_MethodHook()
   {
     protected void beforeHookedMethod(MethodHookParam param) throws Throwable
     {
      param.args[0] = true;
      log("hook self success");
     }
   });
   return;
  }
  
  HookConfig.init();
  
  if(!HookConfig.pkgs.contains(p1.packageName))
   return;
   
  pkgN = p1.packageName;
  
  XposedHelpers.findAndHookMethod(Activity.class,
   "onCreate",Bundle.class,
   new XC_MethodHook()
   {
    protected void afterHookedMethod(MethodHookParam param) throws Throwable
    {
     log("onCreate____");
     Activity act = (Activity) param.thisObject;
     AudioManager am = act.getSystemService(AudioManager.class);
     for(int i=0;i<=10;i++)
     {
      if(am.getStreamVolume(i) != am.getStreamMinVolume(i))
       am.setStreamVolume(i,0,0);
     }
    }
   });
  
  XposedHelpers.findAndHookMethod(AudioManager.class,
  "setStreamVolume",int.class,int.class,int.class,
  new XC_MethodHook()
  {
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable
    {
     log("set____");
     log("stream="+param.args[0]+", progress="+param.args[1]);
     if(!HookConfig.hasStreamType(param.args[0]))return;
     if(!HookConfig.isNoiseless(param.args[0]))
     {
      log("options are not enabled");
      return;
     }
     param.args[1]=0;
     log("changed");
    }
  });
  
  XposedHelpers.findAndHookMethod(AudioManager.class,
  "adjustStreamVolume",int.class,int.class,int.class,
  new XC_MethodHook()
  {
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable
    {
     log("adjust____");
     log("stream="+param.args[0]+", type="+param.args[1]);
     if(!HookConfig.hasStreamType(param.args[0]))return;
     if(!HookConfig.isNoiseless(param.args[0]))
     {
      log("options are not enabled");
      return;
     }
     param.args[1]=AudioManager.ADJUST_LOWER;
     log("changed");
    }
  });
  
  XposedHelpers.findAndHookMethod(AudioManager.class,
   "adjustSuggestedStreamVolume",int.class,int.class,int.class,
   new XC_MethodHook()
   {
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable
    {
     log("adjust__sug__");
     log("stream="+param.args[1]+", type="+param.args[0]);
     if(!HookConfig.hasStreamType(param.args[1]))return;
     if(!HookConfig.isNoiseless(param.args[1]))
     {
      log("options are not enabled");
      return;
     }
     param.args[0]=AudioManager.ADJUST_LOWER;
     log("changed");
    }
   });
   
  XposedHelpers.findAndHookMethod(AudioManager.class,
   "adjustVolume",int.class,int.class,
   new XC_MethodHook()
   {
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable
    {
     log("adjust__def__");
     log("type="+param.args[0]);
     if(!HookConfig.isNoiseless(AudioManager.STREAM_MUSIC))
     {
      log("options are not enabled");
      return;
     }
     param.args[0]=AudioManager.ADJUST_LOWER;
     log("changed");
    }
   });
   
 }
 
 public static void log(String str)
 {
  if(DEBUG)
   XposedBridge.log(TAG+", "+pkgN+", "+str);
 }
 
}
