package com.amaljoyc.pcc.mapper;

import com.amaljoyc.pcc.api.dto.StatisticsDto;
import com.amaljoyc.pcc.model.Statistics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Created by achemparathy on 04.12.17.
 */
@Mapper(componentModel = "spring")
public interface StatisticsMapper {

    @Mappings({
            @Mapping(source = "average", target = "avg"),
            @Mapping(source = "maximum", target = "max"),
            @Mapping(source = "minimum", target = "min")
    })
    StatisticsDto toDto(Statistics statistics);
}