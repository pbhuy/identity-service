package com.pbhuy.identityservice.mappers;

import com.pbhuy.identityservice.dto.request.UserCreationRequest;
import com.pbhuy.identityservice.dto.request.UserUpdateRequest;
import com.pbhuy.identityservice.dto.response.UserResponse;
import com.pbhuy.identityservice.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
