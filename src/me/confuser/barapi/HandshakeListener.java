package me.confuser.barapi;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.packetwrapper.WrapperHandshakeClientSetProtocol;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class HandshakeListener extends PacketAdapter {
    
    private static HashSet<String> newProtocolIPs;
    
    public HandshakeListener(Plugin plugin) {
        super(plugin, ListenerPriority.MONITOR, PacketType.Handshake.Client.SET_PROTOCOL);
        newProtocolIPs = new HashSet<String>();
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        
        WrapperHandshakeClientSetProtocol handshakePacket = new WrapperHandshakeClientSetProtocol(event.getPacket());
        int protocolVersion = handshakePacket.getProtocolVersion();
        
        if (protocolVersion > 5) { // 1.8
            Bukkit.getLogger().info("Set " + getIP(event.getPlayer()) + " protocol ID to 1.8");
            newProtocolIPs.add(getIP(event.getPlayer()));
        }
    }
    
    private String getIP(Player player) {
        return player.getAddress().getAddress().getHostAddress();
    }
    
    public boolean hasNewProtocol(Player player) {
        return newProtocolIPs.contains(getIP(player));
    }
    
    public void clear(Player player) {
        newProtocolIPs.remove(getIP(player));
    }
}
