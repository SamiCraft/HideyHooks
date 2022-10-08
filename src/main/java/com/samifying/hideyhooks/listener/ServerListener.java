package com.samifying.hideyhooks.listener;

import com.samifying.hideyhooks.HideyHooks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.jetbrains.annotations.NotNull;

public class ServerListener implements Listener {

    private final HideyHooks plugin;

    public ServerListener(HideyHooks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldLoadEvent(@NotNull WorldLoadEvent event) {
        if (plugin.getServer().getWorlds().get(0).equals(event.getWorld())) {
            plugin.sendSystemEmbed("Loading the world");
            plugin.getLogger().info("World loading message dispatched");
        }
    }

    @EventHandler
    public void onServerLoadEvent(@NotNull ServerLoadEvent event) {
        if (event.getType() == ServerLoadEvent.LoadType.STARTUP) {
            plugin.sendSystemEmbed("Server loaded");
            plugin.getLogger().info("Server loaded message dispatched");
        }
    }
}
