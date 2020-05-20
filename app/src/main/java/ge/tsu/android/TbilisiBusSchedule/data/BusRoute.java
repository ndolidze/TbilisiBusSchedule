package ge.tsu.android.TbilisiBusSchedule.data;

public class BusRoute {
   private String name;
   private String id;
   private String routeNumber;



    public String getRouteName() {
        return name;
    }

    public void setRouteName(String name) {
        this.name = name;
    }

    public String getRouteId() {
        return id;
    }

    public void setRouteId(String id) {
        this.id= id;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

}
