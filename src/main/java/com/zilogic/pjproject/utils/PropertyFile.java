package com.zilogic.pjproject.utils;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * this class all about loading the all the property in the properties file
 *
 * @author user
 */
public class PropertyFile {

    private Properties properties = new Properties();
    private String DB_URI;
    private String DB_NAME;
    private String DB_PASS;
    private String USER_NAME;

    public String getDB_NAME() {
        return DB_NAME;
    }

    public void setDB_NAME(String DB_NAME) {
        this.DB_NAME = DB_NAME;
    }

    public String getDB_PASS() {
        return DB_PASS;
    }

    public void setDB_PASS(String DB_PASS) {
        this.DB_PASS = DB_PASS;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public PropertyFile() {
        try {
            this.properties.load(new FileReader(new File("pjsua2.properties").getAbsoluteFile()));
            this.DB_URI = this.properties.getProperty("DB_LOCAL");
            this.DB_NAME = this.properties.getProperty("DB_NAME", "pjsua2");
            this.USER_NAME = this.properties.getProperty("USER_NAME", "Guna");
            this.DB_PASS = this.properties.getProperty("DB_PASS", "Gunaguna");
            System.out.print(this.DB_URI + this.DB_NAME + this.DB_PASS + this.USER_NAME);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getLocalizedMessage());
        }
    }

    public String getDB_URI() {
        return DB_URI;
    }

    public void setDB_URI(String DB_URI) {
        this.DB_URI = DB_URI;
    }

}
