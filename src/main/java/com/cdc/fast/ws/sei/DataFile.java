package com.cdc.fast.ws.sei;

import javax.activation.DataHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Utilistateur
 * Date: 17/06/13
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
public class DataFile {

    private String filename;
    private DataHandler dataHandler;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }
}
