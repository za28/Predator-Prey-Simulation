import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 *  A class representing shared characteristics of animals. 
 *  
 * @author Imaan Ghafur and Zahra Amaan
 */
public abstract class Animal extends Living
{
    // Characteristics shared by all Animals (class variables).
    
    // The age at which a animal can start to breed.
    protected static final int BREEDING_AGE = 5;
    // The age to which a animal can live.
    private static final int MAX_AGE = 150;
    
    // The food value of a single animal. In effect, this is the
    // number of steps a animal can go before it has to eat again.
    protected static final int FOOD_VALUE = 12;
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();
    
    private boolean female;

    // The animal's age.
    protected int age;
    // The animal's food level, which is increased by eating.
    protected int foodLevel;
    
    /**
     * Create a Animal. A animal can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the animal will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param time The time object that keeps track of time
     */
    public Animal(boolean randomAge, Field field, Location location, Time time)
    {
        super(field, location, time);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = FOOD_VALUE;
        }
        if(rand.nextInt(100)>25){
            female = true;
        }
        else{
            female = false;
        }
    }
    
    /**
     * Increase the age. This could result in the animal's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this animal more hungry. This could result in the animal's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
     /**
      * Checks if a animal is a female
      * 
      * @return true if animal is female and false if it is a male
      */
    protected boolean isFemale(){
        return female;
    }
}