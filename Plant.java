import java.util.List;
import java.util.Random;
import java.awt.Color;

/**
 * A class representing shared characteristics of plants.
 *
 * @author Imaan Ghafur and Zahra Amaan
 */
public abstract class Plant extends Living
{
    // The plants's position in the field.
    private Location location;
    // probability that the plant grows
    protected double growthProbability;
    
    protected Time t;
    // age of plant
    protected int age;

    /**
     * Create a tree. A tree can be grow and die.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param time The time object that keeps track of time
     */
    public Plant(Field field, Location location, Time time)
    {
        // initialise instance variables
        super(field, location, time);
        t=time;
    }
    
    /**
     * sets a random age for plant 
     */
    protected int setAge(int maxAge){
        Random rand = Randomizer.getRandom();
        int age=rand.nextInt(maxAge);
        return age;
    }
   
    /**
     * every plant will act differently 
     */
    public abstract void act(List<Living> newPlants);
    
    /**
     * every plant will grow at different rates 
     */
    public abstract void grow(List<Living> newPlants);
    
    /**
     * Increase the age. This could result in the plants death.
     */
    protected void incrementAge()
    {
        age++;
    }
}