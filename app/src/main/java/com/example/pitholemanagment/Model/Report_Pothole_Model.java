package com.example.pitholemanagment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Report_Pothole_Model {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("coordinates")
    @Expose
    private String coordinates;

    /**
     * No args constructor for use in serialization
     *
     */
    public Report_Pothole_Model() {
    }

    /**
     *
     * @param address
     * @param coordinates
     * @param token
     */
    public Report_Pothole_Model(String token, String address, String coordinates) {
        super();
        this.token = token;
        this.address = address;
        this.coordinates = coordinates;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

}