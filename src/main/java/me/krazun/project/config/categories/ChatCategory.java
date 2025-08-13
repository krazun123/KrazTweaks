package me.krazun.project.config.categories;

import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;

public final class ChatCategory {

    @LatticeOption(title = "krazconfig.chat.includeChatTimestamps", description = "!!.description")
    @LatticeWidgetButton
    public boolean includeChatTimestamps = false;

}