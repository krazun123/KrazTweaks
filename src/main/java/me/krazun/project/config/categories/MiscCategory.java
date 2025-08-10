package me.krazun.project.config.categories;

import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;

public final class MiscCategory {

    @LatticeOption(title = "krazconfig.misc.ignoreSignatureErrors", description = "!!.description")
    @LatticeWidgetButton
    public boolean ignoreSignatureErrors = false;

}
