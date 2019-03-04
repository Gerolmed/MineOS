package net.mysticsouls.pc.computer.input;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import net.mysticsouls.pc.Main;

public class SeatManager {
	
	private ArrayList<Integer> used = new ArrayList<>();
	private HashMap<UUID, Integer> ids = new HashMap<>();
	
	private Main plugin;
	
	public SeatManager(Main plugin) {
		this.plugin = plugin;
	}
	
	public int getFreeId() {
		int id = Integer.MAX_VALUE;
		while(used.contains(id)) {
			id--;
		}
		used.add(id);
		return id;
	}
	
	public void removeId(int id) {
		used.remove(id);
	}
	
	public void makeSitDown(Player p) {
		if(ids.containsKey(p.getUniqueId()))
			makeStandUp(p.getUniqueId());
		int id = spawnFakeSeat(p);
		makeSeated(p, id);
		ids.put(p.getUniqueId(), id);
		
	}

	public void makeStandUp(UUID id) {
		if(ids.containsKey(id)) {
			removeSeat(ids.get(id), Bukkit.getPlayer(id));
		}
	}
	
	private void removeSeat(Integer entityId, Player player) {
		if(player == null)
			return;
		
		PacketContainer destroy = plugin.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
		
		destroy.getIntegerArrays().write(0, new int[] {entityId});
		
		try {
			plugin.getProtocolManager().sendServerPacket(player, destroy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("deprecation")
	private int spawnFakeSeat(Player player) {
		int entityId = 500;//getFreeId();
		ArmorStand entity = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

		entity.setCollidable(false);
		entity.setVisible(false);
		entity.setBasePlate(false);
		entity.setSmall(true);
		
        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(entity).deepClone();

        entity.remove();
        
		PacketContainer spawn = plugin.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
		
		spawn.getIntegers().write(0, entityId);
		spawn.getIntegers().write(1, (int) EntityType.ARMOR_STAND.getTypeId());
        // Set yaw pitch
		spawn.getBytes().write(0, (byte) 0);
		spawn.getBytes().write(1, (byte) 0);
        // Set object data
		spawn.getBytes().write(2, (byte) 0);
        // Set location
        spawn.getDoubles().write(0, player.getLocation().getX());
        spawn.getDoubles().write(1, player.getLocation().getY());
        spawn.getDoubles().write(2, player.getLocation().getZ());
        
        spawn.getUUIDs().write(0, UUID.randomUUID());
        
		spawn.getDataWatcherModifier()
			.write(0, watcher);
        
		try {
			plugin.getProtocolManager().sendServerPacket(player, spawn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entityId;
	}

	private void makeSeated(Player player, int id) {
		
		PacketContainer mount = plugin.getProtocolManager().createPacket(PacketType.Play.Server.MOUNT);
		mount.getIntegers()
			.write(0, id);
		mount.getIntegerArrays().write(0, new int[] {player.getEntityId()});
		
		try {
			plugin.getProtocolManager().sendServerPacket(player, mount);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
