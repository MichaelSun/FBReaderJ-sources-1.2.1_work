package org.geometerplus.zlibrary.ui.michaelxia.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingManager {
    private static SettingManager gSettingManager = new SettingManager();
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    
    private static final String KEY_BOOTRECEIVER_COUNT = "bt_count";
    
    public static SettingManager getInstance() {
        return gSettingManager;
    }
    
    public void setBootCount(int count) {
        mEditor.putInt(KEY_BOOTRECEIVER_COUNT, count);
        mEditor.commit();
    }
    
    public int getBootCount() {
        return mSharedPreferences.getInt(KEY_BOOTRECEIVER_COUNT, 0);
    }
    
    public void init(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();
    }
    
    private SettingManager() {
    }
}
