package org.amse.ys.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.*;

import android.util.Log;

public class ZipUtil {
    private static final String TAG = "ZipUtil";
    private static boolean DEBUG = false;
    
    private static final int BUFFER_SIZE = 8192;
    protected static byte buf[] = new byte[BUFFER_SIZE];
    
    //private static List<Object> mTmpBuffer = new ArrayList<Object>();
    
    /**
     * out put a file from a InputStream to path and the out file is named as
     * filename. if the path is exists and is not a dir will delete the path,
     * and recreate it. if the path is not exists, create the path.
     * 
     * @param is
     *            InputStream contain the file data. point to a single file data
     * @param targetPath
     *            path point to the out file place. The path must end with
     *            system path seprator
     * @param filename
     *            fileName for the out file
     * @return true if out put file successfully. false if failed. if
     *         inputstream is null or the targetPath is null or the filename is
     *         null return false. if the targetPath is empty string or the
     *         filename is empty string return false.
     */
    public static boolean outputFile(InputStream is, String targetPath, String filename) {
        if (is == null || targetPath.equals("") || targetPath == null 
                || filename.equals("") || filename == null) {
            return false;
        }
        try {
            checkDirectory(targetPath);
            doOutputFile(is, targetPath + filename);
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /*
    public static boolean outputSubFile(InputStream is, String subFile, String targetPath, String filename) {
        if (is == null || targetPath.equals("") || targetPath == null 
                || filename.equals("") || filename == null
                || subFile.equals("") || subFile == null) {
            return false;
        }
        ZipInputStream in = new ZipInputStream(is);
        ZipEntry entry = null;
        try {
            while ((entry = in.getNextEntry()) != null) {
                if (entry.getName().equals(subFile) == true && entry.isDirectory() == false) {
                    checkDirectory(targetPath);
                    doOutputFile(in, targetPath + filename);
                    in.closeEntry();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    */
    /**
     * Unzip a inputstream data to the target path. if the path is exists, and
     * is not a dir will delete the path, and recreate it. if the path is not
     * exists, create the path. if the inputstream is not a package contain some
     * files and dirs, will out put the data to the target and create all the
     * files and dirs and the targetPath.
     * 
     * @param is
     *            is InputStream contain the files data.
     * @param targetPath
     *            targetPath must be end with system path seprator
     * @return true if unzip successflly, false if failed. false if the
     *         inputstream is null or the targetPath is null or the targetPath
     *         is empty string.
     */
    public static boolean UnZipFile(InputStream is, String targetPath, String excludeFilename) {
        if (is == null || targetPath.equals("") || targetPath == null) {
            return false;
        }
        try {
            checkDirectory(targetPath);
            extZipFile(is, targetPath, excludeFilename);
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean UnZipFile(InputStream is, String targetPath) {
        return UnZipFile(is, targetPath, null);
    }

    /**
     * Unzip a source to the targetPath, is similar with the
     * UnZipFile(InputStream is, String targetPath)
     * 
     * @param source
     *            source file to unzip
     * @param targetPath
     *            targetPath to place out files.
     * @return true if successfully, false if failed. false if the source file
     *         is not exists.
     */
    public static boolean UnZipFile(String source, String targetPath) {
        return UnZipFile(source, targetPath, null);
    }
   
    public static boolean UnZipFile(String source, String targetPath, String excludeFilename) {
        File file = new File(source);
        if (file.exists() == false) {
            return false;
        }
        try {
            return UnZipFile(new FileInputStream(file), targetPath, excludeFilename);
        } catch (Exception e) {
        }
        return false;
    }
    
    /**
     * unzip a subdir in the source file to the targetPath. similar with the
     * UnzipSubDir(InputStream is, String subdir, String targetPath)
     * 
     * @param source
     *            source zip files
     * @param subdir
     *            subdir want to unzip in the source
     * @param target
     *            targetPath to place out files
     * @return true if successfully. false if failed. failed if source file is
     *         not exists.
     */
    /*
    public static boolean UnzipSubDir(String source, String subdir, String targetPath) {
        File file = new File(source);
        if (file.exists() == false) {
            return false;
        }
        try {
            return UnzipSubDir(new FileInputStream(file), subdir, targetPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    */
    
    /**
     * unzip subdir in the InputStream to the targePath. if target path is not
     * exists, create the dir, if the target path is exists and not a dir,
     * delete it.
     * 
     * @param is
     *            InputStream contain the files data
     * @param subdir
     *            subdir want to out put
     * @param targetPath
     *            targetPath to place the out files
     * @return true if successfully, false if failed. false if the inputstream
     *         is null or the subdir is null or the targetPath is null or the
     *         subdir is empty or the targetPath is empty.
     */
//    public static boolean UnzipSubDir(InputStream is, String subdir, String targetPath) {
//        LOGD(" In UnzipSubDir : subdir = " + subdir + "  target = " + targetPath);
//        if (subdir == null || targetPath == null || is == null || subdir.equals("") 
//                || targetPath.equals("")) {
//            return false;
//        }
//        checkDirectory(targetPath);
//        
//        ZipInputStream in = new ZipInputStream(is);
//        ZipEntry entry = null;
//        try {
//            while ((entry = in.getNextEntry()) != null) {
//                if (entry.getName().startsWith(subdir) == false) continue;
//                final String fullName = targetPath + entry.getName().substring(subdir.length());
//                if (entry.isDirectory() == true) {
//                    File file = new File(fullName);
//                    if (file.exists() == false) {
//                        file.mkdirs();
//                    }
//                } else {
//                    doOutputFile(in, fullName);
//                    in.closeEntry();
//                }
//            }
//            in = null;
//            return true;
//        } catch (IOException e) {
//            in = null;
//            e.printStackTrace();
//        }
//        return false;
//    }

    /**
     * @deprecated unzip the zip file to memeory, return a map
     */
//    public static Map<String, byte[]> UnZipFileToMem(String source) {
//        File file = new File(source);
//        if (file.exists() == false) {
//            return null;
//        }
//        try {
//            return UnZipFileToMem(new FileInputStream(file));
//        } catch (Exception e) {
//        }
//        return null;
//    }
    
    /**
     * @deprecated unzip the zip InputStream to memeory, return a map 
     */
//    public static Map<String, byte[]> UnZipFileToMem(InputStream is) {
//        Map<String, byte[]> ret = new HashMap<String, byte[]>();
//        List<byte[]> dataList = new ArrayList<byte[]>();
//        List<Integer> dataSizeList = new ArrayList<Integer>();
//        try {
//            ZipInputStream in = new ZipInputStream(is);
//            ZipEntry entry = null;
//            while ((entry = in.getNextEntry()) != null) {
//                LOGD(" In UnZipFileToMem, the entry name:" + entry.getName());
//                if (entry.isDirectory() == true) {
//                    ret.put(entry.getName(), null);
//                } else {
//                    int lenRead = 0;
//                    int totalRead = 0;
//                    do {
//                        byte[] data = new byte[BUFFER_SIZE];
//                        lenRead = in.read(data, 0, BUFFER_SIZE);
//                        if (lenRead > 0) {
//                            dataList.add(data);
//                            dataSizeList.add(lenRead);
//                            totalRead += lenRead;
//                        }
//                    } while (lenRead > 0);
//                    in.closeEntry();
//                    byte[] entryData = new byte[totalRead];
//                    int index = 0;
//                    for (int count=0; count<dataList.size(); ++count) {
//                        byte[] data = dataList.get(count);
//                        Integer length = dataSizeList.get(count);
//                        copy(data, entryData, index, length);
//                        index += length;
//                    }
//                    ret.put(entry.getName(), entryData);
//                    dataSizeList.clear();
//                    dataList.clear();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return ret;
//    }

    /**
     * get the files in the zip file.
     * 
     * @param fileName zip file name. is a full path for the file.
     * @return return a List contain the files in the zip file. if the
     * file is not exists return null.
     */
//    public static List<String> getZipFileList(String fileName) {
//        try {
//            return getZipFileList(new FileInputStream(new File(fileName)));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    
    /**
     * get the file list in the inputstream data
     * 
     * @param is the inputstream data
     * @return the file list contain in the inputstream. if the is is null
     * return null
     */
//    public static List<String> getZipFileList(InputStream is) {
//        List<String> ret = new ArrayList<String>();
//        try {
//            ZipInputStream in = new ZipInputStream(is);
//            ZipEntry entry = null;
//            while ((entry = in.getNextEntry()) != null) {
//                ret.add(entry.getName());
//            }
//            return ret;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    
//    private static void copy(byte[] src, byte[] dest, int offset, int length) {
//        for (int index = 0; index < length; ++index) {
//            dest[offset + index] = src[index];
//        }
//    }

    private static void doOutputFile(InputStream is, String filename)
            throws IOException, FileNotFoundException {
        FileOutputStream os = new FileOutputStream(filename);
        BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER_SIZE);
        int len;
        while ((len = is.read(buf, 0, BUFFER_SIZE)) > 0) {
            bos.write(buf, 0, len);
        }
        bos.flush();
        bos.close();
        os.close();
//        mTmpBuffer.add(os);
//        mTmpBuffer.add(bos);
    }

    private static void extZipFile(InputStream is, String extPlace, String excludeFilename) {

        ZipInputStream in = new ZipInputStream(is);
        ZipEntry entry = null;

        try {
            while ((entry = in.getNextEntry()) != null) {
//                LOGD(" entry file = " + entry.getName());
                if (excludeFilename != null && entry.getName().startsWith(excludeFilename) == true) {
//                    LOGD(" exclude file = " + excludeFilename + " for unzip process");
                    continue;
                }
                final String fullName = extPlace + entry.getName();
                if (entry.isDirectory()) {
                    File file = new File(fullName);
                    file.mkdirs();
                } else {
                    doOutputFile(in, fullName);
                    in.closeEntry();
                }
            }
        } catch (IOException e) {
        } finally {
            in = null;
        }
    }

    private static boolean checkDirectory(String dir) {
        File dirObj = new File(dir);
        if (dirObj.exists()) {
            if (!dirObj.isDirectory()) {
                dirObj.delete();
            }
            return false;
        } 
        if (!dirObj.exists()) {
            dirObj.mkdirs();
        }
        return true;
    }

    /**
     * iterate the dir and add files
     * 
     * @param jos
     *            - JAR output stream
     * @param file
     *            - dir/file name
     * @param pathName
     *            - dir name in ZIP
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static void recurseFiles(ZipOutputStream zos, File file, String pathName) throws IOException,
                                                                                     FileNotFoundException {
        if (file.isDirectory()) {
            pathName = pathName + file.getName() + "/";
            zos.putNextEntry(new ZipEntry(pathName));
            String fileNames[] = file.list();
            if (fileNames != null) {
                for (int i = 0; i < fileNames.length; i++)
                    recurseFiles(zos, new File(file, fileNames[i]), pathName);

            }
        } else {
            ZipEntry zipEntry = new ZipEntry(pathName + file.getName());
            // System.out.println(pathName + "  " + file.getName());
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fin);
            zos.putNextEntry(zipEntry);

            int len;
            while ((len = in.read(buf)) >= 0) {
                zos.write(buf, 0, len);
            }
            in.close();
            zos.closeEntry();
        }
    }

    /**
     * Create ZIP/JAR file.
     * 
     * @param directory
     *            - the dir to be added
     * @param zipFile
     *            - the ZIP filename
     * @param zipFolderName
     *            - pathname in ZIP
     * @param level
     *            - compress level (0~9)
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void makeDirectoryToZip(File directory, File zipFile, String zipFolderName, int level)
                                                                                                        throws IOException,
                                                                                                        FileNotFoundException {
        level = checkZipLevel(level);

        if (zipFolderName == null) {
            zipFolderName = "";
        }

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zos.setLevel(level);

        String fileNames[] = directory.list();
        if (fileNames != null) {
            for (int i = 0; i < fileNames.length; i++) {
                recurseFiles(zos, new File(directory, fileNames[i]), zipFolderName);
            }
        }
        zos.close();
    }
    
    public static void makeFilesToZip(ArrayList<String> fileList, File zipFile, String zipFolderName, int level) 
                            throws IOException, FileNotFoundException {
        level = checkZipLevel(level);
        
        if (fileList == null || (fileList != null && fileList.size() == 0)) {
            return;
        }
        
        if (zipFolderName == null) {
            zipFolderName = "";
        }
        
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zos.setLevel(level);
        
        for (String filename : fileList) {
            recurseFiles(zos, new File(filename), zipFolderName);
        }
        zos.close();
    }
//    public static void recycle() {
//        mTmpBuffer.clear();
//    }
    
    /**
     * Check and set the effective compress level.
     * 
     * @param level
     *            - compress level
     * @return the effective/default compress level
     */
    public static int checkZipLevel(int level) {
        if (level < 0 || level > 9)
            level = 7;
        return level;
    }
    
    public static final boolean deleteDir(File dir) {
        dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    file.delete();
                }
                return false;
            }
        });
        dir.delete();
        return true;
    }
    //=========================================================================
    private static void LOGD(String s) {
        if (DEBUG) {
            Log.d(TAG, s);
        }
    }
}

