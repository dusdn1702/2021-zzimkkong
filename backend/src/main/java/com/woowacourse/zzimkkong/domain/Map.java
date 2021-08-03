package com.woowacourse.zzimkkong.domain;

import com.amazonaws.util.Base64;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    @Lob
    private String mapDrawing;

    @Column(nullable = false)
    @Lob
    private String mapImageUrl;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_map_member"), nullable = false)
    private Member member;

    @Column(nullable = false, unique = true)
    private String publicMapId;

    protected Map() {
        this.publicMapId = UUID.randomUUID().toString();
    }

    public Map(Long id, String name, String mapDrawing, String mapImageUrl, Member member) {
        this(name, mapDrawing, mapImageUrl, member);
        this.id = id;
    }

    public Map(String name, String mapDrawing, String mapImageUrl, Member member) {
        this.name = name;
        this.mapDrawing = mapDrawing;
        this.mapImageUrl = mapImageUrl;
        this.member = member;
        String universalUniqueId = UUID.randomUUID().toString();
        this.publicMapId = new String(Base64.encode(universalUniqueId.getBytes()));
    }

    public void update(String mapName, String mapDrawing, String mapImageUrl) {
        this.name = mapName;
        this.mapDrawing = mapDrawing;
        this.mapImageUrl = mapImageUrl;
    }

    public boolean isNotOwnedBy(final Member manager) {
        return !this.member.equals(manager);
    }

    public void updateImageUrl(final String mapImageUrl) {
        this.mapImageUrl = mapImageUrl;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getName() {
        return name;
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
