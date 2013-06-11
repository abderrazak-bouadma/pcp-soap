package com.cdc.fast.ws.cxf;

import com.cdc.pcp.common.service.PCPNodeService;
import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import java.io.*;

@Component
public class PDFDataSource implements DataSource {

    public static final String APPLICATION_PDF = "application/pdf";
    private final String documentId;
    private PCPNodeService nodeService;

    public PDFDataSource(PCPNodeService nodeService, String documentId) {
        this.documentId = documentId;
        this.nodeService = nodeService;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("c:/document.pdf"));
        //FileInputStream fis = new FileInputStream("c:/document.pdf");
        //RemoteInputStream remoteInputStream = nodeService.getNodeInputStream(documentId);
        return bis;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public String getContentType() {
        return APPLICATION_PDF;
    }

    @Override
    public String getName() {
        return "INDICATE-DOCUMENT-NAME.pdf";
    }
}
