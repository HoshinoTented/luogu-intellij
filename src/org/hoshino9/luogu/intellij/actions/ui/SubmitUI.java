package org.hoshino9.luogu.intellij.actions.ui;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public abstract class SubmitUI extends DialogWrapper {
	protected JPanel mainPanel;
	protected JRadioButton useFileName;
	protected JRadioButton useCustomName;
	protected JTextField customName;
	protected JComboBox language;

	public SubmitUI() {
		super(true);
	}
}
