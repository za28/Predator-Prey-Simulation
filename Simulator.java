import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;


/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing living things.
 * 
 * @author David J. Barnes, Michael Kölling, Zahra Amaan and Imaan Ghafur
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    private int diseaseTime;
    private int diseaseDuration;
    private Boolean diseaseSet=false;
    
    // The default width and depth of the grid
    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_DEPTH = 80;
    
    // The probability that a living thing will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.045;
    private static final double OWL_CREATION_PROBABILITY = 0.018;
    private static final double HUMAN_CREATION_PROBABILITY = 0.036;
    private static final double CYCLOPS_CREATION_PROBABILITY = 0.012;
    private static final double WILDCAT_CREATION_PROBABILITY = 0.05;
    private static final double WAREWOLF_CREATION_PROBABILITY = 0.057;
    private static final double MOUSE_CREATION_PROBABILITY = 0.1;
    private static final double TREE_CREATION_PROBABILITY = 0.01;
    private static final double GRASS_CREATION_PROBABILITY = 0.1;
    
    
    // List of animals in the field.
    private List<Living> animals;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    
    // A graphical view of the simulation.
    private SimulatorView view;
    
    private Time time;
    private List<Disease> diseases;
    private List<Living> plants;
    
    private static boolean isRunning;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        animals = new ArrayList<>();
        diseases = new ArrayList<>();
        plants = new ArrayList<>();
        
        time = new Time();

        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width, time, this);
        view.setColor(Bear.class,new Color(238, 134, 55));
        view.setColor(Owl.class, new Color(227,218,98));
        view.setColor(Human.class, new Color(230,138,179));
        view.setColor(Cyclops.class, new Color(189,36,176));
        view.setColor(WildCat.class, new Color(148,102,222));
        view.setColor(Warewolf.class, new Color(124,16,16));
        view.setColor(Mouse.class, new Color(3,94,252));
        view.setColor(Grass.class, new Color(0, 204, 0));
        view.setColor(Tree.class, new Color(0,102,0));
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (2000 steps).
     */
    public void runLongSimulation()
    {
       
        simulate(2000);
        
    
    }
    
    /**
     * Checks if the simulation is running
     * 
     * @return true if simulation is running, false if it is not
     */
    public Boolean getRunning(){
        return isRunning;
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        if(!isRunning){
            isRunning= true;   
        }
        
        if(diseaseSet==false){
        Random rand = Randomizer.getRandom();
        diseaseDuration=rand.nextInt(150-50)+50; // generate andom num between 50 and 150 
        diseaseTime= rand.nextInt(numSteps);// this generates a random time at which the disease starts to develop
        diseaseSet=true;
        }   
        
        for(int step = 0; step <= numSteps && view.isViable(field) && isRunning == true ; step++) {
            if(time.isDay()){
                simulateOneStep();
            }
            else{
                simulateOneStep();
                delay(100); // runs more slowly at night 
            }
            
        }
        
        isRunning=false;
    }
    
    /**
     * Pauses the simulation
     */
    public void pause(){
        isRunning = false;
    }
    
    /**
     * Stops the simulation
     */
    public void stop(){
        System.exit(0);
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * bear and rabbit.
     */
    public void simulateOneStep()
    
    {
        step++;
        time.setSteps(step);

        // Provide space for newborn animals.
        List<Living> newanimals = new ArrayList<>();   
        List<Disease> newDiseases = new ArrayList<>();
        List<Living> newPlants = new ArrayList<>();
        
        // Let all living things act act.
        for(Iterator<Living> it = animals.iterator(); it.hasNext(); ) {
            Living animal = it.next();
            animal.act(newanimals);
            if(! animal.isAlive()) {
                it.remove();
            }
        }
        
        for(Iterator<Living> they = plants.iterator(); they.hasNext();){
            
            Living plant = they.next();
            plant.act(newPlants);
            if(! plant.isAlive()){
                they.remove();
            }
           
            
        }
        // we check if it is time for disease and that we have not gone over the duration and start the disease
        if(step>diseaseTime-1 && step<diseaseTime+diseaseDuration ){
            
            placeDisease();
            for(Iterator<Disease> d = diseases.iterator(); d.hasNext();){
                Disease disease = d.next();
                disease.spread(newDiseases);
            }
   
        }
        else if (step>diseaseDuration+diseaseTime){
            for(Iterator<Disease> d = diseases.iterator(); d.hasNext();){
                Disease disease = d.next();
                disease.gone();
            }
        }
                
        // Add the newly born animals to the main lists.
        animals.addAll(newanimals);
        diseases.addAll(newDiseases);
        
        view.showStatus(step, field);
    }
    
    /**
     * places the disease at random location
     */
    private void placeDisease(){
        int rMax = field.getDepth()-1;
        int cMax = field.getWidth()-1;
        int min = 0;
        int row = (int)(Math.random()*(rMax-min+1)+min);
        int col = (int)(Math.random()*(cMax-min+1)+min);
        
        Location location = new Location(row,col);
        field.clear(location);
        Disease disease = new Disease(field, location);
        diseases.add(disease);
    }
        
    /**
     * Resets the simulator
     */
    public void reset()
    {
        step = 0;
        time.setSteps(step);
        pause();
        animals.clear();
        diseases.clear();
        plants.clear();
        diseaseSet=false;
        
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    

    /**
     * Randomly populate the field with living things.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        
        
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Bear bear = new Bear(true, field, location, time);
                    animals.add(bear);
                }
                else if(rand.nextDouble() <= OWL_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Owl owl = new Owl(true, field, location, time);
                    animals.add(owl);
                }
                else if(rand.nextDouble() <= HUMAN_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Human human = new Human(true, field, location, time);
                    animals.add(human);
                }
                else if(rand.nextDouble() <= CYCLOPS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Cyclops cyclops = new Cyclops(true, field, location, time);
                    animals.add(cyclops);
                }
                else if(rand.nextDouble() <= WILDCAT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    WildCat wildcat = new WildCat(true, field, location, time);
                    animals.add(wildcat);
                }
                else if(rand.nextDouble() <= WAREWOLF_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Warewolf warewolf = new Warewolf(true, field, location, time);
                    animals.add(warewolf);

                }else if(rand.nextDouble() <= MOUSE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Mouse mouse = new Mouse(true, field, location, time);
                    animals.add(mouse);
                }else if(rand.nextDouble() <= TREE_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Tree tree = new Tree(field, location, time);
                    plants.add(tree);
                   
                    
                    
                }else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Grass grass = new Grass(field, location, time);
                    plants.add(grass);
                   
                    
                }
               
                // else leave the location empty.
            }
        }
        
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    
}
