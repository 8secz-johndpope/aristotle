Feature: Change Password page 
Background:
    Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
    And Create Location as "CountryLocationUk" with locationType "CountryLocationType"
	| name | isdCode |
    | UK   | 44      |
    And Create Location as "CountryLocationUsa" with locationType "CountryLocationType"
	| name | isdCode |
    | USA  | 1       |
    And Open Registration page
	And Register new User as "userOne"
	|name        | email               | password  | confirm_password |  mobile_number |  nri   |
	|Ravi Sharma | ping2ravi@gmail.com | password  | password         |  9876543210    |  false |
	And Open Login page 
	Then Enter Login Email/Mobile Number "ping2ravi@gmail.com"
	And Enter Login Password "password"
	And Click on LoginPage Login Button
	Then Take Screen shot as "login_done.png"
	And Check that home page has been opened
Scenario: UI Test : Check All Default Relevent Fields are on Security page that they exists and are empty/unchecked and enabled and visible
	When Open Security page 
	Then Check Field exists "current_password" 
	And Check Field exists "password" 
	And Check Field exists "confirm_password" 
	And Check Field do not exists "error_label" 
	And Check Field do not exists "success_label" 
	And Check Field exists "change_password_button" 
	And Check Field "current_password" is empty 
	And Check Field "password" is empty 
	And Check Field "confirm_password" is empty
	And Check Field "current_password" is enabled and visible
	And Check Field "password" is enabled and visible
	And Check Field "confirm_password" is enabled and visible
	And Check Field "change_password_button" is enabled and visible
	And Take Screen shot as "security.png"
	
Scenario: Register a new user and login and then update password
	
	When Open Security page
	And Enter Security Current Password "password"
	And Enter Security New Password "PASSWORD123"
	And Enter Security New Confirm Password "PASSWORD123"
	And Click on Security Change Password Button
	Then Wait for field "success_label" to appear
	And Check Field do not exists "error_label"

Scenario: Register a new user and login and then update password by entering wrong current password
    
	When Open Security page
	And Enter Security Current Password "Wrong password"
	And Enter Security New Password "PASSWORD123"
	And Enter Security New Confirm Password "PASSWORD123"
	And Click on Security Change Password Button
	Then Wait for field "error_label" to appear
	And Check Field do not exists "success_label"
	And Check For Error "Old password Do not match" to appear

Scenario: Register a new user and login and then update password by entering wrong non matching new passwords
    
	When Open Security page
	And Enter Security Current Password "password"
	And Enter Security New Password "PASSWORD123"
	And Enter Security New Confirm Password "PASSWORD456"
	And Click on Security Change Password Button
	Then Wait for field "error_label" to appear
	And Check Field do not exists "success_label"
	And Check For Error "Password Do not match" to appear	