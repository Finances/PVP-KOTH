package me.finances.koth;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Executor implements CommandExecutor{
	
	RegionListeners listeners = new RegionListeners(Core.plugin);
	Utils utils = new Utils(Core.plugin);
	Loot loot = new Loot();
	
	FileConfiguration config = Core.plugin.getConfig();
	
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		
		Player player = (Player)sender;
		
		if(commandLabel.equalsIgnoreCase("koth")){
			if(args.length==0){
				if(player.hasPermission("koth.admin")){
					player.sendMessage(ChatColor.AQUA+""+ChatColor.BOLD+"[KOTH] "+ChatColor.GOLD+"");
					player.sendMessage("");
					player.sendMessage(ChatColor.YELLOW+"Admin commands:");
					player.sendMessage(ChatColor.GOLD+"/koth - Displays this table of commands");
					player.sendMessage(ChatColor.GOLD+"/koth add <name> <region> - Adds a new KOTH to the file (uses WorldGuard)");
					player.sendMessage(ChatColor.GOLD+"/koth start <name> - Activates the specified KOTH");
					player.sendMessage(ChatColor.GOLD+"/koth reset - Clears all timers and deactivates any active KOTHs");
					player.sendMessage(ChatColor.GOLD+"/koth reload - Reloads the plugin");
					player.sendMessage("");
					player.sendMessage(ChatColor.YELLOW+"Player commands:");
					player.sendMessage(ChatColor.GOLD+"/koth info - Shows the coordinates of each KOTH");
					player.sendMessage(ChatColor.GOLD+"/koth loot - Displays the collection of loot that the randomized prize is based off of");
				}else{
					player.sendMessage(ChatColor.AQUA+""+ChatColor.BOLD+"[KOTH] "+ChatColor.GOLD+"");
					player.sendMessage("");
					player.sendMessage(ChatColor.YELLOW+"Player commands:");
					player.sendMessage(ChatColor.GOLD+"/koth info - Shows the coordinates of each KOTH");
					player.sendMessage(ChatColor.GOLD+"/koth loot - Displays the collection of loot that the randomized prize is based off of");
				}
			}else{
			//start next statement here
				if(args[0].equalsIgnoreCase("openloot")){
					if(player.hasPermission("koth.openloot")){
						player.openInventory(listeners.loot());
					}else{
						player.sendMessage(ChatColor.RED+"No permission");
					}
				}
				
				if(args[0].equalsIgnoreCase("prize")){
					if(RegionListeners.lootTimer.containsKey(player)){
						player.openInventory(listeners.loot());
						RegionListeners.lootTimer.remove(player);
						RegionListeners.lootTask.remove(player);
					}else{
						player.sendMessage(ChatColor.RED+"You have not capped a KOTH");
					}
				}
				
				if(args[0].equalsIgnoreCase("reset")){
					if(player.hasPermission("koth.reset")){
						listeners.reset();
						player.sendMessage(ChatColor.AQUA+"[KOTH] "+ChatColor.GOLD+"Active koths are now innactive");
						player.sendMessage(ChatColor.AQUA+"[KOTH] "+ChatColor.GOLD+"Use /koth start <koth_name> to start a new one");
					}else{
						player.sendMessage(ChatColor.RED+"No permission");
					}
				}
				
				if(args[0].equalsIgnoreCase("debug")){
					player.sendMessage(RegionListeners.capSet.toString());
				}
			
			if(args[0].equalsIgnoreCase("info")){
				listeners.info(player);
			}
			
			if(args[0].equalsIgnoreCase("loot")){
				loot.showMenu(player);
			}
			
			if(args[0].equalsIgnoreCase("reload")){
				if(player.hasPermission("koth.reload")){
					NameStringHandler.save();
					NameStringHandler.load();
					RegionListeners.addRegionsToMap();
					player.sendMessage("reloaded!");
				}
			}
			
			
			if(args[0].equalsIgnoreCase("start")){
				if(player.hasPermission("koth.start")){
					if(args.length==1){
						player.sendMessage(ChatColor.RED+"Usage: /koth start <koth_name>");
					}else if(args.length==2){
						listeners.activate(player, args[1]);
					}
				}else{
					player.sendMessage(ChatColor.RED+"No permission");
				}
			}
			
			if(args[0].equalsIgnoreCase("add")){
				if(player.hasPermission("koth.add")){
					if(args.length==1){
						player.sendMessage(ChatColor.RED+"Usage: /koth add <koth_name> <wg_region>");
					}else if(args.length==2){
						player.sendMessage(ChatColor.RED+"Usage: /koth add <koth_name> <wg_region>");
					}else{
						NameStringHandler.addKoth(player, args[1], args[2]);
					}
				}else{
					player.sendMessage(ChatColor.RED+"No permission");
				}
			}
		}
	}
		return false;
}

}
