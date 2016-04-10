package com.scu.scuWitkey.core.mapper;

import com.scu.scuWitkey.core.domain.RecommendModel;

import java.util.List;

public interface RecommendMapper {
    List<RecommendModel> fetchRecommends();

    void insertRecommend(RecommendModel recommendModel);

    void updateRecommend(RecommendModel recommendModel);

    RecommendModel selectRecommendById(long id);

    void deleteRecommend(long id);

    void updateRecommendIdById(long id);
}
