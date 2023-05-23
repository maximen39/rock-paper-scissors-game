package com.mahixcode.rockpaperscissors.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue
    @NotNull
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime lastAuthAt;
}
