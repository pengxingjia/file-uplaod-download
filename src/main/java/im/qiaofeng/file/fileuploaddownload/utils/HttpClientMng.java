package im.qiaofeng.file.fileuploaddownload.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

@Slf4j
public class HttpClientMng {

    private static HttpClient client = null;

    public static HttpClient getClient() {
        getTrustedhttpclient();
        return client;
    }

    @SuppressWarnings("deprecation")
    private static synchronized void getTrustedhttpclient() {
        try {
            if (client == null) {
                HttpClientBuilder b = HttpClientBuilder.create();
                // 策略：允许加载所有证书
                SSLContext sslContext = null;
                sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                    @Override
                    public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1) {
                        return true;
                    }
                }).build();
                b.setSslcontext(sslContext);
                // 不检测主机名
                HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                // 创建SSLSocketFactory来加载配置
                SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    hostnameVerifier);
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslSocketFactory).build();
                // 创建connection-manager，配置Registry。支持多线程的使用
                PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);
                b.setConnectionManager(connMgr);
                // 构建HttpClient
                client = b.build();
            }
        } catch (Exception e) {
            log.error("获取httpClient对象失败" + e);
        }
    }

}
