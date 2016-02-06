<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="en">

<head>
<style>
#footer {
    background-color:FFFBDB;
    color:Black;
    clear:both;
    text-align:Left;
   padding:40px;         
}
</style>
</head>
<body>

<div><img src="https://s3-us-west-2.amazonaws.com/static.swarajabhiyan.org/templates/prod/1/images/ds_pic_02.jpg" alt="HTML5 Icon" width="100%" height="128"></div>


<div><center><h1>
<c:if test="${!empty paymentGatewayDonation}">
ONLINE VOLUNTARY CONTRIBUTION - TRANSACTION SUCESSFUL
</c:if>
<c:if test="${empty paymentGatewayDonation}">
ONLINE VOLUNTARY CONTRIBUTION - TRANSACTION FAILED
</c:if>

</H1></center></div>
<hr>

<div>
<Address>
<c:if test="${!empty paymentGatewayDonation}">
Transactions ID &nbsp;&nbsp;:<c:out value="${paymentGatewayDonation.id}"/><br>
Bank Correspondance ID : <c:out value="${paymentGatewayDonation.merchantReferenceNumber}"/><br>
Transaction Amount     : <fmt:formatNumber value="${paymentGatewayDonation.amount}" type="currency"/> <br>
</c:if>
</Address>
</div>

<div>
<p> We at Swaraj Abhiyan thank you for deciding to hold our hands. We strive to respect it with positive action. India must succeed for the sake of all Indians as also for ‘Vasudhaiva Kutumbakam’ in the letter and spirit of our Constitution. To make this dream a reality, let’s sing to Swaraj, together. God bless India.”
</div>
 


<div>
<p> You can share your story of support, read about other initiatives of SWARAJ ABHIYAN or Contribute again</p>
</div>


<div>
<center>
<img src="https://s3-us-west-2.amazonaws.com/static.swarajabhiyan.org/templates/prod/1/images/pic_01.jpg" alt="HTML5 Icon" width="75%" height="50%"></div>
</center>

<br>
<hr>

<div id="footer">
Swaraj Abhiyan has neither authorised nor receives or invites any voluntary contribution in foreign currency or from any person who is not a citizen of India. No FCRA permission or registration is obtained or held by Swaraj Abhiyan. Citizens of India including Non Resident Indians holding valid Indian passports can alone make valid voluntary contribution to Swaraj Abhiyan in Indian Currency only.
</div>

<br>

<div><center><h3>Swaraj Abhiyan Donation Powered By</H3></center></div>
<div><center><img src="https://s3-us-west-2.amazonaws.com/static.swarajabhiyan.org/templates/prod/1/images/ds_logo.pngp+{O_" alt="logo" width="15%" height="10%"></center></div>

</body>
</html>