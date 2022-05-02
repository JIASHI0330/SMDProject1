package snakeladder.game;

import ch.aplu.jgamegrid.Location;

import java.util.List;

public class BasicToggleStrategy implements ToggleStrategy {

    public boolean checkIfToggle(NavigationPane np, GamePane gp){
        int numberOfDice = np.getNumberOfDice();
        int nextPlayer = gp.getCurrentPuppetIndex()  == 0 ? 1 : 0;
        List<Puppet> allPuppets = gp.getAllPuppets();
        Puppet next = allPuppets.get(nextPlayer);
        int puppetIndex = next.getCellIndex();
        int upPaths = 0;
        int downPaths = 0;
        for (int i = puppetIndex + 1; i < (puppetIndex + 1 + 6 * numberOfDice); i++){
            Location loc = gp.cellToLocation(i);
            if (gp.getConnectionAt(loc)!=null){
                Connection conn = gp.getConnectionAt(loc);
                int start = conn.getCellStart();
                int end = conn.getCellEnd();
                //up path
                if (start - end < 0){
                    upPaths++;
                }
                else {
                    downPaths++;
                }
            }
        }
        if (upPaths>=downPaths){
            return true;
        }
        else{
            return false;
        }


        // Check everything through GamePane
        // calculate which position will the next player be at in the next round


        // In those positions, if the up connection is more than the down connection
        // If there is more up and down, return true, otherwise return false

    }
}
