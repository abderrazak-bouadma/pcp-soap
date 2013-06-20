package com.cdc.fast.ws.sei;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * <p>Main Service Document business scope</p>
 */
@WebService
public interface DocumentService {

    /**
     * downloads the document specified by its ID
     */
    @WebMethod(operationName = "download")
    DocumentContentVO download(@WebParam(name = "documentId", mode = WebParam.Mode.IN) String documentId);

    /**
     * uploads a document into Parapheur repository and returns the generated documentId
     */
    @WebMethod(operationName = "upload")
    String upload(@WebParam(name = "label", mode = WebParam.Mode.IN) String label, @WebParam(name = "comment", mode = WebParam.Mode.IN) String comment, @WebParam(name = "subscriberId", mode = WebParam.Mode.IN) String subscriberId, @WebParam(name = "circuitId", mode = WebParam.Mode.IN) String circuitId, @WebParam(name = "dataFile", mode = WebParam.Mode.IN) DataFile dataFile);

    @WebMethod(operationName = "delete")
    void delete(@WebParam(name = "documentId", mode = WebParam.Mode.IN) String documentId);

}
