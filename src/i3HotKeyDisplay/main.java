package i3HotKeyDisplay;

public class main {
	
	public static void main(String[] args) {
		if(args.length<1){
			System.out.println("No config or parsing file given");
			System.exit(0);
		}
		HotKeyDisplay display = new HotKeyDisplay(args[0],args[1]);
		display.draw();
	}
	
	
	
	
	
	
	
	
	
}
