package site.trycatchers.recommendation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CianServiceTest {

    ObjectMapper mapper = new ObjectMapper();
    RestTemplate restTemplate = mock(RestTemplate.class);
    CianService cianService = new CianService(restTemplate, mapper);

    @Test
    public void shouldParseFlats() throws Exception {
        var fileResponse = ResourceUtils.getFile("classpath:cian-response.json");
        var response = Files.readString(fileResponse.toPath());
        var result = cianService.getFlatsFromResponse(response);
        assertThat(result).hasSize(0);
    }
}
