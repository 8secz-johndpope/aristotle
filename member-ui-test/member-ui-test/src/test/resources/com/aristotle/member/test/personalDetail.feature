Feature: Login page 
Background: 
	Given Create Location Type as "CountryLocationType" 
		| name      |
		| Country   |
	And Create Location Type as "CountryRegionLocationType" 
		| name      	  |
		| CountryRegion   |
	And Create Location Type as "CountryRegionAreaLocationType" 
		| name      		  |
		| CountryRegionArea   |   
	And Create Location Type as "StateLocationType" 
		| name      |
		| State     | 
	And Create Location Type as "DistrictLocationType" 
		| name       |
		| District   |
	And Create Location Type as "AcLocationType" 
		| name      			 |
		| AssemblyConstituency   |
	And Create Location Type as "PcLocationType" 
		| name      			   |
		| ParliamentConstituency   |   
	And Create Location as "CountryLocationUk" with locationType "CountryLocationType" 
		| name | isdCode |
		| UK   | 44      |
	And Create Location as "CountryLocationUsa" with locationType "CountryLocationType" 
		| name | isdCode |
		| USA  | 1       |
	And Create Location as "CRLEngland" with locationType "CountryRegionLocationType" under location "CountryLocationUk" 
		| name 		|
		| England   |
	And Create Location as "CRALLondon" with locationType "CountryRegionAreaLocationType" under location "CRLEngland" 
		| name 		|
		| London    |
	And Create Location as "StateHaryana" with locationType "StateLocationType" 
		| name     |
		| Haryana  |
	And Create Location as "DistrictPalwal" with locationType "DistrictLocationType" under location "StateHaryana" 
		| name 		|
		| Palwal    |
	And Create Location as "DistrictFaridabad" with locationType "DistrictLocationType" under location "StateHaryana" 
		| name 		   |
		| Faridabad    |
	And Create Location as "AcPalwal" with locationType "AcLocationType" under location "DistrictPalwal" 
		| name 		|
		| Palwal    |
	And Create Location as "AcBallabgarh" with locationType "AcLocationType" under location "DistrictFaridabad" 
		| name 		    |
		| Ballabgarh    |		
	And Create Location as "PcFaridabad" with locationType "PcLocationType" under location "StateHaryana" 
		| name 		   |
		| Faridabad    |
	And Create Location as "PcAmbala" with locationType "PcLocationType" under location "StateHaryana" 
		| name 		|
		| Ambala    |	
	And Create Location as "PcKarnal" with locationType "PcLocationType" under location "StateHaryana" 
		| name 		|
		| Karnal    |	
	And Create Location as "StateDelhi" with locationType "StateLocationType" 
		| name     |
		| Delhi    |
	And Create Location as "DistrictCentralDelhi" with locationType "DistrictLocationType" under location "StateDelhi" 
		| name 		       |
		| Central Delhi    |
	And Create Location as "AcChandaniChowk" with locationType "AcLocationType" under location "DistrictCentralDelhi" 
		| name 		        |
		| Chandani Chowk    |
	And Create Location as "PcCannaught Place" with locationType "PcLocationType" under location "StateDelhi" 
		| name 		         |
		| Cannuaght Place    |
		
