package org.hoshino9.luogu.intellij.actions.ui;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public abstract class UnlockUI extends DialogWrapper {
	protected JTextField unlockCode;
	protected JPanel mainPanel;

	public UnlockUI() {
		super(true);
	}
}
