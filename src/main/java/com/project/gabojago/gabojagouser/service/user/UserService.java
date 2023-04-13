package com.project.gabojago.gabojagouser.service.user;

import com.project.gabojago.gabojagouser.dto.user.UserDto;

public interface UserService {
  UserDto login(UserDto user);
  UserDto detail(String user);
  UserDto idCheck(String userId);
  int modify(UserDto user);
  int signup(UserDto user);
  int dropout(UserDto user);
}