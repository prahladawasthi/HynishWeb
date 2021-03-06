package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model.User;
import com.services.UserService;

@Controller
@PropertySources(value = { @PropertySource("classpath:messages.properties") })
@RequestMapping("/admin")
public class AdminController {

	private UserService userService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Value("${user.email.already.exist}")
	private String userEmailAlreadyExist;
	@Value("${user.added}")
	private String userAddedSuccessfully;
	@Value("${user.updated}")
	private String userUpdatedSuccessfully;
	@Value("${user.deleted}")
	private String userDeleted;
	@Value("${user.default.password}")
	private String defaultPassword;

	@Autowired
	public AdminController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView adminHome(ModelAndView modelAndView) {
		modelAndView.setViewName("admin/admin");
		return modelAndView;
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public ModelAndView addUser(ModelAndView modelAndView) {
		modelAndView.addObject("user", new User());
		modelAndView.addObject("userRoles", userService.findUserRoles());
		modelAndView.setViewName("admin/addUser");
		return modelAndView;
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public ModelAndView addUser(@Valid User user, BindingResult bindingResult, ModelAndView modelAndView) {

		if (!bindingResult.hasErrors()) {
			if (userService.isUserExist(user)) {
				modelAndView.addObject("message", this.userEmailAlreadyExist);
			} else {
				if (user.getPassword().isEmpty()) {
					user.setPassword(bCryptPasswordEncoder.encode(defaultPassword));
				} else {
					String password = bCryptPasswordEncoder.encode(user.getPassword());
					user.setPassword(password);
				}
				userService.saveUser(user);
				modelAndView.addObject("user", new User());
				modelAndView.addObject("message", userAddedSuccessfully);
			}
		}
		modelAndView.addObject("userRoles", userService.findUserRoles());
		modelAndView.setViewName("admin/addUser");
		return modelAndView;
	}

	@RequestMapping("/user")
	public ModelAndView userHome(ModelAndView modelAndView) {
		modelAndView.addObject("userList", userService.findAllUsers());
		modelAndView.setViewName("admin/userList");
		return modelAndView;
	}

	@RequestMapping(value = "/search")
	public ModelAndView searchUser(ModelAndView modelAndView, @RequestParam String search) {
		modelAndView.addObject("userList", userService.find(search));
		modelAndView.setViewName("admin/userList");
		return modelAndView;
	}

	@RequestMapping(value = "/profile")
	public ModelAndView viewUser(ModelAndView modelAndView, @RequestParam String id) {
		modelAndView.addObject("user", userService.findByID(id));
		modelAndView.setViewName("admin/userProfile");

		return modelAndView;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView deleteUser(ModelAndView modelAndView, @RequestParam String id) {
		modelAndView.addObject("deletedUser", userService.deleteUserById(id).getFirstName());
		modelAndView.addObject("message", userDeleted);
		modelAndView.addObject("userList", userService.findAllUsers());
		modelAndView.setViewName("admin/userList");
		return modelAndView;
	}
}