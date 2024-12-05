public class Weapon {
    private int weaponID;
    private int damage;
    private int fireRate;

    public Weapon(int weaponID, int damage, int fireRate) {
        this.weaponID = weaponID;
        this.damage = damage;
        this.fireRate = fireRate;
    }

    public void shoot() {
        System.out.println("Weapon " + weaponID + " shooting with damage " + damage);
        // Bullet creation logic goes here
    }
}
