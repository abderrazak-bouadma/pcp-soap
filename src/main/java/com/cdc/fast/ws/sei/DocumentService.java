package com.cdc.fast.ws.sei;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface DocumentService {

    @WebMethod(operationName = "download")
        //TODO documentId should be mandatory
    DocumentContentVO download(@WebParam(name = "documentId", mode = WebParam.Mode.IN) String documentId);


}
