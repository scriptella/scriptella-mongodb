package org.scriptella.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.scriptella.mongodb.bridge.MongoBridgeImpl;
import scriptella.spi.ConnectionParameters;
import scriptella.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class MongoBridgeFactory {
    private static final Logger LOG = Logger.getLogger(MongoBridgeFactory.class.getName());

    private ConnectionParameters parameters;

    public MongoBridgeFactory(ConnectionParameters parameters) {
        this.parameters = parameters;
    }

    public MongoBridgeImpl newMongoBridge() {
        MongoClientURI uri;
        MongoClient mc;
        try {
            String modifiedUrl = appendPropertiesToUrl(parameters.getProperties(), parameters.getUrl());
            modifiedUrl = prependUserPasswordToUrl(modifiedUrl, parameters.getUser(), parameters.getPassword());
            uri = new MongoClientURI(modifiedUrl);
            mc = new MongoClient(uri);
            LOG.info("Initialized connection to " + uri);
        } catch (Exception e) {
            throw new MongoDbProviderException("Invalid URI " + parameters.getUrl(), e);
        }

        String database = uri.getDatabase();

        return new MongoBridgeImpl(mc, mc.getDB(database == null ? "admin" : database), uri.getCollection());
    }

    static String appendPropertiesToUrl(Map<String, ?> props, String url) {
        StringBuilder params = new StringBuilder();
        params.append(url.indexOf('?') >= 0 ? '&' : '?');
        for (Map.Entry<String, ?> entry : props.entrySet()) {
            try {
                Object v = entry.getValue();
                if (v != null) {
                    if (params.length() > 1) {
                        params.append('&');
                    }
                    params.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    params.append('=');
                    params.append(URLEncoder.encode(v.toString(), "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }
        if (params.length() == 1) {
            return url;
        }

        return url + params;
    }

    /**
     * Prepends user/password to the specified url if no credentials were provided in it.
     *
     * @param url      connection url
     * @param user     user connection attribute
     * @param password password connection attribute
     * @return url with prepended credentials in a user:password@host syntax.
     */
    static String prependUserPasswordToUrl(String url, String user, String password) {
        if (StringUtils.isEmpty(user) && StringUtils.isEmpty(password)) {
            return url;
        }
        if (url.indexOf('@') >= 0) {
            LOG.warning("User/password already defined in the connection URL, ignoring values from attributes");
            return url;
        }

        int protocolInd = url.indexOf("://");
        if (protocolInd < 0) {
            return url;
        }

        StringBuilder credentials = new StringBuilder();
        if (!StringUtils.isEmpty(user)) {
            credentials.append(user);
        }
        if (!StringUtils.isEmpty(password)) {
            credentials.append(':').append(password);
        }
        return url.substring(0, protocolInd + 3) + credentials.append('@') + url.substring(protocolInd + 3);
    }
}
