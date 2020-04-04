package site.trycatchers.recommendation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import site.trycatchers.recommendation.domain.Flat;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CianService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Value("${cian.pageSize}")
    private int cianPageSize;

    @PostConstruct
    @SneakyThrows
    public void deleteMe() {
        for (var flat : getFlats()) {
            Files.write(Path.of("/Users/glotovdv/IdeaProjects/recomendation/result.txt"),
                    (flat.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
        }
    }

    public List<Flat> getFlats() {
        var offerCount = getOffererCount();
        // cian returns 0 flats after 1500 flat count
        offerCount = Math.min(offerCount, 1500);
        log.info("Offers count = {}", offerCount);

        var flats = new ArrayList<Flat>();
        int lastPageNumber = offerCount / cianPageSize + 1;
        for (int page = 1; page <= lastPageNumber; page++) {
            var cianQuery = buildCianQuery(page);
            var response = makeRequest(cianQuery);
            var flatsOnPage = parseFlatsFromResponse(response.getBody());
            log.info("Flats count on page is {}", flatsOnPage.size());
            log.info("New flats size is {}", flats.size());
            flats.addAll(flatsOnPage);
        }
        log.info("Done, flats count is {}", flats.size());

        return flats;
    }

    private int getOffererCount() {
        var cianQuery = buildCianQuery(1);
        var response =  makeRequest(cianQuery);
        return JsonPath.read(response.getBody(), "$.data.offerCount");
    }

    private ResponseEntity<String> makeRequest(String cianQuery) {
        try {
            var entity = new HttpEntity<>(cianQuery);
            return restTemplate.postForEntity(
                    "https://api.cian.ru/search-offers/v2/search-offers-desktop/", entity, String.class);
        } catch (Exception e) {
            log.error("Couldn't make request to cian", e);
            return ResponseEntity.status(500).body("error");
        }
    }

    @SneakyThrows
    public List<Flat> parseFlatsFromResponse(String response) {
        try {
            var cianResponse = mapper.readValue(response, CianResponse.class);
            return cianResponse.getData().getOffersSerialized();
        } catch (Exception e) {
            log.error("Couldn't parse response", e);
            return List.of();
        }
    }
    
    private String buildCianQuery(int page) {
        return "{" +
                "  \"jsonQuery\": {" +
                "    \"region\": {" +
                "      \"type\": \"terms\"," +
                "      \"value\": [" +
                "        1" +
                "      ]" +
                "    }," +
                "    \"_type\": \"flatrent\"," +
                "    \"room\": {" +
                "      \"type\": \"terms\"," +
                "      \"value\": [" +
                "        1," +
                "        2," +
                "        9" +
                "      ]" +
                "    }," +
                "    \"engine_version\": {" +
                "      \"type\": \"term\"," +
                "      \"value\": 2" +
                "    }," +
                "    \"for_day\": {" +
                "      \"type\": \"term\"," +
                "      \"value\": \"!1\"" +
                "    }," +
                "    \"page\": {" +
                "      \"type\": \"term\"," +
                "      \"value\": " + page +
                "    }" +
                "  }" +
                "}";
    }

    @Data
    private static class CianResponse {
        private CianData data;
        @Data
        private static class CianData {
            private List<Flat> offersSerialized;
        }
    }
}
