package com.mahixcode.rockpaperscissors.game;

import com.mahixcode.rockpaperscissors.network.models.SignType;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Scanner;

@Slf4j
public class Terminal {

    private static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    private final RockPaperScissorsGame game;

    public Terminal(RockPaperScissorsGame game) {
        this.game = game;
    }

    public void startCommandListener() {
        Scanner input = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(ANSI_YELLOW + ">" + ANSI_RESET);
                String command = input.nextLine().strip();
                if (command.isEmpty()) {
                    continue;
                }
                String[] args = command.split("=");
                String[] commandArgs = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
                switch (args[0]) {
                    case "signup":
                        signupCommand(commandArgs);
                        break;
                    case "signin":
                        signinCommand(commandArgs);
                        break;
                    case "start":
                        startCommand();
                        break;
                    case "logout":
                        logoutCommand();
                        break;
                    case "exit":
                        exitCommand();
                        break;
                    case "rock":
                        rockCommand();
                        break;
                    case "paper":
                        paperCommand();
                        break;
                    case "scissors":
                        scissorsCommand();
                        break;
                    default:
                        print("Unknown command!", ANSI_RED);
                        break;
                }
            } catch (Exception e) {
                log.error("Error while command executing: " + e.getMessage());
            }
        }
    }

    public void print(String message) {
        System.out.println(message);
    }

    public void print(String message, String color) {
        print(color + message + ANSI_RESET);
    }

    private void signupCommand(String[] args) {
        if (!game.getClient().isConnected()) {
            game.signup(args[0], args[1]);
        }
    }

    private void signinCommand(String[] args) {
        if (!game.getClient().isConnected()) {
            game.signin(args[0], args[1]);
        }
    }

    private void startCommand() {
        if (game.isLogin() && !game.isInGame()) {
            game.startGame();
        }
    }

    private void logoutCommand() {
        if (game.isLogin()) {
            game.getClient().stop();
        } else {
            print("You are not connected to the server!", ANSI_RED);
        }
    }

    private void rockCommand() {
        if (game.isLogin() && game.isInGame()) {
            game.signGame(SignType.ROCK);
        }
    }

    private void paperCommand() {
        if (game.isLogin() && game.isInGame()) {
            game.signGame(SignType.PAPER);
        }
    }

    private void scissorsCommand() {
        if (game.isLogin() && game.isInGame()) {
            game.signGame(SignType.SCISSORS);
        }
    }

    private void exitCommand() {
        print("bye!");
        System.exit(1);
    }
}