Scenario: 
	Register indian user and check if relevent fields exists and visible and enabled 
	When Open Registration page 
	And Register new User as "userOne" 
		|name        | email               | password  | confirm_password |  mobile_number |  nri   |
		|Ravi Sharma | ping2ravi@gmail.com | password  | password         |  9876543210    |  false |
	And Open Login page 
	And Enter Login Email/Mobile Number "ping2ravi@gmail.com" 
	And Enter Login Password "password" 
	And Click on LoginPage Login Button 
	And Take Screen shot as "login_done.png" 
	And Check that home page has been opened 
	And Take Screen shot as "home_open.png" 
	When Open Personal Detail page 
	Then Check Field exists "name" 
	And Check Field exists "gender" 
	And Check Field exists "date_of_birth" 
	And Check Field exists "id_card_type" 
	And Check Field exists "id_card_number" 
	And Check Field exists "father_name" 
	And Check Field exists "mother_name" 
	And Check Field exists "mother_name" 
	And Check Field exists "nri" 
	And Check Field exists "living_state" 
	And Check Field exists "living_district" 
	And Check Field exists "living_parliament_constituency" 
	And Check Field exists "living_assembly_constituency" 
	And Check Field exists "voting_state" 
	And Check Field exists "voting_district" 
	And Check Field exists "voting_parliament_constituency" 
	And Check Field exists "voting_assembly_constituency" 
	And Check Field exists "save_button" 
	Then Check Field "name" has "Ravi Sharma" 
	Then Check Nothing selected in combo box "gender" 
	Then Check Date Field "date_of_birth" is empty 
	Then Check Nothing selected in combo box "id_card_type" 
	Then Check Nothing selected in combo box "living_state" 
	Then Check Nothing selected in combo box "living_district" 
	Then Check Nothing selected in combo box "living_parliament_constituency" 
	Then Check Nothing selected in combo box "living_assembly_constituency" 
	Then Check Nothing selected in combo box "voting_state" 
	Then Check Nothing selected in combo box "voting_district" 
	Then Check Nothing selected in combo box "voting_parliament_constituency" 
	Then Check Nothing selected in combo box "voting_assembly_constituency" 
	Then Check Field "id_card_number" is empty 
	Then Check Field "father_name" is empty 
	Then Check Field "mother_name" is empty 
	Then Check Field "nri" is not checked 
	Then Check Field "name" is enabled and visible 
	Then Check Field "gender" is enabled and visible 
	Then Check Field "date_of_birth" is enabled and visible 
	Then Check Field "id_card_type" is enabled and visible 
	Then Check Field "id_card_number" is enabled and visible 
	Then Check Field "father_name" is enabled and visible 
	Then Check Field "mother_name" is enabled and visible 
	Then Check Checkbox Field "nri" is enabled and visible 
	Then Check Field "save_button" is enabled and visible 
	
	Then Take Screen shot as "personal.png" 
	
Scenario: 
	Register NRI user and check if relevent fields exists and visible and enabled 
	When Open Registration page 
	And Register new User as "userOne" 
		|name        | email               | password  | confirm_password |  nri   | country_code |  mobile_number |
		|Ravi Sharma | ping2ravi@gmail.com | password  | password         |  true  | UK(44)       |  9876543210    |
	And Open Login page 
	And Enter Login Email/Mobile Number "ping2ravi@gmail.com" 
	And Enter Login Password "password" 
	And Click on LoginPage Login Button 
	And Take Screen shot as "login_done.png" 
	And Check that home page has been opened 
	And Take Screen shot as "home_open.png" 
	When Open Personal Detail page 
	Then Check Field exists "name" 
	And Check Field exists "gender" 
	And Check Field exists "date_of_birth" 
	And Check Field exists "id_card_type" 
	And Check Field exists "id_card_number" 
	And Check Field exists "father_name" 
	And Check Field exists "mother_name" 
	And Check Field exists "mother_name" 
	And Check Field exists "nri" 
	And Check Field do not exists "living_state" 
	And Check Field do not exists "living_district" 
	And Check Field do not exists "living_parliament_constituency" 
	And Check Field do not exists "living_assembly_constituency" 
	And Check Field exists "voting_state" 
	And Check Field exists "voting_district" 
	And Check Field exists "voting_parliament_constituency" 
	And Check Field exists "voting_assembly_constituency" 
	And Check Field exists "country" 
	And Check Field exists "country_region" 
	And Check Field exists "country_region_area" 
	And Check Field exists "save_button" 
	Then Check Field "name" has "Ravi Sharma" 
	Then Check Nothing selected in combo box "gender" 
	Then Check Date Field "date_of_birth" is empty 
	Then Check Nothing selected in combo box "id_card_type" 
	And Check Nothing selected in combo box "voting_state" 
	And Check Nothing selected in combo box "voting_district" 
	And Check Nothing selected in combo box "voting_parliament_constituency" 
	And Check Nothing selected in combo box "voting_assembly_constituency" 
	And Check Nothing selected in combo box "country_region" 
	And Check Nothing selected in combo box "country_region_area" 
	Then Check Field "id_card_number" is empty 
	Then Check Field "father_name" is empty 
	Then Check Field "mother_name" is empty 
	Then Check Field "nri" is checked 
	Then Check Field "name" is enabled and visible 
	Then Check Field "gender" is enabled and visible 
	Then Check Field "date_of_birth" is enabled and visible 
	Then Check Field "id_card_type" is enabled and visible 
	Then Check Field "id_card_number" is enabled and visible 
	Then Check Field "father_name" is enabled and visible 
	Then Check Field "mother_name" is enabled and visible 
	Then Check Checkbox Field "nri" is enabled and visible 
	Then Check Field "save_button" is enabled and visible 
	
	Then Take Screen shot as "personal.png" 
	

