import java.util.List;
import java.util.Iterator;
/**
 * A simple model of a warewolves.
 * Warewolves age, move, eat mice, and die.
 *
 * @author Zahra Amaan and Imaan Ghafur
 */
public class Warewolf extends Animal
{
    //probability of the warewolf getting the disease
    public double diseaseProb = 0.001;
    private Time t;
    
    /**
     * Create a warewolf. A warewolf can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the warewolf will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param time The time object that keeps track of time
     */
    public Warewolf(boolean randomAge, Field field, Location location, Time time)
    {
        super(randomAge,field, location, time);
        t=time;
    }
    
    /**
     * returns the probability of warewolf getting the disease
     */
    public double getDisease()
    {
        return diseaseProb;
    }
    
    /**
     * This is what the warewolf does most of the time: it hunts for
     * wildcats and transforms humans. In the process, it might breed, die of hunger
     * or die of old age.
     * @param newAnimals A list to return newly born animals.
     */
    public void act(List<Living> newAnimals)
    {
        if(!t.isDay()){//only acts at night
        incrementAge();
        incrementHunger();
            if(isAlive()) {
                transform(newAnimals);
    
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
            }
        }
    }
    
    /**
     * Look for wildcats adjacent to the current location.
     * Only the first live wildcats is eaten.
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
            if(animal instanceof WildCat) {
                WildCat wildcat = (WildCat) animal;
                if(wildcat.isAlive()) { 
                    wildcat.setDead();
                    foodLevel = FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    
    /**
     * Check whether or not this animal is to transform at this step.
     * New tranformations will be made into the humans previous locations.
     * @param newAnimals A list to return new warewolfs.
     */
    private void transform(List<Living> newAnimals){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if((age >= BREEDING_AGE) && animal instanceof Human) {
                    Living h = (Human)animal;
                    Location l = h.getLocation();
                    h.setDead();
                    Animal newWolf = new Warewolf(false, field, l,t);
                    newAnimals.add(newWolf);
            }
        }
    }
}
