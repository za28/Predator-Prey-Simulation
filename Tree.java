import java.util.List;
import java.awt.Color;

/**
 * Tree is a type of plant in our simulation
 *
 * @author Zahra Amaan and Imaan Ghafur 
 */
public class Tree extends Plant
{
    //probability that the tree gets infected by a disease
    public double diseaseProb = 0.01;
    // maximumage of tree
    private int maxAge = 1000;
    
    /**
     * Create a tree. A tree can be grow and die.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param time The time object that keeps track of time
     */
    public Tree(Field field, Location location, Time time)
    {
        super(field,location, time);
        
        growthProbability = 0.01;
        age = setAge(maxAge);
    }
    
    /**
     *   This is what the tree does most of the time - it grows (multiplies) or dies of old age.
     *   
     *    @param newPlants A list to return newly grown plants.
     */
     public void act(List<Living> newPlants){
        incrementAge();
        if(age>maxAge){
            setDead();
        }
        else if(isAlive() && growthProbability>=Math.random() ){
            grow(newPlants);
        }
    }
    
    /**
     * returns the probability of tree getting the disease
     */
    public double getDisease()
    {
        return diseaseProb;
    }
    
    /**
     * If area around plant is unoccupied then they grow.
     * 
     * @param newPlants A list to return newly grown plants.
     */
    public void grow(List<Living> newPlants){
        List<Location> newLocation = field.getFreeAdjacentLocations(getLocation());
        
        for(int n = 0; n<3&& newLocation.size()>0; n++){

            Location free = newLocation.remove(0);
            Plant newTree = new Tree(field, free, t);
            newPlants.add(newTree);
            
            
        }

    }
    
}
