Feature: Registration page 
Scenario: UI Test : Check All Default Relevent Fields are on Registration page for User living in India that they exists and are empty/unchecked and enabled and visible
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
	When Open Registration page 
	Then Check Field exists "name" 
	Then Check Field exists "email" 
	Then Check Field exists "password" 
	Then Check Field exists "confirm_password" 
	Then Check Field do not exists "country_code" 
	Then Check Field exists "mobile_number" 
	Then Check Field exists "captcha" 
	Then Check Field do not exists "error_label" 
	Then Check Field exists "register_button" 
	Then Check Field exists "nri" 
	Then Check Field exists "login_button" 
	Then Check Field "name" is empty 
	Then Check Field "email" is empty 
	Then Check Field "password" is empty 
	Then Check Field "confirm_password" is empty 
	Then Check Field "mobile_number" is empty 
	Then Check Field "nri" is not checked 
	Then Check Field "name" is enabled and visible
	Then Check Field "email" is enabled and visible
	Then Check Field "password" is enabled and visible
	Then Check Field "confirm_password" is enabled and visible 
	Then Check Field "mobile_number" is enabled and visible
	Then Check Checkbox Field "nri" is enabled and visible
	Then Check Field "register_button" is enabled and visible
	Then Check Field "login_button" is enabled and visible
	Then Take Screen shot as "indian_registration.png"
	
Scenario: UI Test : Check All Default Relevent Fields are on Registration page for User living abroad/NRI that they exists and are empty/unchecked and enabled and visible
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
	When Open Registration page 
	And Select Registeration NRI checkbox
	Then Check Field exists "name" 
	Then Check Field exists "email" 
	Then Check Field exists "password" 
	Then Check Field exists "confirm_password" 
	Then Check Field exists "country_code" 
	Then Check Field exists "mobile_number" 
	Then Check Field exists "captcha" 
	Then Check Field do not exists "error_label" 
	Then Check Field exists "register_button" 
	Then Check Field exists "nri" 
	Then Check Field exists "login_button" 
	Then Check Field "name" is empty 
	Then Check Field "email" is empty 
	Then Check Field "password" is empty 
	Then Check Field "confirm_password" is empty 
	Then Check Field "mobile_number" is empty 
	Then Check Field "nri" is checked 
	Then Check Field "name" is enabled and visible
	Then Check Field "email" is enabled and visible
	Then Check Field "password" is enabled and visible
	Then Check Field "confirm_password" is enabled and visible 
	Then Check Field "mobile_number" is enabled and visible
	Then Check Checkbox Field "nri" is enabled and visible
	Then Check Field "country_code" is enabled and visible
	Then Check Field "register_button" is enabled and visible
	Then Check Field "login_button" is enabled and visible
	Then Take Screen shot as "nri_registration.png"
	
Scenario: Functional Test : Register an Indian User with All Data
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "success_label" to appear
	Then Registration Button should be enabled
	Then Check one user exists
	| name          | creationType    |  nri  | superAdmin | donor | member |
    | Ravi Sharma   | SelfServiceUser | false | false      | false | false  |
    Then Check one email exists
	| email                 | emailUp             | newsLetter | confirmationType | confirmed |
    | ping2ravi@gmail.com   | PING2RAVI@GMAIL.COM | true       | UN_CONFIRNED     | false     |
    Then Check one phone exists
	| countryCode | phoneNumber | phoneType | confirmed |
    | 91          | 9876543210  | MOBILE    | false     |
    Then Check email and user are connected
    Then Check email and phone are connected
    Then Check phone and user are connected
	Then Take Screen shot as "indian_registration_all_data.png"
    
