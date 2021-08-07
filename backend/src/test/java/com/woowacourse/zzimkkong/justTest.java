package com.woowacourse.zzimkkong;

import com.woowacourse.zzimkkong.domain.Space;
import com.woowacourse.zzimkkong.repository.SpaceRepository;
import com.woowacourse.zzimkkong.repository.SpaceRepositorySupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.woowacourse.zzimkkong.CommonFixture.BE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class justTest {
    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private SpaceRepositorySupport spaceRepositorySupport;

    @Test
    void findByIdTest() {
        spaceRepository.save(BE);
        List<Space> spacesList = spaceRepositorySupport.findById(1L);

        assertThat(spacesList.size()).isEqualTo(1);
        assertThat(spacesList.get(0).getDescription()).isEqualTo(BE.getDescription());

    }
}
