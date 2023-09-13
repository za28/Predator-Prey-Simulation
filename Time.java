
/**
 * This class models time. It has the time of day and time of year (seasons).
 *
 * @author Zahra Amaan and Imaan Ghafur
 */
public class Time
{
    public enum Season{SPRING, SUMMER, AUTUMN, WINTER} 
    public Season season;
    int steps;
    boolean isSummer=false;
    boolean isWinter=false;

    /**
     * Constructor for objects of class Time
     * Initialises the season to spring and the steps to 0
     */
    public Time()
    {
        season = Season.SPRING;
        steps = 0;
    }

    /**
     * Checks if it is day.
     * 
     * @return true if it is day and false if it is night 
     */
    public Boolean isDay(){
        int t = 0;
        
        season = this.getSeason();
        switch (season){
            case SPRING:
                t=10 ;
                break;
            case SUMMER:
                t=15;
                break;
            case AUTUMN:
                t=10;
                break;   
            case WINTER:
                t=5;
                break;
        }
        if(steps % 20 <t){
            return true;   
        }else{
            return false;
        }
        
    }
    
    /**
     * Checks and returns the current season
     * 
     * @return the current season
     */
    public Season getSeason(){
        int t = steps%2000;
        
        if(t<=500){
            isSummer=false;
            isWinter=false;
            return Season.SPRING;
            
            
        }
        else if(t>500 && t<=1000){
            isSummer=true;
            return Season.SUMMER;
            
        }
        else if(t>1000 && t<=1500){
            isSummer=false;
            isWinter=false;
            return Season.AUTUMN;

        }
        else{
            isWinter=true;
            return Season.WINTER;

        }
        
    }
    
    /**
     * Sets the number of steps 
     * 
     * @param s - the number of steps
     */
    public void setSteps(int s){
        steps = s;
    }
    
    /**
     * Checks if the season is winter. True if winter, false if not
     * 
     * @return true if the season is winter and false if it is not
     */
    public Boolean isWinter(){
        return isWinter;
        
    }
    
    /**
     * Checks if the season is summer. True if summer, false if not
     * 
     * @return true if the season is summer and false if it is not
     */
        public Boolean isSummer(){
        return isSummer;
        
    }
    
}
