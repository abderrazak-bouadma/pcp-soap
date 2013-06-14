package com.cdc.fast.ws.cxf;

import com.cdc.fast.ws.sei.DocumentContentVO;
import com.cdc.fast.ws.sei.DocumentService;
import com.cdc.pcp.common.manager.Extension;
import com.cdc.pcp.common.model.ParapheurNodeInformation;
import com.cdc.pcp.common.service.PCPExtensionService;
import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebService;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.io.InputStream;

@WebService(endpointInterface = "com.cdc.fast.ws.sei.DocumentService", portName = "DocumentPort", targetNamespace = "documentTarget")
public class CXFDocumentService extends AbstractCommonService implements DocumentService {

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
}
