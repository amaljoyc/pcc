package com.amaljoyc.pcc.service;

import com.amaljoyc.pcc.api.dto.UploadDto;
import com.amaljoyc.pcc.model.CalculationType;
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
                .asyncExpirationListener((key, count) -> this.reCalculate((Integer) count, CalculationType.DELETE))
                .build();

        statsMap = new ConcurrentHashMap<>();
    }

    @Deprecated
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

    private void reCalculate(Integer count, CalculationType type) {
        Statistics stats = statsMap.get(STATS_KEY);
        if (stats == null) {
            stats = new Statistics(count,(float) count, count, count, 1);
            statsMap.put(STATS_KEY, stats);
            return;
        }

        stats.setCount(uploadMap.size());
        if (type == CalculationType.ADD) {
            stats.setSum(stats.getSum() + count);
            if (count > stats.getMaximum()) {
                stats.setMaximum(count);
            }
            if (count < stats.getMinimum()) {
                stats.setMinimum(count);
            }
        } else if (type == CalculationType.DELETE) {
            stats.setSum(stats.getSum() - count);

            if (uploadMap.isEmpty()) {
                stats.setMaximum(0);
                stats.setMinimum(0);
            } else {
                if (count == stats.getMaximum()) {
                    stats.setMaximum(uploadMap.values().stream().max(Integer::compare).get());
                }
                if (count == stats.getMinimum()) {
                    stats.setMinimum(uploadMap.values().stream().min(Integer::compare).get());
                }
            }
        } else {
            throw new IllegalArgumentException("Undefined CalculationType: " + type);
        }

        if (stats.getSum() == 0 || stats.getCount() == 0) {
            stats.setAverage(0f);
        } else {
            stats.setAverage((float) stats.getSum() / stats.getCount());
        }
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void processUpload(UploadDto uploadDto) {
        long expiryDuration = PccUtil.timeToReachSixtySecondsOldFromNow(uploadDto.getTimestamp());
        uploadMap.put(UUID.randomUUID(), uploadDto.getCount(), expiryDuration, TimeUnit.SECONDS);

        reCalculate(uploadDto.getCount(), CalculationType.ADD);
    }

    @Override
    public Statistics retrieveStats() {
        return statsMap.get(STATS_KEY);
    }
}
