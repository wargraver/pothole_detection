
package com.example.pitholemanagment.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response
{

    String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Error")
    @Expose
    private String error;
    @SerializedName("Pending_request")
    @Expose
    private List<PendingRequest> pendingRequest = null;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Solved_request")
    @Expose
    private List<SolvedRequest> solvedRequest = null;
    @SerializedName("Phn_no")
    @Expose
    private String phnNo;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Response() {
    }

    /**
     * 
     * @param password
     * @param pendingRequest
     * @param name
     * @param message
     * @param error
     * @param phnNo
     * @param token
     * @param status
     * @param solvedRequest
     *
     */

    public Response(String token, String message, String error, List<PendingRequest> pendingRequest, Boolean status, List<SolvedRequest> solvedRequest, String phnNo, String name, String password) {
        super();
        this.token = token;
        this.message = message;
        this.error = error;
        this.pendingRequest = pendingRequest;
        this.status = status;
        this.solvedRequest = solvedRequest;
        this.phnNo = phnNo;
        this.name = name;
        this.password = password;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<PendingRequest> getPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(List<PendingRequest> pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SolvedRequest> getSolvedRequest() {
        return solvedRequest;
    }

    public void setSolvedRequest(List<SolvedRequest> solvedRequest) {
        this.solvedRequest = solvedRequest;
    }

    public String getPhnNo() {
        return phnNo;
    }

    public void setPhnNo(String phnNo) {
        this.phnNo = phnNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
