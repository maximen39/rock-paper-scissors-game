package com.mahixcode.rockpaperscissors.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
public class UserProfile {

    private long id;
    private String username;
}
