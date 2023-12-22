import java.util.ArrayList;
public class Item {
    public static ArrayList<String> heldItems = new ArrayList<String>();
    private boolean held;

    public Item(String name, boolean held){
        if(held){
            heldItems.add(name);
        }
    }
    public Item(String name, boolean held, int quantity){
        if(held){
            for(int i = 0; i < quantity; i++){
                heldItems.add(name);
            }
        }
    }

    public void setHeld(boolean held) {
        this.held = held;
    }
}
