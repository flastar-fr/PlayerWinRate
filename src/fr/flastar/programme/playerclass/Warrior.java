package fr.flastar.programme.playerclass;

import fr.flastar.programme.Player;

/*
 * a des points d'armures qui lui permettent de pas prendre de dégâts
 */

public class Warrior extends Player {

    private int armorPoints;

    public Warrior(String name, double health, double attack, double magic, int armorPoints) {
        super(name, health, attack, magic);
        this.setArmorPoints(armorPoints);
    }

    public Warrior(String name) {
        super(name, 30, 3, 2);
        this.setArmorPoints(1);
    }

    public Warrior(String name, String ... args) {
        super(name, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
        if (args.length == 3) {
            this.setArmorPoints(1);
        } else {
            this.setArmorPoints(Integer.parseInt(args[3]));
        }
    }

    @Override
    public void takeDamage(double attack){
        // gestion armure
        int armorChance = (int) (Math.random() * 3);
        if (armorChance != 0 && this.armorPoints <= 0) {
            // coût critique
            int criticalChance = (int) (Math.random() * 100);
            if (criticalChance == 50) {
                this.setHealth(this.getHealth() - attack * 2);
            } else {
                this.setHealth(this.getHealth() - attack);
            }
        } else {
            this.armorPoints -= 1;
            if (this.armorPoints <= 0) this.armorPoints = 0;
        }

        // joueur mort avec de l'armure
        if (this.getHealth() <= 0 && this.armorPoints != 0) {
            this.setHealth(5);
            this.armorPoints -= 1;
        }

        // passer les PV à 0 pour empêcher le négatif
        if (this.getHealth() <= 0) {
            this.setHealth(0);
        }
    }

    public int getArmorPoints() {
        return armorPoints;
    }

    public void setArmorPoints(int armorPoints) {
        if (armorPoints <= 0) throw new IllegalArgumentException("Armor must be higher than 0");
        this.armorPoints = armorPoints;
    }

    public String getClassName() {
        return "Warrior";
    }

    @Override
    public Player copyClass() {
        return new Warrior(getName(), getHealth(), getAttack(), getMagic(), getArmorPoints());
    }

    @Override
    public String toString() {
        return String.format("Nom : %s, Classe : %s, Vie : %.1f, Attaque : %.1f, Magie : %.1f, Armure : %d",
                this.getName(), getClassName(),this.getHealth(),
                this.getAttack(), this.getMagic(), this.getArmorPoints());
    }

}
