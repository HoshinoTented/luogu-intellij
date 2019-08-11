package org.hoshino9.luogu.intellij.actions.ui;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public abstract class PasteUI extends DialogWrapper {
	protected JPanel mainPanel;
	protected JCheckBox makePublic;

	public PasteUI() {
		super(true);
	}
}
