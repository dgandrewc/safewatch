package com.capston.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.location.LocationListener;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback{
    /* **************************************** 전역변수 선언 **************************************** */
    protected LocationManager locationManager;
    private GoogleMap mMap;
    double latitude=0;
    double longitude=0;
    MarkerOptions markerOptions = new MarkerOptions();
    MarkerOptions dmarkerOptions = new MarkerOptions();
    MarkerOptions pmarkerOptions = new MarkerOptions();
    MarkerOptions cmarkerOptions = new MarkerOptions();
    MarkerOptions kmarkerOptions = new MarkerOptions();
    List<Marker> markerList = new ArrayList<Marker>();
    Marker myMarker, dMarker, cMarker, pMarker, kMarker;
    LatLng Current;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;
    Handler mBluetoothHandler;
    Handler kidsHandler;
    Handler criminalHandler;
    Handler policeHandler;
    Handler dangerHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;
    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static int CRIMINAL_INT = 100;
    public static int DANGER_INT = 101;
    public static int POLICE_INT=102;
    public static int KIDS_INT=103;
    public static int flag = 0;
    public static String user;
    public String kidid="0";
    double DAddress_lat, DAddress_lon;

    /* **************************************** 전역변수 선언 **************************************** */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(user.equals("2")) {
            getMenuInflater().inflate(R.menu.menu2, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_btn1:
                playBtn1();
                return true;
            case R.id.action_btn2:
                playBtn2();
                return true;
            case R.id.action_btn3:
                playBtn3();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void playBtn1(){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
        final TextView text0=new TextView(MainActivity.this);
        text0.setText("   이전 비밀번호");
        final EditText pw0 = new EditText(MainActivity.this);
        final TextView text1=new TextView(MainActivity.this);
        text1.setText("   비밀번호 입력");
        final EditText pw1 = new EditText(MainActivity.this);
        final TextView text2=new TextView(MainActivity.this);
        text2.setText("   비밀번호 확인");
        final EditText pw2 = new EditText(MainActivity.this);
        pw1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        pw2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        SharedPreferences sf= getSharedPreferences("sFile", MODE_PRIVATE);
        final String password = sf.getString("password","");

        if(password.equals("")) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(text1);
            ll.addView(pw1);
            ll.addView(text2);
            ll.addView(pw2);
            alert_confirm.setView(ll);

            alert_confirm.setCancelable(false);
            alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String value1 = pw1.getText().toString();
                    String value2 = pw2.getText().toString();
                    if (value1.equals(value2)) {
                        SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("password", value1);
                        editor.commit();
                    } else {
                        Toast.makeText(MainActivity.this, "비밀번호 오류", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(text0);
            ll.addView(pw0);
            ll.addView(text1);
            ll.addView(pw1);
            ll.addView(text2);
            ll.addView(pw2);
            alert_confirm.setView(ll);

            alert_confirm.setCancelable(false);
            alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String value0=pw0.getText().toString();
                    String value1 = pw1.getText().toString();
                    String value2 = pw2.getText().toString();
                    if(value0.equals(password)) {
                        if (value1.equals(value2)) {
                            SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("password", value1);
                            editor.commit();
                        } else {
                            Toast.makeText(MainActivity.this, "비밀번호 오류", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "비밀번호 오류", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        AlertDialog alert = alert_confirm.create();
        alert.setIcon(R.drawable.police);
        alert.setTitle("비밀번호 설정");
        alert.show();
    }

    public void playBtn2(){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
        alert_confirm.setMessage("취소 비밀번호");
        final EditText et=new EditText(MainActivity.this);
        alert_confirm.setView(et);
        alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sf= getSharedPreferences("sFile", MODE_PRIVATE);
                String password = sf.getString("password","");
                String value=et.getText().toString();
                if (value.equals(password)) {
                    new Thread() {
                        public void run() {
                            getHttpConnection("danger", "DELETE");
                        }
                    }.start();
                    dialogInterface.dismiss();
                }
                else{
                    Toast.makeText(MainActivity.this, "비밀번호 오류", Toast.LENGTH_LONG).show();
                }
            }
        });

        AlertDialog alert = alert_confirm.create();
        alert.setIcon(R.drawable.police);
        alert.setTitle("신고 취소");
        alert.show();
    }
    public void playBtn3(){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
        alert_confirm.setMessage("보호자 휴대폰번호");
        final EditText parentPhone=new EditText(MainActivity.this);
        alert_confirm.setView(parentPhone);
        alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread() {
                    public void run() {
                        try {
                            String LocationEndpoint = "http://180.69.10.151:8000/kids/";
                            getCurrentLocation();
                            DAddress_lat = latitude;
                            DAddress_lon = longitude;
                            String json = "{\"parentPhone\":\""+parentPhone.getText().toString()+"\",\"latitude\":\"" + latitude + "\", \"longitude\":\"" + longitude + "\"}";
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(LocationEndpoint)
                                    .post(RequestBody.create(MediaType.parse("application/json"), json))
                                    .build();
                            Response response = client.newCall(request).execute();
                        }catch(Exception e){System.out.println("AAAA1"+e);}
                        JSONArray obj=getHttpConnection("kids", "GET");
                        try {
                            for (int i = 0; i < obj.length(); i++) {
                                String id = obj.getJSONObject(i).get("id").toString();
                                double tlat = Double.valueOf((String) obj.getJSONObject(i).get("latitude"));
                                double tlon = Double.valueOf((String) obj.getJSONObject(i).get("longitude"));
                                if (tlat - DAddress_lat < 0.0000000001 && tlon - DAddress_lon < 0.0000000001) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("myid", id);
                                    editor.commit();
                                }
                            }
                        }catch(Exception e){System.out.println("AAAA2"+e);}
                    }
                }.start();
            }
        });

        AlertDialog alert = alert_confirm.create();
        alert.setIcon(R.drawable.police);
        alert.setTitle("신고 취소");
        alert.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        user=intent.getExtras().getString("user");
        if(user.equals("1"))
        {
            new Thread(){
                public void run(){
                    Message msg = new Message();
                    msg.what = KIDS_INT;
                    msg.obj = getHttpConnection("kids", "GET");
                    MainActivity.this.kidsHandler.sendMessage(msg);
                }
            }.start();
        }
        /*
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {//
            Toast.makeText(this, "SMS 권한 설명 필요함", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECEIVE_SMS
            }, 1);
        }*
        /* **************************************** 객체 초기화 **************************************** */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if(user.equals("2")) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
            listPairedDevices();
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /* **************************************** 객체 초기화 **************************************** */
        /* **************************************** 핸들러 **************************************** */
        kidsHandler=new Handler(){
            public void handleMessage(android.os.Message msg){
                if (msg.what == KIDS_INT) {
                    try{
                    JSONArray obj= (JSONArray) msg.obj;
                    TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String PhoneNum = telManager.getLine1Number();
                    if(PhoneNum.startsWith("+82")){
                        PhoneNum = PhoneNum.replace("+82", "0");
                    }
                        for (int i = 0; i < obj.length(); i++) {
                            String phone=(String)obj.getJSONObject(i).get("parentPhone");
                            if(phone.equals(PhoneNum)) {
                                kidid=String.valueOf(obj.getJSONObject(i).get("id"));
                                double tlat = Double.valueOf((String) obj.getJSONObject(i).get("latitude"));
                                double tlon = Double.valueOf((String) obj.getJSONObject(i).get("longitude"));
                                Current = new LatLng(tlat, tlon);
                                kmarkerOptions.position(Current);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        kMarker = mMap.addMarker(kmarkerOptions);
                                    }
                                });
                            }
                        }
                    }catch (SecurityException e){System.out.println("AAAA3"+e);}catch(Exception e){System.out.println("AAAA4"+e);}
                }
            }
        };
        mBluetoothHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(flag==0) {
                    if (msg.what == BT_MESSAGE_READ) {
                    String phoneNo = "01050369295";
                    String sms = getCurrentAddrss(latitude, longitude)+"위치에서 긴급상황 발생";
                    /*try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                        Toast.makeText(getApplicationContext(), "경찰 출동중", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }*/
                        flag++;
                        new Thread() {
                            public void run() {
                                getHttpConnection("danger", "POST");
                            }
                        }.start();
                    }
                }
            }
        };

        criminalHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if (msg.what == CRIMINAL_INT) {
                    JSONArray obj= (JSONArray) msg.obj;
                    try {
                        for (int i = 0; i < obj.length(); i++) {
                            double tlat = Double.valueOf((String) obj.getJSONObject(i).get("latitude"));
                            double tlon = Double.valueOf((String) obj.getJSONObject(i).get("longitude"));
                            Current = new LatLng(tlat, tlon);
                            cmarkerOptions.position(Current);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    cMarker = mMap.addMarker(cmarkerOptions);
                                }
                            });
                        }
                    }catch(Exception e){System.out.println("AAAA5"+e);}
                }
            }
        };

        policeHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if (msg.what == POLICE_INT) {
                    JSONArray obj= (JSONArray) msg.obj;
                    try {
                        for (int i = 0; i < obj.length(); i++) {
                            double tlat = Double.valueOf((String) obj.getJSONObject(i).get("latitude"));
                            double tlon = Double.valueOf((String) obj.getJSONObject(i).get("longitude"));
                            Current = new LatLng(tlat, tlon);
                            pmarkerOptions.position(Current);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    pMarker = mMap.addMarker(pmarkerOptions);
                                }
                            });
                        }
                    }catch(Exception e){System.out.println("AAAA6"+e);}
                }
            }
        };

        dangerHandler=new Handler(){
            @Override
            public void handleMessage(android.os.Message msg){
                if(msg.what==DANGER_INT){
                    JSONArray obj=(JSONArray) msg.obj;
                    try{
                        for(int i=0; i<obj.length(); i++){
                            double tlat=Double.valueOf((String)obj.getJSONObject(i).get("latitude"));
                            double tlon=Double.valueOf((String)obj.getJSONObject(i).get("longitude"));
                            double dist=getDistance(tlat, tlon, latitude, longitude);
                            if(dist<1000) {
                                SoundPool siren=new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
                                int sirenSong=siren.load(getApplicationContext(),R.raw.siren, 1);
                                Current=new LatLng(tlat, tlon);
                                dmarkerOptions.position(Current);
                                siren.play(sirenSong, 999999999, 999999999, 1, 0, 1);
                                PendingIntent mPendingIntent=PendingIntent.getActivity(MainActivity.this, 0,
                                        new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(MainActivity.this)
                                        .setSmallIcon(R.drawable.police)
                                        .setContentTitle("위험")
                                        .setContentText("근처에 위험에 처한 아이가 있습니다.")
                                        .setDefaults(Notification.DEFAULT_VIBRATE)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
                                        .setContentIntent(mPendingIntent);
                                NotificationManager mNotificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                mNotificationManager.notify(0, mBuilder.build());
                                runOnUiThread(new Runnable(){
                                    public void run(){
                                        dMarker=mMap.addMarker(dmarkerOptions);
                                        markerList.add(dMarker);
                                    }
                                });
                            }
                        }
                    }catch(Exception e){System.out.println("AAAA7"+e);}
                }
            }

        };


        /* **************************************** 핸들러 **************************************** */
    }
    /* **************************************** 위치 한글변환 **************************************** */
    private String getCurrentAddrss(double latitude, double longitude) {
        Geocoder geoCoder = new Geocoder(this, Locale.KOREAN);
        String Area = null;
        try {
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 5);
            if (addresses.size() > 0) {
                Address mAddress = addresses.get(0);
                Area = null;
                StringBuffer strbuf = new StringBuffer();
                String buf;
                for (int i = 0; (buf = mAddress.getAddressLine(i)) != null; i++) {
                    strbuf.append(buf + "\n");
                }
                Area = strbuf.toString();
                return Area;
            }
        } catch (Exception e) {
            System.out.println("AAAA8"+e);
        }
        return Area;
    }
    /* **************************************** 위치 한글변환 **************************************** */
    /* **************************************** 현재 위치 얻기 **************************************** */
    public void getCurrentLocation(){
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }catch(SecurityException e){System.out.println("AAAA9"+e);}catch(NullPointerException e){System.out.println("AAAA10"+e);}
    }
    /* **************************************** 현재 위치 얻기 **************************************** */
    /* **************************************** 장고 통신 **************************************** */
    private JSONArray getHttpConnection(String DB, String method) {
        try {
            if(method.equals("GET") || method.equals("DELETE")) {
                URL LocationEndpoint = new URL("http://180.69.10.151:8000/"+DB);
                HttpURLConnection myConnection = (HttpURLConnection) LocationEndpoint.openConnection();
                myConnection.setDoOutput(true);
                myConnection.setDoInput(true);
                myConnection.setRequestMethod("GET");
                InputStream is=myConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                JSONArray obj=new JSONArray(builder.toString());
                System.out.println(obj);
                if(method.equals("GET"))
                    return obj;
                else{
                    for (int i = 0; i < obj.length(); i++) {
                        String id= obj.getJSONObject(i).get("id").toString();
                        double tlat = Double.valueOf((String) obj.getJSONObject(i).get("latitude"));
                        double tlon = Double.valueOf((String) obj.getJSONObject(i).get("longitude"));
                        if(tlat-DAddress_lat<0.0000000001 && tlon-DAddress_lon<0.0000000001){
                            URL DeleteEndpoint = new URL("http://180.69.10.151:8000/"+DB+"/"+id+"/");
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(DeleteEndpoint)
                                    .delete()
                                    .build();
                            Response response = client.newCall(request).execute();
                        }
                    }
                }
            }
            if(method.equals("POST"))
            {
                String LocationEndpoint="http://180.69.10.151:8000/danger/";
                getCurrentLocation();
                DAddress_lat=latitude;
                DAddress_lon=longitude;
                String json="{\"latitude\":\""+latitude+"\", \"longitude\":\""+longitude+"\"}";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(LocationEndpoint)
                        .post(RequestBody.create(MediaType.parse("application/json"), json)) //POST로 전달할 내용 설정
                        .build();
                Response response = client.newCall(request).execute();
            }
        } catch (Exception e) {
            System.out.println("AAAA11"+e);
        }
        return null;
    }
    /* **************************************** 장고 통신 **************************************** */
    /* **************************************** 거리 계산 **************************************** */
    public double getDistance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist=dist*1609.344;
        return (dist);
    }
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
    /* **************************************** 거리 계산 **************************************** */
    /* **************************************** on함수(지도 초기화)**************************************** */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        getCurrentLocation();
        mMap = googleMap;
        Current = new LatLng(latitude, longitude);
        markerOptions.position(Current);
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.circle);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.dcircle);
        b=bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
        dmarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.criminal);
        b=bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
        cmarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.police);
        b=bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
        pmarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.kid);
        b=bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
        kmarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        new Thread(){
            public void run(){
                Message msg1 = new Message();
                msg1.what = CRIMINAL_INT;
                msg1.obj = getHttpConnection("criminal", "GET");
                MainActivity.this.criminalHandler.sendMessage(msg1);

                Message msg2=new Message();
                msg2.what = POLICE_INT;
                msg2.obj = getHttpConnection("police", "GET");
                MainActivity.this.policeHandler.sendMessage(msg2);
            }
        }.start();

        myMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Current));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
    /* **************************************** on함수(지도 초기화)**************************************** */
    /* **************************************** on함수(위치 변경시)**************************************** */
    @Override
    public void onLocationChanged(Location location) {
        myMarker.remove();
        getCurrentLocation();
        Current=new LatLng(location.getLatitude(), location.getLongitude());
        markerOptions.position(Current);
        myMarker=mMap.addMarker(markerOptions);
        try {
            for (int i = 0; i < markerList.size(); i++) {
                markerList.get(i).remove();
            }
        }catch(Exception e){System.out.println("AAAA12"+e);}
        if(user.equals("1"))
        {
            try {
                kMarker.remove();
            }catch(Exception e){System.out.println("AAAA12"+e);}
            new Thread(){
                public void run(){
                    try {
                        String LocationEndpoint="http://180.69.10.151:8000/kids/"+kidid+"/";
                        getCurrentLocation();
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(LocationEndpoint)
                                .get()
                                .build();
                        Response response = client.newCall(request).execute();
                        String obj="["+response.body().string()+"]";
                        System.out.println(obj);
                        JSONArray jsonArr=new JSONArray(obj);


                        Message msg = new Message();
                        msg.what = KIDS_INT;
                        msg.obj = jsonArr;
                        MainActivity.this.kidsHandler.sendMessage(msg);
                    }catch(Exception e){System.out.println("AAAA13"+e);}
                }
            }.start();

        }else {
            new Thread(){
                public void run(){
                    try {
                        SharedPreferences sf= getSharedPreferences("sFile", MODE_PRIVATE);
                        final String myid = sf.getString("myid","");
                        String LocationEndpoint = "http://180.69.10.151:8000/kids/"+myid+"/";
                        getCurrentLocation();
                        String json = "{\"latitude\":\"" + latitude + "\", \"longitude\":\"" + longitude + "\"}";
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(LocationEndpoint)
                                .patch(RequestBody.create(MediaType.parse("application/json"), json))
                                .build();
                        Response response = client.newCall(request).execute();
                    }catch(Exception e){System.out.println("AAAA14"+e);}
                }
            }.start();
        }
        new Thread(){
            public void run(){
                Message msg = new Message();
                msg.what = DANGER_INT;
                msg.obj = getHttpConnection("danger", "GET");
                MainActivity.this.dangerHandler.sendMessage(msg);
            }
        }.start();
    }
    /* **************************************** on함수(위치 변경시)**************************************** */
    /* **************************************** 블루투스 **************************************** */
    void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("장치 선택");

                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        connectSelectedDevice(items[item].toString());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    void connectSelectedDevice(String selectedDeviceName) {
        for(BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
    /* **************************************** 블루투스 **************************************** */
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}