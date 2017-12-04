package com.amaljoyc.pcc.api;

import com.amaljoyc.pcc.api.dto.ErrorResponse;
import com.amaljoyc.pcc.api.dto.StatisticsDto;
import com.amaljoyc.pcc.api.dto.UploadDto;
import com.amaljoyc.pcc.mapper.StatisticsMapper;
import com.amaljoyc.pcc.model.Statistics;
import com.amaljoyc.pcc.service.PccService;
import com.amaljoyc.pcc.util.PccUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by achemparathy on 04.12.17.
 */
@Api(value = "/")
@RestController
@RequestMapping(value = "/")
@ApiResponses({
        @ApiResponse(
                code = 500,
                message = "Unexpected Server Error",
                response = ErrorResponse.class)
})
public class PccController {

    @Autowired
    private PccService pccService;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @RequestMapping(method = RequestMethod.POST, value = {"/upload"})
    @ApiOperation(
            value = "Used to mark/report an uploadDto",
            nickname = "uploadDto"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 201,
                    message = "Success"),
            @ApiResponse(
                    code = 204,
                    message = "timestamp is older than 60 secs")
    })
    public ResponseEntity upload(@RequestBody UploadDto uploadDto) {
        if (PccUtil.isOlderThanSixtySeconds(uploadDto.getTimestamp())) {
            return ResponseEntity.noContent().build();
        }

        pccService.processUpload(uploadDto);
        return ResponseEntity.status(201).build();
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/statistics"})
    @ApiOperation(
            value = "Returns the statistics of uploads in the last 60 seconds",
            nickname = "statistics",
            response = StatisticsDto.class
    )
    public StatisticsDto getStatistics() {
        Statistics stats = pccService.retrieveStats();
        if (stats == null) {
            return new StatisticsDto(0, 0.0f, 0, 0, 0);
        } else {
            return statisticsMapper.toDto(stats);
        }
    }

}
