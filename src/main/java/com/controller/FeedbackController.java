package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model.Feedback;
import com.services.EmailService;
import com.services.FeedService;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

	private FeedService feedService;
	private EmailService emailService;

	@Autowired
	public FeedbackController(FeedService feedService, EmailService emailService) {
		this.feedService = feedService;
		this.emailService = emailService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView feedback(ModelAndView modelAndView) {
		modelAndView.addObject("feed", new Feedback());
		modelAndView.setViewName("feedback/feed");
		return modelAndView;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView feedback(@Valid Feedback feed, BindingResult bindingResult, ModelAndView modelAndView) {

		if (!bindingResult.hasErrors()) {
			feedService.saveFeed(feed);
			modelAndView.addObject("message", "Thank you for the feedback.");
			String message = "Thank you for the feedback. /n We are in progress to upgrade this application to serve you better";

			SimpleMailMessage registrationEmail = new SimpleMailMessage();
			registrationEmail.setTo(feed.getUserEmail());
			registrationEmail.setSubject(message);
			registrationEmail.setText(message);
			registrationEmail.setFrom("noreply@domain.com");

			emailService.sendEmail(registrationEmail);

		}
		modelAndView.addObject("feed", new Feedback());
		modelAndView.setViewName("feedback/feed");
		return modelAndView;
	}

	@RequestMapping("/deleteFeed")
	public ModelAndView deleteFeedback(ModelAndView modelAndView, @RequestParam String id) {
		modelAndView.addObject("deletedFeed", feedService.deleteFeedById(id).getUserEmail());
		modelAndView.addObject("message", "Feed back has been deleted");
		modelAndView.addObject("feedList", feedService.findAllFeeds());
		modelAndView.setViewName("feedback/feedList");
		return modelAndView;
	}

	@RequestMapping("/feedList")
	public ModelAndView feedbackList(ModelAndView modelAndView) {
		modelAndView.addObject("feedList", feedService.findAllFeeds());
		modelAndView.setViewName("feedback/feedList");
		return modelAndView;
	}

	@RequestMapping(value = "/viewFeed")
	public ModelAndView viewFeedback(ModelAndView modelAndView, @RequestParam String id) {
		modelAndView.addObject("feed", feedService.findByID(id));
		modelAndView.setViewName("feedback/viewFeed");
		return modelAndView;
	}
}
