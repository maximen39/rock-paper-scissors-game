package com.mahixcode.rockpaperscissors.network.models.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class MessageResponse {

    private String text;
}
