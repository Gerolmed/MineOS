package net.mysticsouls.pc.utils;

public class DisplayHelper {
	
	/**Gets the Slot Id on a display
	 * @param x - starting with 0
	 * @param y - starting with 0
	 * @return
	 */
	public static int getSlotFromCoord(int x, int y) {
		return x+y*9;
	}

}
