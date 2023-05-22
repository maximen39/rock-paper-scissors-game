package com.mahixcode.rockpaperscissors;

import com.esotericsoftware.kryonet.Client;
import com.mahixcode.rockpaperscissors.game.RockPaperScissorsGame;
import com.mahixcode.rockpaperscissors.network.Network;

public class ClientApplication {

    public static void main(String[] args) {
        Client client = new Client();
        Network.register(client.getKryo());
        new RockPaperScissorsGame(client);
    }

}
