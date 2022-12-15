public class Game
{
    private Grid grid;
    private int userRow;
    private int userCol;
    private int msElapsed;
    private int timesGet;
    private int timesAvoid;
    private int speed;
    private boolean speedUp;
    private int count;

    public Game()
    {
        grid = new Grid(10, 10);
        userRow = 0;
        userCol = 0;
        speed = 150;
        speedUp = true;
        count = 10;
        msElapsed = 0;
        timesGet = 0;
        timesAvoid = 0; 
        updateTitle();
        grid.setImage(new Location(userRow, 0), "user.gif");
    }

    public void play()
    {
        while (!isGameOver())
        {
            grid.pause(100);
            handleKeyPress();

            if (msElapsed % speed == 0){
                scrollLeft();
                populateRightEdge();
            }
            updateTitle();
            msElapsed += 30;
        }
    }

    public void handleKeyPress(){
        int key = grid.checkLastKeyPressed();
        if(key == 38 && userRow > 0){ //move up
            grid.setImage(new Location(userRow, userCol), null);
            userRow--;
            handleCollision(new Location(userRow, userCol));
            grid.setImage(new Location(userRow, userCol), "user.gif");
        }else if(key == 40 && userRow < grid.getNumRows() - 1){ //move down
            grid.setImage(new Location(userRow, userCol), null);
            userRow++;
            handleCollision(new Location(userRow, userCol));
            grid.setImage(new Location(userRow, userCol), "user.gif");
        }else if(key == 39 && userCol < grid.getNumCols() - 1){ //move right
            grid.setImage(new Location(userRow, userCol), null);
            userCol++;
            handleCollision(new Location(userRow, userCol));
            grid.setImage(new Location(userRow, userCol), "user.gif");
        }else if(key == 37 && userCol > 0){ //move left
            grid.setImage(new Location(userRow, userCol), null);
            userCol--;
            handleCollision(new Location(userRow, userCol));
            grid.setImage(new Location(userRow, userCol), "user.gif");
        }
    }

    public void populateRightEdge(){
        grid.setImage(new Location((int)(Math.random() * grid.getNumRows()), grid.getNumCols()-1), "avoid.gif");
        grid.setImage(new Location((int)(Math.random() * grid.getNumRows()), grid.getNumCols()-1), "get.gif");
        //grid.setImage(new Location((int)(Math.random() * grid.getNumRows()), grid.getNumCols()-1), null);
    }

    public void scrollLeft(){
        handleCollision(new Location(userRow, userCol + 1));
        for(int i = 0; i < grid.getNumRows(); i++){
            for(int j = 1; j < grid.getNumCols(); j++){
                String image = grid.getImage(new Location(i, j));
                Location imageLeft = new Location(i, j-1);
                if(image != null && !image.equals("user.gif")){
                    if(grid.isValid(imageLeft)){
                        grid.setImage(new Location(i, j),null);
                        grid.setImage(new Location(i, j-1), image);
                        grid.setImage(new Location(userRow, userCol), "user.gif");
                    }
                }else{
                    grid.setImage((imageLeft), null);
                }
            }
        }
        grid.setImage(new Location(userRow, userCol), "user.gif");
    }

    public void handleCollision(Location loc){
        if(grid.isValid(new Location(userRow,userCol + 1)) == true){
            String image = grid.getImage(loc);
            if(image != null){
                if(image.equals("avoid.gif")){
                    timesAvoid++;
                }
                if(image.equals("get.gif")){
                    timesGet++;
                }
            }
        }
    }

    public int getScore()
    {  
        int score = timesGet;
        if(score % 10 == 0){
            if(score != 0 && speedUp == true && speed >= 60){
                speedUp = false;
                speed -= 30;
            }
        }else{
            speedUp = true;
        }
        return score;
    }

    public void updateTitle()
    {
        int healthRemaining = 10 - timesAvoid;
        grid.setTitle("Speed For Need, Score: " + getScore() + " Hits remaining: " + healthRemaining);
    }

    public boolean isGameOver()
    {
        if(timesAvoid >= 10){
            return true;
        }
        return false;
    }

    public static void test()
    {
        Game game = new Game();
        game.play();
    }

    public static void main(String[] args)
    {
        test();
    }
}