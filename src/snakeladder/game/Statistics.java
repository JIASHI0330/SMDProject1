package snakeladder.game;

import java.util.HashMap;
import java.util.Map;

public class Statistics {

    String playerName;
    Map<Integer,Integer> rolledList = new HashMap<>();

    int traverseUp, traverseDown;

    public Statistics(String playerName){
        this.playerName = playerName;
    }
    public void addToRolledList(int value){
        if (rolledList.containsKey(value)){
            rolledList.put(value,rolledList.get(value)+1);
        }
        else{
            rolledList.put(value,1);
        }
    }

    public void appendUpOccurrence(){
        traverseUp+=1;
    }
    public void appendDownOccurrence(){
        traverseDown+=1;
    }

    @Override
    public String toString(){
        String rolledOccurrence = "";
        for (Map.Entry<Integer, Integer> entry : rolledList.entrySet()) {
            Integer diceRoll = entry.getKey();
            Integer occurrence = entry.getValue();
            String item=diceRoll.toString()+"-"+occurrence.toString()+",";
            rolledOccurrence+=item;
        }
        String result = String.format("%s rolled: %s\n%s traversed: up-%d, down-%d",playerName,rolledOccurrence,playerName,traverseUp,traverseDown);
        return result;
    }
}
