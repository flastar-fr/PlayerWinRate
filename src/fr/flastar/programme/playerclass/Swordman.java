package fr.flastar.programme.playerclass;

import fr.flastar.programme.Player;

/*
 * 10x plus de chance de crit
 * 3x damage / crit
 * 2x damage / coup de base
 */

public class Swordman extends Player {

    public Swordman(String name, double health, double attack, double magic) {
        super(name, health, attack, magic);
    }

    public Swordman(String name) {
        super(name, 20, 4, 3);
    }

    @Override
    public void takeDamage(double attack){
        // coût critique
        int criticalChance = (int) (Math.random() * 10);
        if (criticalChance == 0) {
            this.setHealth(this.getHealth() - attack * 3);
        } else {
            this.setHealth(this.getHealth() - attack * 2);
        }

        // passer les PV à 0 pour empêcher le négatif
        if (this.getHealth() <= 0) {
            this.setHealth(0);
        }
    }

    public Swordman(String name, String ... args) {
        super(name, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
    }

    public String getClassName() {
        return "Swordman";
    }

    @Override
    public Player copyClass() {
        return new Swordman(getName(), getHealth(), getAttack(), getMagic());
    }

    @Override
    public String toString() {
        return String.format("Nom : %s, Classe : %s, Vie : %.1f, Attaque : %.1f, Magie : %.1f",
                this.getName(), getClassName(),this.getHealth(), this.getAttack(), this.getMagic());
    }

}
