package ge.tsu.android.TbilisiBusSchedule.services;

import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;;
import java.util.ArrayList;
import ge.tsu.android.TbilisiBusSchedule.data.BusRoute;
public class GetTTCCOMGERoutesAsyncTask extends AsyncTask<Void, Void, ArrayList<BusRoute>> {
 private CallBack callBack;
    @Override
    protected ArrayList<BusRoute> doInBackground(Void... voids) {
        Log.d("doInBackground", Thread.currentThread().getName());
        ArrayList<BusRoute> busRouteArrayList = new ArrayList<>();
        try {
         Document doc=  Jsoup.connect("http://transfer.ttc.com.ge:8080/otp/routers/ttc/routes").get();
         Log.d("document", doc.toString());
            Elements elements =  doc.getElementsByTag("Route");
            for (int i = 2; i <elements.size(); i++) {
               Element routeTag=elements.get(i);
                Elements routeNames=routeTag.getElementsByTag("LongName");
                Elements routeNumbers=routeTag.getElementsByTag("RouteNumber");
                Elements routeIds=routeTag.getElementsByTag("Id");
                if(routeNames.size()>0){
                    BusRoute busRoute=new BusRoute();
                    busRoute.setRouteName(routeNames.get(0).text());
                    busRoute.setRouteNumber(routeNumbers.get(0).text());
                    busRoute.setRouteId(routeIds.get(0).text());
                    busRouteArrayList.add(busRoute);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return busRouteArrayList;

    }


    protected void onPostExecute(ArrayList <BusRoute> busRoutes) {
        Log.d("onPostExecute", Thread.currentThread().getName());
        if (callBack != null) {
            callBack.onDataReceived(busRoutes);
        }
    }
 public  interface CallBack{
        void onDataReceived(ArrayList <BusRoute> busRoutes);
 }
 public void setCallBack(CallBack callBack){
        this.callBack=callBack;
 }
}
