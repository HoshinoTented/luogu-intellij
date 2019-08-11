package org.hoshino9.luogu.intellij.ui.ui;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public abstract class VerifyUI extends DialogWrapper {
	protected JTextField verifyCodeText;
	protected JPanel mainPanel;
	protected JLabel verifyCode;

	public VerifyUI() {
		super(true);
	}
}
