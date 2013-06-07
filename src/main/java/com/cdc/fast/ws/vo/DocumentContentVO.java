package com.cdc.fast.ws.vo;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: Utilistateur
 * Date: 07/06/13
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "documentContentVo")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentContentVO {

    private String documentId;

    @XmlMimeType("application/octet-stream")
    private DataHandler content;


    public DataHandler getContent() {
        return content;
    }

    public void setContent(DataHandler content) {
        this.content = content;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

}
