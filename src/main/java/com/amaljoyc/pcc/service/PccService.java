package com.amaljoyc.pcc.service;

import com.amaljoyc.pcc.api.dto.UploadDto;
import com.amaljoyc.pcc.model.Statistics;

/**
 * Created by achemparathy on 04.12.17.
 */
public interface PccService {

    void processUpload(UploadDto uploadDto);

    Statistics retrieveStats();
}
