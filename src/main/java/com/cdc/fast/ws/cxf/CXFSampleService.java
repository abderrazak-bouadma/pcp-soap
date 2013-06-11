package com.cdc.fast.ws.cxf;

import com.cdc.fast.ws.sei.SampleService;
import com.cdc.fast.ws.sei.SampleVO;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Arrays;
import java.util.List;

@WebService(endpointInterface = "com.cdc.fast.ws.sei.SampleService", portName = "pcpPort",targetNamespace = "com.cdc.fast.ws.vo")
public class CXFSampleService implements SampleService {

    private List<SampleVO> samples = Arrays.asList(new SampleVO[]{new SampleVO("1"), new SampleVO("2"), new SampleVO("3")});

    @Override
    public SampleVO get(@WebParam(name = "id", mode = WebParam.Mode.IN) String id) {
        for(SampleVO sample : samples) {
            if( sample.getId().equals(id))
                return sample;
        }
        return null;
    }
}
