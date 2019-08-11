package org.hoshino9.luogu.intellij.actions.ui;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public abstract class LoginUI extends DialogWrapper {
	protected JTextField account;
	protected JPanel mainPanel;
	protected JPasswordField password;

	public LoginUI() {
		super(true);
	}
}
