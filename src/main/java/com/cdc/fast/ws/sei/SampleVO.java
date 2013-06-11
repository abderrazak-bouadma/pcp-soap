package com.cdc.fast.ws.sei;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "com.cdc.fast.ws.vo.sampleVO")
@XmlAccessorType(XmlAccessType.FIELD)
public class SampleVO {

    private String id;

    public SampleVO() {
    }

    public SampleVO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return id;
    }
}
