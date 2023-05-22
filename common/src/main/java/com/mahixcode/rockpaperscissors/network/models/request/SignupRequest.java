package com.mahixcode.rockpaperscissors.network.models.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class SignupRequest {

    private String username;

    private String password;
}
