package com.sanjay.store.Users;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String oldpassword;
    private String newpassword;
}
