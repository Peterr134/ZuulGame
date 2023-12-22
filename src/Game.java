/**
 *  This class is the main class of the "World of Zuul" application.
 *  "World of Zuul" is a very simple, text based adventure game.  Users
 *  can walk around some scenery. That's all. It should really be extended
 *  to make it more interesting!
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import java.util.HashMap;
public class Game
{
    private Parser parser;
    private Room currentRoom;

    /**
     * Create the game and initialise its internal map.
     */
    public Game()
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room grove, spookyForest, village, stump,
                graveyard, appleTree, caveEntrance, yourHouse,
                wizardTower, farms, waypoint, cave, sleepRoom;

        // create the rooms
        grove = new Room("grove","You enter an opening in the forest around you.", "The small pool of water nearby reflects the moon.");
        spookyForest = new Room("spookyForest","The forest here feels spooky. Wafts of mist blanket sections of the ground.", "You feel scared.");
        village = new Room("village","You enter your home village", "Most people are asleep right now, save for the occasional resident.");
        stump = new Room("stump","A strangely large stump sits in the center of the clearing", "You see a strange shape moving closer from far away.");
        graveyard = new Room("graveyard","You enter the old graveyard for the village.", "You feel an ectoplasmic presence near you.");
        appleTree = new Room("appleTree","You see a large apple tree nearby");
        caveEntrance = new Room("caveEntrance","A dark cave entrance looms in front of you.");
        yourHouse = new Room("yourHouse","You're right outside your house");
        wizardTower = new Room("wizardTower","Your magic teachers tower is right ahead of you", "The magic lights nearby have activated, keeping the place well lit.");
        farms = new Room("farms","You're at the farms you used to work at.", "You see one of the wizards spells helping grow the crops.");
        waypoint = new Room("waypoint","A strange stone stands in front of you.");
        cave = new Room("cave","This is the end of the game for now! good job!");
        sleepRoom = new Room("sleepRoom");

        // initialise room exits (north, east, south, west)
        grove.setExits(waypoint, village, caveEntrance, spookyForest);
        spookyForest.setExits(graveyard, grove, appleTree, stump);
        village.setExits(yourHouse, farms, wizardTower, grove);
        caveEntrance.setExit("inside",cave);
        cave.setExit("outside", caveEntrance);
        yourHouse.setExit("sleep", sleepRoom);
        sleepRoom.setExit("outdoors", yourHouse);
        wizardTower.setItem("broom");


        stump.setExits(null, spookyForest, null, null);
        graveyard.setExits(null, null, spookyForest, null);
        appleTree.setExits(spookyForest, null, null, null);
        caveEntrance.setExits(grove, null, null, null);
        waypoint.setExits(null, null, grove, null);
        farms.setExits(null, null, null, village);
        yourHouse.setExits(null, null, village, null);
        wizardTower.setExits(village, null, null, null);


        currentRoom = yourHouse;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            if(currentRoom.getRoomName() != null) {
                if (currentRoom.getRoomName().equals("sleepRoom")) {
                    System.out.println("You fall asleep");
                    if (Time.isNight()) {
                        Time.setTimeOfDay(480);
                        System.out.println("You wake up in the day");
                    } else {
                        Time.setTimeOfDay(1200);
                        System.out.println("You wake up in the night");
                    }
                }
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Not-Zuul!");
        System.out.println("World of Not-Zuul is a new, incredibly Not-boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println("You play as a young wizard apprentice");
        Time.initializeTimeOfDay();
        System.out.println();
        System.out.println(currentRoom.getDescription());
        printLocationInfo();
    }

    private void printLocationInfo(){
        System.out.print("You can go: ");
        System.out.print(currentRoom.getExitString());
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        String secondWord = command.getSecondWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }else if (commandWord.equals("checkInv")){
            System.out.print("You see: ");
            for(String item : Item.heldItems) {
                System.out.print(" " + item);
            }
            System.out.println();
        }else if(commandWord.equals("grab")){
            if(currentRoom.getItem().equalsIgnoreCase(secondWord)){
                Item.heldItems.add(currentRoom.getItem());
                System.out.println("You obtain the " + currentRoom.getItem());
                currentRoom.setItem("");
            }else{
                System.out.println("You don't see anything to grab...");
            }
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp()
    {
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
    }

    /**
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) {
            System.out.println("You decide against going that way...");
        }
        else {
            currentRoom = nextRoom;
            Time.changeTime(15);
            System.out.println(currentRoom.getDescription());
            if(!currentRoom.getItem().isEmpty()){
                System.out.println("You see a " + currentRoom.getItem() + " nearby");
            }
            if(Time.isNight() && currentRoom.getNightDescription() != null) {
                System.out.println(currentRoom.getNightDescription());
            }
            printLocationInfo();
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
