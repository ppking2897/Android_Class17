package com.example.user.android_class_17;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private LocationManager lmgr;
    private MyGPSListener myGPSListener;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        textView = (TextView)findViewById(R.id.mesg);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);

        } else {
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init() {
        initWebView();

        lmgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        myGPSListener = new MyGPSListener();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myGPSListener);

    }

    @Override
    public void finish() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        lmgr.removeUpdates(myGPSListener);
        super.finish();
    }

    private class MyGPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.v("ppking","onLocationChanged");
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            webView.loadUrl("javascript:goto(" +lat+", " + lng + ")");
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    }


    private void initWebView(){
        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/webview.html");
        webView.addJavascriptInterface(new MyJS(), "android");
    }

    public class MyJS {
        @JavascriptInterface
        public void getLatLng(String lat, String lng){
            Log.v("ppking", lat + ":" + lng);
            textView.setText(lat + ":" + lng);
        }
    }

    public void gotoWhere(View v){
        webView.loadUrl("javascript:goto(60.464118, 22.209453)");
    }
}