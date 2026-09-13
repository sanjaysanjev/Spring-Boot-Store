package com.sanjay.store.Users;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //@Mapping(target="createdAt", expression = "java(java.time.LocalDateTime.now())")
    userDto toDto(User user);

    User registerUser(registerUserRequest request);

    void update(updateuserDto request, @MappingTarget User user);
}
