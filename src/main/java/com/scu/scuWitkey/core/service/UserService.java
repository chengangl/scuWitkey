package com.scu.scuWitkey.core.service;

import com.scu.scuWitkey.core.domain.RecommendModel;
import com.scu.scuWitkey.core.domain.UserModel;
import com.scu.scuWitkey.core.mapper.RecommendMapper;
import com.scu.scuWitkey.core.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecommendMapper recommendMapper;

    private final static long MAX_RECOMMEND_ID = 6;

    public void insertUser(UserModel userModel) {
        this.userMapper.insertUser(userModel);
    }

    public UserModel getUser(String email, String password) {
        return this.userMapper.getUser(email, password);
    }

    public UserModel getUserByEmail(String email) {
        return this.userMapper.getUserByEmail(email);
    }

    public UserModel getUserById(long id) {
        return this.userMapper.getUserById(id);
    }

    public void updateUser(UserModel userModel) {
        this.userMapper.updateUser(userModel);
    }

    public void updateUserAvatar(long id, String avatar) {
        this.userMapper.updateUserAvatar(id,avatar);
    }

    public List<RecommendModel> fetchRecommends() {
        return this.recommendMapper.fetchRecommends();
    }

    public void insertRecommend(RecommendModel recommendModel) {
        RecommendModel recommendModelSelect = selectRecommendById(recommendModel.getId());
        if (null == recommendModelSelect) {
            this.recommendMapper.insertRecommend(recommendModel);
        } else {
            updateRecommend(recommendModel);
        }
    }

    public RecommendModel selectRecommendById(long id) {
        return this.recommendMapper.selectRecommendById(id);
    }

    public void updateRecommend(RecommendModel recommendModel) {
        this.recommendMapper.updateRecommend(recommendModel);
    }

    public void deleteRecommend(long id) {
        this.recommendMapper.deleteRecommend(id);
        for (long i = id + 1; i < MAX_RECOMMEND_ID; i++) {
            this.recommendMapper.updateRecommendIdById(i);
        }
    }
}
