package com.binance.connector.futures.client.utils;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public final class HttpClientSingleton {
    private static OkHttpClient httpClient = null;
    private static final int PING_INTERVAL = 1;

    private HttpClientSingleton() {
    }

    public static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            createHttpClient(null);
        }
        return httpClient;
    }

    public static OkHttpClient getHttpClient(ProxyAuth proxy) {
        if (httpClient == null) {
            createHttpClient(proxy);
        } else {
            verifyHttpClient(proxy);
        }
        return httpClient;
    }

    private static void createHttpClient(ProxyAuth proxy) {
        if (proxy == null) {
            httpClient = new OkHttpClient();
        } else {
            if (proxy.getAuth() == null) {
                httpClient = new OkHttpClient.Builder().proxy(proxy.getProxy())
                    .pingInterval(PING_INTERVAL, TimeUnit.MINUTES).build();
            } else {
                httpClient = new OkHttpClient.Builder().proxy(proxy.getProxy()).proxyAuthenticator(proxy.getAuth())
                    .pingInterval(PING_INTERVAL, TimeUnit.MINUTES).build();
            }
        }
    }

    private static void verifyHttpClient(ProxyAuth proxy) {
        Proxy prevProxy = httpClient.proxy();

        if ((proxy != null && !proxy.getProxy().equals(prevProxy)) || (proxy == null && prevProxy != null)) {
            createHttpClient(proxy);
        }
    }
}
