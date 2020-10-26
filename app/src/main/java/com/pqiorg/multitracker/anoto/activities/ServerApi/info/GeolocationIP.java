package com.pqiorg.multitracker.anoto.activities.ServerApi.info;

import java.io.Serializable;

public class GeolocationIP implements Serializable {

    /* renamed from: as */
    private String f2788as;
    private String city;
    private String country;
    private String countryCode;
    private String isp;
    private String lat;
    private String lon;

    /* renamed from: org reason: collision with root package name */
    private String f5624org;
    private String query;
    private String region;
    private String regionName;
    private String status;
    private String timezone;
    private String zip;

    public String getAs() {
        return this.f2788as;
    }

    public String getCity() {
        return this.city;
    }

    public String getCountry() {
        return this.country;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getIsp() {
        return this.isp;
    }

    public String getLat() {
        return this.lat;
    }

    public String getLon() {
        return this.lon;
    }

    public String getOrg() {
        return this.f5624org;
    }

    public String getQuery() {
        return this.query;
    }

    public String getRegion() {
        return this.region;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public String getStatus() {
        return this.status;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public String getZip() {
        return this.zip;
    }

    public void setAs(String str) {
        this.f2788as = str;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public void setCountryCode(String str) {
        this.countryCode = str;
    }

    public void setIsp(String str) {
        this.isp = str;
    }

    public void setLat(String str) {
        this.lat = str;
    }

    public void setLon(String str) {
        this.lon = str;
    }

    public void setOrg(String str) {
        this.f5624org = str;
    }

    public void setQuery(String str) {
        this.query = str;
    }

    public void setRegion(String str) {
        this.region = str;
    }

    public void setRegionName(String str) {
        this.regionName = str;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public void setTimezone(String str) {
        this.timezone = str;
    }

    public void setZip(String str) {
        this.zip = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GeolocationIP{query='");
        sb.append(this.query);
        sb.append('\'');
        sb.append(", lat='");
        sb.append(this.lat);
        sb.append('\'');
        sb.append(", lon='");
        sb.append(this.lon);
        sb.append('\'');
        sb.append(", city='");
        sb.append(this.city);
        sb.append('\'');
        sb.append(", region='");
        sb.append(this.region);
        sb.append('\'');
        sb.append(", regionName='");
        sb.append(this.regionName);
        sb.append('\'');
        sb.append(", countryCode='");
        sb.append(this.countryCode);
        sb.append('\'');
        sb.append(", country='");
        sb.append(this.country);
        sb.append('\'');
        sb.append(", timezone='");
        sb.append(this.timezone);
        sb.append('\'');
        sb.append(", zip='");
        sb.append(this.zip);
        sb.append('\'');
        sb.append(", as='");
        sb.append(this.f2788as);
        sb.append('\'');
        sb.append(", isp='");
        sb.append(this.isp);
        sb.append('\'');
        sb.append(", org='");
        sb.append(this.f5624org);
        sb.append('\'');
        sb.append(", status='");
        sb.append(this.status);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
