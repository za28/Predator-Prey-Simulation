import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.event.ActionListener;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes, Michael Kölling, Zahra Amaan and Imaan Ghafur
 * @version 2016.02.29
 */
public class SimulatorView extends JFrame
{
    // Colors used for empty locations.
    private static final Color DAY_COLOR = new Color(183, 215, 247);
    private static final Color NIGHT_COLOR = Color.black;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private final String SEASON_PREFIX = "Season: ";
    
    private JLabel stepLabel, population, infoLabel, seasonLabel;
    
    private final String STOP_TEXT = "Stop";
    private JButton stopButton;
    
    private final String PAUSE_TEXT = "Pause";
    private JButton pauseButton;
    
    private final String START_TEXT = "Start";
    private JButton startButton;
    
    private final String RESTART_TEXT = "Restart";
    private JButton restartButton;
    
    private FieldView fieldView;
    
    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;
    private Time time;
    private Simulator simulator;

    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width, Time t, Simulator s)
    {
        stats = new FieldStats();
        colors = new LinkedHashMap<>();
        
        simulator = s;

        setTitle("Fox and Rabbit Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        seasonLabel = new JLabel(SEASON_PREFIX, JLabel.CENTER);
        infoLabel = new JLabel("  ", JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
       
        
        stopButton = new JButton(STOP_TEXT);
        stopButton.addActionListener(event -> new Thread((()-> simulator.stop())).start());
        
        pauseButton = new JButton(PAUSE_TEXT);
        pauseButton.addActionListener(event -> new Thread(()->{
            startButton.setEnabled(true);
            restartButton.setEnabled(true);
            simulator.pause();
        }).start());
        
        startButton = new JButton(START_TEXT);
        startButton.addActionListener(event -> new Thread(()->{
            restartButton.setEnabled(false);
            startButton.setEnabled(false);
            simulator.runLongSimulation();
        }).start());
        
    
        
        restartButton = new JButton(RESTART_TEXT);
        restartButton.addActionListener(event -> new Thread((()-> simulator.reset())).start());
        
        time = t;
        
        setLocation(100, 50);
        
        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        
        JPanel panel = new JPanel();
        
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        
        panel.add(stopButton);
        panel.add(pauseButton);
        panel.add(startButton);
        panel.add(restartButton);
        
        JPanel infoPane = new JPanel(new BorderLayout());
            infoPane.add(stepLabel, BorderLayout.WEST);
            infoPane.add(infoLabel, BorderLayout.CENTER);
            infoPane.add(seasonLabel, BorderLayout.EAST);
        contents.add(panel, BorderLayout.EAST);
        contents.add(infoPane, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    
    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class animalClass, Color color)
    {
        colors.put(animalClass, color);
    }

    /**
     * Display a short information label at the top of the window.
     */
    public void setInfoText(String text)
    {
        infoLabel.setText(text);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass)
    {
        Color col = colors.get(animalClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(int step, Field field)
    {
        if(!isVisible()) {
            setVisible(true);
        }
            
        seasonLabel.setText(SEASON_PREFIX + time.getSeason()); 
        
        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();
        
        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if(animal instanceof Living) {
                    stats.incrementCount(animal.getClass());
                    if(time.isWinter()){
                        setColor(Grass.class,new Color(128, 166,179 ));
                        setColor(Tree.class,new Color(107,153,199));
                    } 
                    else if(time.isSummer()){
                        setColor(Grass.class,new Color(179,158,77));
                        setColor(Tree.class,new Color(102,82,0));
                        
                    }
                    else{
                        setColor(Grass.class, new Color(0, 204, 0));
                        setColor(Tree.class, new Color(0,102,0));
                    }
                    
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                }
                else if(animal instanceof Disease){
                    Disease disease1 = (Disease) animal;
                    stats.incrementCount(disease1.getClass());
                    fieldView.drawMark(col, row, Color.RED);
                    
                    
                }
                // changes background (free space colour) depending on time of day
                
                else {
                    if(time.isDay()){
                    fieldView.drawMark(col, row, DAY_COLOR);
                    }
                    else{
                    fieldView.drawMark(col, row, NIGHT_COLOR);
                    }
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }
    
    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