Scenario: Register indian user and Update Personal Details 
	When Open Registration page 
	And Register new User as "userOne" 
		|name        | email               | password  | confirm_password |  mobile_number |  nri   |
		|Ravi		 | ping2ravi@gmail.com | password  | password         |  9876543210    |  false |
	And Open Login page 
	And Enter Login Email/Mobile Number "ping2ravi@gmail.com" 
	And Enter Login Password "password" 
	And Click on LoginPage Login Button 
	And Take Screen shot as "login_done.png" 
	And Check that home page has been opened 
	And Take Screen shot as "home_open.png" 
	When Open Personal Detail page 
	Then Enter Personal Detail User Name "Ravi Sharma" 
	And Select Personal Detail Gender "Male" 
	And Enter Personal Detail Date of Birth "01-Jan-1971" 
	And Select Personal Detail ID Card Type "Driving License" 
	And Enter Personal Detail ID Card Number "AX123456789" 
	And Enter Personal Detail Father Name "Mr. Sharma" 
	And Enter Personal Detail Mother Name "Mrs. Sharma" 
	And Select Personal Detail Living State "Haryana"
	And Select Personal Detail Living District "Palwal"
	And Select Personal Detail Living PC "Faridabad"
	And Select Personal Detail Living AC "Palwal"
	And Select Personal Detail Voting State "Delhi"
	And Select Personal Detail Voting District "Central Delhi"
	And Select Personal Detail Voting PC "Cannuaght Place"
	And Select Personal Detail Voting AC "Chandani Chowk"
	And Click on Personal Detail Save Button
	Then Take Screen shot as "personal.png" 
	And Check one user exists 
		| name          | creationType    |  nri   | superAdmin | donor | member  | fatherName | motherName | gender | identityType  | identityNumber |
		| Ravi Sharma   | SelfServiceUser | false  | false      | false | false   |Mr. Sharma  | Mrs. Sharma| Male   |Driving License| AX123456789    |

@current 		
Scenario: 
	Register NRI user and Update Personal Details
	When Open Registration page 
	And Register new User as "userOne" 
		|name        | email               | password  | confirm_password |  nri   | country_code |  mobile_number |
		|Ravi        | ping2ravi@gmail.com | password  | password         |  true  | UK(44)       |  9876543210    |
	And Open Login page 
	And Enter Login Email/Mobile Number "ping2ravi@gmail.com" 
	And Enter Login Password "password" 
	And Click on LoginPage Login Button 
	And Take Screen shot as "login_done.png" 
	And Check that home page has been opened 
	And Take Screen shot as "home_open.png" 
	When Open Personal Detail page 
	Then Enter Personal Detail User Name "Ravi Sharma" 
	And Select Personal Detail Gender "Male" 
	And Enter Personal Detail Date of Birth "01-Jan-1971" 
	And Select Personal Detail ID Card Type "Driving License" 
	And Enter Personal Detail ID Card Number "AX123456789" 
	And Enter Personal Detail Father Name "Mr. Sharma" 
	And Enter Personal Detail Mother Name "Mrs. Sharma" 
	And Select Personal Detail Country "UK"
	And Select Personal Detail Country Region "England"
	And Select Personal Detail Country Region Area "London"
	And Select Personal Detail Voting State "Delhi"
	And Select Personal Detail Voting District "Central Delhi"
	And Select Personal Detail Voting PC "Cannuaght Place"
	And Select Personal Detail Voting AC "Chandani Chowk"
	And Click on Personal Detail Save Button
	Then Take Screen shot as "personal.png" 
	And Check one user exists 
		| name          | creationType    |  nri   | superAdmin | donor | member  | fatherName | motherName | gender | identityType  | identityNumber |
		| Ravi Sharma   | SelfServiceUser | true   | false      | false | false   |Mr. Sharma  | Mrs. Sharma| Male   |Driving License| AX123456789    |
		
	
	Then Take Screen shot as "personal.png" 		