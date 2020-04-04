package site.trycatchers.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import site.trycatchers.recommendation.domain.Flat;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;

/**
 * Service for finding the best flats
 */
@Service
@RequiredArgsConstructor
public class TopFlatsService {

    private final CianService cianService;

    @PostConstruct
    @SneakyThrows
    public void deleteMe() {
        Path path = Path.of("/Users/glotovdv/IdeaProjects/recomendation/result.txt");
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        for (var flat : getTopFlats(5)) {
            Files.write(path, (flat.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
        }
    }

    public List<Flat> getTopFlats(int top) {
        var flats = cianService.getFlats();
        if (flats.isEmpty()) {
            return flats;
        }

        // Always sorting from best to worst!!!

        flats.sort(Comparator.comparingInt(f -> f.getBargainTerms().getPrice()));
        var price = 0;
        for (int i = 0, rank = 0; i < flats.size(); i++) {
            var flat = flats.get(i);
            int p = flat.getBargainTerms().getPrice();
            if (p > price) {
                price = p;
                rank++;
            }
            flat.setPriceRank(rank);
        }

        // starting with the biggest area flats
        flats.sort((f1, f2) -> - Double.compare(f1.getTotalArea(), f2.getTotalArea()));
        var area = Double.MAX_VALUE;
        for (int i = 0, rank = 0; i < flats.size(); i++) {
            var flat = flats.get(i);
            int a = flat.getBargainTerms().getPrice();
            if (a < area) {
                area = a;
                rank++;
            }
            flat.setPriceRank(rank);
        }

        flats.sort(Comparator.comparingInt(f -> f.getBargainTerms().getAgentFee()));
        var fee = 0;
        for (int i = 0, rank = 0; i < flats.size(); i++) {
            var flat = flats.get(i);
            int f = flat.getBargainTerms().getAgentFee();
            if (f > fee) {
                fee = f;
                rank++;
            }
            flat.setPriceRank(rank);
        }

        flats.sort(Comparator.comparingInt(Flat::rankSum));

        return flats.subList(0, top);
    }
}
