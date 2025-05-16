package javachip.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
@Getter
public enum LocalType {
       
        SGI("서울/경기/인천"),
        GANGWON("강원"),
        CHUNGCHEONG("충청"),
        JEONBUK("전북"),
        JNGJ("전남/광주"),
        DGGB("경북/대구"),
        GNBNUL("경남/부산/울산"),
        JEJU("제주");

        @JsonCreator
        public static LocalType from(String value) {
                if (value == null || value.trim().isEmpty()) {
                        return null;
                }
                try {
                        return LocalType.valueOf(value);
                } catch (IllegalArgumentException e) {
                        return null; // 또는 throw new CustomException("지원하지 않는 지역");
                }
        }
        private final String displayName;

        LocalType(String displayName) {
                this.displayName = displayName;
        }

    public static LocalType fromDisplayName(String displayName) {
                return Arrays.stream(values())
                        .filter(e -> e.displayName.equals(displayName))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unknown displayName: " + displayName));
        }
}
