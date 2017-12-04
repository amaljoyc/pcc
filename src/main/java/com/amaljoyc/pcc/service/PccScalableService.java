package com.amaljoyc.pcc.service;

import com.amaljoyc.pcc.api.dto.UploadDto;
import com.amaljoyc.pcc.model.Statistics;
import com.amaljoyc.pcc.util.PccUtil;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by achemparathy on 04.12.17.
 */
@Service
public class PccScalableService implements PccService {

    private static final String STATS_KEY = "last60Seconds";

    private ExpiringMap<UUID, Integer> uploadMap = null;
    private ConcurrentHashMap<String, Statistics> statsMap = null;

    @PostConstruct
    private void init() {
        uploadMap = ExpiringMap.builder()
                .variableExpiration()
                .expirationPolicy(ExpirationPolicy.CREATED)
                .asyncExpirationListener((key, count) -> this.reCalculate())
                .build();

        statsMap = new ConcurrentHashMap<>();
    }

    private void reCalculate() {
        Collection<Integer> values = uploadMap.values();

        if (values.isEmpty()) {
            statsMap.remove(STATS_KEY);
        } else {
            int sum = values.stream().mapToInt(Integer::intValue).sum();
            float avg = (float) values.stream().mapToInt(Integer::intValue).average().getAsDouble();
            int max = values.stream().max(Integer::compare).get();
            int min = values.stream().min(Integer::compare).get();
            int count = values.size();

            Statistics stats = new Statistics(sum, avg, max, min, count);
            statsMap.put(STATS_KEY, stats);
        }
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void processUpload(UploadDto uploadDto) {
        long expiryDuration = PccUtil.timeToReachSixtySecondsOldFromNow(uploadDto.getTimestamp());
        uploadMap.put(UUID.randomUUID(), uploadDto.getCount(), expiryDuration, TimeUnit.SECONDS);

        reCalculate();
    }

    @Override
    public Statistics retrieveStats() {
        return statsMap.get(STATS_KEY);
    }
}
