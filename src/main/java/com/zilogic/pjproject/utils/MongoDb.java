package com.zilogic.pjproject.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author user
 */
public class MongoDb {

    MongoClient client = null;
    MongoCredential credenials = null;
    MongoClients clients = null;
    MongoDatabase db;

    public MongoClient getClient() {
        return client;
    }

    public void setClient(MongoClient client) {
        this.client = client;
    }

    public MongoCredential getCredenials() {
        return credenials;
    }

    public void Connect() {
        this.client = (MongoClient) MongoClients.create(new PropertyFile().getDB_URI());
    }

    public void Connect(String uri,String name, String pass, String dbname) {
        this.client = new MongoClient("localhost",27017);
        this.credenials = MongoCredential.createCredential(name, dbname, pass.toCharArray());
    }

    public void setCredenials(MongoCredential credenials) {
        this.credenials = credenials;
    }

    /*
    @param establishing the connection to the particular dataBase and then collection
     */
    public MongoDatabase establishConnection(String databaseName) {
        this.db = this.client.getDatabase(databaseName);
        return db;
    }

}
