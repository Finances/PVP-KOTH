package me.finances.koth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionListeners implements Listener{
	
	Player capper;
	Core plugin;
	FileConfiguration config = Core.plugin.getConfig();
	
	public RegionListeners(Core instance){
		this.plugin = instance;
	}
	
	private HashMap<String, String> NameMap = NameStringHandler.NameMap;
	
	public static ArrayList<Player> capSet = new ArrayList<Player>();
	
	public static HashMap<Player, Integer> lootTimer = new HashMap<Player, Integer>();
	public static HashMap<Player, BukkitRunnable> lootTask = new HashMap<Player, BukkitRunnable>();
	
	//Key of ActivatedMap is the value of NameMap
	public static HashMap<String, Boolean> ActivatedMap = new HashMap<String, Boolean>();
	
	public static void addRegionsToMap(){
		for(String s : NameStringHandler.NameMap.values()){
			ActivatedMap.put(s, false);
		}
	}
	
	public void activate(Player p, String s){
		for(boolean b : ActivatedMap.values()){
			if(b==true){
				p.sendMessage(ChatColor.RED+"Error - A KOTH is already in progress");
				return;
			}
		}
		
		if(!ActivatedMap.containsKey(NameStringHandler.NameMap.get(s))){
			p.sendMessage(ChatColor.RED+"Error - "+s+" does not exist");
		}else{
			ActivatedMap.put(NameStringHandler.NameMap.get(s), true);
			p.sendMessage(ChatColor.AQUA+"[KOTH] "+ChatColor.GOLD+s+" has been activated");
		}
	}
	
	@EventHandler
	public Player getCapper(RegionEnterEvent e){
		
		for(String s : ActivatedMap.keySet()){
			
			if(ActivatedMap.get(s)==true){
				if(e.getRegion().getId().equalsIgnoreCase(s)){
					capSet.add(e.getPlayer());
				}
			}
			
		}
		
		if(capSet.size()>0){
			capper = capSet.get(0);
		}
		
		return capper;
		
	}
	
	@EventHandler
	public void onEnter(RegionEnterEvent e){
		
		int startCount = 900;
		
		for(String s : NameMap.keySet()){
			
			if(e.getPlayer()==capper){
				if(ActivatedMap.get(NameMap.get(s))==true){
					if(e.getRegion().getId().equalsIgnoreCase(NameMap.get(s))){
						Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+""+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+s);
						Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.plugin, new KOTHTimer(startCount), 0, 20);
					}
				}
			}
			
		}
		
	}
	
	@EventHandler
	public void onLeave(RegionLeaveEvent e){
		
		if(e.getPlayer()==capper){
			
			for(String s : NameMap.keySet()){
				
				if(e.getRegion().getId().equalsIgnoreCase(NameMap.get(s))){
					Bukkit.getScheduler().cancelTasks(Core.plugin);
					capSet.clear();
				}
				
				if(e.getRegion().getId().equalsIgnoreCase(NameMap.get(s))){
					if(ActivatedMap.get(NameMap.get(s))==true){
						Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Control of "+s+" was lost");
					}
				}
				
			}
			
		}
		
	}
		
	
	public Inventory loot(){
		
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.YELLOW+"Loot Collection");
		Random rand = new Random();
		
		inv.clear();
		int armor_slots = 2;
		int block_slots = 2;
		int potmatslots = 2;
		int otherslots = 2;
		for(int i = 0; i<=armor_slots; i++) {
			int n = rand.nextInt(Loot.armor().length);
			inv.addItem(new ItemStack(Loot.armor()[n]));
			}
		for(int i = 0; i<=block_slots; i++) {
			int n = rand.nextInt(Loot.blocks().length);
			inv.addItem(new ItemStack(Loot.blocks()[n]));
			}
		for(int i = 0; i<=potmatslots; i++) {
			int n = rand.nextInt(Loot.potmaterials().length);
			inv.addItem(new ItemStack(Loot.potmaterials()[n]));
			}
		for(int i = 0; i<=otherslots; i++) {
			int n = rand.nextInt(Loot.other().length);
			inv.addItem(new ItemStack(Loot.other()[n]));
			}
		
		return inv;
		
	}
	
	public void reset(){
		
		for(String s : ActivatedMap.keySet()){
			ActivatedMap.put(s, false);
		}
		
		capper = null;
		capSet.clear();
	}
	
	public void info(Player p){
		
		p.sendMessage(ChatColor.YELLOW+"KOTHs on this map:");
		
		for(String s : NameMap.keySet()){
			p.sendMessage(ChatColor.RED+s);
		}
		
	}
	
