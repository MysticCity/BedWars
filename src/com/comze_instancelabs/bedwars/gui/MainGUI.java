package com.comze_instancelabs.bedwars.gui;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comze_instancelabs.bedwars.Main;
import com.comze_instancelabs.bedwars.villager.Merchant;
import com.comze_instancelabs.bedwars.villager.MerchantOffer;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;

public class MainGUI {

	Main plugin;
	PluginInstance pli;
	public HashMap<String, IconMenu> lasticonm = new HashMap<String, IconMenu>();
	public LinkedHashMap<String, ItemStack> category_items = new LinkedHashMap<String, ItemStack>();

	public MainGUI(PluginInstance pli, Main plugin) {
		this.plugin = plugin;
		this.pli = pli;
		this.loadCategoryItemsLater();
	}

	public void openGUI(final String p) {
		IconMenu iconm;
		if (lasticonm.containsKey(p)) {
			iconm = lasticonm.get(p);
		} else {
			iconm = new IconMenu(pli.getMessagesConfig().shop_item, 27, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					if (event.getPlayer().getName().equalsIgnoreCase(p)) {
						if (pli.global_players.containsKey(p)) {
							if (pli.getArenas().contains(pli.global_players.get(p))) {
								String d = event.getName();
								Player p = event.getPlayer();
								openCategory(p, d);
							}
						}
					}
					event.setWillClose(true);
				}
			}, plugin);

			for (int i = 0; i < 9; i++) {
				iconm.setOption(i, new ItemStack(Material.STAINED_GLASS_PANE), "", "");
			}
			for (int i = 18; i < 27; i++) {
				iconm.setOption(i, new ItemStack(Material.STAINED_GLASS_PANE), "", "");
			}
		}

		int c = 9;
		for (String ac : category_items.keySet()) {
			// TODO Category.java with lore and displayname
			iconm.setOption(c, category_items.get(ac), ac, ac);
			c++;
		}

		iconm.open(Bukkit.getPlayerExact(p));
		lasticonm.put(p, iconm);
	}

	public void loadCategoryItemsLater() {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				FileConfiguration config = plugin.gui.getConfig();
				if (config.isSet("maingui.category_items")) {
					for (String aclass : config.getConfigurationSection("maingui.category_items.").getKeys(false)) {
						// TODO Category.java with lore and displayname
						category_items.put(aclass, Util.parseItems(config.getString("maingui.category_items." + aclass + ".items")).get(0));
					}
				}
			}
		}, 20L);
	}

	public void openCategory(final Player p, final String category) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				if (category.equalsIgnoreCase("blocks")) {
					if (plugin.BlocksMerchant.hasCustomer()) {
						plugin.BlocksMerchant.clone().openTrading(p);
					} else {
						plugin.BlocksMerchant.openTrading(p);
					}
				} else if (category.equalsIgnoreCase("armor")) {
					plugin.ArmorMerchant.clone().openTrading(p);
				} else if (category.equalsIgnoreCase("pickaxes")) {
					plugin.PickaxesMerchant.clone().openTrading(p);
				} else if (category.equalsIgnoreCase("swords")) {
					plugin.SwordsMerchant.clone().openTrading(p);
				} else if (category.equalsIgnoreCase("bows")) {
					plugin.BowsMerchant.clone().openTrading(p);
				} else if (category.equalsIgnoreCase("consumables")) {
					plugin.ConsumablesMerchant.clone().openTrading(p);
				} else if (category.equalsIgnoreCase("chests")) {
					plugin.ChestsMerchant.clone().openTrading(p);
				} else if (category.equalsIgnoreCase("potions")) {
					plugin.PotionsMerchant.clone().openTrading(p);
				} else if (category.equalsIgnoreCase("specials")) {
					plugin.SpecialsMerchant.clone().openTrading(p);
				}
			}
		}, 2L);
	}
}
