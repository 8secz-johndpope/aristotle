<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">
<head>

<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script>
function validateCheckBox() {
	if ($('#declaration').prop('checked')) {
        return true;
    }else{
        alert("Please accept declaration by selecting checkbox, id");
        return false;
    }
};

</script>

<style>
#footer {
	background-color: FFFBDB;
	color: Black;
	clear: both;
	text-align: Left;
	padding: 40px;
}

.button {
	background-color: #B41515;
	border: none;
	color: white;
	padding: 15px 160px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 20px;
	margin: 10px 12px;
	cursor: pointer;
}

.button3 {
	border-radius: 8px;
}

#header {
	background-color: black;
	border: none;
	color: white;
	padding: 5px 30px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 30px;
	margin: auto;
}
</style>

</head>
<body>
	<div class="container">
		<div class="panel panel-default">
			<div class="col-sm-12">

				<div>
					<img src="https://s3-us-west-2.amazonaws.com/static.swarajabhiyan.org/templates/prod/1/images/ds_pic_02.jpg" alt="HTML5 Icon" width="100%" height="128" />
				</div>

				<div>
					<center>
						<h1>ONLINE VOLUNTARY CONTRIBUTION</H1>
					</center>
				</div>

				<hr>
				<br>

				<div id="header">Declaration</div>

				<br> <br>

				<div>
					<input type="checkbox" name="Declare" id="declaration">&nbsp; <strong> I declare that I am a citizen of India, above 18 years of age and am making this contribution
						voluntarily from my own legally earned personal funds. I also permit Swaraj Abhiyan to communicate with me using my phone and/or email specified above. The particulars and
						statements made above are correct to my knowledge and nothing material has been concealed or withheld therefrom.<strong><br>
				</div>

				<br> <br>


				<center>
				<a href="https://www.instamojo.com/SwarajAbhiyan/donations-for-swaraj-abhiyan/?intent=buy" onclick="return validateCheckBox();">
					<button class="button button3">Contribute Now</button>
				</a>
				</center>
				<br> <br>

				<center>
					<div>
						<img src="https://s3-us-west-2.amazonaws.com/static.swarajabhiyan.org/templates/prod/1/images/pic_01.jpg" alt="HTML5 Icon" width="75%" height="50%" />
					</div>
				</center>

				<br>
				<hr>

				<div id="footer">Swaraj Abhiyan has neither authorised nor receives or invites any voluntary contribution in foreign currency or from any person who is not a citizen of
					India. No FCRA permission or registration is obtained or held by Swaraj Abhiyan. Citizens of India including Non Resident Indians holding valid Indian passports can alone make
					valid voluntary contribution to Swaraj Abhiyan in Indian Currency only.</div>

				<br>

				<div>
					<center>
						<h3>Swaraj Abhiyan Donation Powered By</H3>
					</center>
				</div>
				<div>
					<center>
						<img src="https://s3-us-west-2.amazonaws.com/static.swarajabhiyan.org/templates/prod/1/images/ds_logo.png" alt="logo" width="20%" height="10%">
					</center>
				</div>

			</div>
		</div>
	</div>
</body>

</html>




<html>
<head>

</head>
<body>

</body>
</html>