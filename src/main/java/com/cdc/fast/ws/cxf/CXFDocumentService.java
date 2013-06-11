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

@WebService(endpointInterface = "com.cdc.fast.ws.sei.DocumentService", portName = "DocumentPort", targetNamespace = "parapheur")
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
        getHttpResponse().setHeader("Content-Length", "2465121");

        // DataSource
        DataSource ds = new PDFDataSource(nodeService, documentId);
        DataHandler dh = new DataHandler(ds);
        contentVO.setContent(dh);

        // return the content
        return contentVO;


        //} else {
        // throw new Fault(new BusinessException(("Permission Denied")));
        // }
    }
}
