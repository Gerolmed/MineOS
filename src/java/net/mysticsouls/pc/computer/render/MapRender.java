package net.mysticsouls.pc.computer.render;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class MapRender extends MapRenderer {

	private MapView mapView;
	private MapCanvas mapCanvas;
	private Player player;
		
	@Override
	public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
		
		if(mapCanvas == null)
			return;
			
		
		if(mapView != this.mapView)
			this.mapView = mapView;
		if(mapCanvas != this.mapCanvas)
			this.mapCanvas = mapCanvas;
			
		if(player != this.player)
			this.player = player;
		
		
	}
	
	public void render() {
		render(mapView, mapCanvas, player);
	}

	public MapCanvas getMapCanvas() {
		return mapCanvas;
	}

}
