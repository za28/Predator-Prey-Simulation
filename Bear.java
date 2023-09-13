import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a bear.
 * Bears age, move, eat mice, and die.
 * 
 * @author David J. Barnes, Michael Kölling, Zahra Amaan and Imaan Ghafur
 * @version 2016.02.29 (2)
 */
public class Bear extends Animal
{
    //probability of the bear getting the disease
    public double diseaseProb = 0.01;
    private Time t;
    // maximum litter size
    protected static final int MAX_LITTER_SIZE = 2;
    /**
     * Create a bear. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the bear will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param time The time object that keeps track of time
     */
    public Bear(boolean randomAge, Field field, Location location, Time time)
    {
        super(randomAge,field, location, time);
        t=time;
    }
    
    /**
     * This is what the Bear does most of the time: it hunts for
     * mice. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newAnimals A list to return newly born animals.
     */
    public void act(List<Living> newAnimals)
    
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            double deathProbability=0.04;
            giveBirth(newAnimals);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            
            else {
                // Overcrowding.
                setDead();
            }
            if(t.isSummer() && Math.random()<=deathProbability){
                    setDead(); // dehydration
                }
        }
    }
    
    /**
     * Look for mice adjacent to the current location.
     * Only the first live mouse is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Mouse) {
                Mouse mouse = (Mouse) animal;
                if(mouse.isAlive()) { 
                    mouse.setDead();
                    foodLevel = FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * returns the probability of bear getting the disease
     */
        public double getDisease()
    {
        return diseaseProb;
    }
    
    /**
     * Check whether or not this animal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newAnimals A list to return newly born animals.
     */
    private void giveBirth(List<Living> newAnimals)
    {
        // New bears are born into adjacent locations.
        // Get a list of adjacent free locations.
        if(isFemale()){
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed();
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Animal young = new Bear(false, field, loc, t);
                newAnimals.add(young);
            }
        }
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed()) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A animal can breed if it ia a female and is next to a male.
     * The animal must also be above the breeding age to be able to breed.
     */
    private boolean canBreed()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if((age >= BREEDING_AGE) && animal instanceof Bear) {
                Bear f = (Bear)animal;
                if(!f.isFemale()){
                    return true;
                }
            }
        }
        return false;
    }
}
