package me.finances.koth;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NameStringHandler {
	
	private static Core plugin = Core.plugin;
	
	public static HashMap<String, String> NameMap = new HashMap<String, String>();
	
	public static void save(){
		
		for(String name : NameMap.keySet()){
			
			plugin.getConfig().set("koths."+name, NameMap.get(name));
			
		}
		
		plugin.saveConfig();
		
	}
	
	public static void load(){
		
		if(!plugin.getConfig().contains("koths")) return;
		
		for(String s : plugin.getConfig().getConfigurationSection("koths").getKeys(false)){
			
			NameMap.put(s, plugin.getConfig().getString("koths."+s));
			
		}
		
	}
	
	public static void addKoth(Player p, String name, String region){
		
		if(NameMap.containsKey(name) || NameMap.containsValue(region)){
			p.sendMessage(ChatColor.RED+"Error - "+name+" already exists under region "+NameMap.get(name));
		}else{
			NameMap.put(name, region);
			p.sendMessage(ChatColor.AQUA+"[KOTH] "+ChatColor.GOLD+name+" has been created with region "+region);
		}
		
		RegionListeners.addRegionsToMap();
		
	}

}
