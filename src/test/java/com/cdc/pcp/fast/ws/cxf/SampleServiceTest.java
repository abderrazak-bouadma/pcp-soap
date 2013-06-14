package com.cdc.pcp.fast.ws.cxf;

import com.cdc.fast.ws.sei.SampleVO;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.junit.Test;

import javax.xml.namespace.QName;

/**
 * Created with IntelliJ IDEA.
 * User: Utilistateur
 * Date: 11/06/13
 * Time: 15:27
 * To change this template use File | Settings | File Templates.
 */
public class SampleServiceTest {

    @Test
    public void test_of_SampleService() throws Exception {

        //
        final String id = "1";
        final String wsdl = "http://localhost/parapheur-soap/soap/v1/Samples?wsdl";
        final String operation = "download";
        final QName opName = new QName("http://sei.ws.fast.cdc.com/", "download");

        //
        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
        Client client = factory.createClient(wsdl);
        Object[] result = client.invoke(opName, id);
        SampleVO sampleVO = (SampleVO) result[0];
        System.out.println("id " + sampleVO.getId());
    }
}
