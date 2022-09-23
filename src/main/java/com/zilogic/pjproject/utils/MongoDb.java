package com.zilogic.pjproject.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author user
 */
public class MongoDb {

    private final PropertyFile profile = new PropertyFile();
    private final MongoClient client = new MongoClient();
    private final MongoCredential credenials = MongoCredential.createCredential("Guna", "Pjsua2", "Gunaguna12".toCharArray());
    private final MongoDatabase db = client.getDatabase(profile.getDB_NAME());
    {
        System.out.println("Connected with Db");
    }
    
    public MongoClient getClient() {
        return this.client;
    }

    public MongoCredential getCredenials() {
        return credenials;
    }

    public MongoDatabase getDb() {
        return this.db;
    }

}
