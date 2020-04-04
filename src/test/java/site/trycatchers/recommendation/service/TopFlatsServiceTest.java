package site.trycatchers.recommendation.service;

import org.junit.jupiter.api.Test;
import site.trycatchers.recommendation.domain.Flat;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TopFlatsServiceTest {

    private CianService cianService = mock(CianService.class);
    private TopFlatsService flatsService = new TopFlatsService(cianService);

    @Test
    public void shouldReturnTheBestFlat() {
        var flats = new ArrayList<Flat>();
        // 1 + 3 + 1
        var best = Flat.builder().totalArea(40.0).bargainTerms(new Flat.BargainTerms(100_000, 10)).build();
        flats.add(best);
        // 3 + 1 + 2
        flats.add(Flat.builder().totalArea(200.0).bargainTerms(new Flat.BargainTerms(1_000_000, 20)).build());
        // 2 + 2 + 3
        flats.add(Flat.builder().totalArea(100.0).bargainTerms(new Flat.BargainTerms(300_000, 30)).build());

        when(cianService.getFlats()).thenReturn(flats);
        var top = flatsService.getTopFlats(1);

        assertThat(top.get(0)).isEqualTo(best);
    }
}
