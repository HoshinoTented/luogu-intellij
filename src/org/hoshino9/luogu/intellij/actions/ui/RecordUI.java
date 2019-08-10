package org.hoshino9.luogu.intellij.actions.ui;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public abstract class RecordUI extends DialogWrapper {
	protected JTextArea infoText;
	protected JPanel mainPanel;

	public RecordUI() {
		super(true);
	}
}
