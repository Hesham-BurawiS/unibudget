package ia;

import java.awt.EventQueue;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UserRegistration extends User {

	private JFrame frmUnibudget;
	private JTextField firstNameTxt;
	private JTextField lastNameTxt;
	private JTextField emailTxt;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswdField;
	private JLabel notificationLbl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserRegistration window = new UserRegistration();
					window.frmUnibudget.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserRegistration() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmUnibudget = new JFrame();
		frmUnibudget.addKeyListener(new KeyAdapter() {
		});
		frmUnibudget.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LoginMenu.main(null);
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
				LoginMenu.main(null);
			}
		});
		frmUnibudget.setResizable(false);
		frmUnibudget.setTitle("UniBudget");
		frmUnibudget.setBounds(100, 100, 470, 415);
		frmUnibudget.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUnibudget.getContentPane().setLayout(null);
		
		JLabel welcomeLbl = new JLabel("Register for UniBudget");
		welcomeLbl.setBounds(134, 6, 205, 22);
		welcomeLbl.setFont(new Font("Tahoma", Font.BOLD, 18));
		frmUnibudget.getContentPane().add(welcomeLbl);
		
		notificationLbl = new JLabel("");
		notificationLbl.setForeground(Color.RED);
		notificationLbl.setBounds(134, 39, 205, 14);
		frmUnibudget.getContentPane().add(notificationLbl);
		
		JLabel firstNameLbl = new JLabel("First Name");
		firstNameLbl.setBounds(106, 59, 261, 17);
		firstNameLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		firstNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
		frmUnibudget.getContentPane().add(firstNameLbl);
		
		firstNameTxt = new JTextField();
		firstNameTxt.setBounds(106, 82, 261, 20);
		firstNameLbl.setLabelFor(firstNameTxt);
		frmUnibudget.getContentPane().add(firstNameTxt);
		firstNameTxt.setColumns(10);
		
		JLabel lastNameLbl = new JLabel("Last Name");
		lastNameLbl.setBounds(106, 108, 261, 17);
		lastNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
		lastNameLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frmUnibudget.getContentPane().add(lastNameLbl);

		lastNameTxt = new JTextField();
		lastNameTxt.setBounds(106, 131, 261, 20);
		frmUnibudget.getContentPane().add(lastNameTxt);	
		lastNameTxt.setColumns(10);
		
		JLabel emailLbl = new JLabel("Email");
		emailLbl.setBounds(106, 157, 261, 17);
		emailLbl.setHorizontalAlignment(SwingConstants.CENTER);
		emailLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frmUnibudget.getContentPane().add(emailLbl);
		
		emailTxt = new JTextField();
		emailTxt.setBounds(106, 180, 261, 20);
		frmUnibudget.getContentPane().add(emailTxt);
		
		emailTxt.setColumns(10);
		
		JLabel passwordLbl = new JLabel("Password");
		passwordLbl.setBounds(207, 206, 58, 17);
		passwordLbl.setHorizontalAlignment(SwingConstants.CENTER);
		passwordLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frmUnibudget.getContentPane().add(passwordLbl);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(106, 229, 261, 20);
		frmUnibudget.getContentPane().add(passwordField);
		
		JLabel confrimPasswdLbl = new JLabel("Confirm Password");
		confrimPasswdLbl.setBounds(181, 255, 110, 17);
		confrimPasswdLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		confrimPasswdLbl.setHorizontalAlignment(SwingConstants.CENTER);
		frmUnibudget.getContentPane().add(confrimPasswdLbl);	
		
		confirmPasswdField = new JPasswordField();
		confirmPasswdField.setBounds(106, 278, 261, 20);
		frmUnibudget.getContentPane().add(confirmPasswdField);		
		
		JButton registerBtn = new JButton("Register");
		registerBtn.setBounds(106, 329, 261, 25);
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Must run .getText in function otherwise you get empty vars
				String firstName = firstNameTxt.getText();
				String lastName = lastNameTxt.getText();
				String email = emailTxt.getText();
				String unHashedPassword = passwordField.getText();
				String passwordConfirmation = confirmPasswdField.getText();
				String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
				String hashedPassword = null;

				
				// Presence Check
				if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || unHashedPassword.isBlank() ) {
					notificationLbl.setText("Please fill in all fields!");
					return;
				}
				// Email Format Check
				else if (!email.matches(regex)) {
					notificationLbl.setText("Please enter a valid email!");
					return;
				}
				
				// Password match validation 
				else if (unHashedPassword.equals(passwordConfirmation)) {
					hashedPassword = BCrypt.hashpw(unHashedPassword, BCrypt.gensalt()); 
					/* The hash is created by calling the BCrypt.hashpw() function with the parameters of the unhashed password and a 
					 * BCrypt generated salt. This helps strengthen the hash function preventing attacks such 
					 * as a rainbow attack and as the salt is not directly passed to the function it becomes 
					 * less susceptible to attack.*/
					notificationLbl.setText("");
					}
				else {
					notificationLbl.setText("Error your passwords don't match!");
					return;
				}
				
				try {
					/*Values entered by the user are used to instantiate a new User object,
				    its properties being used to insert values into the database for permanent storage purposes.*/ 
				   User newUser = new User(email,firstName,lastName,hashedPassword); 
				   // The JDBC connector is used to create a database Connection object.
				   Connection conn = DriverManager.getConnection("jdbc:mysql://db.burawi.tech:3306/unibudget", "hesho" , "cQnfD23b8tiYk!7h");
				    
				    // An SQL statement is built using prepared statements to insert the users data into the database once it has been validated.
			        String sql = "INSERT INTO users " + "VALUES (default,?, ?, ?, ?)";
			        PreparedStatement preparedStatement = conn.prepareStatement(sql);
			        preparedStatement.setString(1, newUser.email);
			        preparedStatement.setString(2, newUser.hashedPassword);
			        preparedStatement.setString(3, newUser.firstName);
			        preparedStatement.setString(4, newUser.lastName);

			        preparedStatement.executeUpdate(); 
				    
			        notificationLbl.setForeground(Color.GREEN);
			        notificationLbl.setText("Successfully registered!");
			        frmUnibudget.dispose();
				   
				} catch (SQLException ex) {
				    // handle any errors
				    System.out.println("SQLException: " + ex.getMessage());
				    System.out.println("SQLState: " +  ex.getSQLState());
				    System.out.println("VendorError: " +  ex.getErrorCode());
				}
			}
		});
		registerBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frmUnibudget.getContentPane().add(registerBtn);
		

		
		
		
	}

}
