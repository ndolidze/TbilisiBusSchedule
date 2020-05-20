package ge.tsu.android.TbilisiBusSchedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import ge.tsu.android.TbilisiBusSchedule.data.BusRoute;
import ge.tsu.android.TbilisiBusSchedule.data.BusStop;
import ge.tsu.android.TbilisiBusSchedule.data.BusStopInformationBoard;
import ge.tsu.android.TbilisiBusSchedule.services.GetTTCCOMGEBusStopsAsyncTask;
import ge.tsu.android.TbilisiBusSchedule.services.GetTTCCOMGERoutesAsyncTask;
import ge.tsu.android.TbilisiBusSchedule.services.GetTTCCOMGEStopArrivalTimesAsyncTask;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MainActivity extends AppCompatActivity {
 private  BusRouteArrayAdapter busRouteArrayAdapter;
 private BusStopsArrayAdapter busStopsArrayAdapter;
 private BusStopInformationArrayAdapter busStopInformationArrayAdapter;
 private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.routes);
       busRouteArrayAdapter=new BusRouteArrayAdapter(this, 0, new ArrayList<BusRoute>());
       busStopsArrayAdapter=new BusStopsArrayAdapter(this, 0, new ArrayList<BusStop>());
      busStopInformationArrayAdapter=new BusStopInformationArrayAdapter(this, 0, new ArrayList<BusStopInformationBoard>());
    }
    public void searchForRoutes(View view) {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        GetTTCCOMGERoutesAsyncTask getTTCCOMGERoutesAsyncTask = new GetTTCCOMGERoutesAsyncTask();
        Log.d("route", getTTCCOMGERoutesAsyncTask.toString());
        GetTTCCOMGERoutesAsyncTask.CallBack callback = new GetTTCCOMGERoutesAsyncTask.CallBack() {
            @Override
            public void onDataReceived(ArrayList<BusRoute> busRoutes) {
                listView.setAdapter(busRouteArrayAdapter);
                busRouteArrayAdapter.addAll(busRoutes);
                findViewById(R.id.progress).setVisibility(View.GONE);
            }
        };
        getTTCCOMGERoutesAsyncTask.setCallBack(callback);
        getTTCCOMGERoutesAsyncTask.execute();


    }

    public void searchForStops(View view) throws MalformedURLException {
        TextView textView= (TextView) view;
        String number= (String) textView.getText();
        URL url = null;
        for(int i=0; i<busRouteArrayAdapter.getCount(); i++) {
            BusRoute busRoute=busRouteArrayAdapter.getItem(i);
            if(number.equals(busRoute.getRouteNumber())){
              url= new URL("http://transfer.ttc.com.ge:8080/otp/routers/ttc/routeInfo?routeNumber="+number+"&type=bus");
              if(url!=null){
                  break;
              }
            }
        }

        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        GetTTCCOMGEBusStopsAsyncTask getTTCCOMGEBusStopsAsyncTask=new GetTTCCOMGEBusStopsAsyncTask(url);
        GetTTCCOMGEBusStopsAsyncTask.BusStopsCallback busStopsCallback=new GetTTCCOMGEBusStopsAsyncTask.BusStopsCallback() {
            @Override
            public void onBusStopsDataReceived(ArrayList<BusStop> busStops) {
                listView.setAdapter(busStopsArrayAdapter);
                busStopsArrayAdapter.addAll(busStops);
                findViewById(R.id.progress).setVisibility(View.GONE);

            }
        };
        getTTCCOMGEBusStopsAsyncTask.setBusStopsCallBack(busStopsCallback);
        getTTCCOMGEBusStopsAsyncTask.execute();

    }
    public void searchForBusStopInformationBoard(View view) throws MalformedURLException {
       TextView textView= (TextView) view;
       String busStopName= (String) textView.getText();
        URL url=null;
      for(int i=0; i<busStopsArrayAdapter.getCount(); i++){
          BusStop busStop=busStopsArrayAdapter.getItem(i);
          if(busStopName.equals(busStop.getStopName())){
              String id=busStop.getStopId();
               url=new URL("http://transfer.ttc.com.ge:8080/otp/routers/ttc/stopArrivalTimes?stopId="+id);
          }
          if(url!=null){
              break;
          }
      }
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
      GetTTCCOMGEStopArrivalTimesAsyncTask getTTCCOMGEStopArrivalTimesAsyncTask=new GetTTCCOMGEStopArrivalTimesAsyncTask(url);
       GetTTCCOMGEStopArrivalTimesAsyncTask.BusStopInfoCallback busStopInfoCallback=new GetTTCCOMGEStopArrivalTimesAsyncTask.BusStopInfoCallback() {
           @Override
           public void onBusStopsInfoReceived(ArrayList<BusStopInformationBoard> busStopsInfo) {
            listView.setAdapter(busStopInformationArrayAdapter);
               busStopInformationArrayAdapter.addAll(busStopsInfo);
              findViewById(R.id.progress).setVisibility(View.GONE);

            }
     };
        getTTCCOMGEStopArrivalTimesAsyncTask.setCallBack(busStopInfoCallback);
        getTTCCOMGEStopArrivalTimesAsyncTask.execute();



  }
}

class BusRouteArrayAdapter extends ArrayAdapter<BusRoute>{
 private  Context mContext;
    public BusRouteArrayAdapter(@NonNull Context context, int resource, @NonNull List<BusRoute> objects) {
        super(context, resource, objects);
        mContext=context;
    }
    @Override
    @NonNull
    public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_route_item, parent, false);
        BusRoute busRoute = getItem(position);
        Button button=view.findViewById(R.id.busRouteNumber);
        button.setText(busRoute.getRouteNumber());
        TextView textView = view.findViewById(R.id.busRouteName);
        textView.setText(busRoute.getRouteName());
        return view;
    }
}
class BusStopsArrayAdapter extends ArrayAdapter<BusStop>{
    private  Context mContext;
    public BusStopsArrayAdapter(@NonNull Context context, int resource, @NonNull List<BusStop> objects) {
        super(context, resource, objects);
        mContext=context;
    }
    @Override
    @NonNull
    public View getView ( int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_bus_stop_item, parent, false);
        BusStop busStop = getItem(position);
        TextView textView = view.findViewById(R.id.busStopName);
        textView.setText(busStop.getStopName());

        return view;
    }
}
class BusStopInformationArrayAdapter extends ArrayAdapter<BusStopInformationBoard>{
  private  Context mContext;
    public BusStopInformationArrayAdapter(@NonNull Context context, int resource, @NonNull List<BusStopInformationBoard> objects) {
        super(context, resource, objects);
        mContext=context;
    }
    @Override
    @NonNull
    public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater=(LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.view_stop_arrival_time_item, parent, false);
        BusStopInformationBoard busStopInformationBoard=getItem(position);
        TextView textView=view.findViewById(R.id.arrivalTime);
        textView.setText(busStopInformationBoard.getBusNumber()+" "+busStopInformationBoard.getBusName()+" _ "
                +busStopInformationBoard.getArrivalTime());
        return  view;
    }

}

