package me.krazun.project.config.categories;

import com.moulberry.lattice.LatticeDynamicFrequency;
import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.constraint.LatticeIntRange;
import com.moulberry.lattice.annotation.constraint.LatticeShowIf;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;
import com.moulberry.lattice.annotation.widget.LatticeWidgetKeybind;
import com.moulberry.lattice.annotation.widget.LatticeWidgetSlider;
import com.moulberry.lattice.annotation.widget.LatticeWidgetTextField;
import me.krazun.project.config.api.custom.CustomKeybind;

public final class ChatCategory {

    @LatticeOption(title = "krazconfig.chat.compactInputBox", description = "!!.description")
    @LatticeWidgetButton
    public boolean compactInputBox = false;

    @LatticeOption(title = "krazconfig.chat.compactInputBoxMinimumWidth", description = "!!.description")
    @LatticeWidgetSlider
    @LatticeIntRange(min = 40, max = 320)
    @LatticeShowIf(function = "shouldShowCompactInputBoxMinimumWidth", frequency = LatticeDynamicFrequency.EVERY_TICK)
    public int compactInputBoxMinimumWidth = 180;

    public boolean shouldShowCompactInputBoxMinimumWidth() {
        return compactInputBox;
    }

    @LatticeOption(title = "krazconfig.chat.includeChatTimestamps", description = "!!.description")
    @LatticeWidgetButton
    public boolean includeChatTimestamps = false;

    @LatticeOption(title = "krazconfig.chat.chatTimestampPattern", description = "!!.description")
    @LatticeWidgetTextField
    public String chatTimestampPattern = "HH:mm:ss";

    @LatticeOption(title = "krazconfig.chat.copyChat", description = "!!.description")
    @LatticeWidgetKeybind(allowModifiers = true)
    public CustomKeybind copyChat = new CustomKeybind(null, 0, false, false, false, false);

}