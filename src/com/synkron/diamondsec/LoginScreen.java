package com.synkron.diamondsec;

import com.synkron.diamondsec.connectors.InfoWareConnector;
import com.synkron.diamondsec.connectors.LoginConnector;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;
import net.rim.device.api.util.StringProvider;

public class LoginScreen extends BaseScreen implements FieldChangeListener{

	TextField txtUsername;
	PasswordEditField txtPassword;
	CustomLabelField lblUsername, lblPassword;
	CustomButtonField btnLogin;
	CustomHyperLinkButtonField btnForgotPass;
	
	VerticalFieldManager vManager;
	HorizontalFieldManager hUserManager, hPasswordManager;

	public LoginScreen() {
		super(NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH);
		
		setTitle();	
		setStatus();
		
		addMenuItem(new MenuItem(new StringProvider("Login"), 0, 0){
			public void run(){
				handleLogin();
			}
		});
		
		vManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL| USE_ALL_WIDTH);
		
		lblUsername = new CustomLabelField();
		lblUsername.setText("Customer ID");
		lblUsername.setMargin(30,5,0,10);
		lblUsername.setFontColor(Color.WHITE);
		
		txtUsername = new TextField(Field.FIELD_LEADING | TextField.NO_NEWLINE);
		txtUsername.setMaxSize(10);
		txtUsername.setBorder(BorderFactory.createSimpleBorder(new XYEdges(2,2,2,2)));
		txtUsername.setMargin(20,10,0,0);
		txtUsername.setPadding(10,10,10,10);
		txtUsername.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		
		/*get the length of the customerid label and add padding for password 
		 * textbox alignment. 
		 * */
		Font defaultFont = Font.getDefault();
		int fieldPadding = (defaultFont.getAdvance(lblUsername.getText()) -
				defaultFont.getAdvance("Password") + 5);
		
		lblPassword = new CustomLabelField();
		lblPassword.setText("Password");
		lblPassword.setMargin(20,fieldPadding,0,10);
		lblPassword.setFontColor(Color.WHITE);
		
		txtPassword = new PasswordEditField();
		txtPassword.setMaxSize(10);
		txtPassword.setMargin(10,10,0,0);
		txtPassword.setPadding(10,10,10,10);
		txtPassword.setBorder(BorderFactory.createSimpleBorder(new XYEdges(2,2,2,2)));
		txtPassword.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		
		btnLogin = new CustomButtonField("Login",Color.LIGHTGREEN);
		btnLogin.setChangeListener(this);
		btnLogin.setMargin(new XYEdges(20,0,10,0));
		
		hUserManager = new HorizontalFieldManager(Field.FIELD_VCENTER);
		hUserManager.add(lblUsername);
		hUserManager.add(txtUsername);
		
		vManager.add(hUserManager);
		
		hPasswordManager = new HorizontalFieldManager();
		hPasswordManager.add(lblPassword);
		hPasswordManager.add(txtPassword);
		
        vManager.add(hPasswordManager);		
        vManager.add(btnLogin);
		
		btnForgotPass = new 
			CustomHyperLinkButtonField("Forgot Your Password? Click Here");
		btnForgotPass.setChangeListener(this);
		
		vManager.add(btnForgotPass);
		
		vManager.add(new CustomHyperLinkButtonField("You Do Not have an ID? Click Here"));
		
		add(vManager);
	}

	public boolean onClose() {
        setDirty(false);
        return super.onClose();
    }

	public void handleLogin(){
		LoginStatusScreen theStatus = new LoginStatusScreen();
		UiApplication.getUiApplication().pushScreen(theStatus);
		String _loginUrl = InfoWareConnector.API_LOGIN_URL;
		_loginUrl = _loginUrl+ txtUsername.getText()+'|'+txtPassword.getText();
		LoginConnector _loginConnector = new LoginConnector(_loginUrl,txtUsername.getText());
		_loginConnector.start();
	}
	
	public void fieldChanged(Field arg0, int arg1) {
		UiApplication diamondSec = UiApplication.getUiApplication();
		
		if(arg0 == btnLogin){
			//check for empty values in username and password controls.
			if(txtUsername.getText().equals("") || txtPassword.getText().equals("")){
				UiApplication.getUiApplication().pushScreen(
						new LoginStatusScreen("Customer ID or Password cannot be blank"));
			}
			else{
				handleLogin();
			}
		}
		if(arg0 == btnForgotPass){
			
			diamondSec.pushScreen(new ForgotPasswordScreen());
		}
	}
	
}
