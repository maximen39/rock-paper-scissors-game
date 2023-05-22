package com.mahixcode.rockpaperscissors.network.models.request;

import com.mahixcode.rockpaperscissors.network.models.SignType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class SignGameRequest {

    private SignType sign;
}