Scenario: Functional Test : Register an Indian User with Phone Number missing
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	#And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "success_label" to appear
	Then Registration Button should be enabled
	Then Check one user exists
	| name          | creationType    |  nri  | superAdmin | donor | member |
    | Ravi Sharma   | SelfServiceUser | false | false      | false | false  |
    Then Check one email exists
	| email                 | emailUp             | newsLetter | confirmationType | confirmed |
    | ping2ravi@gmail.com   | PING2RAVI@GMAIL.COM | true       | UN_CONFIRNED     | false     |
    Then Check no phone exists
    Then Check email and user are connected
    Then Take Screen shot as "indian_registration_with_email.png"
    
 
 Scenario: Functional Test : Register an Indian User with Email missing
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	#And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "error_label" to appear
	Then Check For Error "Email must be provided" to appear
	Then Registration Button should be enabled
	Then Check no user exists
    Then Check no email exists
    Then Check no phone exists
    Then Take Screen shot as "indian_registration_without_email.png"
 
 Scenario: Functional Test : Register an Indian User with Email where that email already exists
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "success_label" to appear
	Then Registration Button should be enabled
	Then Check one user exists
	| name          | creationType    |  nri  | superAdmin | donor | member |
    | Ravi Sharma   | SelfServiceUser | false | false      | false | false  |
    Then Check one email exists
	| email                 | emailUp             | newsLetter | confirmationType | confirmed |
    | ping2ravi@gmail.com   | PING2RAVI@GMAIL.COM | true       | UN_CONFIRNED     | false     |
    Then Check one phone exists
	| countryCode | phoneNumber | phoneType | confirmed |
    | 91          | 9876543210  | MOBILE    | false     |
    Then Check email and user are connected
    Then Check email and phone are connected
    Then Check phone and user are connected   
    And Clear Registeration form
	And Enter Registeration Name "Xyz"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Enter Registeration Mobile Number "1234567890"
	And Click on Registration Button
	Then Wait for field "error_label" to appear
	Then Check For Error "Email [ping2ravi@gmail.com] is already registered" to appear
    Then Take Screen shot as "indian_registration_with_email_already_exists.png"
	
Scenario: Functional Test : Register an Indian User with Phone where that phone already exists
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "success_label" to appear
	Then Registration Button should be enabled
	Then Check one user exists
	| name          | creationType    |  nri  | superAdmin | donor | member |
    | Ravi Sharma   | SelfServiceUser | false | false      | false | false  |
    Then Check one email exists
	| email                 | emailUp             | newsLetter | confirmationType | confirmed |
    | ping2ravi@gmail.com   | PING2RAVI@GMAIL.COM | true       | UN_CONFIRNED     | false     |
    Then Check one phone exists
	| countryCode | phoneNumber | phoneType | confirmed |
    | 91          | 9876543210  | MOBILE    | false     |
    Then Check email and user are connected
    Then Check email and phone are connected
    Then Check phone and user are connected   
    And Clear Registeration form
	And Enter Registeration Name "Xyz"
	And Enter Registeration Email "ping2ravixyz@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	Then Wait for field "error_label" to appear
	Then Check For Error "Mobile [9876543210] already registered" to appear	
    Then Take Screen shot as "indian_registration_with_phone_already_exists.png"
    
 Scenario: Functional Test : Register an Indian User where passwords dont match
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password123"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "error_label" to appear
	Then Check For Error "Password do not match" to appear
	Then Registration Button should be enabled
	Then Check no user exists
    Then Check no email exists
    Then Check no phone exists   
    Then Take Screen shot as "indian_registration_with_password_do_not_match.png"
    
 @current   
 Scenario: Functional Test : Register NRI user With all Details
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
    Given Create Location as "CountryLocationUk" with locationType "CountryLocationType"
	| name | isdCode |
    | UK   | 44      |
    Given Create Location as "CountryLocationUsa" with locationType "CountryLocationType"
	| name | isdCode |
    | USA  | 1       |
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Select Registeration NRI checkbox
	And Select Registration Country "UK(44)"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "success_label" to appear
	Then Registration Button should be enabled
	Then Check one user exists
	| name          | creationType    |  nri  | superAdmin | donor | member |
    | Ravi Sharma   | SelfServiceUser | true  | false      | false | false  |
    Then Check one email exists
	| email                 | emailUp             | newsLetter | confirmationType | confirmed |
    | ping2ravi@gmail.com   | PING2RAVI@GMAIL.COM | true       | UN_CONFIRNED     | false     |
    Then Check one phone exists
	| countryCode | phoneNumber | phoneType     | confirmed |
    | 44          | 9876543210  | NRI_MOBILE    | false     |
    Then Check email and user are connected
    Then Check email and phone are connected
    Then Check phone and user are connected   
    Then Take Screen shot as "nri_registration_with_all_data.png"
    
