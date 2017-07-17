package com.example.daniel.videostreaming;


import android.Manifest;
import android.annotation.SuppressLint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.daniel.videostreaming.models.Videos;
import com.example.daniel.videostreaming.utils.http.OkHttpRequest;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VideoStreaming extends MultiDexApplication implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected String userAgent;

    private GoogleApiClient mGoogleApiClient;
    private final String URL = "http://mconfdev.ufjf.br/aplicativo/index.php?req=3&key=app";

    private  double distancia;
    private double latitude;
    private double longetude;
    private Location location;
    private GoogleMap map;

    private LocationRequest locationRequest;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private List<WifiConfiguration> listConfi;
    private String wifis[];
    private WifiScanReceiver wifiReciever;
    private int ipAddress;
    private String ipString;
    private int RSSILevels = 256;

    private static Videos videos;


    private String teste = null;

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        callConnection();

        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        wifiManager.startScan();

        wifiInfo =  wifiManager.getConnectionInfo();

        ipAddress = wifiInfo.getIpAddress();
        ipString = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }


    private synchronized void callConnection() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {
            latitude = location.getLatitude();
            longetude = location.getLongitude();

            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.i("LOG", "onConnectionFailed(" + connectionResult + ")");
        pararConexaoComGoogleApi();

    }

    public void startLocationUpdates() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100000);
        locationRequest.setFastestInterval(50000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest,  this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public void pararConexaoComGoogleApi() {
        //Verificando se está conectado para então cancelar a conexão!
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    @SuppressLint("DefaultLocale")
    public String formatNumber(double distance) {
        String unit = "m";
        if (distance > 1000) {
            distance /= 1000;
            unit = "km";
        }

        return String.format("%3.2f%s", distance, unit);
    }

    public String calculaDistancia(double lat_this, double log_this, double lat_final, double log_final){

        LatLng posicaoInicial = new LatLng(lat_this,log_this);
        LatLng posicaiFinal = new LatLng(lat_final,log_final);
        distancia = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);

        return formatNumber(distancia);
    }


    public Location getLocation() {
        return location;
    }

    public void setMap(GoogleMap m){
        map = m;
    }



    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            latitude = location.getLatitude();
            longetude = location.getLongitude();

            Log.d("LOCAL", "Latitude -> " + latitude + " Longetude -> " + longetude);

            this.location = location;
        }

        if (map != null){
            LatLng sydney = new LatLng(-37.1886, 145.708);
            map.clear();
            map.addMarker(new MarkerOptions().position(sydney).title("Casa do Daniel"));
            map.moveCamera(CameraUpdateFactory.zoomTo(8));
            map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

        @SuppressLint({"NewApi", "LocalSuppress"})
        String frequency = String.valueOf(wifiInfo.getFrequency());//+ WifiInfo.FREQUENCY_UNITS;
        String linkSpeed = String.valueOf(wifiInfo.getLinkSpeed());//+ WifiInfo.LINK_SPEED_UNITS;

        String strWifiInfo = "SSID: " + wifiInfo.getSSID() + "\n" +
                             "BSSID: " + wifiInfo.getBSSID() + "\n" +
                             "IP Address: " + ipString + "\n" +
                             "MAC Address: " + wifiInfo.getMacAddress() + "\n" +
                             "Frequency: " + frequency + "\n" +
                             "LinkSpeed: " + linkSpeed + "\n" +
                             "Rssi: " + wifiInfo.getRssi() + "dBm" + "\n" +
                             "Rssi Level: " + WifiManager.calculateSignalLevel(wifiInfo.getRssi(), RSSILevels) +
                             " of " + RSSILevels;

        Log.d("WIFI", strWifiInfo);

        /*
        Map<String, String> params = new HashMap<String, String>();

        JSONObject parameter = new JSONObject();

        try {
            parameter.put("key", "app" );
            parameter.put("latitude", String.valueOf(latitude) );
            parameter.put("longetude", String.valueOf(longetude) );
            parameter.put("ssid", wifiInfo.getSSID());
            parameter.put("bssid", wifiInfo.getBSSID());
            parameter.put("ipAddress", ipString);
            parameter.put("macAddress", wifiInfo.getMacAddress());
            parameter.put("frequency", frequency);
            parameter.put("linkSpeed", linkSpeed);
            parameter.put("rssi", String.valueOf(wifiInfo.getRssi()) );
            parameter.put("rssiLevel", String.valueOf(WifiManager.calculateSignalLevel(wifiInfo.getRssi(), RSSILevels)) );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        */

        String info =  URL + "&"
                + "latitude="+String.valueOf(latitude)
                + "&" + "longetude=" + String.valueOf(longetude)
                + "&" + "ssid="+wifiInfo.getSSID().replace('"',' ').replace(" ", "")
                + "&" + "bssid=" + wifiInfo.getBSSID()
                + "&" + "ipAddress=" + ipString
                + "&" + "macAddress=" + wifiInfo.getMacAddress()
                + "&" + "frequency=" + frequency
                + "&" + "linkSpeed=" + linkSpeed
                + "&" + "rssi=" + String.valueOf(wifiInfo.getRssi())
                + "&" + "rssiLevel=" + String.valueOf(WifiManager.calculateSignalLevel(wifiInfo.getRssi(), RSSILevels));



        //JSONObject parameter = new JSONObject(params);

        new EnviaInfo().execute(info);
        //Log.d("JSON", parameter.toString());
        Log.d("JSON", "Teste -> " + info);

    }

    public static Videos getVideos(){
        if (videos == null){
            videos = new Videos();
        }

        return ( videos );
    }

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifiManager.getScanResults();
            wifis = new String[wifiScanList.size()];

            for(int i = 0; i < wifiScanList.size(); i++){
                wifis[i] = ((wifiScanList.get(i)).toString());
            }

        }
    }

    private class EnviaInfo extends AsyncTask<String, String, String>{


        @Override
        protected String doInBackground(String... strings) {

            String reposta = null;
            OkHttpRequest okHttp = new OkHttpRequest();

            Log.d("JSON", "TES_DO_ -> " + strings[0]);

            try {
                reposta = okHttp.get(strings[0]);
                teste = reposta;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return reposta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
