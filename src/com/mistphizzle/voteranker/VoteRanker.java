package com.mistphizzle.voteranker;

import java.util.logging.Logger;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.permission.Permission;

public class VoteRanker extends JavaPlugin {

	public static VoteRanker plugin;
	public static Logger log;
	
	public static Permission permission = null;
	
	public void onEnable() {
		plugin = this;
		VoteRanker.log = this.getLogger();
		
		getServer().getPluginManager().registerEvents(new VoteListener(this), this);
		
		getConfig().options().copyDefaults(true);
		
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			setupPermissions();
		}
		
		new Methods(this);
		DBConnection.init();
	}
	
	private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}
