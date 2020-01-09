package com.wd.pydjc.pred.service.impl;

import com.wd.pydjc.common.dto.PredStatusDto;
import com.wd.pydjc.pred.dao.PredStatusDao;
import com.wd.pydjc.pred.model.PredStatus;
import com.wd.pydjc.pred.service.PredStatusServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PredStatusImpl implements PredStatusServices {

    private static final Logger log = LoggerFactory.getLogger(PredStatusImpl.class.getName());

    private static final long serialVersionUID = 7298196594565587335L;

    @Autowired
    private PredStatusDao predStatusDao;

    @Override
    @Transactional
    public PredStatus savePredStatus(PredStatusDto predStatusDto) {
        PredStatus predStatus = predStatusDto;
        predStatusDao.save(predStatus);
        log.debug("新增模型状态{}",predStatus.getSalt());
        return predStatus;
    }

    @Override
    @Transactional
    public PredStatus updatePredStatus(PredStatus predStatus) {
        predStatusDao.update(predStatus);

        return predStatus;
    }

    @Override
    public PredStatus getPredStatus(String slat) {
        return predStatusDao.getPredStatus(slat);
    }
}
