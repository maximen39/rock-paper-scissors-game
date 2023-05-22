package com.mahixcode.rockpaperscissors.game.models;

import com.mahixcode.rockpaperscissors.entity.OutcomeType;
import com.mahixcode.rockpaperscissors.network.models.SignType;
import lombok.Data;

@Data
public class Step {

    private SignType userSign;

    private SignType serverSign;

    private OutcomeType outcome;

    private OutcomeType gameOutcome;

}
