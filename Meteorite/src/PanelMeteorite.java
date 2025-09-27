import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;

class PanelMeteorite extends JFrame {

    int amountMeteorite = Constants.amount_meteorite;
    JLabel[] meteorite = new JLabel[amountMeteorite];
    meteoriteThread[] mtoT = new meteoriteThread[amountMeteorite];
    ImageIcon[] mtoIcon = new ImageIcon[amountMeteorite];
    int count = amountMeteorite;
    JLabel Count_Meteorite = new JLabel();

    boolean[] dead = new boolean[amountMeteorite];

    Random rand = new Random();
    // พื้นหลัง
    private JPanel BackG;

    PanelMeteorite() {
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        BackG = new JPanel(null);
        BackG.setBackground(Color.BLACK);
        BackG.setBounds(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        add(BackG);

        Count_Meteorite.setText("Meteorite : " + this.count);
        Count_Meteorite.setFont(new Font("SansSerif", Font.BOLD, 16));
        Count_Meteorite.setForeground(Color.WHITE);

        int marginCount = 16;
        Count_Meteorite.setBounds(Constants.WINDOW_WIDTH - 160 - marginCount, marginCount, 160, 30);
        BackG.add(Count_Meteorite);

        for (int i = 0; i < meteorite.length; i++) {
            String chosenFile = Constants.imageFiles[rand.nextInt(Constants.imageFiles.length)];

            ImageIcon rawIcon = new ImageIcon(System.getProperty("user.dir") + File.separator + "src" + File.separator
                    + "Image" + File.separator + chosenFile);
            Image scaled = rawIcon.getImage().getScaledInstance(Constants.Scale_Meteorite, Constants.Scale_Meteorite,
                    Image.SCALE_SMOOTH);
            mtoIcon[i] = new ImageIcon(scaled);

            meteorite[i] = new JLabel(mtoIcon[i]);
            meteorite[i].setSize(50, 50);

            int W = Constants.WINDOW_WIDTH;
            int H = Constants.WINDOW_HEIGHT;
            // สุ่มตำแหน่ง (กันขอบ 30px)(
            int margin = 30;

            meteorite[i].setLocation(
                    rand.nextInt(Math.max(1, W - meteorite[i].getWidth() - 2 * margin)) + margin,
                    rand.nextInt(Math.max(1, H - meteorite[i].getHeight() - 2 * margin)) + margin);
            BackG.add(meteorite[i]);

            // ความเร็วสุ่ม
            double dx = (rand.nextBoolean() ? 1 : -1) * (rand.nextDouble(1.2) + 0.2); // 0.2..1.4
            double dy = (rand.nextBoolean() ? 1 : -1) * (rand.nextDouble(1.2) + 0.2);

            mtoT[i] = new meteoriteThread(meteorite[i], BackG, dx, dy, this);
        }

        setVisible(true);

        // รอสร้างทุก Thread เสร็จก่อนค่อยไป start Thread ทุกตัว
        for (int i = 0; i < meteorite.length; i++) {
            mtoT[i].start();
        }
    }

    void check_Collisions() {
        for (int i = 0; i < meteorite.length; i++) {
            if (meteorite[i] == null || dead[i] || !meteorite[i].isVisible())
                continue;

            for (int j = i + 1; j < meteorite.length; j++) {
                if (meteorite[j] == null || dead[j] || !meteorite[j].isVisible())
                    continue;

                if (Checkcircle(i, j)) {
                    int kill = rand.nextBoolean() ? i : j;
                    if (!dead[kill] && meteorite[kill] != null && meteorite[kill].isVisible()) {
                        dead[kill] = true;
                        meteorite[kill].setVisible(false);
                        try {
                            if (mtoT[kill] != null)
                                mtoT[kill].interrupt();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    public void setCount() {
        Count_Meteorite.setText("Meteorite : " + this.count);
    }

    public void reduce_Count() {
        this.count--;
    }

    private boolean Checkcircle(int i, int j) {
        JLabel a = meteorite[i], b = meteorite[j];
        // center ของแต่ละลูก
        double ax = a.getX() + a.getWidth() / 2.0;
        double ay = a.getY() + a.getHeight() / 2.0;
        
        double bx = b.getX() + b.getWidth() / 2.0;
        double by = b.getY() + b.getHeight() / 2.0;

        // หา radius
        double ra = Math.min(a.getWidth(), a.getHeight()) * 0.50;
        double rb = Math.min(b.getWidth(), b.getHeight()) * 0.50;

        // สูตร distance= (x1−x2)^2 + (y1−y2)^2
        double dx = ax - bx;
        double dy = ay - by;

        double dist2 = dx * dx + dy * dy;

        // รัศมี
        double sumR = ra + rb;

        // หาระยะห่าง^2 <= รัศมี^2
        return dist2 <= sumR * sumR; // แตะกันพอดี = ชน
    }

}