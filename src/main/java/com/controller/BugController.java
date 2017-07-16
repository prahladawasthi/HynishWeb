package com.controller;

import com.login.MongoUserDetails;
import com.model.BugStatus;
import com.model.Bugs;
import com.model.User;
import com.services.BugService;
import com.services.EmailService;
import com.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@PropertySources(value = {@PropertySource("classpath:messages.properties")})
@RequestMapping("/bug")
public class BugController {

    private BugService bugService;
    private EmailService emailService;
    private UserService userService;

    @Autowired
    public BugController(BugService bugService, EmailService emailService, UserService userService) {
        this.bugService = bugService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView raiseBug(ModelAndView modelAndView) {
        modelAndView.addObject("bugs", new Bugs());
        modelAndView.setViewName("bug/addBug");
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView raiseBug(@Valid Bugs bugs, BindingResult bindingResult, ModelAndView modelAndView) {

        if (!bindingResult.hasErrors()) {

            if (!SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal().equals("anonymousUser")) {
                MongoUserDetails loggedInUser = (MongoUserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                bugs.setBugRaisedByEmail(loggedInUser.getEmail());
            }

            bugs.setStatus(BugStatus.Open.toString());
            bugService.saveBug(bugs);
            modelAndView.addObject("message", "Thank you for raising issue.");
            String message = "Thank you for raising the issue. We got this in our list and fix it very soon";

            try {
                SimpleMailMessage registrationEmail = new SimpleMailMessage();
                registrationEmail.setTo(bugs.getBugRaisedByEmail());
                registrationEmail.setSubject("Thank you for the raising the issue");
                registrationEmail.setText(message);
                registrationEmail.setFrom("noreply@domain.com");

                emailService.sendEmail(registrationEmail);

            } catch (Exception e) {
                modelAndView.addObject("emailmessage", "we were unable to send you message due to " + e.getMessage());

            }
        }
        modelAndView.addObject("bugs", new Bugs());
        modelAndView.setViewName("bug/addBug");
        return modelAndView;
    }

    @RequestMapping("/deleteBug")
    public ModelAndView deleteBug(ModelAndView modelAndView, @RequestParam String id) {
        modelAndView.addObject("deletedBug", bugService.deleteBugById(id).getBugTitle());
        modelAndView.addObject("message", "Bug has been deleted");
        modelAndView.addObject("bugList", bugService.findAllBugs());
        modelAndView.setViewName("bug/bugList");
        return modelAndView;
    }

    @RequestMapping("/bugList")
    public ModelAndView bugList(ModelAndView modelAndView) {
        modelAndView.addObject("bugList", bugService.findAllBugs());
        modelAndView.setViewName("bug/bugList");
        return modelAndView;
    }

    @RequestMapping(value = "/viewBug")
    public ModelAndView viewBug(ModelAndView modelAndView, @RequestParam String id) {
        Bugs bugs = bugService.findByID(id);
        Optional<User> optional = userService.findUserByEmail(bugs.getBugRaisedByEmail());
        if (optional.isPresent()) {
            User user = optional.get();
            modelAndView.addObject("raisedBy", user.getFirstName() + " " + user.getLastName());
        } else {
            modelAndView.addObject("raisedBy", bugs.getBugRaisedByEmail());
        }

        modelAndView.addObject("bugs", bugService.findByID(id));

        modelAndView.setViewName("bug/viewBug");
        return modelAndView;
    }
}
