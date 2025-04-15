package dormitoryfamily.doomz.global.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.http.HttpResponseInterceptor;

import javax.net.ssl.SSLContext;
import java.util.Arrays;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("dev")
public class OpenSearchConfigDev {

    @Value("${opensearch.endpoint}")
    private String endpoint;

    @Value("${opensearch.username}")
    private String username;

    @Value("${opensearch.password}")
    private String password;

    @Bean
    public RestClient restClient() {
        try {
            // SSL 인증서 검증을 비활성화함
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(null, (x509Certificates, s) -> true)
                    .build();

            // 인증 정보를 설정함
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));

            // RestClientBuilder를 생성하여 인증과 SSL 설정을 적용함
            return RestClient.builder(new HttpHost(endpoint, 443, "https"))
                    .setRequestConfigCallback(requestConfigBuilder ->
                            requestConfigBuilder
                                    .setConnectTimeout(5000)  // 5초
                                    .setSocketTimeout(60000)  // 60초
                    )
                    .setHttpClientConfigCallback(httpClientBuilder -> {
                        httpClientBuilder.setDefaultHeaders(Arrays.asList(
                                new BasicHeader("Content-Type", "application/json")));
                        httpClientBuilder.addInterceptorLast((HttpResponseInterceptor)
                                (response, context) ->
                                        response.addHeader("X-Elastic-Product", "Elasticsearch"));
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                                .setSSLContext(sslContext);
                    })
                    .build();
        } catch (Exception e) {
            log.error("❌ RestClient 생성 실패", e);
            throw new RuntimeException("RestClient 생성 실패", e);
        }
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }
}