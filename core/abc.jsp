<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="http://static.swarajabhiyan.org/templates/dev/1/css/style.css" />
<link rel="stylesheet" type="text/css" href="http://static.swarajabhiyan.org/templates/dev/1/css/responsive.css" />
<link rel="stylesheet" type="text/css" href="http://static.swarajabhiyan.org/templates/dev/1/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="http://static.swarajabhiyan.org/templates/dev/1/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="http://static.swarajabhiyan.org/templates/dev/1/css/font-awesome.min.css" />

<script> Handlebars.registerHelper('trimString', function(passedString) {      
 
 if(passedString.length <= 300){        return passedString;     }        var theString = passedString.substring(0,300);      return new Handlebars.SafeString(theString)     }); </script>
<script id="entry-template" type="text/x-handlebars-template">

 </script>

<header>
	<div class="hdrContnr center relPostn">
		<a class="logo fltlft" href="javascript:void(0)"><img
			src="http://static.swarajabhiyan.org/templates/dev/1/images/logo.png" height="71" width="124" /></a>
		<div class="hdrToolContnr dispTable center absPostn">
			<span class="fltlft">
				<form action="javascript:void(0)" id="formsearch" method="get">
					<fieldset class='relPostn'>
						<legend class='absPostn'>Newsletter Subscription</legend>
						<input type="text" class="txtSearch" placeholder='Provide your email ID' /> <input type="submit"
							class="btnSubmit btnBg btnStyle relPostn" value="Submit" />
					</fieldset>
				</form>
			</span> <span class="fltrht"> <input type="button" class="btnSignUp btnBg btnStyle" value="Sign Up" /> <input
				type="button" class="btnSignIn btnStyle relPostn" value="Sign In" />
			</span>
			<div class="clear"></div>
			<ul class="ulMainMenu center absPostn dispTable">

				{{#each TopMenu.menus}}
				<li><a href="{{url}}" class="{{class}}"> <span>{{title}}</span></a> 
				{{#if submenu}}
					<ul class="ulSub mrgnpdng">
						{{#each submenu}}
						<li><a href="{{url}}">{{title}}</a></li>
						{{//each}}
					</ul> 
				{{/if}}
				</li> 
				{{/each}}


				<li><a href="javascript:void(0)" class="home"> <span>Home</span>
				</a></li>
				<li><a href="javascript:void(0)" class="group"> <span>About Us</span>
				</a>
					<ul class="ulSub mrgnpdng">
						<li><a href="javascript:void(0)">Leadership</a></li>
						<li><a href="javascript:void(0)">Our Mission</a></li>
						<li><a href="javascript:void(0)">Public Forum</a></li>
					</ul></li>
				<li><a href="javascript:void(0)" class="event"> <span>Events</span>
				</a></li>
				<li><a href="javascript:void(0)" class="liveTv"> <span>Live TV</span>
				</a></li>
				<li><a href="javascript:void(0)" class="contact"> <span>Contact Us</span>
				</a></li>
				<div class="clear"></div>
			</ul>
		</div>
		<a class="mapIco fltrht" href="javascript:void(0)"> <img
			src="http://static.swarajabhiyan.org/templates/dev/1/images/map-s.png" height="55" width="98" />
			<div>Choose State</div>
		</a>
		<div class="clear"></div>
		<div class="clear"></div>
	</div>
</header>
<div class="divGallery">
	<img src="http://static.swarajabhiyan.org/templates/dev/1/images/img-1.jpg" style="max-width: 100%; width: 100%;" />

</div>
<div class="maincontainer center">
	<div class="lftContnr fltlft">
		<div>
			<div class="hanger relPostn">
				<h3 class="fltlft hangerHdng mrgnpdng absPostn">News</h3>
				<span class="fltrht nxtPrvCont relPostn"> <a href="javascript:void(0)" class="fltlft prvIco"></a> <a
					href="javascript:void(0)" class="fltlft nxtIco"></a>
				</span>
			</div>
			<div class="clear"></div>
		</div>
		<div class="contentDiv" style='padding-top: 35px;'>
			{{#each NewsList}}
			<div class="fltlft divtask">
				<h4 class='mrgnpdng'>{{title}}</h4>
				<br />
				<p class='paraNews'>{{trimString contentSummary}}</p>
			</div>
			{{/each}}
			<div class="clear"></div>
		</div>
		<div class="contentDiv mrgnTop">
			<div class="fltlft divtask">
				<h4 class="hdngBlogs">
					<div class="hdngSpn">
						<span>Blogs</span>
						<button type="button" class="fltrht btnviewAll">Read all</button>
					</div>
				</h4>
				<ul class="ulBlogs mrgnpdng">
					<li>
						<p>
							Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet... <a
								href="javascript:void(0)" class='fullEvent'>Read More</a>
						</p>
					</li>
					<li>
						<p>
							Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet... <a
								href="javascript:void(0)" class='fullEvent'>Read More</a>
						</p>
					</li>
					<li>
						<p>
							Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet... <a
								href="javascript:void(0)" class='fullEvent'>Read More</a>
						</p>
					</li>
				</ul>
			</div>
			<div class="fltlft divtask relPostn" style='min-height: 337px !important;'>
				<h4 class="hdngPolls">
					<div class="hdngSpn">
						<span>Polls</span>
						<button type="button" class="fltrht btnviewAll">View all</button>
					</div>
				</h4>
				<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet?</p>
				<div class="selPolls">
					<label> <input type="radio" name="polls" /> Lorem ipsum dolor
					</label> <label> <input type="radio" name="polls" /> Molesite consequat
					</label> <label> <input type="radio" name="polls" /> Consecteture adipiscing elit
					</label>
				</div>
				<br />
				<p class='paraCasturvote absPostn'>
					Please <a href="javascript:void(0)" class="linkSignin">Sign in</a> to vote
					<button type="button" class="voteurCast fltrht">Cast your Vote</button>
				</p>
				<div class="clear"></div>
			</div>
			<div class="clear"></div>
		</div>
		<div class="contentDiv mrgnTop">
			<div class="divtask divWid">
				<h4 class="hdngVideos">
					<div class="hdngSpn">
						<span>Videos</span>
						<button type="button" class="fltrht btnviewAll">View all</button>
					</div>
				</h4>
				<ul class="ulVideo mrgnpdng">
					<li>
						<div>
							<embed width="100%" height="122" src="https://www.youtube.com/embed/6ay54pPpHh8" />
						</div>
						<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit</p>
						<button type="button" class="voteCast center">Watch Now</button>
					</li>
					<li>
						<div>
							<embed width="100%" height="122" src="https://www.youtube.com/embed/owWmxA_3Sg8" />
						</div>
						<p>Lorem ipsum dolor sit amet, consectetuer adipiscing</p>
						<button type="button" class="voteCast fltrht">Watch Now</button>
					</li>
					<li>
						<div>
							<embed width="100%" height="122" src="https://www.youtube.com/embed/1W6OPG0QDbo" />
						</div>
						<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit,?</p>
						<button type="button" class="voteCast fltrht">Watch Now</button>
					</li>
					<div class="clear"></div>
				</ul>
			</div>
		</div>
		<div class="mrgnTop">
			<div class="mrgnTop">
				<div class="hanger relPostn">
					<h3 class="fltlft upcomEvent mrgnpdng absPostn">Upcoming Events</h3>
					<span class="fltrht nxtPrvCont relPostn"> <a href="javascript:void(0)" class="fltlft prvIco"></a> <a
						href="javascript:void(0)" class="fltlft nxtIco"></a>
					</span>
				</div>
			</div>
		</div>
		<div class="clear"></div>
		<div class="contentDiv">
			<div class="divtask divWid">
				<ul class="ulVideo ulEvent mrgnpdng">
					<li>
						<div class="fltlft eventLeft">
							<div class="calMonth">MAY</div>
							<div class="calDate">17</div>
						</div>
						<div class="fltrht evntRight">
							<p>
								Jayalalitha's acquittal: A Travesty of Justice <br />-Swaraj Abhiyan Press Note
							</p>
							<p class="time mrgnpdng">@ 8:00 am - 11:00 am</p>
							<a href="javascript:void(0)" class="fullEvent fltrht">Full View</a>
							<div class='clear'></div>
						</div>
						<div class="clear"></div>
					</li>
					<li>
						<div class="fltlft eventLeft">
							<div class="calMonth">MAY</div>
							<div class="calDate">24</div>
						</div>
						<div class="fltrht evntRight">
							<p>
								Jayalalitha's acquittal: A Travesty of Justice <br />-Swaraj Abhiyan Press Note
							</p>
							<p class="time mrgnpdng">@ 8:00 am - 11:00 am</p>
							<a href="javascript:void(0)" class="fullEvent fltrht">Full View</a>
							<div class='clear'></div>
						</div>
						<div class="clear"></div>
					</li>
					<li>
						<div class="fltlft eventLeft">
							<div class="calMonth">MAY</div>
							<div class="calDate">06</div>
						</div>
						<div class="fltrht evntRight">
							<p>
								Jayalalitha's acquittal: A Travesty of Justice <br />-Swaraj Abhiyan Press Note
							</p>
							<p class="time mrgnpdng">@ 8:00 am - 11:00 am</p>
							<a href="javascript:void(0)" class="fullEvent fltrht">Full View</a>
							<div class='clear'></div>
						</div>
						<div class="clear"></div>
					</li>
					<div class="clear"></div>
				</ul>
			</div>
		</div>
	</div>
	<div class="rhtContnr fltrht">
		<ul class="rhtButn mrgnpdng">
			<li><a href='javascript:void(0)'>
					<div class='fltlft'>
						<h5 class='mrgnpdng'>To Spr...e..ad</h5>
						<h3 class='mrgnpdng'>JOIN US NOW</h3>
					</div> <span class='fltrht rhtArow fa fa-angle-right'></span>
					<div class='clear'></div>
			</a></li>
			<li><a href='javascript:void(0)'>
					<div class='fltlft'>
						<h5 class='mrgnpdng'>To discuss & share ideas</h5>
						<h3 class='mrgnpdng'>GO TO FORUMS</h3>
					</div> <span class='fltrht rhtArow fa fa-angle-right'></span>
					<div class='clear'></div>
			</a></li>
			<li><a href='javascript:void(0)'>
					<div class='fltlft'>
						<h5 class='mrgnpdng'>click here</h5>
						<h3 class='mrgnpdng'>DONATE NOW</h3>
					</div> <span class='fltrht rhtArow fa fa-angle-right'></span>
					<div class='clear'></div>
			</a></li>
			<li><a href='javascript:void(0)'>
					<div class='fltlft'>
						<h5 class='mrgnpdng'>See a list of</h5>
						<h3 class='mrgnpdng'>SWARAJ CENTERS</h3>
					</div> <span class='fltrht rhtArow fa fa-angle-right'></span>
					<div class='clear'></div>
			</a></li>
			<li><a href='javascript:void(0)'>
					<div class='fltlft'>
						<h5 class='mrgnpdng'>Participate in</h5>
						<h3 class='mrgnpdng'>AGENDA 4 INDIA</h3>
					</div> <span class='fltrht rhtArow fa fa-angle-right'></span>
					<div class='clear'></div>
			</a></li>
		</ul>
		{{#if fbboxplugin}}
		<!--==================Facebook=====================-->
		<div class='socmrgnTop'>
			<div class="hanger relPostn">
				<h3 class="fltlft hangerHdng mrgnpdng absPostn">Facebook</h3>
			</div>
			<div class="clear"></div>
		</div>
		<div class="fbBlock">{{{fbboxplugin}}}</div>
		{{/if}} {{#if fbboxplugin1}}
		<!--==================Twitter=====================-->
		<div class='socmrgnTop'>
			<div class="hanger relPostn">
				<h3 class="fltlft hangerHdng mrgnpdng absPostn">Twitter</h3>
			</div>
			<div class="clear"></div>
		</div>
		<div class='twrBlock'>{{{twitterboxplugin}}}</div>
		{{/if}}
	</div>

	<div class="clear"></div>
</div>

<br />
<br />
<!--======================================Footer Part=================================================-->
<div class="footerContainer">
	<div class="ftrContnr center">
		<ul class="ulFtrMain mrgnpdng">
			<li>
				<ul class="ulftrMenu mrgnpdng">
					<li><h3>The Party</h3></li>
					<li><a href="javascript:void(0)">About the party</a></li>
					<li><a href="javascript:void(0)">Leadership</a></li>
					<li><a href="javascript:void(0)">View Current Events</a></li>
					<li><a href="javascript:void(0)">Live TV</a></li>
					<li><a href="javascript:void(0)">Our Mission</a></li>
				</ul>
			</li>
			<li>
				<ul class="ulftrMenu">
					<li><h3>Media Resources</h3></li>
					<li><a href="javascript:void(0)">Press Releases</a></li>
					<li><a href="javascript:void(0)">Speeches</a></li>
					<li><a href="javascript:void(0)">Video Releases</a></li>
					<li><a href="javascript:void(0)">Public Forum</a></li>
					<li><a href="javascript:void(0)">Photo Gallery</a></li>
				</ul>
			</li>
			<li>
				<ul class="ulftrMenu">
					<li><h3>Get Involved</h3></li>
					<li><a href="javascript:void(0)">Join the party</a></li>
					<li><a href="javascript:void(0)">Donate Now</a></li>
					<li><h3 class='mrgnpdng'>Contact Us</h3></li>
					<li><a href="javascript:void(0)">A-189, Sec-43, Noida, UP</a></li>
					<li><a href="javascript:void(0)">+917210222333</a></li>
				</ul>
			</li>
			<li>
				<ul class="ulftrMenu ulSocial">
					<li><a href="javascript:void(0)" class='fb'> Join on Facebook <span>/swarajabhiyan</span>
					</a></li>
					<li><a href="javascript:void(0)" class='twr'> Follow on Twitter <span>/swaraj_abhiyan</span>
					</a></li>
					<li><a href="javascript:void(0)" class='utube'> Suscribe on Youtube <span>SWARAJ ABHIYAN</span>
					</a></li>
				</ul>
			</li>
			<div class='clear'></div>
		</ul>
		<p class='copyRight'>&copy; Swaraj Abhiyan. All Rights Reserved</p>
	</div>
</div>
