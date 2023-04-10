public class City implements Comparable<City>{
    private String name;
    private String state;
    private int latDeg;
    private int latMin;
    private int longDeg;
    private int longMin;

    public City(String n, String st, int ladeg, int lamin, int lodeg, int lomin) {
        name = n;
        state = st;
        latDeg = ladeg;
        latMin = lamin;
        longDeg = lodeg;
        longMin = lomin;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }


    public int getLatDeg() {
        return latDeg;
    }

    public int getLatMin() {
        return latMin;
    }


    public int getLongDeg() {
        return longDeg;
    }

    public int getLongMin() {
        return longMin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setLatDeg(int latDeg) {
        this.latDeg = latDeg;
    }

    public void setLatMin(int latMin) {
        this.latMin = latMin;
    }

    public void setLongDeg(int longDeg) {
        this.longDeg = longDeg;
    }

    public void setLongMin(int longMin) {
        this.longMin = longMin;
    }

    @Override
    public String toString() {
        return  name + ", " + state;
    }

    @Override
    public int compareTo(City obj) {
        return this.name.compareTo((obj.name));
    }



}
