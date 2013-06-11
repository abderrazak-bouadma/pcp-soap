package com.cdc.fast.ws.sei;

import com.cdc.pcp.spi.vo.DocumentVO;
import org.apache.cxf.annotations.DataBinding;
import org.apache.cxf.jaxb.JAXBDataBinding;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
@DataBinding(value = JAXBDataBinding.class)
public interface DocumentService {
    @WebMethod(operationName = "getAllDocuments")
    DocumentVO[] all();

    @WebMethod(operationName = "getContent")
    DocumentContentVO getContent(@WebParam(name = "documentId",mode = WebParam.Mode.IN) String documentId);


}
