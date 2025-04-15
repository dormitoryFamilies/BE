package dormitoryfamily.doomz.global.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import org.apache.http.message.BasicHeader;
import java.util.Arrays;

@Slf4j
@Configuration
public class ElasticsearchConfig {

    // OpenSearch 엔드포인트 주소
    @Value("${opensearch.endpoint}")
    private String endpoint;

    // OpenSearch 인증 사용자 이름
    @Value("${opensearch.username}")
    private String username;

    // OpenSearch 인증 비밀번호
    @Value("${opensearch.password}")
    private String password;

    @Bean
    public ElasticsearchClient elasticsearchClient() {

        try {
            // SSL 인증서 검증을 비활성화함 (개발 환경에서만 사용할 것)
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(null, (x509Certificates, s) -> true)
                    .build();

            // 인증 정보를 설정함
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));

            // RestClientBuilder를 생성하여 인증과 SSL 설정을 적용함
            RestClientBuilder builder = RestClient.builder(
                            new HttpHost(endpoint, 443, "https"))
                    .setHttpClientConfigCallback(httpClientBuilder ->
                            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                                    .setSSLContext(sslContext)
                                    .setDefaultHeaders(Arrays.asList(
                                            new BasicHeader("Content-Type", "application/json"),
                                            new BasicHeader("X-Elastic-Product", "Elasticsearch")
                                    )));

            // 전송 객체(Transport)를 생성하고 JSON 매퍼로 Jackson을 사용함
            ElasticsearchTransport transport = new RestClientTransport(
                    builder.build(), new JacksonJsonpMapper());

            // Elasticsearch 클라이언트를 생성함
            log.info("✅ OpenSearch 클라이언트 생성 완료");
            return new ElasticsearchClient(transport);

        } catch (Exception e) {
            log.error("❌ OpenSearch 클라이언트 생성 실패", e);
            throw new RuntimeException("OpenSearch 클라이언트 생성 실패", e);
        }
    }
}