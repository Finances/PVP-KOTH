package me.finances.koth;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin{
	
	public static Core plugin;
	
	public void onEnable(){
		
		plugin = this;
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new RegionListeners(this), this);
		pm.registerEvents(new Loot(), this);
		
		getCommand("koth").setExecutor(new Executor());
		
		saveDefaultConfig();
		NameStringHandler.load();
		RegionListeners.addRegionsToMap();

	}
	
	public void onDisable(){
		
		NameStringHandler.save();
		
	}
	

}
