import java.util.List;

/**
 * Grass is a type of plant in our simulation
 *
 * @author Zahra Amaan and Imaan Ghafur 
 */
public class Grass extends Plant
{
    // probability that the grass grows
    private double growthProbability;
    //probability that the grass gets infected by a disease
    public double diseaseProb = 0.001;
    // maximumage of grass
    private int maxAge = 1000;
    
    /**
     * Create grass. Grass can grow and die.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param time The time object that keeps track of time
     */
    public Grass(Field field, Location location, Time time)
    {
        super(field,location, time);
        growthProbability = 0.03;
        age=setAge(maxAge);
        
    }
    
    /**
     *   This is what the grass does most of the time - it grows (multiplies) or dies of old age.
     *   
     *   @param newPlants A list to return newly grown plants.
     */
    public void act(List<Living> newPlants){
        incrementAge();
        if(t.isWinter()){
            growthProbability=0.001;//grass grows slower in winter
        }else{
           growthProbability=0.01;
        }
        if(age>maxAge){
            setDead();
        }
        if(isAlive() && growthProbability>=Math.random() ){
            grow(newPlants);
            
        }
    }
    
    /**
     * returns the probability of grass getting the disease
     */
    public double getDisease()
    {
        return diseaseProb;
    }
    
    /**
     * If area around plant is unoccupised then they grow
     * 
     * @param newPlants A list to return newly grown plants.
     */
    public void grow(List<Living> newPlants){
        List<Location> newLocation = field.getFreeAdjacentLocations(getLocation());
        
        for(int n = 0; n<3&& newLocation.size()>0; n++){

            Location free = newLocation.remove(0);
            Plant newGrass = new Grass(field, free, t);
            newPlants.add(newGrass);
            
            
        }
        
    }

}
