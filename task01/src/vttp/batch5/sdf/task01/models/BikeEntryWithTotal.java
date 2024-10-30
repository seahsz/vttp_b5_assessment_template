package vttp.batch5.sdf.task01.models;

public class BikeEntryWithTotal {

    private BikeEntry be;
    private int total;

    public BikeEntryWithTotal(BikeEntry be, int total) {
        this.be = be;
        this.total = total;
    }

    public BikeEntry getBe() {
        return be;
    }

    public void setBe(BikeEntry be) {
        this.be = be;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    
    
}
