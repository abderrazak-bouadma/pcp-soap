package com.cdc.fast.ws.cxf;

import com.cdc.fast.ws.sei.DocumentService;
import com.cdc.pcp.spi.vo.DocumentVO;

import javax.jws.WebService;

@WebService(endpointInterface = "com.cdc.fast.ws.sei.DocumentService",portName = "DocumentPort", targetNamespace = "parapheur")
public class CXFDocumentService implements DocumentService{
    @Override
    public DocumentVO[] all() {
        return new DocumentVO[]{new DocumentVO()};
    }


}
