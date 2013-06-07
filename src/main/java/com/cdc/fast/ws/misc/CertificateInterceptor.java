package com.cdc.fast.ws.misc;

import com.cdc.pcp.common.manager.CryptoManager;
import com.cdc.pcp.common.manager.DefaultCryptoManager;
import com.cdc.pcp.common.model.UserInformation;
import com.cdc.pcp.common.service.PCPUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * X.509 Certificate interceptor
 */
public class CertificateInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Log LOG = LogFactory.getLog(CertificateInterceptor.class);

    private CertificateHandler certificateHandler = CertificateHandler.build();

    private CryptoManager cryptoManager = new DefaultCryptoManager();

    @Autowired
    public PCPUserService pcpUserService;

    public CertificateInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
        if (certificateHandler.isClientCertificateAvailableInHttpRequest(request)) {
            try {
                X509Certificate certificate = certificateHandler.getClientCertificate(request);
                String username = certificateHandler.getDNParameter(certificate, CertificateHandler.EMAIL_DN_ELEMENT_KEY);
                String password = cryptoManager.generateClientCertificatePassword(username);
                UserInformation userInformation = processLoginAttempt(username, password);
                request.setAttribute(CertificateHandler.USERNAME_HTTP_REQUEST_KEY, userInformation.getUsername());
                LOG.debug("username has been registered within actual http request !");
            } catch (CertificateException e) {
                LOG.error("USER IS NOT AUTHORIZED", e);
                throw new Fault(e);
            } catch (Exception e) {
                LOG.error("USER IS NOT AUTHORIZED", e);
                throw new Fault(e);
            }
        }
    }

    public void handleFault(Message message) {
         LOG.error("$$$$$$$$$$$$$$$$ SOAP FAULT");
    }

    private UserInformation processLoginAttempt(String username, String password) throws Exception {
        if (username == null || password == null) {
            return null;
        }
        if (username.trim().isEmpty()) {
            return null;
        }
        return pcpUserService.doLogin(username, password);
    }
}
