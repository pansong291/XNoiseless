package pansong291.xposed.noiseless.hook;

import android.media.AudioManager;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage
{
 private static final boolean DEBUG = true;
 public static final String TAG = "LOG_E_TAG";
 public static String pkgN = "unknown";

 @Override
 public void handleLoadPackage(LoadPackageParam p1) throws Throwable
 {
  HookConfig.init();
  
  if(!HookConfig.pkgs.contains(p1.packageName))
   return;
   
  pkgN = p1.packageName;
  
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
