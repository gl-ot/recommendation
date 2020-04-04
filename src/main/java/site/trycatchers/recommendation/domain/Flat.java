package site.trycatchers.recommendation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Flat {

    private String id;
    private String fullUrl;
    private double totalArea;
    private Geo geo;
    private BargainTerms bargainTerms;


    // the less number in the rank the better flat it is
    private int priceRank;
    private int areaRank;
    private int feeRank;

    public int rankSum() {
        return priceRank + areaRank + feeRank;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BargainTerms {
        private int price;
        private int agentFee;
    }

    @Data
    public static class Geo {
        private Coordinates coordinates;
    }

    @Data
    public static class Coordinates {
        private double lat;
        private double lng;
    }
}
