package com.wd.pydjc.pred.service;

import com.wd.pydjc.common.dto.PredStatusDto;
import com.wd.pydjc.pred.model.PredStatus;
import org.springframework.stereotype.Service;

@Service
public interface PredStatusServices {
    PredStatus savePredStatus(PredStatusDto predStatusDto);
    PredStatus updatePredStatus(PredStatus predStatus);
    PredStatus getPredStatus(String slat);
}
