package com.cdc.fast.ws.sei;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * <p>Main Service Document business scope</p>
 */
@WebService
public interface DocumentService {

    @WebMethod(operationName = "download")

    /**
     * downloads the document specified by its ID
     */
    DocumentContentVO download(@WebParam(name = "documentId", mode = WebParam.Mode.IN) String documentId);

    @WebMethod(operationName = "upload")
    /**
     * uploads a document into Parapheur repository and returns the generated documentId
     */
    String upload(@WebParam(name = "filename", mode = WebParam.Mode.IN) String filename, @WebParam(name = "label", mode = WebParam.Mode.IN) String label, @WebParam(name = "comment", mode = WebParam.Mode.IN) String comment, @WebParam(name = "circuit", mode = WebParam.Mode.IN) String circuit, @WebParam(name = "content", mode = WebParam.Mode.IN) DataHandler content);

}
