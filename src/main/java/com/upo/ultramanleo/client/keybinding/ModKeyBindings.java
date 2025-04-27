package com.upo.ultramanleo.client.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final String KEY_CATEGORY_ULTRAMANLEO = "key.category.ultramanleo";
    public static final String KEY_ULTRAMAN_SKILL = "key.ultramanleo.skill_z";
    public static final String KEY_ULTRAMAN_FIREBALL = "key.ultramanleo.skill_x";

    public static final KeyMapping ULTRAMAN_Z_KEY = new KeyMapping(
            KEY_ULTRAMAN_SKILL,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            KEY_CATEGORY_ULTRAMANLEO
    );
    public static final KeyMapping ULTRAMAN_X_KEY = new KeyMapping(
            KEY_ULTRAMAN_FIREBALL,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            KEY_CATEGORY_ULTRAMANLEO
    );
}
