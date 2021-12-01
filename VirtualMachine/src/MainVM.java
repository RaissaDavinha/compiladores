import javax.swing.SwingUtilities;

public class MainVM {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Window window = new Window();
				window.setVisible(true);
			}
		});
	}
}
