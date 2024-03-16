package fr.flastar.programme.playerclass;

import fr.flastar.programme.Player;

/*
 * chance de se heal quand il fait des dégâts
 */

public class Mage extends Player {

    public Mage(String name, double health, double attack, double magic) {
        super(name, health, attack, magic);
    }

    public Mage(String name) {
        super(name, 20, 2, 4);
    }

    public Mage(String name, String ... args) {
        super(name, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
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

        // passer les PV à 0 pour empêcher le négatif
        if (this.getHealth() <= 0) {
            this.setHealth(0);
        }

        healSpell(this.getMagic()/2);
    }
    public void healSpell(double magic) {
        int randomValue = (int) (Math.random() * 2);
        if (randomValue == 0) {
            if (this.getHealth() < 20 && this.getHealth() > 0) {
                this.setHealth(this.getHealth() + magic * 2);
                if (this.getHealth() > 20) {
                    this.setHealth(20);
                }
            }
        }
    }

    public String getClassName() {
        return "Mage";
    }

    @Override
    public Player copyClass() {
        return new Mage(getName(), getHealth(), getAttack(), getMagic());
    }

    @Override
    public String toString() {
        return String.format("Nom : %s, Classe : %s, Vie : %.1f, Attaque : %.1f, Magie : %.1f",
                this.getName(), this.getClassName(),this.getHealth(), this.getAttack(), this.getMagic());
    }

}
