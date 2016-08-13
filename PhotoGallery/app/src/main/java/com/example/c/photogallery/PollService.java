package com.example.c.photogallery;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;

// 알람 매니저가 일정시간마다 호출하게끔..
public class PollService extends IntentService {
    private static final int POLL_INTERVAL = 1000 * 10; // 60초

    public PollService() {
        super("PollService");
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = PollService.newIntent(context);
        PendingIntent pIntent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (isOn) {
            am.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    POLL_INTERVAL,
                    pIntent);
        } else {
            am.cancel(pIntent);
            pIntent.cancel();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (isNetworkAvailable() == false) {
            return;
        }

        // Fragment 자신은 context가 없고 Fragment를 호스팅하는 Activity에 있어서
        // getActivity()를 넘기지만, 서비스는 this 자신을 넘기면 된다.
        String query = QueryPreperence.getStoredQuery(this);
        String lastResultId = QueryPreperence.getLastResultId(this);
        List<GalleryItem> items;

        // 서비스는 UI쪽이 없기 때문에 워킹쓰레드에서 바로 해도 상관없다.
        if (query == null) {
            items = new FlickrFetchr().fetchRecentPhotos();
        } else {
            items = new FlickrFetchr().searchPhotos(query);
        }

        if (items.size() == 0) {
            return;
        }

        String resultId = items.get(0).getId();
        if (resultId.equals(lastResultId)) {
            Log.d("PollService", "Got old data");
        } else {
            Log.d("PollService", "Got new data");
        }

        QueryPreperence.setLastResultId(this, resultId);
    }

    private boolean isNetworkAvailable() {
        // ACCESS_NETWORK_STATE 퍼미션이 필요하다.
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // 와이파이나 LTE가 켜져있는지..
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;

        // 와이파이만 켜져있을 경우라면.. 와이파이가 AP에 연결이 되어있느냐..
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent intent = PollService.newIntent(context);
        PendingIntent pIntent =
                PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        // FLAG_NO_CREATE 플래그로 생성되어 있는지 없는지 확인
        // 이미 세팅이 되어있으면 true
        return pIntent != null;
    }
}
