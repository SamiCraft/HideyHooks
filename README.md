# HideyHooks
Discord webhook integration for the HideySMP

### Requirements

- Paper 1.19.2
- [HideyLink](https://github.com/SamiCraft/HideyLink)

### Configuration
```yaml
webhook: url
color:
  system: 65535
  join: 65280
  leave: 16711680
  death: 8388736
  advancement: 5768601
```
- `webhook` - Discord webhook url
- `color.system` - Color used for system embeds (ex: server starting)
- `color.join` - Color used on player join embeds
- `color.leave` - Color used on player leave embeds
- `color.death` - Color used on player death embeds
- `color.advancement` - Color used on player advancement embeds

> Keep in mind that color values can be both in decimal and hexadecimal formats, but it's recommended to set theme in decimal values

### For developers

Make sure to add the plugin as your dependency
```xml
<repositories>
    <repository>
        <id>pequla-repo</id>
        <url>https://maven.pequla.com/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.samifying</groupId>
        <artifactId>hidey-hooks</artifactId>
        <version>1.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

Example usage:
```java
PluginManager manager = getServer().getPluginManager();
HideyHooks hooks = (HideyHooks) manager.getPlugin("HideyHooks");
if (hooks == null) {
  getLogger().severe("HideyHooks not found");
  manager.disablePlugin(this);
  return;
}

// Instance of WebhookClient
hooks.getClient().send("Hello from another plugin");
```

> All webhooks are sent async. Minn's [discord-webhooks](https://github.com/MinnDevelopment/discord-webhooks) are used as discord webhook api wrapper
