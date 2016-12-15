package mousemove;

import java.awt.Robot;

public class RobotTest {
	public static void main(String[] args) throws Exception{
		a();
	}
	
	public static void a() throws Exception{
		Robot robot = new Robot();
		int x = 90;
		int y = 90;
		long time = 1000 * 60;
		while(true){
			if(x<100){
				x+=100;
			}else{
				x-=100;
			}
			if(y<100){
				y+=100;
			}else{
				y-=100;
			}
			
			robot.mouseMove(x, y);
			Thread.sleep(time);
		}
		
	}
	
}
