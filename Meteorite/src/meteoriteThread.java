import javax.swing.*;

class meteoriteThread extends Thread {
    JLabel meteorite;
    JPanel BackG;
    double dx, dy;
    double posX, posY;
    double maxSpeed = Constants.MAX_SPEED;
    int id ;
    private PanelMeteorite owner;

    meteoriteThread(JLabel meteorite, JPanel BackG, double dx, double dy, PanelMeteorite owner,int id) {
        this.meteorite = meteorite;
        this.BackG = BackG;
        this.dx = dx;
        this.dy = dy;
        this.owner = owner;
        this.posX = meteorite.getX();
        this.posY = meteorite.getY();
        this.id  = id;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted() && !PanelMeteorite.getDead(this.id) ) {
                posX += dx;
                posY += dy;
                if (posX <= 0) {
                    dx = -dx * Constants.increase_SPEED;
                    if (dx >  maxSpeed) dx =  maxSpeed;
                    if (dx < -maxSpeed) dx = -maxSpeed;
                    posX = 0;
                }
                if (posX >= BackG.getWidth() - meteorite.getWidth() - 10) {
                    dx = -dx * Constants.increase_SPEED;
                    if (dx >  maxSpeed) dx =  maxSpeed;
                    if (dx < -maxSpeed) dx = -maxSpeed;
                    posX = BackG.getWidth() - meteorite.getWidth() - 10;
                }
                if (posY <= 0) {
                    dy = -dy * Constants.increase_SPEED;
                    if (dy >  maxSpeed) dy =  maxSpeed;
                    if (dy < -maxSpeed) dy = -maxSpeed;
                    posY = 0;
                }
                if (posY >= BackG.getHeight() - meteorite.getHeight() - 40) {
                    dy = -dy * Constants.increase_SPEED;
                    if (dy >  maxSpeed) dy =  maxSpeed;
                    if (dy < -maxSpeed) dy = -maxSpeed;
                    posY = BackG.getHeight() - meteorite.getHeight() - 40;
                }
                if (!PanelMeteorite.getDead(this.id)){
                    meteorite.setLocation((int) posX, (int) posY);
                    owner.check_Collisions();
                }

                Thread.sleep(10);
            }
        } catch (InterruptedException ee) {
        }
    }

}
