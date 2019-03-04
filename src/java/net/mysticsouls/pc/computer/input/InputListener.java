package net.mysticsouls.pc.computer.input;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import net.mysticsouls.pc.Main;
import net.mysticsouls.pc.user.User;

public class InputListener extends PacketAdapter{

	private Main plugin;
	
	public InputListener(Main plugin) {
		super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE);
		this.plugin = plugin;
		
	}
	
	@Override
	public void onPacketReceiving(PacketEvent event) {
		PacketContainer packet = event.getPacket();
		if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
			Player player = event.getPlayer();
			User user = plugin.getUserManager().getUser(player.getUniqueId());
			if(user == null || !user.isInComputer())
				return;
			
			Input input = null;
			
            try {
            	{
            		Field field =  packet.getBooleans().getField(1);
            		field.setAccessible(true);
            		boolean shift = field.getBoolean(packet.getHandle());
            		
            		if(shift)
            			input = Input.SNEAK;
            	}
            	{
            		Field field =  packet.getBooleans().getField(0);
            		field.setAccessible(true);
            		boolean space = field.getBoolean(packet.getHandle());
            		if(space)
            			input = Input.JUMP;
            	}
            	{
            		Field field =  packet.getFloat().getField(1);
            		field.setAccessible(true);
            		float forward = field.getFloat(packet.getHandle());
            		
            		if(forward > 0)
            			input = Input.FORWARD;
            		else if(forward > 0)
            			input = Input.BACKWARD;
            	}
            	{
            		Field field =  packet.getFloat().getField(0);
            		field.setAccessible(true);
            		float side = field.getFloat(packet.getHandle());
            		
            		if(side > 0)
            			input = Input.LEFT;
            		else if(side < 0)
            			input = Input.RIGHT;
            	}
				event.setCancelled(true);
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
            user.getComputer().handleInput(input);
            
		}
	}

}