Scenario: Functional Test : Register NRI user Without Phone Number
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
    Given Create Location as "CountryLocationUk" with locationType "CountryLocationType"
	| name | isdCode |
    | UK   | 44      |
    Given Create Location as "CountryLocationUsa" with locationType "CountryLocationType"
	| name | isdCode |
    | USA  | 1       |
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Select Registeration NRI checkbox
	#And Select Registration Country "UK(44)"
	#And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "error_label" to appear
	Then Check For Error "Phone Number must be provided" to appear
	Then Registration Button should be enabled
	Then Check no user exists
    Then Check no email exists
    Then Check no phone exists
 
@current   
Scenario: Functional Test : Register NRI user Without Email
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
    Given Create Location as "CountryLocationUk" with locationType "CountryLocationType"
	| name | isdCode |
    | UK   | 44      |
    Given Create Location as "CountryLocationUsa" with locationType "CountryLocationType"
	| name | isdCode |
    | USA  | 1       |
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	#And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Select Registeration NRI checkbox
	And Select Registration Country "UK(44)"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "error_label" to appear
	Then Check For Error "Email must be provided" to appear
	Then Registration Button should be enabled
	Then Check no user exists
    Then Check no email exists
    Then Check no phone exists
    
Scenario: Functional Test : Register NRI user Without Country Code
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
    Given Create Location as "CountryLocationUk" with locationType "CountryLocationType"
	| name | isdCode |
    | UK   | 44      |
    Given Create Location as "CountryLocationUsa" with locationType "CountryLocationType"
	| name | isdCode |
    | USA  | 1       |
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Select Registeration NRI checkbox
	#And Select Registration Country "UK(44)"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "error_label" to appear
	Then Check For Error "Country Code must be provided" to appear
	Then Registration Button should be enabled
	Then Check no user exists
    Then Check no email exists
    Then Check no phone exists    
    
Scenario: Functional Test : Register NRI user when Passwords do not match
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
    Given Create Location as "CountryLocationUk" with locationType "CountryLocationType"
	| name | isdCode |
    | UK   | 44      |
    Given Create Location as "CountryLocationUsa" with locationType "CountryLocationType"
	| name | isdCode |
    | USA  | 1       |
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password123"
	And Select Registeration NRI checkbox
	And Select Registration Country "UK(44)"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "error_label" to appear
	Then Check For Error "Password do not match" to appear
	Then Registration Button should be enabled
	Then Check no user exists
    Then Check no email exists
    Then Check no phone exists
	
Scenario: Functional Test : Register NRI user With email where that email already exists
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
    Given Create Location as "CountryLocationUk" with locationType "CountryLocationType"
	| name | isdCode |
    | UK   | 44      |
    Given Create Location as "CountryLocationUsa" with locationType "CountryLocationType"
	| name | isdCode |
    | USA  | 1       |
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Select Registeration NRI checkbox
	And Select Registration Country "UK(44)"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "success_label" to appear
	Then Registration Button should be enabled
	Then Check one user exists
	| name          | creationType    |  nri  | superAdmin | donor | member |
    | Ravi Sharma   | SelfServiceUser | true  | false      | false | false  |
    Then Check one email exists
	| email                 | emailUp             | newsLetter | confirmationType | confirmed |
    | ping2ravi@gmail.com   | PING2RAVI@GMAIL.COM | true       | UN_CONFIRNED     | false     |
    Then Check one phone exists
	| countryCode | phoneNumber | phoneType     | confirmed |
    | 44          | 9876543210  | NRI_MOBILE    | false     |
    Then Check email and user are connected
    Then Check email and phone are connected
    Then Check phone and user are connected  
    Then Clear Registeration form
    And Enter Registeration Name "Kiyansh Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Select Registeration NRI checkbox
	And Select Registration Country "UK(44)"
	And Enter Registeration Mobile Number "1234567890"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "error_label" to appear
	Then Check For Error "Email [ping2ravi@gmail.com] is already registered" to appear

