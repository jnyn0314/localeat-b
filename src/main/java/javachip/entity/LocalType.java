package javachip.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LocalType {
        SGI,
        GYEONGGI,
        GANGWON,
        CHUNGCHEONG,
        JEONBUK,
        JNGJ,
        DGGB,
        GNBNUL,
        JEJU;

        @JsonCreator
        public static LocalType from(String value) {
                if (value == null || value.trim().isEmpty()) {
                        return null;
                }
                return LocalType.valueOf(value);
        }

}
