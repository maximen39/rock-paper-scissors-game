package com.mahixcode.rockpaperscissors.network.models.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class SigninRequest {

    private String username;

    private String password;
}
