package com.cdc.fast.ws.sei;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.BindingType;

@XmlRootElement(name = "DocumentContentVO")
@XmlAccessorType(XmlAccessType.FIELD)
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_MTOM_BINDING)
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
