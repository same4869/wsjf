package com.dt.wsjf.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.dt.wsjf.R;
import com.dt.wsjf.bean.NotificationBean;
import com.dt.wsjf.json.JSONFormatExcetion;
import com.dt.wsjf.json.JSONToBeanHandler;
import com.dt.wsjf.ui.MainActivity;
import com.dt.wsjf.utils.LogUtil;

import cn.bmob.push.PushConstants;

/**
 * Created by wangxun on 2018/3/13.
 */

public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            String datas = intent.getStringExtra("msg");
            LogUtil.d("客户端收到推送内容：" + datas);
            try {
                NotificationBean notificationBean = JSONToBeanHandler.fromJsonString(datas, NotificationBean.class);
                setupNotification(context, notificationBean.getTitle(), notificationBean.getContent());
            } catch (JSONFormatExcetion jsonFormatExcetion) {
                jsonFormatExcetion.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupNotification(Context context, String title, String content) {
        LogUtil.d("setupNotification");
        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle(title)
                //设置通知内容
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_VIBRATE);
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1

        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, new Intent(context, MainActivity.class), 0);

        builder.setContentIntent(contentIntent);

        notifyManager.notify(1, builder.build());
    }

}
