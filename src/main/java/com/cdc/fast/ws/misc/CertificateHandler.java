/*
 * Copyright (c) CDC-FAST 2013.
 */

package com.cdc.fast.ws.misc;

import com.cdc.pcp.common.manager.CertificateManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * X509 Certificate handler used to process authentication and authorization for
 * services accessibility
 */
public class CertificateHandler {

	private static final Log _logger = LogFactory.getLog(CertificateHandler.class);

	public CertificateHandler() {
	}
	
	public static final CertificateHandler build() {
		return new CertificateHandler();
	}
	/**
	 * <p>
	 * Key element to be watched to retrieve Dn from certificate
	 */
	public static final String EMAIL_DN_ELEMENT_KEY = "OID.1.2.840.113549.1.9.1";
	private static final String CERTIFICATE_REQUEST_ATTRIBUTE = "javax.servlet.request.X509Certificate";
	private static final String USER_CERT_HEADER_NAME = "user-cert";
	private static final String DN_ELEMENT_SEPARATOR = ", ";
	private static final int RFC822NAME_ID = 1;
	private static final String CERTIF_END_LABEL = "-----END CERTIFICATE-----";
	private static final String CERTIF_BEGIN_LABEL = "-----BEGIN CERTIFICATE-----";
	public static final String USERNAME_HTTP_REQUEST_KEY = "USERNAME";

	/**
	 * @param request
	 * @return <p>
	 *         true if the certificate is available within the request.
	 */
	public boolean isClientCertificateAvailableInHttpRequest(HttpServletRequest request) {
		Object certificate = request.getAttribute(CERTIFICATE_REQUEST_ATTRIBUTE);
		String userCertificateHeader = request.getHeader(USER_CERT_HEADER_NAME);
		return (certificate != null) || (userCertificateHeader != null && !userCertificateHeader.isEmpty());
	}

	/**
	 * <p>
	 * Extracts the client certificate as an instance of {@link java.security.cert.X509Certificate}
	 *
	 * @param request
	 * @return the {@link java.security.cert.X509Certificate} certificate instance.
	 * @throws java.security.cert.CertificateException
	 */
	public X509Certificate getClientCertificate(HttpServletRequest request) throws CertificateException {

		// Dans un premier temps, on tente la méthode mod_jk
		Object certObject = request.getAttribute(CERTIFICATE_REQUEST_ATTRIBUTE);
		_logger.debug("javax.servlet.request.X509Certificate:" + certObject);
		if (certObject != null && certObject instanceof X509Certificate[]) {
			X509Certificate[] certificates = (X509Certificate[]) certObject;
			if (certificates.length > 0) {
				X509Certificate resultCertificate = certificates[0];
				_logger.info("[MOD_JK];client certificate dn: " + resultCertificate.getSubjectX500Principal().getName());
				return resultCertificate;
			}
		}

		// Si ça ne marche pas, on passe en mod_proxy avec le header http
		String userCertHeader = request.getHeader(USER_CERT_HEADER_NAME);
		_logger.debug("userCertHeader:" + userCertHeader);
		X509Certificate resultCertificate = toX509Certificate(userCertHeader);
		_logger.info("[MOD_PROXY];client certificate dn: " + resultCertificate.getSubjectX500Principal().getName());
		return resultCertificate;
	}

	/**
	 * <p>
	 * Returns the value of a DN <b>Distinguished Name</b> from parameter
	 * {@link java.security.cert.X509Certificate} instance.
	 *
	 * @param certificate
	 * @param key
	 * @return <p>
	 *         the parameter value
	 */
	public String getDNParameter(X509Certificate certificate, String key) {
		String dn = new String(certificate.getSubjectX500Principal().getName(X500Principal.RFC1779));
		String[] dnSplit = dn.split(DN_ELEMENT_SEPARATOR);

		// Recherche de la clé dans le DN du certificate
		for (String element : dnSplit) {
			if (element.startsWith(key)) {
				String email = element.substring(key.length() + 1);
				return email;
			}
		}

		// Si la clé n'est pas trouvée dans le DN et que la clé recherchée est
		// l'email, on recherche la clé dans "subjectAlternativeNames"
		try {
			if (key.equals(CertificateManager.EMAIL_DN_ELEMENT_KEY)) {
				Collection<List<?>> subjectAltNames = certificate.getSubjectAlternativeNames();
				if (subjectAltNames != null) {
					for (List<?> subjectAltName : subjectAltNames) {
						if (((Integer) (subjectAltName.get(0))).intValue() == RFC822NAME_ID) {
							String email = (String) subjectAltName.get(1);
							return email;
						}
					}
				}
			}
		} catch (CertificateParsingException e) {
			_logger.error("", e);
		}

		//
		return null;
	}

	/**
	 * <p>
	 * Tranforms the parameter header to it's {@link java.security.cert.X509Certificate} equivalent
	 * header.
	 *
	 * @param userCertHeader
	 * @return the {@link java.security.cert.X509Certificate} certificate instance
	 * @throws java.security.cert.CertificateException
	 */
	private X509Certificate toX509Certificate(String userCertHeader) throws CertificateException {
		// décodage du certificate client (remplacement des espaces par des
		// retours chariot)
		String decodedUserCert = CERTIF_BEGIN_LABEL
				+ userCertHeader.substring(CERTIF_BEGIN_LABEL.length(), userCertHeader.length() - CERTIF_END_LABEL.length()).replace(' ', '\n')
				+ CERTIF_END_LABEL;
		ByteArrayInputStream decodedUserCertInputStream = new ByteArrayInputStream(decodedUserCert.getBytes());
		X509Certificate resultCertificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(decodedUserCertInputStream);
		return resultCertificate;
	}
}
