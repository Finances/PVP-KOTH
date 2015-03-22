package me.finances.koth;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Loot implements Listener{
	
	private static int encLevel = Core.plugin.getConfig().getInt("enc-level");
	
	public static Inventory menu = Bukkit.createInventory(null, 54, ChatColor.RED+"KOTH loot");
	
	public static ItemStack[] armor(){
		
		ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, encLevel);
		helm.addEnchantment(Enchantment.DURABILITY, 3);
		
		ItemStack chestp = new ItemStack(Material.DIAMOND_CHESTPLATE);
		chestp.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, encLevel);
		chestp.addEnchantment(Enchantment.DURABILITY, 3);
		
		ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS);
		legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, encLevel);
		legs.addEnchantment(Enchantment.DURABILITY, 3);
		
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, encLevel);
		boots.addEnchantment(Enchantment.DURABILITY, 3);
		boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
		
		ItemStack[] lootlist = {helm,chestp,legs,boots,};
		
		return lootlist;
		
		}
	
	public static ItemStack[] blocks(){
		
		ItemStack d1 = new ItemStack(Material.DIAMOND_BLOCK, 4);
		ItemStack d2 = new ItemStack(Material.DIAMOND_BLOCK, 8);
		
		ItemStack e1 = new ItemStack(Material.EMERALD_BLOCK, 4);
		ItemStack e2 = new ItemStack(Material.EMERALD_BLOCK, 8);
		
		ItemStack g1 = new ItemStack(Material.GOLD_BLOCK, 8);
		ItemStack g2 = new ItemStack(Material.GOLD_BLOCK, 16);
		
		ItemStack[] lootlist = {d1,d2,e1,e2,g1,g2};
		
		return lootlist;
		
	}
	
	public static ItemStack[] potmaterials(){
		
		ItemStack gp1 = new ItemStack(Material.SULPHUR, 32);
		ItemStack gp2 = new ItemStack(Material.SULPHUR, 64);
		
		ItemStack slimeball1 = new ItemStack(Material.SLIME_BALL, 16);
		ItemStack slimeball2 = new ItemStack(Material.SLIME_BALL, 32);
		
		ItemStack[] lootlist = {gp1,gp2,slimeball1,slimeball2};
		
		return lootlist;
		
	}
	
	public static ItemStack[] other(){
		
		ItemStack loot4 = new ItemStack(Material.DIAMOND_SWORD);
		loot4.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 4);
		
		
		ItemStack pearls1 = new ItemStack(Material.ENDER_PEARL, 8);
		ItemStack pearls2 = new ItemStack(Material.ENDER_PEARL, 16);
		
		ItemStack fortune4 = new ItemStack(Material.DIAMOND_PICKAXE);
		fortune4.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 4);
		
		ItemStack beacon = new ItemStack(Material.BEACON);
		
		ItemStack[] lootlist = {loot4,pearls1,pearls2,beacon,fortune4};
		
		return lootlist;
		
	}
	
	
	
	public void showMenu(Player p){
		Player player = p;
		player.openInventory(menu);
		
		menu.setItem(0, armor()[0]);
		menu.setItem(9, armor()[1]);
		menu.setItem(18, armor()[2]);
		menu.setItem(27, armor()[3]);
		
		
		
		
	}
	
	@EventHandler
	public void click(InventoryClickEvent e){
		if(e.getInventory().getTitle().equals(menu.getTitle())){
			e.setCancelled(true);
		}
	}

}
