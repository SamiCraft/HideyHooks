package com.samifying.hideyhooks.listener;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import com.samifying.hideyhooks.HideyHooks;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final HideyHooks plugin;
    private final FileConfiguration config;

    public PlayerListener(HideyHooks plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.sendWebhookEmbed(player, config.getInt("color.join"),
                event.getJoinMessage(),
                new WebhookEmbedBuilder().addField(currentlyOnline(event))
        );
        plugin.getLogger().info("Player join message dispatched");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.sendWebhookEmbed(player, config.getInt("color.leave"),
                event.getQuitMessage(),
                new WebhookEmbedBuilder().addField(currentlyOnline(event))
        );
        plugin.getLogger().info("Player leave message dispatched");
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        plugin.sendWebhookEmbed(player, config.getInt("color.death"),
                event.getDeathMessage(), new WebhookEmbedBuilder()
        );
        plugin.getLogger().info("Player death message dispatched");
    }

    @EventHandler
    public void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent event) {
        String[] advancement = event.getAdvancement().getKey().getKey().split("/");
        // Recipes should not be displayed
        if (advancement[0].equalsIgnoreCase("recipes")) {
            return;
        }
        String category = StringUtils.capitalize(advancement[0]);
        String name = StringUtils.capitalize(advancement[1].replace("_", " "));
        String format = String.format("%s: %s", category, name);

        Player player = event.getPlayer();
        plugin.sendWebhookEmbed(player, config.getInt("color.advancement"),
                player.getName() + " made an advancement",
                new WebhookEmbedBuilder()
                        .addField(new WebhookEmbed.EmbedField(false, "Advancement:", format))
        );
        plugin.getLogger().info("Player advancement message dispatched");
    }

    private WebhookEmbed.EmbedField currentlyOnline(PlayerEvent event) {
        Server server = plugin.getServer();
        int i = (event instanceof PlayerQuitEvent) ? 1 : 0;
        String online = String.format("%s/%s", server.getOnlinePlayers().size() - i, server.getMaxPlayers());
        return new WebhookEmbed.EmbedField(false, "Online:", online);
    }
}
