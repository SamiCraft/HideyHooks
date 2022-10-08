package com.samifying.hideyhooks;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.samifying.hideyhooks.listener.PlayerListener;
import com.samifying.hideyhooks.listener.ServerListener;
import com.samifying.hideylink.HideyLink;
import com.samifying.hideylink.model.DataModel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;

public final class HideyHooks extends JavaPlugin {

    private HideyLink link;
    private WebhookClient client;

    @Override
    public void onEnable() {
        // Saving default config
        saveDefaultConfig();

        // Plugin startup logic
        PluginManager manager = getServer().getPluginManager();
        getLogger().info("Retrieving HideyLink instance");
        link = (HideyLink) manager.getPlugin("HideyLink");
        if (link == null) {
            getLogger().severe("HideyLink not found");
            manager.disablePlugin(this);
            return;
        }

        // Webhook setup
        getLogger().info("Setting up webhooks");
        String url = getConfig().getString("webhook");
        if (url == null || url.isEmpty() || url.equals("url")) {
            getLogger().severe("Please set webhook url in properties");
            manager.disablePlugin(this);
            return;
        }

        // Assigning webhook dispatcher threads
        WebhookClientBuilder builder = new WebhookClientBuilder(url);
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("MessageDispatcher");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        client = builder.build();
        getLogger().info("Webhook client is ready");

        // Discord notification
        new Thread(() -> {
            sendSystemEmbed("Server starting");
            getLogger().info("Plugin loaded, webhook message dispatched");
        }, "PluginEnabledDispatcher").start();

        // Registering listeners
        getLogger().info("Registering listeners");
        manager.registerEvents(new PlayerListener(this), this);
        manager.registerEvents(new ServerListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        sendSystemEmbed("Server stopped");
        getLogger().info("Server stopping message dispatched");
    }

    public void sendWebhookEmbed(Player player, int color, String title, WebhookEmbedBuilder builder) {
        DataModel data = link.getPlayers().get(player.getUniqueId());
        if (data == null || client == null) return;
        client.send(new WebhookMessageBuilder()
                .setUsername(player.getName())
                .setAvatarUrl("https://visage.surgeplay.com/face/" + player.getUniqueId())
                .addEmbeds(builder
                        .setColor(color)
                        .setAuthor(new WebhookEmbed.EmbedAuthor(data.getNickname(), data.getAvatar(), null))
                        .setDescription("**" + PluginUtils.sanitize(title) + "**")
                        .setFooter(new WebhookEmbed.EmbedFooter(data.getId(), null))
                        .setTimestamp(Instant.now())
                        .build())
                .build());
    }

    public void sendSystemEmbed(String message) {
        if (client == null) return;
        client.send(new WebhookEmbedBuilder()
                .setColor(getConfig().getInt("color.system"))
                .setDescription("**" + message + "**")
                .setTimestamp(Instant.now())
                .build());
    }

    public WebhookClient getClient() {
        return client;
    }
}
