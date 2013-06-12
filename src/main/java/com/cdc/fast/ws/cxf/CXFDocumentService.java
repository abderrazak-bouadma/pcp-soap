package com.cdc.fast.ws.cxf;

import com.cdc.fast.ws.sei.DocumentContentVO;
import com.cdc.fast.ws.sei.DocumentService;
import com.cdc.pcp.common.manager.Extension;
import com.cdc.pcp.common.model.ParapheurNodeInformation;
import com.cdc.pcp.common.service.PCPExtensionService;
import com.cdc.pcp.spi.vo.DocumentVO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebService;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@WebService(endpointInterface = "com.cdc.fast.ws.sei.DocumentService", portName = "DocumentPort", targetNamespace = "parapheur")
// Below annotation activates MTOM, without this the PDF response
// would be inlined as base64Binary within the SOAP response
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public class CXFDocumentService extends AbstractCommonService implements DocumentService {

    @Autowired
    private PCPExtensionService extensionService;

    @Override
    public DocumentVO[] all() {
        DocumentVO documentVO = new DocumentVO();
        documentVO.setDocId("123456789");
        documentVO.setDocSize(1024);
        return new DocumentVO[]{documentVO};
    }

    @Override
    public DocumentContentVO getContent(String documentId) {

        //  if (hasPermissionToAccessNode(documentId)) {

        //
        ParapheurNodeInformation nodeInformation = nodeService.getParapheurNodeInformation(documentId);
        Extension extension = extensionService.getExtensionForFilename(nodeInformation.getFilename());

        // setting some http headers in response
        getHttpResponse().setHeader("Content-Length", String.valueOf(nodeInformation.getFileSize()));
        getHttpResponse().setContentType(extension.contentType);
        if (extension.isPragmaPrivate) {
            getHttpResponse().setHeader("pragma", "private");
        }

        //

        // RemoteInputStream remoteInputStream = nodeService.getNodeInputStream(documentId);
        // InputStream fileInputStream = RemoteInputStreamClient.wrap(remoteInputStream);

        // prepare result
        DocumentContentVO contentVO = new DocumentContentVO();
        contentVO.setDocumentId(documentId);

        //
        //getHttpResponse().setHeader("Content-Length", "4558");

        // DataSource
        //DataSource ds = new PDFDataSource(nodeService, documentId);
        //DataSource ds2 = new FileDataSource("c:/logo.jpg");
        //DataHandler dh = new DataHandler(ds2);
        DataSource ds3 = null;
        try {
            InputStream inputStream =  new FileInputStream(new File("c:/test.pdf"));

            /*
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            while (inputStream.read(buffer) != 0) {
                byteArrayOutputStream.write(buffer);
            }
            */
            ds3 = new ByteArrayDataSource(inputStream, "application/pdf");

        } catch (Exception e) {
            try {
                SOAPFault fault = SOAPFactory.newInstance().createFault();
                fault.setFaultString(e.getMessage());
                fault.setFaultCode(new QName("URI_NS_SOAP_ENVELOPE", "Server"));
                throw new SOAPFaultException(fault);
            } catch (SOAPException e1) {
                throw new RuntimeException("Problem downloading document : " + e1.getMessage());
            }
        }

        //
        contentVO.setContent(new DataHandler(ds3));

        // return the content
        return contentVO;


        //} else {
        // throw new Fault(new BusinessException(("Permission Denied")));
        // }
    }
}
