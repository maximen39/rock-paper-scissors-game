package com.mahixcode.rockpaperscissors.network.models.response;

import com.mahixcode.rockpaperscissors.models.UserProfile;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class SigninResponse {

    private UserProfile userProfile;

    private String message;

    private boolean isContinueGame;
}
