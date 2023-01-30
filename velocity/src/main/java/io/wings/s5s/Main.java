package io.wings.s5s;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "S5S_velocity",
        name = "S5S-velocity",
        version = "1.0",
        description = "SOCKS5 Server plugin",
        authors = {"WINGS-IO"}
)
public class Main {

    @Inject
    private Logger log;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        log.info("s");
    }
}
