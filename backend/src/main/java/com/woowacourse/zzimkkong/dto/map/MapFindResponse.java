package com.woowacourse.zzimkkong.dto.map;

import com.woowacourse.zzimkkong.domain.Map;

public class MapFindResponse {
    private Long mapId;
    private String mapName;
    private String mapDrawing;
    private String mapImageUrl;
    private String publicMapId;

    public MapFindResponse() {
    }

    private MapFindResponse(Long mapId, String mapName, String mapDrawing, String mapImageUrl) {
        this.mapId = mapId;
        this.mapName = mapName;
        this.mapDrawing = mapDrawing;
        this.mapImageUrl = mapImageUrl;
    }

    private MapFindResponse(Long mapId, String mapName, String mapDrawing, String mapImageUrl, String publicMapId) {
        this.mapId = mapId;
        this.mapName = mapName;
        this.mapDrawing = mapDrawing;
        this.mapImageUrl = mapImageUrl;
        this.publicMapId = publicMapId;
    }

    public static MapFindResponse of(Map map, String publicMapId) {
        return new MapFindResponse(
                map.getId(),
                map.getName(),
                map.getMapDrawing(),
                map.getMapImageUrl(),
                publicMapId
        );
    }

    public Long getMapId() {
        return mapId;
    }

    public String getMapName() {
        return mapName;
    }

    public String getMapDrawing() {
        return mapDrawing;
    }

    public String getMapImageUrl() {
        return mapImageUrl;
    }

    public String getPublicMapId() {
        return publicMapId;
    }
}
