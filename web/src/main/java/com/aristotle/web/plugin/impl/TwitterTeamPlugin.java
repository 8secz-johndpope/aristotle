package com.aristotle.web.plugin.impl;

import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.TwitterAccount;
import com.aristotle.core.persistance.TwitterTeam;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.TwitterService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TwitterTeamPlugin extends AbstractDataPlugin {

    @Autowired
    private TwitterService twitterService;

    private static final String TEAM_URL_PARAMETER = "teamUrl";



    public TwitterTeamPlugin() {
    }

    public TwitterTeamPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            clearCookie(httpServletRequest, httpServletResponse);
            Long twitterAccountId = getLongCookie(httpServletRequest, "ti", null);
            logger.info("twitterAccountId = {}" , twitterAccountId);
            String teamUrl = getStringParameterFromPathOrParams(httpServletRequest, TEAM_URL_PARAMETER);
            logger.info("teamUrl = {}" , teamUrl);
            if (teamUrl == null) {
                context.addProperty(name, "{\"error\":\"No TeamUrl Specified\"}");
                return;
            }
            JsonObject jsonObject = new JsonObject();
            TwitterTeam twitterTeam = twitterService.getTwitterTeamByUrl(teamUrl);
            if (twitterTeam == null) {
            	logger.info("Invalid Team : {}" , teamUrl);
                jsonObject.addProperty("message", "Invalid Team");
            } else {
            	logger.info("Checking if twitterAccountId {} already part of the team : {}" , twitterAccountId , twitterTeam.getId());
                boolean userPartOfTheTeam = twitterService.isTwitterAccountPartOfTwitterTeam(twitterAccountId, twitterTeam.getId());
                jsonObject.addProperty("userPartOfTheTeam", userPartOfTheTeam);
                jsonObject.addProperty("teamLoginUrl", "/twitter/team/" + teamUrl);
                jsonObject.addProperty("teamUrl", teamUrl);
                jsonObject.addProperty("global", twitterTeam.isGlobal());
                if (userPartOfTheTeam) {
                    // add existing team users in context
                    addTeamUsers(jsonObject, twitterTeam);
                }
            }
            addTwitterTeamSourceAccount(jsonObject, twitterTeam);
            addTwitterTeams(jsonObject, twitterTeam);
            context.add(name, jsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void clearCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
    	String param = httpServletRequest.getParameter("forget");
    	if(param != null){
    		Cookie cookie = new Cookie("ti", "0");
        	cookie.setMaxAge(0);
        	cookie.setPath("/");
        	httpServletResponse.addCookie(cookie);
    	}
    	
    }
    private Long getLongCookie(HttpServletRequest httpServletRequest, String cookieName, Long defaultValue){
    	Cookie[] cookies = httpServletRequest.getCookies();
    	
    	for(Cookie oneCookie : cookies){
    		if(oneCookie.getName().equals(cookieName)){
    			try{
    				return Long.parseLong(oneCookie.getValue());
    			}
    			catch(Exception ex){
    					
    			}
    		}
    	}
    	return defaultValue;
    }
    private String getStringCookie(HttpServletRequest httpServletRequest, String cookieName, String defaultValue){
    	Cookie[] cookies = httpServletRequest.getCookies();
    	for(Cookie oneCookie : cookies){
    		if(oneCookie.getName().equals(cookieName)){
    			try{
    				return oneCookie.getValue();
    			}
    			catch(Exception ex){
    					
    			}
    		}
    	}
    	return defaultValue;
    }
    /*
    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
            logger.info("user = {}" , user);
            String teamUrl = getStringParameterFromPathOrParams(httpServletRequest, TEAM_URL_PARAMETER);
            logger.info("teamUrl = {}" , teamUrl);
            if (teamUrl == null) {
                context.addProperty(name, "{\"error\":\"No TeamUrl Specified\"}");
                return;
            }
            JsonObject jsonObject = new JsonObject();
            TwitterTeam twitterTeam = twitterService.getTwitterTeamByUrl(teamUrl);
            if (twitterTeam == null) {
            	logger.info("Invalid Team : {}" , teamUrl);
                jsonObject.addProperty("message", "Invalid Team");
            } else if (user == null) {
                jsonObject.addProperty("message", "Not logged In");
            } else {
            	logger.info("Checking if user already part of the team : {}, {}" , user.getId() , twitterTeam.getId());
                boolean userPartOfTheTeam = twitterService.isUserPartOfTwitterTeam(user.getId(), twitterTeam.getId());
                jsonObject.addProperty("userPartOfTheTeam", userPartOfTheTeam);
                jsonObject.addProperty("teamLoginUrl", "/twitter/team/" + teamUrl);
                jsonObject.addProperty("teamUrl", teamUrl);
                jsonObject.addProperty("global", twitterTeam.isGlobal());
                if (userPartOfTheTeam) {
                    // add existing team users in context
                    addTeamUsers(jsonObject, twitterTeam);
                }
            }
            addTwitterTeamSourceAccount(jsonObject, twitterTeam);
            addTwitterTeams(jsonObject, twitterTeam);
            context.add(name, jsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    */

    private void addTwitterTeamSourceAccount(JsonObject jsonObject, TwitterTeam currentTwitterTeam) throws AppException {
        if (currentTwitterTeam.isGlobal()) {
            return;
        }
        Set<TwitterAccount> twitterAccounts = currentTwitterTeam.getTweetSource();
        if (twitterAccounts == null || twitterAccounts.isEmpty()) {
            return;
        }
        JsonArray jsonArray = new JsonArray();
        JsonObject oneTwitterAccountJsonObject;
        for (TwitterAccount oneTwitterAccount : twitterAccounts) {
            oneTwitterAccountJsonObject = new JsonObject();
            oneTwitterAccountJsonObject.addProperty("imageUrl", oneTwitterAccount.getImageUrl());
            oneTwitterAccountJsonObject.addProperty("screenName", oneTwitterAccount.getScreenName());
            jsonArray.add(oneTwitterAccountJsonObject);
        }
        jsonObject.add("sourceMembers", jsonArray);

    }
    private void addTwitterTeams(JsonObject jsonObject, TwitterTeam currentTwitterTeam) throws AppException {
        List<TwitterTeam> twitterTeams = twitterService.getAllTwitterTeams();
        JsonArray jsonArray = new JsonArray();
        JsonObject oneTwitterTeamJsonObject;
        for (TwitterTeam oneTwitterTeam : twitterTeams) {
            if (oneTwitterTeam.getId().equals(currentTwitterTeam.getId())) {
                continue;
            }
            oneTwitterTeamJsonObject = new JsonObject();
            oneTwitterTeamJsonObject.addProperty("url", oneTwitterTeam.getUrl());
            oneTwitterTeamJsonObject.addProperty("name", oneTwitterTeam.getName());
            jsonArray.add(oneTwitterTeamJsonObject);
        }
        jsonObject.add("twitterTeams", jsonArray);
        
    }
    private void addTeamUsers(JsonObject jsonObject, TwitterTeam twitterTeam) {

        Set<TwitterAccount> twitterAccounts = twitterTeam.getTweetTweeters();
        if (twitterAccounts == null || twitterAccounts.isEmpty()) {
            return;
        }
        JsonArray jsonArray = new JsonArray();
        JsonObject oneTwitterAccountJsonObject;
        for (TwitterAccount oneTwitterAccount : twitterAccounts) {
            oneTwitterAccountJsonObject = new JsonObject();
            oneTwitterAccountJsonObject.addProperty("imageUrl", oneTwitterAccount.getImageUrl());
            oneTwitterAccountJsonObject.addProperty("screenName", oneTwitterAccount.getScreenName());
            jsonArray.add(oneTwitterAccountJsonObject);
        }
        jsonObject.add("teamMembers", jsonArray);
    }
}
/*
 * <html>
<head>
[[META_TAGS]] [[RESOURCES]]


<script>
    $(function() {
        $( "#tabs" ).tabs();
    });
    </script>

<style type="text/css">
.ui-tabs .ui-tabs-hide {
	position: absolute !important;
	left: -10000px !important;
	display: block !important;
}
</style>
 <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>

</head>
<body>


	[[TOPMENU]]
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<!--<script src="../js/jquery-1.9.1.min.js"></script>-->
	<!--<script src="bootstrap.min.js"></script>-->
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<!--<script src="ie10-viewport-bug-workaround.js"></script>-->

	<!-- jssor slider scripts-->
	<!-- use jssor.js + jssor.slider.js instead for development -->
	<!-- jssor.slider.mini.js = (jssor.js + jssor.slider.js) -->

	<div class="maincontainer">
		<div class="lftContnr col-xs-12 col-sm-12 col-md-8">
			<div>
				<div class="hanger relPostn">
					<h3 class="fltlft hangerHdng mrgnpdng absPostn">Join Twitter Team</h3>
				</div>
				<div class="clear"></div>
			</div>
			<div class="contentDiv newsPad">
				<div id="tabs">
					<ul>
					   <li><a href="#tabs-1">Team</a></li>
						<li><a href="#tabs-2">Team Members</a></li>
						<li><a href="#tabs-3">Other Teams</a></li>
					</ul>
					<div id="tabs-1">
						<div class="newsCont">
							{{#if TwitterTeamPlugin.userPartOfTheTeam}}
							<h3 class="">You are already part of the team, relax and enjoy auto retweets</h3>
							<h5>You can Join other teams by going into "other Teams" tab</h5>


                                <br><br>
                                {{#if TwitterTeamPlugin.global}}
                                       <h4>By joining this Team you agree to retweet few handpicked tweets from Swaraj Abhiyan</h4>
                                {{else}}
                                       <h4>By joining this Team you agree to retweet all tweets from following account</h4>
                                       <table>
                                         {{#each TwitterTeamPlugin.sourceMembers}}
                                        <tr>
                                                <td><a href="https://twitter.com/{{screenName}}"><img src="{{imageUrl}}" /></a></td>
                                                <td><a href="https://twitter.com/{{screenName}}" class="twitter-follow-button" data-show-count="false" data-size="large">Follow {{screenName}}</a></td>
                                        </tr>
                                       {{/each}}
                                       </table>
                                      <br>
                                {{/if}}
							<br> <br>
							{{else}}
                                                  {{#if LoggedInUserPlugin.loggedIn}}
							<h3 class="">Please Login Below to become part of the team</h3>
							<a href="{{TwitterTeamPlugin.teamLoginUrl}}?redirect=/socialmedia/twitter/team/{{TwitterTeamPlugin.teamUrl}}"><img
								src="http://static.swarajabhiyan.org/templates/prod/1/images/twitter01.png" /></a>
								<br>
								{{#if TwitterTeamPlugin.global}}
                                                                   <h4>By joining this Team you agree to retweet few handpicked tweets from Swaraj Abhiyan</h4>
                                                                 {{else}}
                                                                    <h4>By joining this Team you agree to retweet all tweets from following account</h4>
                                                                 <table>
                                                                    {{#each TwitterTeamPlugin.sourceMembers}}
                                                                    <tr>
                                                                      <td><a href="https://twitter.com/{{screenName}}"><img src="{{imageUrl}}" /></a></td>
                                                                      <td><a href="https://twitter.com/{{screenName}}" class="twitter-follow-button" data-show-count="false" data-size="large">Follow {{screenName}}</a></td>
                                                                   </tr>
                                                                   {{/each}}
                                                                 </table>
                                <br>
                                                                 {{/if}} 
							 {{else}}
							      <h3 class="">Please Login using your Swaraj Abhiyan Account</h3>
							{{/if}} 
							
							{{/if}}

						</div>
					</div>
					<div id="tabs-2">
					{{#if TwitterTeamPlugin.userPartOfTheTeam}}
                            <br> 
                            <h4>Meet your team member, please follow each other</h4>
                            <table>
                                {{#each TwitterTeamPlugin.teamMembers}}
                                <tr>
                                    <td><a href="https://twitter.com/{{screenName}}"><img src="{{imageUrl}}" /></a></td>
                                    <td><a href="https://twitter.com/{{screenName}}" class="twitter-follow-button" data-show-count="false" data-size="large">Follow {{screenName}}</a></td>
                                </tr>
                                {{/each}}
                            </table>
                     {{else}}
                     <h4>Only team members can view each others</h4>
                            
                     {{/if}}
					</div>
					<div id="tabs-3">
					   <table>

                                {{#each TwitterTeamPlugin.twitterTeams}}
                                <tr>
                                    <td><a href="http://www.swarajabhiyan.org/socialmedia/twitter/team/{{url}}">{{name}}</a></td>
                                </tr>
                                {{/each}}
                            </table>
					</div>
				</div>
				<!--------changed by soumya------->


			</div>
			<!--------changed by soumya end------->
		</div>

		[[RIGHT_SIDE]]
		<div class="clear"></div>
	</div>
	[[FOOTER]]

</body>
</html>
*/
