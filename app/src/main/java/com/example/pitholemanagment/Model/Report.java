package com.example.pitholemanagment.Model;

public class Report {
    String id,place,status,cordinates;

    public Report(String id, String status, String place,String cordinates)
    {
        this.id = id;
        this.place = place;
        this.cordinates=cordinates;
        this.status = status;
    }

    public String getCordinates() {
        return cordinates;
    }

    public void setCordinates(String cordinates) {
        this.cordinates = cordinates;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
