package com.cdc.fast.ws.cxf;

import com.cdc.fast.ws.sei.DocumentContentVO;
import com.cdc.fast.ws.sei.DocumentService;
import com.cdc.pcp.common.manager.Extension;
import com.cdc.pcp.common.model.Abonne;
import com.cdc.pcp.common.model.ParapheurNodeInformation;
import com.cdc.pcp.common.model.UserInformation;
import com.cdc.pcp.common.service.PCPExtensionService;
import com.cdc.pcp.common.service.exception.UploadFileNodesException;
import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

@WebService(endpointInterface = "com.cdc.fast.ws.sei.DocumentService", portName = "DocumentPort", targetNamespace = "documentTarget")
public class CXFDocumentService extends AbstractCommonService implements DocumentService {

    private static final Logger logger = Logger.getLogger(CXFDocumentService.class.getName());

    @Autowired
    private PCPExtensionService extensionService;

    @Override
    public DocumentContentVO download(String documentId) {

        if (hasPermissionToAccessNode(documentId)) {

            //
            ParapheurNodeInformation nodeInformation = nodeService.getParapheurNodeInformation(documentId);
            Extension extension = extensionService.getExtensionForFilename(nodeInformation.getFilename());

            //
            InputStream fileInputStream = null;
            try {
                RemoteInputStream remoteInputStream = nodeService.getNodeInputStream(documentId);
                fileInputStream = RemoteInputStreamClient.wrap(remoteInputStream);
            } catch (IOException e) {
                try {
                    SOAPFault fault = SOAPFactory.newInstance().createFault();
                    fault.setFaultString(e.getMessage());
                    throw new SOAPFaultException(fault);
                } catch (SOAPException e1) {
                    throw new RuntimeException("Problem downloading document : " + e1.getMessage());
                }
            }

            // prepare result
            DocumentContentVO contentVO = new DocumentContentVO();
            contentVO.setDocumentId(documentId);

            DataSource ds = null;
            try {
                ds = new ByteArrayDataSource(fileInputStream, extension.contentType);
                contentVO.setContent(new DataHandler(ds));
            } catch (Exception e) {
                try {
                    SOAPFault fault = SOAPFactory.newInstance().createFault();
                    fault.setFaultString(e.getMessage());
                    fault.setFaultCode(new QName("FILE ACCESS ERROR", "PARAPHEUR MESSAGE : Server"));
                    throw new SOAPFaultException(fault);
                } catch (SOAPException e1) {
                    throw new RuntimeException("Problem downloading document : " + e1.getMessage());
                }
            }

            //
            return contentVO;

        } else {
            try {
                SOAPFault fault = null;
                fault = SOAPFactory.newInstance().createFault();
                fault.setFaultString("ACCESS DENIED");
                fault.setFaultCode(new QName("ACCESS_DENIED", "PARAPHEUR MESSAGE : Access Denied"));
                throw new SOAPFaultException(fault);
            } catch (SOAPException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String upload(String filename, String label, String comment, String circuit, DataHandler content) {

        // create temp file
        File tempFile = null;
        try {
            tempFile = File.createTempFile("parapheur-", ".bin");
            content.writeTo(new FileOutputStream(tempFile));
        } catch (IOException e) {
            SOAPFault fault = null;
            try {
                fault = SOAPFactory.newInstance().createFault();
                fault.setFaultString("Erreur lors de la creqtion d'un fichier temporaire");
                throw new SOAPFaultException(fault);
            } catch (SOAPException e1) {
                throw new RuntimeException(e1);
            }
        }

        // upload into the repository
        List<String> createdNodeRefs = null;
        String noderefId = null;
        try {
            UserInformation userInformation = userService.getUserInformation(getUsername());
            Set<Abonne> abonnes = userInformation.getAbonnes();
            // TODO Expose a SubscriberService for subscriber entity retrieve thorough Repository back end !!!
            createdNodeRefs = nodeService.uploadFileNodes(Arrays.asList(new File[]{tempFile}), buildListOfOneElement(filename), buildListOfOneElement(label), buildListOfOneElement(comment), userInformation, circuit);
            noderefId = ((createdNodeRefs.size() > 0) ? createdNodeRefs.get(0) : "");
            logger.info("Node created : " + noderefId);
        } catch (UploadFileNodesException e) {
            SOAPFault fault = null;
            try {
                fault = SOAPFactory.newInstance().createFault();
                fault.setFaultString(e.getMessage());
                throw new SOAPFaultException(fault);
            } catch (SOAPException e1) {
                throw new RuntimeException(e1);
            }
        }

        // return the nodeRefId of the created document
        return noderefId;
    }

    private List<String> buildListOfOneElement(String item) {
        return Arrays.asList(new String[]{item});
    }
}
