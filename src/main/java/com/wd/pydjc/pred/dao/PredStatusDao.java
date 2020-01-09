package com.wd.pydjc.pred.dao;

import com.wd.pydjc.pred.model.PredStatus;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PredStatusDao {
    @Insert("insert into pred_status(salt,featureAnalysis,modelAnalysis,svr,decisiontree,adaboost,extratrees,randomforest,grandientboosting,kneighbour,arima,gru,lstm,rnn,seq2seq) values (#{salt},#{featureAnalysis},#{modelAnalysis},#{svr},#{decisiontree},#{adaboost},#{extratrees},#{randomforest},#{grandientboosting},#{kneighbour},#{arima},#{gru},#{lstm},#{seq2seq})")
    int save(PredStatus predStatus);

    @Select("select * from pred_status p where p.salt = #{salt}")
    PredStatus getPredStatus(@Param("salt") String salt);

    @Delete("delete from pred_status where salt=#{salt}")
    int deletePredStatus(@Param("salt") String salt);
    int update(PredStatus predStatus);

}
