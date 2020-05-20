package ge.tsu.android.TbilisiBusSchedule.services;

import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.URL;
import java.util.ArrayList;
import ge.tsu.android.TbilisiBusSchedule.data.BusStopInformationBoard;

public class GetTTCCOMGEStopArrivalTimesAsyncTask extends AsyncTask<Void, Void, ArrayList<BusStopInformationBoard>> {
    private BusStopInfoCallback busStopInfoCallback;
    private URL url;

    public GetTTCCOMGEStopArrivalTimesAsyncTask(URL url) {
        this.url = url;
    }

    @Override
    protected ArrayList<BusStopInformationBoard> doInBackground(Void... voids) {

        ArrayList<BusStopInformationBoard> busStopInformationBoards=new ArrayList<>();
        try {

            Document doc =Jsoup.connect(url.toString()).get();
            Elements elements = doc.getElementsByTag("ArrivalTimes");
            for (int i = 0; i < elements.size(); i++) {
                Element routeTag = elements.get(i);
                Elements routeNames = routeTag.getElementsByTag("RouteName");
                Elements routeNumbers = routeTag.getElementsByTag("RouteNumber");
                Elements routeArrivalTime = routeTag.getElementsByTag("ArrivalTime");
                if (routeNames.size() > 0) {
                    BusStopInformationBoard busStopInformationBoard = new BusStopInformationBoard();
                    busStopInformationBoard.setBusName(routeNames.get(0).text());
                    busStopInformationBoard.setBusNumber(routeNumbers.get(0).text());
                    busStopInformationBoard.setArrivalTime(routeArrivalTime.get(0).text());
                    busStopInformationBoards.add(busStopInformationBoard);

                }
                else{
                    BusStopInformationBoard busStopInformationBoard=new BusStopInformationBoard();
                    busStopInformationBoard.setBusName("#DARCHISAXLSHI");
                    busStopInformationBoards.add(busStopInformationBoard);
                }


            }
        }
        catch (Exception e) {

        }

        return busStopInformationBoards;}

    protected void onPostExecute(ArrayList <BusStopInformationBoard> busStopInformationBoards) {
        Log.d("onPostExecute", Thread.currentThread().getName());
        if (busStopInfoCallback != null) {
            busStopInfoCallback.onBusStopsInfoReceived(busStopInformationBoards);
        }
    }
public  interface BusStopInfoCallback{
    void onBusStopsInfoReceived(ArrayList <BusStopInformationBoard> busStopInformationBoards);
}
    public void setCallBack(GetTTCCOMGEStopArrivalTimesAsyncTask.BusStopInfoCallback callBack){
        this.busStopInfoCallback=callBack;
    }
}

