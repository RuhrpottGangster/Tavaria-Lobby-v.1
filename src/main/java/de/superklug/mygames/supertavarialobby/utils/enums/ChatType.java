package de.superklug.mygames.supertavarialobby.utils.enums;

import lombok.Getter;

public enum ChatType {
    
    NONE("§cDeaktiviert"), VIP("§6Gold§8/§aEmerald§8/§3Diamond"), ALL("§aAktiviert");
    
    private final @Getter String name;

    private ChatType(final String name) {
        this.name = name;
    }

}
