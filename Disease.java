import java.util.Random;
import java.util.List;
/**
 * This calss models the disease. Diseases appear randomly.
 *
 * @author Imaan Ghafur and Zahra Amaan
 */
public class Disease
{
    private Field field;
    private int col;
    private int row;
    private Location location;

    /**
     * Constructor for objects of class Disease
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
        public Disease(Field field, Location location)
    {
        // initialise instance variables
        this.field=field;
        setLocation(location);

    }
    
    /**
     * Sets the location of the disease.
     * 
     * @param location The location within the field.
     */
        public void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Returns the location of the disease.
     * 
     * @return location The location within the field.
     */
    public Location getLocation(){
        return location;
    }
    
    /**
     * This method allows the disease to infect an animal and then to take up the location of that animal 
     * the disease object that replaces the animal is an 'infected animal'
     * the infection leads to the death of the animal
     * 
     * @param diseases A list to return newly born diseases.
     * @return diseases The list of new diseases
     */
        public List spread(List<Disease> diseases){
        List<Location> adjacent = field.adjacentLocations(getLocation());
        int counter = 0;
        int maxInfected = 3;
        
        for(int m = 0; m<adjacent.size() && counter<maxInfected  ; m++){
           Location l = adjacent.get(m);
           Object affect = field.getObjectAt(l);
           double randomDbl = Math.random();
           
            if(affect instanceof Living  ){
               Living animal = (Living) affect;
               if(animal.isAlive()&& randomDbl <= animal.getDisease()){ // and no animal is extinct
                   Location newLocation = animal.getLocation();
                   animal.setDead();
                   Disease newDisease = new Disease(field, newLocation);
                   diseases.add(newDisease);
                   counter++;
                   
                   
                }
            }
            
        }
        return diseases;
    }
    
    /**
     * Deletes the disease
     */        
        public void gone(){
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
}
