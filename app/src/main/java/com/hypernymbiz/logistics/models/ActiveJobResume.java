package com.hypernymbiz.logistics.models;

/**
 * Created by Metis on 07-Feb-18.
 */

public class ActiveJobResume {


    private Double slat;
    private Double slong;
    private Double elat;
    private Double elong;
    private Integer driver_id;
    private String job_id;
    private String start_time;
    private String end_time;
    private String driver_start_time;

    public ActiveJobResume(Double slat, Double slong, Double elat, Double elong, Integer driver_id, String job_id, String start_time, String end_time, String driver_start_time) {
        this.slat = slat;
        this.slong = slong;
        this.elat = elat;
        this.elong = elong;
        this.driver_id = driver_id;
        this.job_id = job_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.driver_start_time = driver_start_time;
    }

    public Double getSlong() {
        return slong;
    }

    public void setSlong(Double slong) {
        this.slong = slong;
    }



    public Double getElong() {
        return elong;
    }

    public void setElong(Double elong) {
        this.elong = elong;
    }

    public Integer getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(Integer driver_id) {
        this.driver_id = driver_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDriver_start_time() {
        return driver_start_time;
    }

    public void setDriver_start_time(String driver_start_time) {
        this.driver_start_time = driver_start_time;
    }

    public Double getSlat() {
        return slat;
    }

    public void setSlat(Double slat) {
        this.slat = slat;
    }

    public Double getElat() {
        return elat;
    }

    public void setElat(Double elat) {
        this.elat = elat;
    }
}
