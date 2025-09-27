import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;

class PanelMeteorite extends JFrame {

    int amountMeteorite = Constants.amount_meteorite;
    JLabel[] meteorite = new JLabel[amountMeteorite];
    meteoriteThread[] mtoT = new meteoriteThread[amountMeteorite];
    ImageIcon[] mtoIcon = new ImageIcon[amountMeteorite];

    boolean[] dead = new boolean[amountMeteorite];

    Random rand = new Random();
    // พื้นหลัง
    private JPanel BackG;

    PanelMeteorite() {
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackG = new JPanel(null);
        BackG.setBackground(Color.BLACK);

        for (int i = 0; i < meteorite.length; i++) {
            String chosenFile = Constants.imageFiles[rand.nextInt(Constants.imageFiles.length)];

            ImageIcon rawIcon = new ImageIcon(System.getProperty("user.dir") + File.separator + "src" + File.separator + "Image" + File.separator + chosenFile);
            Image scaled = rawIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            mtoIcon[i] = new ImageIcon(scaled);

            meteorite[i] = new JLabel(mtoIcon[i]);
            meteorite[i].setSize(50, 50);

            int W = Constants.WINDOW_WIDTH;
            int H = Constants.WINDOW_HEIGHT;
            // สุ่มตำแหน่ง (กันขอบ 50px)
            int margin = 50;

            meteorite[i].setLocation(
                    rand.nextInt(Math.max(1, W - meteorite[i].getWidth() - 2 * margin)) + margin,
                    rand.nextInt(Math.max(1, H - meteorite[i].getHeight() - 2 * margin)) + margin);
            BackG.add(meteorite[i]);

            // ความเร็วสุ่ม
            double dx = (rand.nextBoolean() ? 1 : -1) * (rand.nextDouble(0.6) + 0.2); // 0.2..0.8
            double dy = (rand.nextBoolean() ? 1 : -1) * (rand.nextDouble(0.6) + 0.2);

            mtoT[i] = new meteoriteThread(meteorite[i], BackG, dx, dy, this);
        }

    
        add(BackG);
        setVisible(true);

        // รอสร้างทุก Thread เสร็จก่อนค่อยไป start Thread
        for (int i = 0; i < meteorite.length; i++) {
            mtoT[i].start();
        }
    }

    void check_Collisions() {
    for (int i = 0; i < meteorite.length; i++) {
        if (meteorite[i] == null || dead[i] || !meteorite[i].isVisible()) continue;
        Rectangle r1 = meteorite[i].getBounds();

        for (int j = i + 1; j < meteorite.length; j++) {
            if (meteorite[j] == null || dead[j] || !meteorite[j].isVisible()) continue;
            Rectangle r2 = meteorite[j].getBounds();

            if (r1.intersects(r2)) {
                int kill = rand.nextBoolean() ? i : j;
                if (!dead[kill] && meteorite[kill] != null && meteorite[kill].isVisible()) {
                    dead[kill] = true;
                    meteorite[kill].setVisible(false);
                    try { 
                        if (mtoT[kill] != null) mtoT[kill].interrupt(); 
                    } catch (Exception e) {}
                }
            }
        }
    }
}

}