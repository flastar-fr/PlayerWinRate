package fr.flastar.programme;

import fr.flastar.programme.neededclasses.*;
import fr.flastar.programme.playerclass.*;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Consolever {

    public static <T> T createPlayerClass(Class<T> classe, String name, String ... args) throws Exception {

        if (args.length == 0) {
            return classe.getDeclaredConstructor(String.class).newInstance(name);
        }

        return classe.getDeclaredConstructor(String.class, String[].class).newInstance(name, args);
    }

    public static void main(String[] args) throws Exception {

        // import players depuis le fichier
        ArrayList<Player> players = new ArrayList<>();
        File playerFile = new File("inputfiles/players_init.txt");
        try (BufferedReader fileRead = new BufferedReader(new FileReader(playerFile))) {
            Map<String, Player> playerMapInit = new HashMap<>();
            String line;
            String[] playerData;
            while ((line = fileRead.readLine()) != null) {  // lecture du fichier jusqu'à la fin

                // pré traitement
                line = line.replaceAll("\\s+", "");
                playerData = line.split(",");

                String[] o;
                try {
                    o = Arrays.copyOfRange(playerData, 2, playerData.length);
                } catch (IllegalArgumentException e) {
                    // format (pseudo)
                    if (line.matches("[a-zA-Z0-9_]+$")) {
                        players.add(new NoClass(playerData[0]));
                    }
                    continue;
                }

                playerData[0] = playerData[0].toLowerCase();

                // gestion class Demon, (pseudo + 69, 69, 69)
                if (line.contains("69,69,69")) {
                    players.add(new Demon(playerData[0], 69, 69, 69));
                }

                // gestion class Zimox, (pseudo + 0, 0, 0)
                else if (line.contains("0,0,0")) {
                    players.add(new Zimox("zimox_0", 0, 0, 0));
                }

                // format (class, pseudo + HP, attack, magie + armor)
                else if (line.matches("^[a-zA-Z]+,\\w+((,[0-9.]+){3})?(,\\d)?+$")) {
                    playerMapInit.put("mage", createPlayerClass(Mage.class, playerData[1], o));
                    playerMapInit.put("warrior", createPlayerClass(Warrior.class, playerData[1], o));
                    playerMapInit.put("swordman", createPlayerClass(Swordman.class, playerData[1], o));
                    if (playerMapInit.containsKey(playerData[0])) {
                        players.add(playerMapInit.get(playerData[0]));
                    } else throw new IllegalArgumentException("Invalid class name : " + line);
                }

                else throw new IllegalArgumentException("Player " + line + " can't be imported");

            }
        }


        // création de la map
        System.out.println("Players : ");
        Map<String, Integer[]> playerWinUntried = new HashMap<>();

        // affichage des stats
        for (Player player : players) {
            playerWinUntried.put(player.getName(), new Integer[]{0, 0});
            System.out.println(player);
        }
        System.out.println("\n------------------------------------------------------------\n");


        System.out.println("Games execution :");
        // afficher l'heure de début
        LocalTime hourNow = LocalTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        String hourFormated = hourNow.format(format);
        System.out.println("Heure d'entrée dans la boucle : " + hourFormated);

        // exécution LOOP de fois
        double percent;
        int previousPercent = 0;
        System.out.print("\rProgression actuel : 0%");
        ArrayList<Player> playersCopy;
        TimeCalculator calculTotalTime = new TimeCalculator();
        TimeCalculator calculTime;
        Average timeAverageCopy = new Average();
        Average timeAverageGame = new Average();
        Average timeAverageAdd = new Average();
        Average timeAverageProgress = new Average();

        long LOOP = 1_000_000;
        for (int i = 0; i < LOOP; i++) {
            // faire l'exécution à de nouveaux players à chaque fois
            calculTime = new TimeCalculator();
            playersCopy = new ArrayList<>();
            for (Player player : players) {
                playersCopy.add(player.copyClass());
            }
            timeAverageCopy.addToResult(calculTime.getTimer());

            // exécuter et additioner les points (kills / win)
            calculTime.restart();
            Game game = new Game(playersCopy);
            timeAverageGame.addToResult(calculTime.getTimer());

            // gestion
            calculTime.restart();
            String playerWinner = game.getWinner();
            Map<String, Integer> playerKills = game.getKills();

            // win
            Integer[] playerStats = playerWinUntried.get(playerWinner);
            playerWinUntried.put(playerWinner, new Integer[]{playerStats[0] + 1, playerStats[1]});

            // kills
            assert playerKills != null;
            for (Map.Entry<String, Integer> entryGame : playerKills.entrySet()) {
                int win = playerWinUntried.get(entryGame.getKey())[0];
                int kills = playerWinUntried.get(entryGame.getKey())[1];
                playerWinUntried.put(entryGame.getKey(), new Integer[]{win, kills + entryGame.getValue()});
            }
            timeAverageAdd.addToResult(calculTime.getTimer());


            // progression dynamique
            calculTime.restart();
            percent = ((double) i / LOOP * 100);
            if (previousPercent + 1 == Math.floor(percent)) {
                System.out.print("\rProgression actuel : " + (int) Math.floor(percent) + "%");
                previousPercent = (int) Math.floor(percent);
            }
            timeAverageProgress.addToResult(calculTime.getTimer());

        }
        System.out.print("\rProgression actuel : 100%");

        /* trie de la map
         * Méthode : trier une liste contenant les clés et la valeur que je veux trier
         * repasser la liste en LinkedHashMap et rajouter les valeurs manquantes
         */
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        for (Player player : players) {
            list.add(new AbstractMap.SimpleEntry<>(player.getName(), playerWinUntried.get(player.getName())[0]));
        }
        list.sort(Map.Entry.comparingByValue());
        Map<String, Integer[]> playerWinTried = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            playerWinTried.put(entry.getKey(), new Integer[]{entry.getValue(),
                    playerWinUntried.get(entry.getKey())[1]});
        }

        // affichage : heure de fin, LOOP + temps de calcul, players
        hourNow = LocalTime.now();
        hourFormated = hourNow.format(format);

        System.out.println("\nHeure de sortie de la boucle : " + hourFormated);
        System.out.println("\n------------------------------------------------------------\n");

        System.out.println("Games stats reveal :");
        double totalTime = calculTotalTime.getTimer();
        System.out.printf("Total game : %,d, Calcul time : %,.2f s, Total kills : %,d\n",
                LOOP, totalTime, LOOP * (players.size()-1));
        for (Map.Entry<String, Integer[]> entry : playerWinTried.entrySet()) {
            String key = entry.getKey();
            Integer[] value = entry.getValue();
            double winRate = (double) value[0] / LOOP * 100;
            System.out.printf("Player : %s, Winrate : %.3f%%, Game won : %,d, Player kills : %,d\n",
                    key, winRate, value[0], value[1]);
        }

        try (FileOutputStream fos = new FileOutputStream("outputfiles/calcultime_cons.txt")) {
            fos.write((String.format("Temps total : %.2f s\n", totalTime)).getBytes());
            fos.write(String.format("Temps copy : %.3e s / %.3f s, %.3f%%\n",
                    timeAverageCopy.calculAverage(), timeAverageCopy.getAddResult(),
                    timeAverageCopy.getAddResult()/totalTime*100).getBytes());
            fos.write(String.format("Temps game : %.3e s / %.3f s, %.3f%%\n",
                    timeAverageGame.calculAverage(), timeAverageGame.getAddResult(),
                    timeAverageGame.getAddResult()/totalTime*100).getBytes());
            fos.write(String.format("Temps add : %.3e s / %.3f s, %.3f%%\n",
                    timeAverageAdd.calculAverage(), timeAverageAdd.getAddResult(),
                    timeAverageAdd.getAddResult()/totalTime*100).getBytes());
            fos.write(String.format("Temps progress : %.3e s / %.3f s, %.3f%%\n",
                    timeAverageProgress.calculAverage(), timeAverageProgress.getAddResult(),
                    timeAverageProgress.getAddResult()/totalTime*100).getBytes());
        }
    }
}
