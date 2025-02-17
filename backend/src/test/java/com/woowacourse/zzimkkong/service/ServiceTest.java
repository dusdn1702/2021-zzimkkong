package com.woowacourse.zzimkkong.service;

import com.woowacourse.zzimkkong.infrastructure.StorageUploader;
import com.woowacourse.zzimkkong.infrastructure.TimeConverter;
import com.woowacourse.zzimkkong.repository.MapRepository;
import com.woowacourse.zzimkkong.repository.MemberRepository;
import com.woowacourse.zzimkkong.repository.ReservationRepository;
import com.woowacourse.zzimkkong.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ServiceTest {
    @MockBean
    protected MemberRepository members;

    @MockBean
    protected ReservationRepository reservations;

    @MockBean
    protected MapRepository maps;

    @MockBean
    protected SpaceRepository spaces;

    @MockBean
    protected StorageUploader storageUploader;
}
