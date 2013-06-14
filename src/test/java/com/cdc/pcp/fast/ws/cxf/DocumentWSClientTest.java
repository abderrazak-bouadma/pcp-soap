package com.cdc.pcp.fast.ws.cxf;

import com.cdc.fast.ws.sei.DocumentContentVO;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.junit.Test;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Utilistateur
 * Date: 10/06/13
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class DocumentWSClientTest {

    @Test
    public void soap_operation_getContent_should_return_pdf_document() throws Exception {

        final String documentId = "931c43c0-66a1-4ca3-bd86-5195a8f9e3de";
        final String wsdl = "http://localhost/parapheur-soap/soap/v1/Documents?wsdl";
        final String operation = "download";
        final QName opName = new QName("http://sei.ws.fast.cdc.com/", "download");

        //
        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
        Client client = factory.createClient(wsdl);
        Object[] result = client.invoke(opName, documentId);
        DocumentContentVO documentContentVO = (DocumentContentVO) result[0];
        InputStream inputStream = documentContentVO.getContent().getInputStream();
        File file = new File("c:/job/test.pdf");
        if (file.exists() ) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        while (inputStream.read(buffer) > 0) {
            outputStream.write(buffer);
        }
        outputStream.close();
    }
}
