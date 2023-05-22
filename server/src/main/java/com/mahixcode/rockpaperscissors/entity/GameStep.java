package com.mahixcode.rockpaperscissors.entity;

import com.mahixcode.rockpaperscissors.network.models.SignType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "game_step")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
public class GameStep {

    private Long id;

    private Game game;

    private SignType serverSign;

    private SignType userSign;

    private OutcomeType outcome;
}
