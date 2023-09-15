# Predator-Prey-Simulation
Simulation of predator and prey

<img width="452" alt="image" src="https://github.com/za28/Predator-Prey-Simulation/assets/114661472/393e6a07-1d07-46c5-9cbf-1e99b8ba7c51">


## Description of simulation: 
### Species:
We have 7 species of animals in our simulation represented by different colours 
**Animals** 
•	-  Cyclops: dark purple 
•	-  Human: pink 
•	-  Warewolf: brown 
•	-  Wild cat: light purple 
•	-  Owl: yellow 
•	-  Bear: orange 
•	-  Mouse: blue
We have 2 species of plants in our simulation **Plants**
•	-  Tree: dark green (dark brown in summer and blue in winter) 
•	-  Grass: light green (brown in summer and light blue in winter) 
**Interactions:**
Cyclops is at the top of the food chain in our Simulation, it is not prey to anything, however it kills and then eats the Humans. Humans are hunted by cyclops and werewolves, werewolves do not eat humans however humans are transformed into warewolfs, warewolfs also eat the wildcat. Humans hunt bears who eat mice. Mice are eaten by bears, wildcats and owls, mice are at the bottom of the food chain, however mice do eat grass. 
 
### Behaviour: 
Our simulation is random each time we will see different animals dominate we have done it this way to make it more realistic as in the wild we can never accurately predict what will happen. 
Depending on the time of day and the season some animals exhibit different behaviours, Owls are more likely to hunt at night and werewolves only act at night. Bears are more likely to die in summer due to dehydration and cyclopses are more likely to die in winter as they freeze to death. 
Warewolfs do not breed instead the population size is increased by transforming humans, however just like breeding werewolves need to be a certain age to be able to transform the humans. 
Furthermore, when disease hits, different animals have different probabilities of being infected the owls have the highest probability of being infected by disease and warewolf has the lowest probability of being infected. 
Grass and Trees grow all year round, grass grows at a faster rate than trees. In the winter the growth rate of grass decreases. In addition, the colours of the plants change in Summer and Winter. 
### Other elements of the simulation: 
**Time**
In our simulation we have a time class which enables us to simulate the time of day (day and night) and the time of year (seasons). In our time class we have a method called getSeason which calculate the current season using the number of steps that have passed. We also have a boolean isDay method which checks whether it is day or not. (When it is not day, it is night). In summer the days are longer, and the nights are shorter and in winter the days are 
shorter, and the nights are longer. In our simulation everything moves slower at night to show that things are not as active during the night. The seasons and time of day also has effects on some of the animals and the plants. The owls act less frequently during the night and the werewolves only act during the night. As for the plants, the grass and trees change colour in summer as they become dry, and they also change colour in the winter as they become frosty. Also, the grass grows at a slower rate during the winter. 
**Disease**
Every time a new simulation is run diseaseTime and diseaseDuration integer values are generated base on the number of steps the simulation will run for. When steps are equal to disease time the disease is activated, it is first put at a random location and if there is an animal there it will be infected. After this the disease will spread to any living organisms around it. In order to make sure that the spread of disease is not too rapid each living thing has a disease probability and if that is reached then it will be infected. When a living thing is infected, it is killed and replaced by a disease object, this disease object signifies an ‘infected’ animal or plant, however once the diseaseDuration is reached everything that had been infected is killed. 
### Challenge tasks 
•	-  **Simulate plants** 
We have made a plant superclass in which we have a method to set the plant age and also, we have 2 abstract methods, act and grow. We have made these abstract as in simulator we will be able to call them on plant and each plant will be able implement them differently. We have 2 plants; trees and grass, the growth rate of these 2 plants differ, grass grows quicker than trees. 
Plants appear on free locations in the simulation and grass can be eaten by mice. 
Furthermore, depending on the season, the growth rate of grass is altered and the colour of both plants is changed. 
•	-  **Simulate seasons** 
We have created a time class in which we simulate season. We have simulated season using an Enum. Our enumerated type is called Season and we have 4 seasons spring, summer, autumn and winter which are our elements. 
In order to calculate the season, we have a getSeason method in which we determine the season depending on how many steps have passed. We have made it so that each season is 500 steps long so in our long simulation we will be able to see 1 year. We have done this so that we can visually see all the seasons and all the effects that the seasons have on the animals, animals and the length of the days. 
The animals that are affected by the seasons are the Cyclops and the Bear. The cyclops can freeze to death in the winter and the bear can die from dehydration in the summer. 
For the plants, the trees and grass change colour depending on the season. In the summer the plants become brown because they are dry and in the winter the plants become white because they are frosty. In spring and autumn, the plants are
green. The grass also grows at a slower rate in the winter. 
The length of the day is also affected by the season. In the summer the days are longer, and the nights are shorter but in winter the days are shorter, and the nights are longer. 
To show the time of year we added a JLabel which shows the current season that the simulation is in while the simulation is running. We added the JLabel to the JPanel and the JPanel to the container. 
•	-  **Simulate disease** 
We created a disease class in which we have written methods to set location of disease, spread the disease and delete the disease. 
In Simulator we have a method named placeDisease that will place the disease in a random location. Once the diseases’ location has been set we use the spread method (defined in disease) to spread the disease to instances of living around it. If there is an instance of living next to a disease we will first check if the disease probability has been reached (this is the probability that the animal will be infected) if it has then that object will be set to dead and a new disease object will take over and this shows that the living thing has been infected. Once the duration of the disease has ended all the infected creatures will die. 
In simulator we also have diseaseTime and diseaseDuration these are integer values which are randomly generated each time the simulation runs and define at which step disease will start and how long it will go on for. seasonTime will always be within the timeframe that the user sets, we have done this so that no matter how many steps the user has specified we will still be able to see at least the beginnings of a disease. 
•	-  **Buttons** 
We have added 4 buttons to our simulation: Start, Stop, Pause and Restart. The Start button starts the simulation. The Stop button stops and quits the simulation. The pause button pauses the simulation. The restart button resets the simulation. 
The buttons are implemented using Jbutton for each button. The JButtons are added to a JPanel which uses a box layout. We have used a box layout to organize our buttons and put them in a single column. The panel is then added to the container. We have programmed the buttons using action event listeners and lambdas. The action event listener adds an action when the user clicks the button. We used lambdas in our action event listener to call methods in the simulator class when the buttons are clicked. 

![image](https://github.com/za28/Predator-Prey-Simulation/assets/114661472/37b2d5c6-dc26-4dde-b1b7-958fa4818c7b)
