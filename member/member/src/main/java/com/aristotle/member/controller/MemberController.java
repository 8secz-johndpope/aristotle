package com.aristotle.member.controller;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MemberController {
    @Autowired
    private UserService userService;

    @RequestMapping("/sc/admin/delete/member")
    @ResponseBody
    public List<String> deleteMemberMembers(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {

        try{
            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
            if(user == null){
                throw new AppException("User not logged In");
            }
            Set<Long> allowedUsers = new HashSet<>();
            allowedUsers.add(2L);//Ravi
            allowedUsers.add(38L);//Neeraj
            if(!allowedUsers.contains(user.getId())){
                throw new AppException(user.getName() +" you are not allowed to do this operation");
            }

            String memberId = httpServletRequest.getParameter("mid");
            if(StringUtils.isEmpty(memberId)){
                return Lists.newArrayList("mid must be provided");
            }
            List<String> status = userService.deleteUserByMemberId(memberId);
            return status;
        }catch(Throwable ex){
            ex.printStackTrace();
            return Lists.newArrayList(ex.getMessage());
        }

    }
}
