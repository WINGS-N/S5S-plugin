package io.wings.s5s;

import fucksocks.server.SocksProxyServer;
import fucksocks.server.SocksProxyServerFactory;
import fucksocks.server.User;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

public final class Main extends Plugin {

    Logger log = getLogger();
    SocksProxyServer proxyServer = null;

    public void makeConfig() throws IOException {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onEnable() {
        Configuration config = null;
        try {
            makeConfig();
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
            this.onDisable();
        }
        assert config != null;

        log.info("SOCKS5 PORT: " + config.getInt("port", 1080));
        log.info("SOCKS5 AUTH: " + config.getBoolean("auth", true));

        if (config.getBoolean("auth", true)) {
            User user = new User(config.getString("user", "admin"), config.getString("pass", "admin"));
            proxyServer = SocksProxyServerFactory.newUsernamePasswordAutenticationServer(user);
        } else {
            proxyServer = SocksProxyServerFactory.newNoAuthenticaionServer();
        }

        try {
            log.info("SOCKS5 TIMEOUT: " + proxyServer.getTimeout());
            proxyServer.start(config.getInt("port", 1080));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        if (proxyServer != null) {
            proxyServer.shutdown();
        }
    }
}
