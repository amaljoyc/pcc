package com.amaljoyc.pcc;

import com.amaljoyc.pcc.api.dto.StatisticsDto;
import com.amaljoyc.pcc.api.dto.UploadDto;
import com.amaljoyc.pcc.util.PccUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by achemparathy on 04.12.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PccApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PccIntegrationTest {

    private static final String UPLOAD_URL = "/upload";
    private static final String STATISTICS_URL = "/statistics";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSuccessfulUpload() {
        HttpEntity entity = new HttpEntity(new UploadDto(2, PccUtil.getEpoch()), new HttpHeaders());
        ResponseEntity response = restTemplate.postForEntity(UPLOAD_URL, entity, ResponseEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }

    @Test
    public void testFailedUpload() {
        HttpEntity entity = new HttpEntity(new UploadDto(2, PccUtil.getEpoch() - PccUtil.SIXTY_SECONDS - 1), new HttpHeaders());
        ResponseEntity response = restTemplate.postForEntity(UPLOAD_URL, entity, ResponseEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
    }

    @Test
    public void testEmptyStats() {
        StatisticsDto statisticsDto = restTemplate.getForObject(STATISTICS_URL, StatisticsDto.class);
        assertThat(statisticsDto, is(new StatisticsDto(0, 0.0f, 0, 0, 0)));
    }

    @Test
    public void testStatsForSingleUpload() {
        HttpEntity entity = new HttpEntity(new UploadDto(2, PccUtil.getEpoch()), new HttpHeaders());
        restTemplate.postForEntity(UPLOAD_URL, entity, ResponseEntity.class);

        StatisticsDto statisticsDto = restTemplate.getForObject(STATISTICS_URL, StatisticsDto.class);
        assertThat(statisticsDto, is(new StatisticsDto(2, 2.0f, 2, 2, 1)));
    }

    @Test
    public void testStatsForMultipleUpload() {
        HttpEntity entity1 = new HttpEntity(new UploadDto(2, PccUtil.getEpoch()), new HttpHeaders());
        restTemplate.postForEntity(UPLOAD_URL, entity1, ResponseEntity.class);
        HttpEntity entity2 = new HttpEntity(new UploadDto(3, PccUtil.getEpoch()), new HttpHeaders());
        restTemplate.postForEntity(UPLOAD_URL, entity2, ResponseEntity.class);

        StatisticsDto statisticsDto = restTemplate.getForObject(STATISTICS_URL, StatisticsDto.class);
        assertThat(statisticsDto, is(new StatisticsDto(5, 2.5f, 3, 2, 2)));
    }

    @Test
    public void testStatsForExpiredUpload() throws InterruptedException {
        HttpEntity entity1 = new HttpEntity(new UploadDto(2, PccUtil.getEpoch()), new HttpHeaders());
        restTemplate.postForEntity(UPLOAD_URL, entity1, ResponseEntity.class);
        HttpEntity entity2 = new HttpEntity(new UploadDto(3, PccUtil.getEpoch() - PccUtil.SIXTY_SECONDS + 1), new HttpHeaders());
        restTemplate.postForEntity(UPLOAD_URL, entity2, ResponseEntity.class);
        Thread.sleep(2000);

        StatisticsDto statisticsDto = restTemplate.getForObject(STATISTICS_URL, StatisticsDto.class);
        assertThat(statisticsDto, is(new StatisticsDto(2, 2.0f, 2, 2, 1)));
    }
}
