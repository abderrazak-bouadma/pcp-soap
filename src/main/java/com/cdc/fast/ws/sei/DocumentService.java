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

    /**
     * <p>downloads the document specified by its ID to client</p>
     */
    @WebMethod(operationName = "download")
    DocumentContentVO download(@WebParam(name = "documentId", mode = WebParam.Mode.IN) String documentId);

    /**
     * <p>uploads a document from client side into main repository and returns the generated documentId</p>
     */
    @WebMethod(operationName = "upload")
    String upload(@WebParam(name = "label", mode = WebParam.Mode.IN) String label, @WebParam(name = "comment", mode = WebParam.Mode.IN) String comment, @WebParam(name = "subscriberId", mode = WebParam.Mode.IN) String subscriberId, @WebParam(name = "circuitId", mode = WebParam.Mode.IN) String circuitId, @WebParam(name = "dataFileVO", mode = WebParam.Mode.IN) DataFileVO dataFileVO);

    /**
     * <p>deletes documents from repository and all related information (document it self, folder, meta data, ...)</p>
     *
     * @param documentId
     */
    @WebMethod(operationName = "delete")
    void delete(@WebParam(name = "documentId", mode = WebParam.Mode.IN) String documentId);

    /**
     * <p>uploads document related meta data.</p>
     * <p>Meta data will be validated</p>
     *
     * @param documentId
     * @param xmlMeta
     */
    @WebMethod(operationName = "send-doc-metadata")
    String sendMeta(@WebParam(name = "xml-meta-data", mode = WebParam.Mode.IN) MetaFileVO metaDataFile);

    @WebMethod(operationName = "uploadMeta")
    String uploadMeta(@WebParam(name = "documentId", mode = WebParam.Mode.IN) String documentId, DataHandler dataHandler);

}
