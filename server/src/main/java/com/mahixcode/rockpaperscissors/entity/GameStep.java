package com.mahixcode.rockpaperscissors.entity;

import com.mahixcode.rockpaperscissors.network.models.SignType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "game_step")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
public class GameStep {

    @Id
    @GeneratedValue
    @NotNull
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    @ToString.Exclude
    private Game game;

    @NotNull
    private SignType serverSign;

    private SignType userSign;

    @NotNull
    private OutcomeType outcome;
}
