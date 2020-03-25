package site.trycatchers.recommendation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import site.trycatchers.recommendation.domain.Flat;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CianService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public List<Flat> getFlats() {
        var entity = new HttpEntity<>("{\"jsonQuery\":{\"region\":{\"type\":\"terms\",\"value\":[1]},\"_type\":\"flatrent\",\"room\":{\"type\":\"terms\",\"value\":[1,2,9]},\"engine_version\":{\"type\":\"term\",\"value\":2},\"for_day\":{\"type\":\"term\",\"value\":\"\\u00211\"},\"page\":{\"type\":\"term\",\"value\":2}}}");
        var result = restTemplate.postForEntity("https://api.cian.ru/search-offers/v2/search-offers-desktop/", entity, String.class);
        return getFlatsFromResponse(result.getBody());
    }

    public List<Flat> getFlatsFromResponse(String response) {
        var flats = new ArrayList<Flat>();

        // todo fill the flat
        return flats;
    }
}
