package pansong291.xposed.noiseless;

import pansong291.xposed.noiseless.activity.CrashActivity;
import pansong291.crash.CrashApplication;

public class MyApplication extends CrashApplication
{
 @Override
 public Class<?> getPackageActivity()
 {
  setShouldShowDeviceInfo(true);
  return CrashActivity.class;
 }

}
