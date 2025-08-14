package me.krazun.project.config.api.custom;

import com.mojang.blaze3d.platform.InputConstants;
import com.moulberry.lattice.keybind.KeybindInterface;
import com.moulberry.lattice.keybind.LatticeInputType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;
import java.util.List;

public final class CustomKeybind implements KeybindInterface {

    private LatticeInputType type;
    private int value;
    private boolean shiftMod, ctrlMod, altMod, superMod;

    public CustomKeybind(LatticeInputType type, int value, boolean shiftMod, boolean ctrlMod, boolean altMod, boolean superMod) {
        this.type = type;
        this.value = value;
        this.shiftMod = shiftMod;
        this.ctrlMod = ctrlMod;
        this.altMod = altMod;
        this.superMod = superMod;
    }

    @Override
    public Component getKeyMessage() {
        if(this.type == null) {
            return Component.literal("None");
        }

        StringBuilder builder = new StringBuilder();

        if(this.shiftMod) {
            builder.append("Shift+");
        }

        if(ctrlMod) {
            builder.append("Ctrl+");
        }

        if(this.altMod) {
            builder.append("Alt+");
        }

        if(this.superMod) {
            builder.append("Super+");
        }

        Component name = switch (this.type) {
            case KEYSYM -> InputConstants.Type.KEYSYM.getOrCreate(value).getDisplayName();
            case SCANCODE -> InputConstants.Type.SCANCODE.getOrCreate(value).getDisplayName();
            case MOUSE -> InputConstants.Type.MOUSE.getOrCreate(value).getDisplayName();
        };

        return Component.literal(builder.toString()).append(name);
    }

    @Override
    public void setKey(LatticeInputType type, int value, boolean shiftMod, boolean ctrlMod, boolean altMod, boolean superMod) {
        this.type = type;
        this.value = value;
        this.shiftMod = shiftMod;
        this.ctrlMod = ctrlMod;
        this.altMod = altMod;
        this.superMod = superMod;
    }

    @Override
    public void setUnbound() {
        this.type = null;
    }

    public boolean isUnbound() {
        return type == null;
    }

    @Override
    public Collection<Component> getConflicts() {
        return List.of();
    }

    public boolean matches(int key, boolean mouse) {
        final var window = Minecraft.getInstance().getWindow().getWindow();
        if (isUnbound()) return false;
        if(key != value) return false;

        if (shiftMod) {
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) != GLFW.GLFW_PRESS &&
                    GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) != GLFW.GLFW_PRESS) {
                return false;
            }
        }

        if (ctrlMod) {
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) != GLFW.GLFW_PRESS &&
                    GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_CONTROL) != GLFW.GLFW_PRESS) {
                return false;
            }
        }

        if (altMod) {
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) != GLFW.GLFW_PRESS &&
                    GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_ALT) != GLFW.GLFW_PRESS) {
                return false;
            }
        }

        if (superMod) {
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SUPER) != GLFW.GLFW_PRESS &&
                    GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SUPER) != GLFW.GLFW_PRESS) {
                return false;
            }
        }

        if(mouse) {
            return GLFW.glfwGetMouseButton(window, key) == GLFW.GLFW_PRESS;
        }

        return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
    }
}