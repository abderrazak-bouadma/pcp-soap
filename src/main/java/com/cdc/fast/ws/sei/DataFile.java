package com.cdc.fast.ws.sei;

import javax.activation.DataHandler;

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
