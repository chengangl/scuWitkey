package com.scu.scuWitkey.core.mapper;

import com.scu.scuWitkey.core.domain.UserModel;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    UserModel getUser(@Param("email") String email, @Param("password") String password);

    UserModel getUserById(@Param("id") long id);

    UserModel getUserByEmail(@Param("email") String email);

    void updateUser(UserModel userModel);

    void updateUserAvatar(@Param("id") long id, @Param("avatar") String avatar);

    void insertUser(UserModel userModel);
}
