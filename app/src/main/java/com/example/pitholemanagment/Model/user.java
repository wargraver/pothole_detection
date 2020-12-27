package com.example.pitholemanagment.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class user {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phn_no")
    @Expose
    private String phnNo;

    /**
     * No args constructor for use in serialization
     *
     */
    public user() {
    }

    /**
     *
     * @param password
     * @param name
     * @param phnNo
     * @param email
     */

    public user(String name, String password, String email, String phnNo) {
        super();
        this.name = name;
        this.password = password;
        this.email = email;
        this.phnNo = phnNo;
    }


    public user(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public user(String email) {
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhnNo() {
        return phnNo;
    }

    public void setPhnNo(String phnNo) {
        this.phnNo = phnNo;
    }

}