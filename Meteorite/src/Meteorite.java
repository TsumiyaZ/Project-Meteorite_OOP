import javax.swing.*;

public class Meteorite {
    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog(null,
                "Enter Meteorite : ",
                String.valueOf(Constants.amount_meteorite));
        int asteroidCount = Constants.amount_meteorite;
        try {
            asteroidCount = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            asteroidCount = Constants.amount_meteorite;
        }

        new PanelMeteorite(asteroidCount);
    }
}
