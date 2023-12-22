/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  The exits are labelled north,
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
import java.util.HashMap;
import java.util.Set;
public class Room
{
    public String description;
    private HashMap<String, Room> exits;
    public String nightDescription;
    private String roomName;
    private String item;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String roomName, String description)
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        this.roomName = roomName;
    }

    public Room(String roomName){
        description = "";
        nightDescription = "";
        exits = new HashMap<String, Room>();
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public Room(String roomName, String description, String nightDescription)
    {
        this.description = description;
        this.nightDescription = nightDescription;
        exits = new HashMap<String, Room>();
    }

    /**
     * Define the exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     * @param north The north exit.
     * @param east The east east.
     * @param south The south exit.
     * @param west The west exit.
     */
    public void setExits(Room north, Room east, Room south, Room west)
    {
        if(north != null) {
            exits.put("north", north);
        }
        if(east != null) {
            exits.put("east", east);
        }
        if(south != null) {
            exits.put("south", south);
        }
        if(west != null) {
            exits.put("west", west);
        }
    }

    public Room getExit(String direction){
        return exits.get(direction);
    }

    public void setExit(String direction, Room neighbor){
        exits.put(direction, neighbor);
    }



    /**
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }

    public String getNightDescription() {
        return nightDescription;
    }

    public String getItem() {
        if(item != null) {
            return item;
        }else{
            return "";
        }
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getExitString() {
        String returnString = "";
        Set<String> keys = exits.keySet();
        for (String exit : keys){
            returnString += " " + exit;
        }
        return returnString;
    }
}
