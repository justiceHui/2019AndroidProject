package xyz.jhnah917.androidproject2019;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Util {

    public static void restartApp() {
        Context context = MainActivity.ctx;
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        System.exit(0);
    }

    private static String WWWResult = "";
    private static boolean WWWEnd = false;
    private static String WWWLink = "";

    public static String WWW(String link) {
        Util.WWWLink = link;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL(Util.WWWLink);
                    URLConnection con = url.openConnection();
                    if(con != null) {
                        con.setConnectTimeout(5000);
                        con.setUseCaches(false);
                        InputStreamReader isr = new InputStreamReader(con.getInputStream());
                        BufferedReader br = new BufferedReader(isr);
                        String str = br.readLine();
                        String line = "";
                        while((line = br.readLine()) != null) {
                            str += "\n" + line;
                        }
                        br.close();
                        isr.close();
                        WWWResult = str;
                        WWWEnd = true;

                    }
                } catch(Exception e) {
                    WWWResult = e.toString();
                    WWWEnd = true;
                }
            }
        });
        t.start();
        String ret = "";
        while(true){
            if(WWWEnd){
                WWWEnd = false;
                WWWLink = "";
                ret = WWWResult;
                break;
            }
        }
        return ret;
    }

    public static String strJoin(String[] arr, String something) {
        String ret = "";
        try {
            for (int i=0; i<arr.length; i++) {
                ret += arr[i];
                ret += something;
            }
            return ret;
        } catch (Exception e) {
            return e.toString();
        }
    }

    public static void setGlobalFont(Context context, View view){
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int len = vg.getChildCount();
                for (int i = 0; i < len; i++) {
                    View v = vg.getChildAt(i);
                    if (v instanceof TextView) {
                        ((TextView) v).setTypeface(Typeface.createFromAsset(
                                context.getAssets(), "나눔스퀘어라운드 Regular.ttf"));
                    }
                    setGlobalFont(context, v);
                }
            }
        }
    }

}
