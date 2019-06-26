package xyz.jhnah917.androidproject2019;

import android.util.Log;

import java.io.*;
import java.lang.*;

public class FileIO {
    public static String folder = "mnt/sdcard/SunrinBot/";
    public static String logPath = folder + "log.txt";
    public static String scriptPath = folder + "response.js";
    public static String powerPath = folder + "power.txt";
    public static String prefixPath = folder + "prefix.txt";

    public static void saveFile(String path, String content, boolean bool){
        try{
            File file = new File(path);
            if(!file.exists()) return;
            FileWriter fw = new FileWriter(file, bool);
            BufferedWriter bw = new BufferedWriter(fw);
            String str = new String(content);
            bw.write(str);
            bw.close();
            fw.close();
        }catch(IOException e){

        }
    }

    public static boolean makeFile(String path){
        try{
            File file = new File(path);
            if(file.exists()) return true;
            if(file.createNewFile()) return true;
            else return false;
        }catch(IOException e){
            return false;
        }
    }

    public static String readFile(String path){
        //Log.d("asdf", "readFile!");
        try {
            File file = new File(path);
            if(!file.exists()) return null;
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String str = br.readLine();
            String line = "";
            while( (line = br.readLine()) != null ){
                str += '\n' + line;
            }
            fis.close();
            isr.close();
            br.close();
            //Log.d("asdf", str);
            return str;
        }catch(IOException e){
            return "";
        }
    }
}
