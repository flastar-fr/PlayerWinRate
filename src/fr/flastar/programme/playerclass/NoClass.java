package fr.flastar.programme.playerclass;

import fr.flastar.programme.Player;

public class NoClass extends Player {

    public NoClass(String name, double health, double attack, double magic) {
        super(name, health, attack, magic);
    }

    public NoClass(String name) {
        super(name);
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
        return "Undefined";
    }

    @Override
    public Player copyClass() {
        return new NoClass(getName(), getHealth(), getAttack(), getMagic());
    }

    @Override
    public String toString() {
        return String.format("Nom : %s, Classe : %s, Vie : %.1f, Attaque : %.1f, Magie : %.1f",
                this.getName(), getClassName(),this.getHealth(), this.getAttack(), this.getMagic());
    }

}
