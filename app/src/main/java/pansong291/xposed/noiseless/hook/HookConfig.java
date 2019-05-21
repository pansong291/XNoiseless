package pansong291.xposed.noiseless.hook;

import android.media.AudioManager;
import de.robv.android.xposed.XSharedPreferences;
import java.util.Set;
import java.util.TreeSet;
import pansong291.xposed.noiseless.MyApplication;
import pansong291.xposed.noiseless.activity.Zactivity;

public class HookConfig
{
 private static boolean inited = false;
 private static final String KEY_STREAM = "key_stream",
 KEY_PKG = "key_pkg";
 public static boolean[] noiseless = new boolean[5];
 public static Set<String> pkgs;
 
 public static boolean hasStreamType(int streamType)
 {
  return 0 <= streamType && streamType <= 10;
 }
 
 public static boolean isNoiseless(int streamType)
 {
  boolean b = false;
  switch(streamType)
  {
   case AudioManager.STREAM_VOICE_CALL:
    b = noiseless[0];
    break;
   case AudioManager.STREAM_MUSIC:
   case 9: // STREAM_TTS
   case AudioManager.STREAM_ACCESSIBILITY:
    b = noiseless[1];
    break;
   case AudioManager.STREAM_ALARM:
    b = noiseless[2];
    break;
   case 6: // STREAM_BLUETOOTH_SCO
    b = noiseless[3];
    break;
   default: // STREAM_RING
    // STREAM_SYSTEM
    // STREAM_NOTIFICATION
    // 7 STREAM_SYSTEM_ENFORCED
    // STREAM_DTMF
    b = noiseless[4];
  }
  return b;
 }
 
 public static void init()
 {
  if(inited)return;
  XSharedPreferences xsp = new XSharedPreferences(MyApplication.class.getPackage().getName());
  xsp.makeWorldReadable();
  xsp.reload();
  int value = xsp.getInt(KEY_STREAM,0);
  for(int i = 0, x = 1; i < noiseless.length; i++, x<<=1)
   noiseless[i] = (value & x) == x;
  pkgs = xsp.getStringSet(KEY_PKG, new TreeSet<String>());
  inited = true;
 }
 
 public static void init(Zactivity za)
 {
  if(inited)return;
  int value = za.getPrefs().getInt(KEY_STREAM,0);
  for(int i = 0, x = 1; i < noiseless.length; i++, x<<=1)
   noiseless[i] = (value & x) == x;
  pkgs = za.getPrefs().getStringSet(KEY_PKG, new TreeSet<String>());
  inited = true;
 }
 
 public static void save(Zactivity za)
 {
  int value = 0;
  for(int i = 0, x = 1; i < noiseless.length; i++, x<<=1)
   if(noiseless[i]) value |= x;
  za.getPrefs().edit()
  .putInt(KEY_STREAM,value)
  .putStringSet(KEY_PKG,pkgs)
  .commit();
  
 }
 
}