public class KOTHTimer extends BukkitRunnable{
		
		public int startCountdown = 0;
		
		String kothName = "nullKOTH";
		
		public KOTHTimer(int start){
			this.startCountdown = start;
		}
		
		@Override
		public void run(){
			startCountdown--;
			
			for(String s : NameMap.keySet()){
				if(ActivatedMap.get(NameMap.get(s))==true){
					kothName = s;
				}
			}
			
			if(startCountdown==870){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (14:30)");
			}
			
			if(startCountdown==840){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (14:00)");
			}
			
			if(startCountdown==810){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (13:30)");
			}
			
			if(startCountdown==780){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (13:00)");
			}
			
			if(startCountdown==750){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (12:30)");
			}
			
			if(startCountdown==720){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (12:00)");
			}
			
			if(startCountdown==690){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (11:30)");
			}
			
			if(startCountdown==660){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (11:00)");
			}
			
			if(startCountdown==630){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (10:30)");
			}
			
			if(startCountdown==600){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (10:00)");
			}
			
			if(startCountdown==570){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (9:30)");
			}
			
			if(startCountdown==540){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (9:00)");
			}
			
			if(startCountdown==510){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (8:30)");
			}
			
			if(startCountdown==480){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (8:00)");
			}
			
			if(startCountdown==450){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (7:30)");
			}
			
			if(startCountdown==420){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (7:00)");
			}
			
			if(startCountdown==390){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (6:30)");
			}
			
			if(startCountdown==360){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (6:00)");
			}
			
			if(startCountdown==330){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (5:30)");
			}
			
			if(startCountdown==300){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (5:00)");
			}
			
			if(startCountdown==270){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (4:30)");
			}
			
			if(startCountdown==240){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (4:00)");
			}
			
			if(startCountdown==210){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (3:30)");
			}
			
			if(startCountdown==180){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (3:00)");
			}
			
			if(startCountdown==150){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (2:30)");
			}
			
			if(startCountdown==120){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (2:00)");
			}
			
			if(startCountdown==90){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (1:30)");
			}
			
			if(startCountdown==60){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (1:00)");
			}
			
			if(startCountdown==30){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+"Someone is contesting "+kothName+ChatColor.RED+" (0:30)");
			}
			
			
			if(startCountdown==0){
				Bukkit.broadcastMessage(ChatColor.RED+"["+ChatColor.YELLOW+"KOTH"+ChatColor.RED+"] "+ChatColor.GOLD+kothName+" has been capped by "+ChatColor.YELLOW+capper.getName());
				Bukkit.getScheduler().cancelTask(startCountdown);
				
				capper.playSound(capper.getLocation(), Sound.GLASS, 20, 20);
				
				capper.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"You capped "+kothName+"! Use /koth prize to obtain your prize");
				capper.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"This command will time out in 5 minutes");
				
				lootTimer.put(capper, 300);
				lootTask.put(capper, new BukkitRunnable(){
					public void run(){
						lootTimer.put(capper, lootTimer.get(capper)-1);
						if(lootTimer.get(capper)==0){
							lootTimer.remove(capper);
							lootTask.remove(capper);
						}
					}
				});
				
				lootTask.get(capper).runTaskTimer(Core.plugin, 20, 20);
				
				reset();
			}
		}

	}
	
	
}
