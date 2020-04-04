package site.trycatchers.recommendation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CianServiceTest {

    @Autowired
    CianService cianService;

    @Test
    public void shouldParseFlats() throws Exception {
        var fileResponse = ResourceUtils.getFile("classpath:cian-response.json");
        var response = Files.readString(fileResponse.toPath());
        var result = cianService.parseFlatsFromResponse(response);
        System.out.println(result);
        assertThat(result).hasSize(28);
        var first = result.get(0);
        assertThat(first.getId()).isEqualTo("227609808");
        assertThat(first.getFullUrl()).isEqualTo("https://www.cian.ru/rent/flat/227609808/");
        assertThat(first.getTotalArea()).isEqualTo(70.0);
        assertThat(first.getGeo().getCoordinates().getLng()).isEqualTo(37.534092);
        assertThat(first.getGeo().getCoordinates().getLat()).isEqualTo(55.74938);
        assertThat(first.getBargainTerms().getPrice()).isEqualTo(220000);
        assertThat(first.getBargainTerms().getAgentFee()).isEqualTo(0);
    }
}
