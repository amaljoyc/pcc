package com.amaljoyc.pcc;

import com.amaljoyc.pcc.api.dto.UploadDto;
import com.amaljoyc.pcc.model.Statistics;
import com.amaljoyc.pcc.service.PccScalableService;
import com.amaljoyc.pcc.service.PccService;
import com.amaljoyc.pcc.util.PccUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

/**
 * Created by achemparathy on 04.12.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PccUnitTest {

    @Configuration
    static class Config {

        @Bean
        PccService pccService() {
            return new PccScalableService();
        }
    }

    @Autowired
    private PccService pccService;

    @Test
    public void testEmptyStats() {
        Statistics stats = pccService.retrieveStats();
        assertNull(stats);
    }

    @Test
    public void testStatsForSingleUpload() throws InterruptedException {
        UploadDto dto = new UploadDto(2, PccUtil.getEpoch());
        pccService.processUpload(dto);

        Statistics actualStats = pccService.retrieveStats();
        Statistics expectedStats = new Statistics(2, 2f, 2, 2, 1);
        assertThat(expectedStats, is(actualStats));
    }

    @Test
    public void testStatsForMultipleUpload() {
        UploadDto dto1 = new UploadDto(2, PccUtil.getEpoch());
        pccService.processUpload(dto1);
        UploadDto dto2 = new UploadDto(3, PccUtil.getEpoch());
        pccService.processUpload(dto2);

        Statistics actualStats = pccService.retrieveStats();
        Statistics expectedStats = new Statistics(5, 2.5f, 3, 2, 2);
        assertThat(expectedStats, is(actualStats));
    }

    @Test
    public void testStatsForExpiredUpload() throws InterruptedException {
        UploadDto dto1 = new UploadDto(2, PccUtil.getEpoch());
        pccService.processUpload(dto1);
        UploadDto dto2 = new UploadDto(3, PccUtil.getEpoch() - PccUtil.SIXTY_SECONDS + 1);
        pccService.processUpload(dto2);
        Thread.sleep(2000);

        Statistics actualStats = pccService.retrieveStats();
        Statistics expectedStats = new Statistics(2, 2f, 2, 2, 1);
        assertThat(expectedStats, is(actualStats));
    }
}
