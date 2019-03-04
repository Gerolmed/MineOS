package net.mysticsouls.pc.utils;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CooldownManager {
	private HashMap<UUID, HashMap<String, Integer>> cds = new HashMap<>();

	public void add(UUID id, String s, int time) {
		if(cds.get(id) == null) {
			cds.put(id, new HashMap<>());
		}
		cds.get(id).put(s, time);
		
	}
	
	public boolean isOnCooldown(UUID id, String s) {
		if(cds.get(id) == null)
			return false;
		return (cds.get(id).containsKey(s));
	}
	
	public int getCooldown(UUID id, String s) {
		if(cds.get(id) == null || cds.get(id).get(s) == null)
			return 0;
		return cds.get(id).get(s);
	}
	
	public void Update(JavaPlugin m) {
		Bukkit.getScheduler().runTaskTimer(m, new Runnable() {
			
			@Override
			public void run() {
				HashMap<UUID, String> toRem = new HashMap<>();
				
				for(Entry<UUID, HashMap<String, Integer>> set1 : cds.entrySet()) {
					for(Entry<String, Integer> set : set1.getValue().entrySet()) {
						set.setValue(set.getValue()-1);
						if(set.getValue() == 0) {
							toRem.put(set1.getKey(), set.getKey());
						}
					}
				}
				
				
				for(Entry<UUID, String> set : toRem.entrySet()) {
					cds.get(set.getKey()).remove(set.getValue());
				}
				
			}
		}, 1, 1);
	}
	
}