Scenario: Functional Test : Register NRI user With phone/countrycode where that phone/countrycode already exists
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
    Given Create Location as "CountryLocationUk" with locationType "CountryLocationType"
	| name | isdCode |
    | UK   | 44      |
    Given Create Location as "CountryLocationUsa" with locationType "CountryLocationType"
	| name | isdCode |
    | USA  | 1       |
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Select Registeration NRI checkbox
	And Select Registration Country "UK(44)"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "success_label" to appear
	Then Registration Button should be enabled
	Then Check one user exists
	| name          | creationType    |  nri  | superAdmin | donor | member |
    | Ravi Sharma   | SelfServiceUser | true  | false      | false | false  |
    Then Check one email exists
	| email                 | emailUp             | newsLetter | confirmationType | confirmed |
    | ping2ravi@gmail.com   | PING2RAVI@GMAIL.COM | true       | UN_CONFIRNED     | false     |
    Then Check one phone exists
	| countryCode | phoneNumber | phoneType     | confirmed |
    | 44          | 9876543210  | NRI_MOBILE    | false     |
    Then Check email and user are connected
    Then Check email and phone are connected
    Then Check phone and user are connected  
    Then Clear Registeration form
    And Enter Registeration Name "Kiyansh Sharma"
	And Enter Registeration Email "ping2ravi123@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Select Registeration NRI checkbox
	And Select Registration Country "UK(44)"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "error_label" to appear
	Then Check For Error "Mobile [9876543210] already registered" to appear	
 
 Scenario: Functional Test : Register NRI user With phone/countrycode where that phone already exists but countrycode is diferent
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
    Given Create Location as "CountryLocationUk" with locationType "CountryLocationType"
	| name | isdCode |
    | UK   | 44      |
    Given Create Location as "CountryLocationUsa" with locationType "CountryLocationType"
	| name | isdCode |
    | USA  | 1       |
	When Open Registration page 
	And Enter Registeration Name "Ravi Sharma"
	And Enter Registeration Email "ping2ravi@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Select Registeration NRI checkbox
	And Select Registration Country "UK(44)"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "success_label" to appear
	Then Registration Button should be enabled
	Then Check one user exists
	| name          | creationType    |  nri  | superAdmin | donor | member |
    | Ravi Sharma   | SelfServiceUser | true  | false      | false | false  |
    Then Check one email exists
	| email                 | emailUp             | newsLetter | confirmationType | confirmed |
    | ping2ravi@gmail.com   | PING2RAVI@GMAIL.COM | true       | UN_CONFIRNED     | false     |
    Then Check one phone exists
	| countryCode | phoneNumber | phoneType     | confirmed |
    | 44          | 9876543210  | NRI_MOBILE    | false     |
    Then Check email and user are connected
    Then Check email and phone are connected
    Then Check phone and user are connected  
    Then Clear Registeration form
    And Enter Registeration Name "Kiyansh Sharma"
	And Enter Registeration Email "ping2ravi123@gmail.com"
	And Enter Registeration Password "password"
	And Enter Registeration Confirm Password "password"
	And Select Registeration NRI checkbox
	And Select Registration Country "USA(1)"
	And Enter Registeration Mobile Number "9876543210"
	And Click on Registration Button
	#Then Registration Button should be disabled //Its very fast so unable to assert
	Then Wait for field "success_label" to appear
       
 Scenario: Functional Test : Open Register Page and then Click on 'Already Registered? Login Now' Link
	Given Create Location Type as "CountryLocationType"
	| name      |
    | Country   | 
	When Open Registration page 
	And Click on Already Registerd Button
	Then Check that login page has been opened