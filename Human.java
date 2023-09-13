import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a human.
 * Humans age, move, hunt bears, and die.
 *
 * @author Zahra Amaan and Imaan Ghafur
 */
public class Human extends Animal
{
    //probability of the human getting the disease
    public double diseaseProb = 0.02;
    private Time t;
    // maximum litter size
    protected static final int MAX_LITTER_SIZE = 5;
    /**
     * Create a human. A human can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the human will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param time The time object that keeps track of time
     */
    public Human(boolean randomAge, Field field, Location location, Time time)
    {
        super(randomAge,field, location, time);
        t=time;
    }
    
    /**
     * returns the probability of human getting the disease
     */
            public double getDisease()
    {
        return diseaseProb;
    }
    
    /**
     * This is what the human does most of the time: it hunts for
     * Bears. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newAnimals A list to return newly born animals.
     */
    public void act(List<Living> newAnimals)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newAnimals);            
            // Move towards a source of food if found.
            Location newLocation = Hunt();
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
        }
    }
    
    /**
     * Look for Bears adjacent to the current location.
     * Only the first live Bear is hunted.
     * @return Where bear was found, or null if it wasn't.
     */
    private Location Hunt()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Bear) {
                Bear Bear= (Bear) animal;
                if(Bear.isAlive()) { 
                    Bear.setDead();
                    foodLevel = FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this animal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newAnimals A list to return newly born animals.
     */
    private void giveBirth(List<Living> newAnimals)
    {
        // New humans are born into adjacent locations.
        // Get a list of adjacent free locations.
        if(isFemale()){
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed();
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Animal young = new Human(false, field, loc,t);
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
            if((age >= BREEDING_AGE) && animal instanceof Human) {
                Human h = (Human)animal;
                if(!h.isFemale()){
                    return true;
                }
            }
        }
        return false;
    }
}
