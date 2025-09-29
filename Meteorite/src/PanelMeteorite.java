import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;

class PanelMeteorite extends JFrame {

    int amountMeteorite = Constants.amount_meteorite;
    JLabel[] meteorite ;
    meteoriteThread[] mtoT ;
    ImageIcon[] mtoIcon;
    JLabel Count_Meteorite = new JLabel();
    private ImageIcon bomb;
    static boolean[] dead;
    static boolean[] exploding ;

    Random rand = new Random();
    // พื้นหลัง
    private JPanel BackG;

    // โหลดครั้งเดียว
    ImageIcon[] preloadedIcons;

    PanelMeteorite(int PanelMeteorite) {
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        String bombPath = System.getProperty("user.dir") + File.separator
                + "Image" + File.separator + "bomb.gif";
        bomb = new ImageIcon(bombPath);

        String[] files = Constants.imageFiles;
        preloadedIcons = new ImageIcon[files.length];

        for (int i = 0; i < files.length; i++) {
            ImageIcon raw =  new ImageIcon(System.getProperty("user.dir") + File.separator
                    + "Image" + File.separator + files[i]);
            Image scaled = raw.getImage().getScaledInstance(Constants.Scale_Meteorite,
                    Constants.Scale_Meteorite,
                    Image.SCALE_SMOOTH);
            preloadedIcons[i] = new ImageIcon(scaled);
        }

        amountMeteorite = PanelMeteorite;
        System.out.println("debug "+amountMeteorite);
        meteorite = new JLabel[amountMeteorite];
        mtoT = new meteoriteThread[amountMeteorite];
        mtoIcon = new ImageIcon[amountMeteorite];
        dead = new boolean[amountMeteorite];
        exploding = new boolean[amountMeteorite];

        BackG = new JPanel(null);
        BackG.setBackground(Color.BLACK);
        BackG.setBounds(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        add(BackG);

        int marginCount = 16;
        Count_Meteorite.setBounds(Constants.WINDOW_WIDTH - 160 - marginCount, marginCount, 160, 30);
        BackG.add(Count_Meteorite);

        for (int i = 0; i < meteorite.length; i++) {

            meteorite[i] = new JLabel(preloadedIcons[rand.nextInt(preloadedIcons.length)]);

            meteorite[i].setSize(64, 64);

            int W = Constants.WINDOW_WIDTH;
            int H = Constants.WINDOW_HEIGHT;
            // สุ่มตำแหน่ง (กันขอบ 30px)(
            int margin = 30;

            meteorite[i].setLocation(
                    rand.nextInt(Math.max(1, W - meteorite[i].getWidth() - 2 * margin)) + margin,
                    rand.nextInt(Math.max(1, H - meteorite[i].getHeight() - 2 * margin)) + margin);
            BackG.add(meteorite[i]);

            // ความเร็วสุ่ม
            double dx = (rand.nextBoolean() ? 1 : -1) * (rand.nextDouble(1) + 0.2); // 0.2 - 1.2
            double dy = (rand.nextBoolean() ? 1 : -1) * (rand.nextDouble(1) + 0.2);

            mtoT[i] = new meteoriteThread(meteorite[i], BackG, dx, dy, this,i);
        }

        setVisible(true);

        // รอสร้างทุก Thread เสร็จก่อนค่อยไป start Thread ทุกตัว
        for (int i = 0; i < meteorite.length; i++) {
            mtoT[i].start();
        }
    }

    public static boolean getDead(int id ){
        if (exploding[id] && dead[id]){
            return true;
        }
        return false;
    }


    void check_Collisions() {
        for (int i = 0; i < meteorite.length; i++) {
            if (meteorite[i] == null || dead[i] || exploding[i] || !meteorite[i].isVisible()) continue;

            for (int j = 0; j < meteorite.length; j++) {
                if (i == j) continue;

                if (meteorite[j] == null || dead[j] || exploding[j] || !meteorite[j].isVisible()) continue;

                if (Checkcircle(i, j)) {
                    if (i < j) {
                        if (!dead[i] && !exploding[i]) {
                            dead[i] = true;
                            exploding[i] = true;
                            explode(i);
                            mtoT[i].interrupt();
                        }
                    } else {
                        if (!dead[j] && !exploding[j]) {
                            dead[j] = true;
                            exploding[j] = true;
                            explode(j);
                            mtoT[j].interrupt();
                        }
                    }
                }
            }
        }
    }



    public void explode(int id) {
        if (!dead[id]) return;

        meteorite[id].setIcon(bomb);

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(200);
                meteorite[id].setVisible(false);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                exploding[id] = false;
            }
        }, "Explosion-" + id);

        t.start();
    }

    private boolean Checkcircle(int i, int j) {
        JLabel a = meteorite[i], b = meteorite[j];
        // center ของแต่ละลูก
        double ax = a.getX() + a.getWidth() / 2.0;
        double ay = a.getY() + a.getHeight() / 2.0;
        
        double bx = b.getX() + b.getWidth() / 2.0;
        double by = b.getY() + b.getHeight() / 2.0;

        // หา radius
        double ra = Math.min(a.getWidth(), a.getHeight()) * 0.40;
        double rb = Math.min(b.getWidth(), b.getHeight()) * 0.40;

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