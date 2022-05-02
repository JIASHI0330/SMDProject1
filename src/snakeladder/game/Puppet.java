package snakeladder.game;

import ch.aplu.jgamegrid.*;
import java.awt.Point;

public class Puppet extends Actor
{
  private GamePane gamePane;
  private NavigationPane navigationPane;
  private int cellIndex = 0;
  private int nbSteps;
  private Connection currentCon = null;
  private int y;
  private int dy;
  private boolean isAuto;
  private String puppetName;

  private boolean isLowest = false;
  private boolean isBack = false;

  private Statistics stats;

  public Statistics getStats(){
    return this.stats;
  }

  private ToggleStrategy toggleStrategy = new BasicToggleStrategy();

  public void setBack(boolean isBack){
    this.isBack = isBack;
  }

  Puppet(GamePane gp, NavigationPane np, String puppetImage,String puppetName)
  {
    super(puppetImage);
    this.gamePane = gp;
    this.navigationPane = np;
    this.puppetName = puppetName;
    this.stats = new Statistics(puppetName);
  }

  public boolean isAuto() {
    return isAuto;
  }

  public void setAuto(boolean auto) {
    isAuto = auto;
  }

  public String getPuppetName() {
    return puppetName;
  }

  public void setPuppetName(String puppetName) {
    this.puppetName = puppetName;
  }

  void go(int nbSteps)
  {
    if (cellIndex == 100)  // after game over
    {
      cellIndex = 0;
      setLocation(gamePane.startLocation);
    }
    this.nbSteps = nbSteps;

    if (nbSteps > 0){
      stats.addToRolledList(nbSteps);
    }

    // check if the roll has the lowest value
    if(nbSteps == navigationPane.getNumberOfDice()){
      isLowest = true;
    }else{
      isLowest = false;
    }


    setActEnabled(true);
  }

  void resetToStartingPoint() {
    cellIndex = 0;
    setLocation(gamePane.startLocation);
    setActEnabled(true);
  }

  int getCellIndex() {
    return cellIndex;
  }

  /* private void moveToNextCell()
  {
    int tens = cellIndex / 10;
    int ones = cellIndex - tens * 10;
    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
    {
      if (ones == 0 && cellIndex > 0)
        setLocation(new Location(getX(), getY() - 1));
      else
        setLocation(new Location(getX() + 1, getY()));
    }
    else     // Cells starting left 20, 40, .. 100
    {
      if (ones == 0)
        setLocation(new Location(getX(), getY() - 1));
      else
        setLocation(new Location(getX() - 1, getY()));
    }
    cellIndex++;
  } */

  private void moveToCell(int nbSteps){
    if(nbSteps > 0){
      cellIndex++;
    }else if(nbSteps < 0){
      cellIndex--;
    }
    setLocation(GamePane.cellToLocation(cellIndex));
  }

  public void act()
  {
    if ((cellIndex / 10) % 2 == 0)
    {
      if (isHorzMirror())
        setHorzMirror(false);
    }
    else
    {
      if (!isHorzMirror())
        setHorzMirror(true);
    }

    // Animation: Move on connection
    if (currentCon != null && !(isLowest && currentCon.cellEnd - currentCon.cellStart < 0))
    {
      int x = gamePane.x(y, currentCon);
      setPixelLocation(new Point(x, y));
      y += dy;

      // Check end of connection
      if ((dy > 0 && (y - gamePane.toPoint(currentCon.locEnd).y) > 0)
        || (dy < 0 && (y - gamePane.toPoint(currentCon.locEnd).y) < 0))
      {
        gamePane.setSimulationPeriod(100);
        setActEnabled(false);
        setLocation(currentCon.locEnd);
        cellIndex = currentCon.cellEnd;
        setLocationOffset(new Point(0, 0));
        currentCon = null;
        navigationPane.prepareRoll(cellIndex);
      }
      return;
    }

    // Normal movement
    if (nbSteps != 0)
    {
      moveToCell(nbSteps);

      if (cellIndex == 100)  // Game over
      {
        setActEnabled(false);
        navigationPane.prepareRoll(cellIndex);
        return;
      }

      if(nbSteps > 0){
        nbSteps--;
      }
      if(nbSteps < 0){
        nbSteps++;
      }
      if (nbSteps == 0)
      {
        // Check if on connection start
        if ((currentCon = gamePane.getConnectionAt(getLocation())) != null && !(isLowest && currentCon.cellEnd - currentCon.cellStart < 0))
        {
          gamePane.setSimulationPeriod(50);
          y = gamePane.toPoint(currentCon.locStart).y;
          if (currentCon.locEnd.y > currentCon.locStart.y)
            dy = gamePane.animationStep;
          else
            dy = -gamePane.animationStep;
          if (currentCon instanceof Snake)
          {
            navigationPane.showStatus("Digesting...");
            navigationPane.playSound(GGSound.MMM);
          }
          else
          {
            navigationPane.showStatus("Climbing...");
            navigationPane.playSound(GGSound.BOING);
          }

          if (currentCon.cellEnd - currentCon.cellStart > 0){
            stats.appendUpOccurrence();
          }
          else{
            stats.appendDownOccurrence();
          }
        }
        //changed this for q3
        else if(this.isBack==true){
          System.out.println("backwards");
          setActEnabled(true);
          this.isBack = false;
        }
        else
        {
          setActEnabled(false);
          navigationPane.prepareRoll(cellIndex);
        }

        // Check if needed to use toggle mode
        if(isAuto){
          boolean toToggle = toggleStrategy.checkIfToggle(navigationPane, gamePane);
          if(toToggle){
            navigationPane.toggleButton(true);
          }else{
            navigationPane.toggleButton(false);
          }
        }
      }
    }
  }

}
