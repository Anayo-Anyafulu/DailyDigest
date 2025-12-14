package junioranyafulu.DailyDigest.dto.rawg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawgGame {

    private Long id;

    private String name;

    private String slug;

    private String released;

    private Boolean tba;

    @JsonProperty("background_image")
    private String backgroundImage;

    private Double rating;

    @JsonProperty("rating_top")
    private Integer ratingTop;

    @JsonProperty("ratings_count")
    private Integer ratingsCount;

    @JsonProperty("reviews_count")
    private Integer reviewsCount;

    private Integer metacritic;

    private Integer playtime;

    @JsonProperty("suggestions_count")
    private Integer suggestionsCount;

    private String updated;

    private List<Platform> platforms;

    private List<Genre> genres;

    private List<Store> stores;

    private List<Tag> tags;

    @JsonProperty("short_screenshots")
    private List<Screenshot> shortScreenshots;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Platform {
        private PlatformDetail platform;

        @JsonProperty("released_at")
        private String releasedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlatformDetail {
        private Long id;
        private String name;
        private String slug;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Genre {
        private Long id;
        private String name;
        private String slug;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Store {
        private Long id;
        private StoreDetail store;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreDetail {
        private Long id;
        private String name;
        private String slug;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        private Long id;
        private String name;
        private String slug;
        private String language;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Screenshot {
        private Long id;
        private String image;
    }
}
