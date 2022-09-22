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

    public PropertyFile() {
        try {
            this.properties.load(new FileReader(new File("pjsua2.properties").getAbsoluteFile()));
            this.DB_URI = this.properties.getProperty("DB_LOCAL");
            System.out.print(this.DB_URI);
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getLocalizedMessage());
        }
    }

    public String getDB_URI() {
        return DB_URI;
    }
//this.properties.get("DB_URL")

    public void setDB_URI(String DB_URI) {
        this.DB_URI = DB_URI;
    }

}
