# Requirements Document
CMPT 370
Group A3

## Platform
Robosport must have the capability to run on the Java Virtual Machine, and therefore will run 
on any platform that has the capability to run the Java Virtual Machine. Developer will run test on different operation system such as, Linux and window. Also, developer will test the program  on tuxworld.

## Goals  
There are three main goals for the project:  

1. Write a [simulator](#simulator) that executes the game.   
2. Create a [test platform](#test-bench) to debug the robots.  
3. Write a team of robots in RobotLanguage to compete in matches.  

## Scope
### Must have
#### Simulator
The program must have the ability to import robots via plain-text files containing programs written using the robot language as defined in the RobotSport370 Language specification. During a Robosport match each robot must behave according to their program and must follow the commands defined in their program as defined in the specification with the limitation that the robot must not violate the [rules of a RoboSport Match](#robosport-rules).

There must exist a user interface for the program such that a user may select "RoboSport370 language" program files for
the robots participating in any given match. This interface must also allow the user to define a match to take place,
choosing among the various match types available as are specified below, including at minimum [Watch Match](#watch-match).
and [Instant Results](#instant-results).

### Should have
The program should if possible contain the [Test-Bench](#test-bench) mode specified below.
There should have play and stop button when play tournament. This help user have better control when they watch the tournament.

### Could have
Program could have sound system, which for different weapon when they do damange. Also there could have background music to make the game is more intersting.

### Would Like to have
If development time permits, it would be desirable to have the ability to import entire robot 'teams' as a single file.
Higher quality graphics are also dependent on time available, as is the depth of the robolanguage debugger optionally 
provided as part of the [Test-Bench](#test-bench).

There may be a online version which allow user use different computer control the program.

### Robot Limitations
Every Robot has the following characteristics:
- Health Points
- Movement Range
- Weapon power
- Weapon range
- Team number
- Robot number

Health: If health is 0 or below the robot disappears from the board

Weapon power: The robot will deal this many points of damage when they deal a successful hit to another 
robot.

Weapon range: The robot can successfully hit a robot which is within this many squares of the robot.

Team number: Each team is assigned a number which will allow the robots to identify who is an ally and who is a foe.

Robot number: Each robot is assigned a number which will allow the robots and user to identify each robot on a team.

Movement Range: Each time a robot moves one square its "Movement Range" is decreased by one. It replenishes to its 
specified value at the beginning of each turn.

## Time Limit

Each robot match will terminate after a set amount of turns, this is to prevent stalemates. If a match times out, then whichever team has the most surviving robots will be declared the winner. In the case where both teams have the same number of robots, the team with the most cumulative health points is the winner. If the cumulative health points are equivalent, then the match is declared to be a tie.

## Use Case Diagram

![Use Case](images/use_case.png)\  

## Scenarios
- When the user selects tournament mode from the main screen, an interface to select and configure robot teams will appear.
- When the user confirms their robot team in tournament mode they will be presented with an option to choose interactive 
  or instant mode.
    - When the user chooses interactive mode a screen will appear showing the robot match interface.
    - When the user chooses instant mode a screen will appear presenting the results.

- When the user selects versus mode from the main screen, an interface to select and configure individual robots will appear.
- When the user confirms their robot team in versus mode they will be presented with an option to choose interactive 
  or instant mode.
    - When the user chooses interactive mode a screen will appear showing the robot match interface.
    - When the user chooses instant mode a screen will appear presenting the results.

- When the user selects test-bench mode from the main screen, an interface to select and configure individual robots will appear.
- When the user confirms their robot team in test-bench mode a screen displaying the robot match debugger interface will appear.

Robots must be able to participate and run in tournaments and perform according to [rules and limitations](#robot-limitations).

## Interfaces
### Main Interface
The main interface contains the options to select from the available modes as specified in [Modes](#modes).

## Sequence Diagram
![Interaction Sequence](images/interaction-sequence.png)\  

## Storyboards
Main window:  
![Main](images/Main.jpg)\  

Team selection window:  
![Team Select](images/Teamselect.jpg)\  

Game screen:  
![Game](images/Game.jpg)\  

Debug screen:  
![Debug](images/Debug.jpg)\  

Results screen:  
![Results](images/Results.jpg)\  

## Robosport-Rules

## Modes
#### Watch Match
This mode will allow the user to select 2 or more robots to fight one another and must allow the user to select
program files from which to load each of these robots. The user can either watch the match or step through the 
match one action at a time and observe the results. After the match, it will provide a print out of relevant statistics from the match, including at minimum the winner of the match.

#### Instant Results
This mode will allow the user to select 2 or more robots to fight one another and must allow the user to select
program files from which to load each of these robots. The match will be simulated in full without any visual output to the user. After the match, it will provide a print out of relevant statistics from the match, including at minimum the winner of the match.

#### Test-Bench
Test-Bench mode contains the capability for the user to load robots from files. It must then allow the user to step through 
the program and observe the results. As the match progresses the user must be able to view the statistics of the robots 
involved (e.g. their health, defense, etc.) and change the state of the robots (stats, position)
via a command terminal. Robots may be loaded in via command.