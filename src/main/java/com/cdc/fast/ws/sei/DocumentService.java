package com.cdc.fast.ws.sei;

import com.cdc.pcp.spi.vo.DocumentVO;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface DocumentService {

    @WebMethod(operationName = "download")
    DocumentContentVO download(@WebParam(name = "documentId", mode = WebParam.Mode.IN) String documentId);


}
