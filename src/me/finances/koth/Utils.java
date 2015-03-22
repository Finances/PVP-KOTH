package me.finances.koth;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {
	
	RegionListeners listener = new RegionListeners(Core.plugin);
	
	Core plugin;
	FileConfiguration config = Core.plugin.getConfig();
	List<String> koths = config.getStringList("koth-areas");
	
	public Utils(Core instance){
		this.plugin = instance;
	}
	
	public void displayKOTHs(Player p){
		
		p.sendMessage("");
		p.sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"KOTH Areas on This Map");
		
		for(String s: koths){
			p.sendMessage(ChatColor.RED+s);
		}
		p.sendMessage("");
		
	}
	
	public void givePrize(Player p){
		
		ItemStack[] prize_list = {new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_BLOCK, 64), new ItemStack(Material.GOLD_BLOCK, 32)};
		
		for(ItemStack i : prize_list)
			for(int n=0; n<prize_list.length; n++){
				
				p.getInventory().setItem(n, i);
				
			}
	}

}
