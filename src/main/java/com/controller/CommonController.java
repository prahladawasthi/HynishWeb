package com.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.login.MongoUserDetails;
import com.model.User;
import com.services.UserService;

@Controller
@PropertySources(value = { @PropertySource("classpath:messages.properties") })
@RequestMapping("/common")
public class CommonController {

	private UserService userService;

	@Value("${user.updated}")
	private String userUpdatedSuccessfully;

	@Autowired
	public CommonController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView userHome(ModelAndView modelAndView) {
		MongoUserDetails user = (MongoUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		
		Optional<User> optional = userService.findUserByEmail(user.getEmail());

		modelAndView.addObject("user", optional.get());
		modelAndView.setViewName("common/profile");
		return modelAndView;
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView updateUser(ModelAndView modelAndView, @RequestParam String id) {
		User user = userService.findByID(id);
		modelAndView.addObject("user", user);
		modelAndView.addObject("userRoles", userService.findUserRoles());
		modelAndView.setViewName("admin/updateUser");
		return modelAndView;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateUser(@Valid User user, BindingResult bindingResult, ModelAndView modelAndView) {

		if (!bindingResult.hasErrors()) {
			userService.saveUser(user);
			modelAndView.addObject("message", userUpdatedSuccessfully);
		}

		modelAndView.addObject("userRoles", userService.findUserRoles());
		modelAndView.setViewName("admin/updateUser");

		return modelAndView;
	}
}
