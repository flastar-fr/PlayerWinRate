package fr.flastar.programme.playerclass;

import fr.flastar.programme.Player;

/*
 * rien de particulier : juste nul
 */

public class Zimox extends Player {

    public Zimox(String name, double health, double attack, double magic) {
        super(name, health, attack, magic);
    }

    @Override
    public void takeDamage(double attack){
        // coût critique
        int criticalChance = (int) (Math.random() * 100);
        if (criticalChance == 50) {
            this.setHealth(this.getHealth() - attack * 2);
        } else {
            this.setHealth(this.getHealth() - attack);
        }

        if (this.getHealth() <= 0) {
            this.setHealth(0);
        }
    }

    public String getClassName() {
        return "Zimox_0";
    }

    @Override
    public Player copyClass() {
        return new Zimox(getName(), getHealth(), getAttack(), getMagic());
    }

    @Override
    public String toString() {
        return String.format("Nom : %s, Classe : %s, Vie : %.1f, Attaque : %.1f, Magie : %.1f",
                this.getName(), getClassName(),this.getHealth(), this.getAttack(), this.getMagic());
    }

}
