package site.trycatchers.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RecommendationApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecommendationApplication.class, args);
	}


	@Autowired
	private RestTemplate restTemplate;

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		var entity = new HttpEntity<>("{\"jsonQuery\":{\"region\":{\"type\":\"terms\",\"value\":[1]},\"_type\":\"flatrent\",\"room\":{\"type\":\"terms\",\"value\":[1,2,9]},\"engine_version\":{\"type\":\"term\",\"value\":2},\"for_day\":{\"type\":\"term\",\"value\":\"\\u00211\"},\"page\":{\"type\":\"term\",\"value\":2}}}");
		var result = restTemplate.postForEntity("https://api.cian.ru/search-offers/v2/search-offers-desktop/", entity, String.class);
		System.out.println(result.getBody());
	}

}
