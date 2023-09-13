import java.util.List;

/**
 * A class representing shared characteristics of living things.
 * 
 * @author David J. Barnes, Michael Kölling, Zahra Amaan and Imaan Ghafur
 * @version 2016.02.29 (2)
 */
public abstract class Living
{
    // Whether alive or not.
    private boolean alive;
    // The animal's field.
    protected Field field;
    // The animal's position in the field.
    private Location location;
    
    //time shared by all living things
    protected Time time;
    //probability that an animal will get a disease
    protected double diseaseProb;
    
    /**
     * Create a new living thing at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param time The time object that keeps track of time
     */
    public Living(Field field, Location location, Time time)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        time = time;
        diseaseProb = Math.random();
    }
    
    /**
     * Make this living thing act - that is: make it do
     * whatever it wants/needs to do.
     * @param newLiving A list to receive new living things.
     */
    abstract public void act(List<Living> newLiving);
    
    /**
     * Returns the probability that an animal will get a disease
     */
    abstract public double getDisease();

    /**
     * Check whether the living thing is alive or not.
     * @return true if the living thing is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the living thing is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the living thing's location.
     * @return The living thing's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the living thing at the new location in the given field.
     * @param newLocation The living thing's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the living thing's field.
     * @return The living thing's field.
     */
    protected Field getField()
    {
        return field;
    }
}
