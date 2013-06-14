package com.cdc.fast.ws.sei;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


@WebService
public interface SampleService {

    @WebMethod(action = "download", operationName = "download")
    SampleVO get(@WebParam(name = "id", mode = WebParam.Mode.IN) String id);
}
