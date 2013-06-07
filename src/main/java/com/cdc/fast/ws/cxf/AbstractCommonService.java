package com.cdc.fast.ws.cxf;

import com.cdc.fast.ws.misc.CertificateHandler;
import com.cdc.pcp.common.service.PCPNodeService;
import com.cdc.pcp.common.service.PCPUserService;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceContext;

/**
 * Created with IntelliJ IDEA.
 * User: Utilistateur
 * Date: 07/06/13
 * Time: 15:48
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCommonService {


    @Autowired
    PCPNodeService nodeService;
    @Autowired
    PCPUserService userService;
    private WebServiceContext context;

    /**
     * @return current username for the current authenticated user.
     */
    protected final String getUsername() {
        return (String) getHttpRequest().getAttribute(CertificateHandler.USERNAME_HTTP_REQUEST_KEY);
    }

    /**
     * @param nodeId
     * @return <code>true</code> if current authenticated user has permission to access the indicated node
     */
    protected boolean hasPermissionToAccessNode(String nodeId) {
        return nodeService.hasPermission(nodeService.getParapheurNodeInformation(nodeId), userService.getUserInformation(getUsername()));
    }

    /**
     * @return HttpServletRequest from current MessageContext
     */
    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) context.getMessageContext().get(AbstractHTTPDestination.HTTP_REQUEST);
    }

    /**
     * @return HttpServletResponse from current MessageContext
     */
    protected HttpServletResponse getHttpResponse() {
        return (HttpServletResponse) context.getMessageContext().get(AbstractHTTPDestination.HTTP_RESPONSE);
    }
}
