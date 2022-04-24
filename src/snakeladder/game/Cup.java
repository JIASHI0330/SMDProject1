package snakeladder.game;

import java.util.ArrayList;

public class Cup {
    private ArrayList<Die> dice;

    private NavigationPane np;
    private int totalRolled;

    public Cup(NavigationPane np){
        this.np = np;
        this.dice = new ArrayList<>();
        this.totalRolled = 0;
    }

    public void roll(int nb){
        int size = dice.size();
        Die die = new Die(nb, this, size + 1);
        dice.add(die);
        totalRolled += nb;
    }

    public void finishedRolling(int index){
        if(index == np.getNumberOfDice()){
            np.startMoving(totalRolled);
            resetCup();
        }
    }

    public ArrayList<Die> getAllDice(){
        return this.dice;
    }

    private void resetCup(){
        this.totalRolled = 0;
        this.dice.clear();
    }
}
