package com.cdc.fast.ws.sei;

import com.cdc.pcp.common.model.MetaData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "meta")
@XmlAccessorType(XmlAccessType.FIELD)
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
