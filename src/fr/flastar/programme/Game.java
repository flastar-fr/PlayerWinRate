package fr.flastar.programme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {

    private final ArrayList<Player> players;
    private final Map<String, Integer> playersKills = new HashMap<>();
    private final ArrayList<Player> playerDead = new ArrayList<>();

    public Game(ArrayList<Player> players) {
        this.players = players;

        launchGame(players);
    }

    public void launchGame(ArrayList<Player> players) {

        // gestion joueurs 0 HP
        for (Player player :players) {
            if (player.getHealth() == 0) {
                this.playerDead.add(player);
            }
        }

        // faire des dégâts jusqu'à avoir 1 survivant
        while (this.playerDead.size() != players.size() - 1) {
            for (Player player : players) {
                if (!this.playerDead.contains(player)) {

                    // décider du joueur qui va prendre les dégâts et vérif de sa vie
                    Player playerTake = null;
                    boolean healthVerif = true;
                    while (healthVerif) {
                        playerTake = players.get((int) (Math.random() * players.size()));

                        if (playerTake.getHealth() != 0 && playerTake != player) {
                            healthVerif = false;
                        }
                    }

                    // dégâts aléatoires
                    int randomValue = (int) (Math.random() * (player.getMagic()+1));
                    playerTake.takeDamage(player.getAttack() + randomValue);

                    // ajout dans la dead liste + kill
                    if (playerTake.getHealth() == 0) {
                        this.playersKills.merge(player.getName(), 1, Integer::sum);
                        this.playerDead.add(playerTake);
                    }

                }
            }
        }
    }

    public Map<String, Integer> getKills() {
        return this.playersKills;
    }

    public String getWinner() {
        for (Player player : players) {
            if (!this.playerDead.contains(player)) {
                return player.getName();
            }
        }
        return null;
    }

    public String toString() {
        return String.format("Cette game composé des joueurs : %s a été gagné par %s et les kills sont : %s",
                this.players, this.getWinner(), this.getKills());
    }

}
