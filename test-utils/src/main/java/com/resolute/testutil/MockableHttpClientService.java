package com.resolute.testutil;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;

import com.resolute.service.httpclient.HttpClientService;

public class MockableHttpClientService implements HttpClientService {
  
  public HttpResponse getHttpResponse () {
    return null;
  }
  
  @Override
  public <T> T execute(HttpHost target, HttpUriRequest request, HttpClientContext context,
      ResponseHandler<? extends T> handler) throws IOException, ClientProtocolException {
    return handler.handleResponse(getHttpResponse());
  }

}