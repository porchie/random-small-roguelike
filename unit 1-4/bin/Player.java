public class Player extends MovingEnt{
  
private boolean keyCollected;
private int hpPots;
private int maxHp;


  public Player()
  {
    String playerChar = 
    "<span bgcolor= white>"              + 
    "<font color='blue'>"                + 
    "@"                                  +
    "</span>"                            +
    "</font>"; 

    setChar(playerChar);
    setRow(0);
    setCol(0);
    maxHp = 100;
    setHp(maxHp);
    
    setDmg(1);
    keyCollected = false;
    hpPots = 0;
  }

  public void keyCollected(boolean bool)
  {
    keyCollected = bool;
  }

  public boolean getKey()
  {
    return keyCollected;
  }

  public void addPot()
  {
    hpPots++;
  }

  public void usePot()
  {
    hpPots--;
  }

  public int getPots()
  {
    return hpPots;
  }

  @Override
  public void takeDmg(int dmg)
  {
    setHp(getHp() - dmg);
    if (getHp() > maxHp)
    {
      setHp(maxHp);
    }
    if (getHp() < 0)
    {
      setHp(0);
    }
  }
  

 
 }