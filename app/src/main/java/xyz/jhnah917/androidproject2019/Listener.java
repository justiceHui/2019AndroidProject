package xyz.jhnah917.androidproject2019;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.SpannableString;

import org.mozilla.javascript.*;
import org.mozilla.javascript.annotations.JSStaticFunction;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.tools.debugger.Main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

public class Listener extends NotificationListenerService {
    private static Notification.Action sess; //답장 세션 저장
    private static android.content.Context exec; //context
    private static Function responderFunc;
    private static ScriptableObject execScope;


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        notiListener(sbn);
    }

    private void notiListener(StatusBarNotification sbn){
        if (sbn.getPackageName().equals("com.kakao.talk")) { //노티의 출처가 카톡이라면
            Notification.WearableExtender watch = new Notification.WearableExtender(sbn.getNotification()); //안드로이드 웨어 생성
            for (Notification.Action action : watch.getActions()) { //안드 웨어로 노티에서 할 수 있는 모든 명령
                final String title = action.title.toString().toLowerCase();
                Log.d("asdf", title);
                if (title.contains("reply") || title.contains("Reply") || title.contains("답장")) { //명령이 전송 명령이면
                    sess = action; //세션 저장
                    exec = getApplicationContext();
                    responder(sbn.getNotification().extras.getString("android.title"), sbn.getNotification().extras.get("android.text")); //응답 함수로 넘겨줌
                }
            }
        }
    }

    public static void initScript(){
        try {
            File folder = new File(FileIO.folder);
            if (!folder.exists()) folder.mkdirs();
            File script = new File(FileIO.scriptPath);
            if (!script.exists()){
                Toast.makeText(Main3Activity.ctx, "스크립트 파일 생성중", Toast.LENGTH_SHORT).show();
                Boolean isMake = FileIO.makeFile(FileIO.scriptPath);
                if(!isMake){
                    Toast.makeText(Main3Activity.ctx, "스크립트 파일 생성 실패", Toast.LENGTH_SHORT).show();
                }
                String source = "function response(room, msg, sender){\n\n}";
                FileIO.saveFile(FileIO.scriptPath, source, false);
            }
            Context parseContext = org.mozilla.javascript.Context.enter();
            parseContext.setOptimizationLevel(-1);
            Script script_real = parseContext.compileReader(new FileReader(script), script.getName(), 0, null);
            ScriptableObject scope = parseContext.initStandardObjects();
            ScriptableObject.defineClass(scope, Kakaotalk.class);
            execScope = scope;
            script_real.exec(parseContext, scope);
            responderFunc = (Function) scope.get("response", scope);
            Context.exit();
        }catch(Exception e){
            FileIO.saveFile(FileIO.logPath, e.toString() + "\n\n", true);
            Process.killProcess(Process.myPid());
        }
    }

    private void responder(String room, Object _msg) {
        if (responderFunc == null || execScope == null) initScript();
        Context parseContext = Context.enter();
        parseContext.setOptimizationLevel(-1);

        String sender;
        String msg;
        if (_msg instanceof String) {
            sender = room;
            msg = (String) _msg;
        } else {
            String html = Html.toHtml((SpannableString) _msg);
            sender = Html.fromHtml(html.split("<b>")[1].split("</b>")[0]).toString();
            msg = Html.fromHtml(html.split("</b>")[1].split("</p>")[0].substring(1)).toString();
        }

        if(MainActivity.power == false) return;

        try {
            Log.d("response", "aaa" + room + " " + msg + " " + sender);
            xyz.jhnah917.androidproject2019.Script.main(room, msg, sender);
            responderFunc.call(parseContext, execScope, execScope, new Object[]{room, msg, sender});
        }catch(Exception e){
            FileIO.saveFile(FileIO.logPath, e.toString() + "\n\n", true);
        }
    }
    public static void send(String value) {
        if (sess == null) return;
        Intent sendIntent = new Intent();
        Bundle msg = new Bundle();
        for (RemoteInput inputable : sess.getRemoteInputs()) msg.putCharSequence(inputable.getResultKey(), value);
        RemoteInput.addResultsToIntent(sess.getRemoteInputs(), sendIntent, msg);

        try {
            sess.actionIntent.send(exec, 0, sendIntent);
        } catch (PendingIntent.CanceledException e) {

        }
    }

    public static class Kakaotalk extends ScriptableObject{
        public Kakaotalk(){
            super();
        }

        @Override
        public String getClassName(){
            return "Kakaotalk";
        }

        @JSStaticFunction
        public static void replyLast(String str){
            xyz.jhnah917.androidproject2019.Listener.send(str);
        }

        @JSStaticFunction
        public static String getWebText(String url){
            return Util.WWW(url);
        }
    }

}
