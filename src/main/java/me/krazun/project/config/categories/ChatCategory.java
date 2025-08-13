package me.krazun.project.config.categories;

import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;
import com.moulberry.lattice.annotation.widget.LatticeWidgetTextField;

public final class ChatCategory {

    @LatticeOption(title = "krazconfig.chat.compactInputBox", description = "!!.description")
    @LatticeWidgetButton
    public boolean compactInputBox = false;

    @LatticeOption(title = "krazconfig.chat.includeChatTimestamps", description = "!!.description")
    @LatticeWidgetButton
    public boolean includeChatTimestamps = false;

    @LatticeOption(title = "krazconfig.chat.chatTimestampPattern", description = "!!.description")
    @LatticeWidgetTextField
    public String chatTimestampPattern = "HH:mm:ss";

}