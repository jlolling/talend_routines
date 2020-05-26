package routines;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

public class HttpClient {
	
	private static Logger LOG = LoggerFactory.getLogger(HttpClient.class);
	private int statusCode = 0;
	private String statusMessage = null;
	private int maxRetriesInCaseOfErrors = 0;
	private int currentAttempt = 0;
	private long waitMillisAfterError = 1000l;
	private CloseableHttpClient closableHttpClient = null;
	private HttpClientContext context = null;
	
	public HttpClient(String urlStr, String user, String password, int timeout, int socketTimeout) throws Exception {
		closableHttpClient = createCloseableClient(urlStr, user, password, timeout, socketTimeout);
	}
	
	private HttpEntity buildEntity(JsonNode node) throws UnsupportedEncodingException {
		if (node != null && node.isNull() == false && node.isMissingNode() == false) {
			HttpEntity entity = new StringEntity(node.toString(), "UTF-8");
			return entity;
		} else {
			return null;
		}
	}

	public String execute(HttpPost request, boolean expectResponse) throws Exception {
		String responseContent = "";
		currentAttempt = 0;
		for (currentAttempt = 0; currentAttempt <= maxRetriesInCaseOfErrors; currentAttempt++) {
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
            CloseableHttpResponse httpResponse = null;
            try {
            	if (context != null) {
                	httpResponse = closableHttpClient.execute(request, context);
            	} else {
                	httpResponse = closableHttpClient.execute(request);
            	}
            	statusCode = httpResponse.getStatusLine().getStatusCode();
            	statusMessage = httpResponse.getStatusLine().getReasonPhrase();
            	if (expectResponse || (statusCode != 204 && statusCode != 205)) {
                	responseContent = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                	if (responseContent == null || responseContent.trim().isEmpty()) {
                		throw new Exception("Empty response received.");
                	}
            	}
            	try {
                	httpResponse.close();
                	httpResponse = null;
            	} catch (Exception ce) {
            		// ignore
            	}
            	if (statusCode > 300) {
            		throw new Exception("Got status-code: " + statusCode + ", reason-phrase: " + statusMessage + ", response: " + responseContent);
            	}
            	break;
            } catch (Throwable e) {
            	if (currentAttempt < maxRetriesInCaseOfErrors) {
                	// this can happen, we try it again
                	LOG.warn("POST request: " + request.getURI() + " failed (" + (currentAttempt + 1) + ". attempt, " + (maxRetriesInCaseOfErrors - currentAttempt) + " retries left). \n   Payload: " + EntityUtils.toString(request.getEntity()) + "\n   Waiting " + waitMillisAfterError + "ms and retry request.", e);
                	Thread.sleep(waitMillisAfterError);
            	} else {
//                	LOG.error("POST request: " + request.getURI() + " failed (No retry left, max: " + maxRetriesInCaseOfErrors + "). \n   Payload: " + EntityUtils.toString(request.getEntity()), e);
                	throw new Exception("POST request: " + request.getURI() + " failed. No retry left, max: " + maxRetriesInCaseOfErrors + ".\n   Payload: " + EntityUtils.toString(request.getEntity()), e);
            	}
            } finally {
            	if (httpResponse != null) {
                	try {
                    	httpResponse.close();
                	} catch (Exception ce) {
                		// ignore
                	}
            	}
            }
		} // for
        return responseContent;
	}

	public String post(String urlStr, JsonNode node, boolean expectResponse) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("POST " + urlStr + " body: " + node.toString());
		}
        HttpPost request = new HttpPost(urlStr);
        request.getConfig();
        if (node != null) {
            request.setEntity(buildEntity(node));
            request.addHeader("Connection", "Keep-Alive");
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type", "application/json;charset=UTF-8");
            request.addHeader("Keep-Alive", "timeout=5, max=0");
        }
        return execute(request, expectResponse);
	}

	private CloseableHttpClient createCloseableClient(String urlStr, String user, String password, int timeout, int socketTimeout) throws Exception {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        if (closableHttpClient == null) {
            if (user != null && user.trim().isEmpty() == false) {
        		URL url = new URL(urlStr);
                credsProvider.setCredentials(
                        AuthScope.ANY,
                        new UsernamePasswordCredentials(user, password));
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(socketTimeout + 100)
                        .setConnectTimeout(timeout)
                        .setConnectionRequestTimeout(timeout)
                        .setRedirectsEnabled(true)
                        .setRelativeRedirectsAllowed(false)
                        .setAuthenticationEnabled(true)
                        .build();
                AuthCache authCache = new BasicAuthCache();
                HttpHost httpHost = new HttpHost(url.getHost(), url.getPort());
                authCache.put(httpHost, new BasicScheme());
                context = HttpClientContext.create();
                context.setCredentialsProvider(credsProvider);
                context.setAuthCache(authCache);
                CloseableHttpClient client = HttpClients.custom()
                        .setDefaultCredentialsProvider(credsProvider)
                        .setDefaultRequestConfig(requestConfig)
                        .build();
            	closableHttpClient = client;
                return client;
            } else {
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(socketTimeout + 100)
                        .setConnectTimeout(timeout)
                        .setConnectionRequestTimeout(timeout)
                        .setRedirectsEnabled(true)
                        .setRelativeRedirectsAllowed(true)
                        .build();
                CloseableHttpClient client = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        .build();
            	closableHttpClient = client;
                return client;
            }
        } else {
        	return closableHttpClient;
        }
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public int getMaxRetriesInCaseOfErrors() {
		return maxRetriesInCaseOfErrors;
	}

	public void setMaxRetriesInCaseOfErrors(Integer maxRetriesInCaseOfErrors) {
		if (maxRetriesInCaseOfErrors != null) {
			this.maxRetriesInCaseOfErrors = maxRetriesInCaseOfErrors;
		}
	}

	public int getCurrentAttempt() {
		return currentAttempt;
	}

	public long getWaitMillisAfterError() {
		return waitMillisAfterError;
	}

	public void setWaitMillisAfterError(Long waitMillisAfterError) {
		if (waitMillisAfterError != null) {
			this.waitMillisAfterError = waitMillisAfterError;
		}
	}

	public void close() {
		if (closableHttpClient != null) {
			try {
				closableHttpClient.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public CloseableHttpClient getClosableHttpClient() {
		return closableHttpClient;
	}

	public void setClosableHttpClient(CloseableHttpClient closableHttpClient) {
		this.closableHttpClient = closableHttpClient;
	}

}
