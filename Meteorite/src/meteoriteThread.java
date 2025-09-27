import javax.swing.*;

class meteoriteThread extends Thread {
    JLabel label;
    JPanel panel;
    double dx, dy;
    double posX, posY;

    private PanelMeteorite owner;

    meteoriteThread(JLabel label, JPanel panel, double dx, double dy, PanelMeteorite owner) {
        this.label = label;
        this.panel = panel;
        this.dx = dx;
        this.dy = dy;
        this.owner = owner;
        this.posX = label.getX();
        this.posY = label.getY();
    }

    @Override
    public void run() {
        try {
            while (!interrupted()) {
                posX += dx;
                posY += dy;

                if (posX <= 0) {
                    dx = -dx * 1.1; // กลับทิศ + เพิ่มความเร็ว 10%
                    posX = 0;
                }
                if (posX >= panel.getWidth() - label.getWidth()) {
                    dx = -dx * 1.1;
                    posX = panel.getWidth() - label.getWidth();
                }
                if (posY <= 0) {
                    dy = -dy * 1.1;
                    posY = 0;
                }
                if (posY >= panel.getHeight() - label.getHeight()) {
                    dy = -dy * 1.1;
                    posY = panel.getHeight() - label.getHeight();
                }

                label.setLocation((int) posX, (int) posY);

                owner.check_Collisions();

                Thread.sleep(16);
            }
        } catch (InterruptedException ee) {
        }

    }
}