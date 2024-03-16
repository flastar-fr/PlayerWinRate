package fr.flastar.programme;

public abstract class Player {
    private String name;
    private double health;
    private double attack;
    private double magic;

    private boolean initialised = false;


    public Player(String name, double health, double attack, double magic){
        this.setName(name);
        this.setHealth(health);
        this.setAttack(attack);
        this.setMagic(magic);
        this.initialised = true;
    }

    public Player(String name) {
        this(name, 20, 3, 2);
    }

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public double getAttack() {
        return attack;
    }

    public double getMagic() {
        return magic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(double health) {
        if (health < 0 && !this.initialised) throw new PlayerAttributeException();
        this.health = health;
    }

    public void setAttack(double attack) {
        if (attack < 0) throw new PlayerAttributeException("Player attack can't be under 0");
        this.attack = attack;
    }

    public void setMagic(double magic) {
        if (magic < 0) throw new PlayerAttributeException("Player magic can't be under 0");
        this.magic = magic;
    }

    public abstract void takeDamage(double attack);

    public abstract String getClassName();

    public abstract Player copyClass();

    @Override
    public String toString() {
        return String.format("Nom : %s, Vie : %.1f, Attaque : %.1f, Magie : %.1f",
                this.name, this.health, this.attack, this.magic);
    }

}
