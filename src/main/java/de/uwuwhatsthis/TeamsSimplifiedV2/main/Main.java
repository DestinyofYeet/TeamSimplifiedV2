package de.uwuwhatsthis.TeamsSimplifiedV2.main;

import de.uwuwhatsthis.TeamsSimplifiedV2.commands.ChunkCommand;
import de.uwuwhatsthis.TeamsSimplifiedV2.commands.TeamCommand;
import de.uwuwhatsthis.TeamsSimplifiedV2.completers.TeamTabCompletion;
import de.uwuwhatsthis.TeamsSimplifiedV2.events.OnPlayerJoinEvent;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.Defaults;
import de.uwuwhatsthis.TeamsSimplifiedV2.teams.TeamManager;
import de.uwuwhatsthis.TeamsSimplifiedV2.utils.bstats.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;
    private static TeamManager manager;

    private FileConfiguration config;

    @Override
    public void onEnable(){
        getLogger().info("Loading...!");
        plugin = this;

        config = getConfig();

        config.addDefault(Defaults.CONFIG_MAX_TEAM_NAME_LENGTH.getValue(), 20);
        config.addDefault(Defaults.CONFIG_MAX_TEAM_TAG_LENGTH.getValue(), 5);
        config.addDefault(Defaults.CONFIG_SHOW_FANCY_TAB_NAME.getValue(), true);
        config.addDefault(Defaults.CONFIG_EXTENSIONS_ENABLE_DYNMAP.getValue(), true);
        config.addDefault(Defaults.CONFIG_EXTENSIONS_ENABLE_BLUEMAP.getValue(), true);

        config.options().copyDefaults(true);
        saveConfig();

        manager = TeamManager.loadTeams();

        if (manager == null){
            manager = new TeamManager();
        }

        getCommand("chunk").setExecutor(new ChunkCommand());

        getCommand("team").setExecutor(new TeamCommand());
        getCommand("team").setTabCompleter(new TeamTabCompletion());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new OnPlayerJoinEvent(), this);

        if (config.getBoolean(Defaults.CONFIG_BSTATS_ENABLE.getValue())){
            new Metrics(this, 20170);
        }

        Main.getManager().triggerRerender();

        getLogger().info("Done");
    }

    @Override
    public void onDisable(){
        getLogger().info("Stopping...");
        getLogger().info("Saving teams");
        manager.saveTeams();

        getLogger().info("Stopping rendering");
        manager.stopRenderTask();
        getLogger().info("Done");
    }

    public static Main getPlugin(){
        return plugin;
    }

    public static TeamManager getManager(){
        return manager;
    }

    public static void setManager(TeamManager manager){
        Main.manager = manager;
    }
}
