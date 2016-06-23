@current
Feature: Registration page 
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
Scenario: UI Test : Check All Default Relevent Fields are on Login page that they exists and are empty/unchecked and enabled and visible
	When Open Login page 
	Then Check Field exists "email_mobile_number" 
	Then Check Field exists "password" 
	Then Check Field do not exists "error_label" 
	Then Check Field do not exists "success_label" 
	Then Check Field exists "register_button" 
	Then Check Field exists "login_button" 
	Then Check Field "email_mobile_number" is empty 
	Then Check Field "password" is empty 
	Then Check Field "email_mobile_number" is enabled and visible
	Then Check Field "password" is enabled and visible
	Then Check Field "register_button" is enabled and visible
	Then Check Field "login_button" is enabled and visible
	Then Take Screen shot as "login.png"
	
Scenario: Register a new indian user and login user by email address
	
    Given Open Registration page
	And Register new User as "userOne"
	|name        | email               | password  | confirm_password |  mobile_number |  nri   |
	|Ravi Sharma | ping2ravi@gmail.com | password  | password         |  9876543210    |  false |
	And Open Login page 
	Then Enter Login Email/Mobile Number "ping2ravi@gmail.com"
	And Enter Login Password "password"
	And Click on LoginPage Login Button
	Then Take Screen shot as "login_done.png"
	And Check that home page has been opened
	And Take Screen shot as "home_open.png"

Scenario: Register a new indian user without phone and login user by email address
    Given Open Registration page
	And Register new User as "userOne"
	|name        | email               | password  | confirm_password |   nri   |
	|Ravi Sharma | ping2ravi@gmail.com | password  | password         |   false |
	And Open Login page 
	Then Enter Login Email/Mobile Number "ping2ravi@gmail.com"
	And Enter Login Password "password"
	And Click on LoginPage Login Button
	Then Take Screen shot as "login_done.png"
	And Check that home page has been opened
	And Take Screen shot as "home_open.png"
	
Scenario: Register a new indian user and login user by phone
    Given Open Registration page
	And Register new User as "userOne"
	|name        | email               | password  | confirm_password |  mobile_number |  nri   |
	|Ravi Sharma | ping2ravi@gmail.com | password  | password         |  9876543210    |  false |
	And Open Login page 
	Then Enter Login Email/Mobile Number "9876543210"
	And Enter Login Password "password"
	And Click on LoginPage Login Button
	Then Take Screen shot as "login_done_phone.png"	
	And Check that home page has been opened
	And Take Screen shot as "home_open.png"

Scenario: Register a new NRI user and login user by email
    Given Open Registration page
	And Register new User as "userOne"
	|name        | email               | password  | confirm_password |  nri   | country_code |  mobile_number |
	|Ravi Sharma | ping2ravi@gmail.com | password  | password         |  true  | UK(44)       |  9876543210    |
	And Open Login page 
	Then Enter Login Email/Mobile Number "ping2ravi@gmail.com"
	And Enter Login Password "password"
	And Click on LoginPage Login Button
	Then Take Screen shot as "login_done.png"
	And Check that home page has been opened
	And Take Screen shot as "home_open.png"
	
Scenario: Register a new NRI user and login user by phone
    Given Open Registration page
	And Register new User as "userOne"
	|name        | email               | password  | confirm_password |  nri   | country_code |  mobile_number |
	|Ravi Sharma | ping2ravi@gmail.com | password  | password         |  true  | UK(44)       |  9876543210    |
	And Open Login page 
	Then Enter Login Email/Mobile Number "9876543210"
	And Enter Login Password "password"
	And Click on LoginPage Login Button
	Then Take Screen shot as "login_done.png"	
	And Check that home page has been opened
	And Take Screen shot as "home_open.png"	
Scenario: On Register page click on Already Registered button, Login now
    Given Open Registration page
	And Register new User as "userOne"
	|name        | email               | password  | confirm_password |  nri   | country_code |  mobile_number |
	|Ravi Sharma | ping2ravi@gmail.com | password  | password         |  true  | UK(44)       |  9876543210    |
	And Open Login page 
	Then Enter Login Email/Mobile Number "9876543210"
	And Enter Login Password "password"
	And Click on LoginPage Login Button
	Then Take Screen shot as "login_done.png"	
	And Check that home page has been opened
	And Take Screen shot as "home_open.png"	