package ge.tsu.android.TbilisiBusSchedule.services;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import java.net.URL;
import java.util.ArrayList;
import ge.tsu.android.TbilisiBusSchedule.data.BusStop;
import ge.tsu.android.TbilisiBusSchedule.data.Stop;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GetTTCCOMGEBusStopsAsyncTask extends AsyncTask <Void, Void, ArrayList<BusStop>> {
   private BusStopsCallback callback;
   private URL url;

    public GetTTCCOMGEBusStopsAsyncTask(URL url) {
        this.url = url;
    }

    @Override
    protected ArrayList<BusStop> doInBackground(Void... voids) {
        Log.d("doInBackground", Thread.currentThread().getName());
        Stop stops=new Stop();
        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url.toString()).build();
            Response response = client.newCall(request).execute();
            String busStopXml=response.body().string();
            stops=new Gson().fromJson(busStopXml, Stop.class);

               }

        catch (Exception e) {
            e.printStackTrace();
        }

             return stops.getBusStops();}


        protected void onPostExecute(ArrayList<BusStop> busStops) {
        Log.d("onPostExecute", Thread.currentThread().getName());
        if (callback != null) {
            callback.onBusStopsDataReceived(busStops);
        }
    }
    public  interface BusStopsCallback{
        void onBusStopsDataReceived(ArrayList<BusStop> busStops);
    }
    public void setBusStopsCallBack(GetTTCCOMGEBusStopsAsyncTask.BusStopsCallback callBack){
        this.callback=callBack;
    }
}
