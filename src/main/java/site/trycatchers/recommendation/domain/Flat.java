package site.trycatchers.recommendation.domain;

import lombok.Data;

@Data
public class Flat {

    private String id;
    private String fullUrl;
    private double totalArea;
    private Geo geo;
    private BargainTerms bargainTerms;

    @Data
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
