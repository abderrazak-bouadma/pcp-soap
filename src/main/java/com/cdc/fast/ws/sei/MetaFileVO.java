package com.cdc.fast.ws.sei;

import com.cdc.pcp.common.model.MetaData;

import java.util.List;

public class MetaFileVO {

    private String documentId;

    private List<MetaData> metaDataList;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public List<MetaData> getMetaDataList() {
        return metaDataList;
    }

    public void setMetaDataList(List<MetaData> metaDataList) {
        this.metaDataList = metaDataList;
    }
}
