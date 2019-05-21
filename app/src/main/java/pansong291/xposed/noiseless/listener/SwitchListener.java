package pansong291.xposed.noiseless.listener;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import pansong291.xposed.noiseless.R;
import pansong291.xposed.noiseless.hook.HookConfig;

public class SwitchListener implements OnCheckedChangeListener
{

 @Override
 public void onCheckedChanged(CompoundButton p1, boolean p2)
 {
  int index = p1.getId() - R.id.switch0;
  HookConfig.noiseless[index] = p2;
 }
 
}
