
package com.example.pitholemanagment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingRequest
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("address")
    @Expose
    private  String address;
    @SerializedName("coordinates")
    @Expose
    private String coordinates;
    @SerializedName("reported_by")
    @Expose
    private String reportedBy;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("__v")
    @Expose
    private Integer v;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PendingRequest() {
    }

    /**
     * 
     * @param v
     * @param coordinates
     * @param id
     * @param reportedBy
     * @param status
     */
    public PendingRequest(String id, String coordinates, String reportedBy, Boolean status, Integer v) {
        super();
        this.id = id;
        this.coordinates = coordinates;
        this.reportedBy = reportedBy;
        this.status = status;
        this.v = v;
    }

    public PendingRequest(String id, String address, String coordinates, String reportedBy, Boolean status, Integer v) {
        this.id = id;
        this.address = address;
        this.coordinates = coordinates;
        this.reportedBy = reportedBy;
        this.status = status;
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
