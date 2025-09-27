import javax.swing.*;

class meteoriteThread extends Thread {
    JLabel meteorite;
    JPanel BackG;
    double dx, dy;
    double posX, posY;

    private PanelMeteorite owner;

    meteoriteThread(JLabel meteorite, JPanel BackG, double dx, double dy, PanelMeteorite owner) {
        this.meteorite = meteorite;
        this.BackG = BackG;
        this.dx = dx;
        this.dy = dy;
        this.owner = owner;
        this.posX = meteorite.getX();
        this.posY = meteorite.getY();
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                posX += dx;
                posY += dy;

                if (posX <= 0) {
                    dx = -dx * 1.25; // กลับทิศ + เพิ่มความเร็ว 20%
                    posX = 0;
                }
                if (posX >= BackG.getWidth() - meteorite.getWidth() - 10) {
                    dx = -dx * 1.25;
                    posX = BackG.getWidth() - meteorite.getWidth() - 10;
                }
                if (posY <= 0) {
                    dy = -dy * 1.25;
                    posY = 0;
                }
                if (posY >= BackG.getHeight() - meteorite.getHeight() - 40) {
                    dy = -dy * 1.25;
                    posY = BackG.getHeight() - meteorite.getHeight()  - 40;
                }

                meteorite.setLocation((int) posX, (int) posY);

                owner.check_Collisions();

                Thread.sleep(16);
            }
        } catch (InterruptedException ee) {
        } finally {
            owner.reduce_Count();
            owner.setCount();
        }
    }

}
