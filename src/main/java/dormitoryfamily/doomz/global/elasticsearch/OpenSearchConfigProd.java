package dormitoryfamily.doomz.global.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import java.util.Arrays;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
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
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@Slf4j
public class OpenSearchConfigProd {

    @Value("${opensearch.endpoint}")
    private String endpoint;

    @Value("${opensearch.username}")
    private String username;

    @Value("${opensearch.password}")
    private String password;

    @Bean
    public RestClient restClient() {
        try {
            SSLContext sslContext = SSLContextBuilder.create().build();

            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));

            return RestClient.builder(new HttpHost(endpoint, 443, "https"))
                    .setRequestConfigCallback(builder -> builder.setConnectTimeout(5000).setSocketTimeout(60000))
                    .setHttpClientConfigCallback(httpClientBuilder ->
                            httpClientBuilder.setDefaultHeaders(Arrays.asList(
                                            new BasicHeader("Content-Type", "application/json")))
                                    .addInterceptorLast((HttpResponseInterceptor)
                                            (response, context) -> response.addHeader("X-Elastic-Product", "Elasticsearch"))
                                    .setDefaultCredentialsProvider(credentialsProvider)
                                    .setSSLContext(sslContext))
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
