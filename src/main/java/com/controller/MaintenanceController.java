package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model.Staff;
import com.services.StaffService;

@Controller
@PropertySources(value = { @PropertySource("classpath:messages.properties") })
@RequestMapping("/maintenance")
public class MaintenanceController {

	private StaffService staffService;
	
	@Value("${user.added}")
	private String userAddedSuccessfully;
	@Value("${user.updated}")
	private String userUpdatedSuccessfully;
	@Value("${user.deleted}")
	private String userDeleted;

	@Autowired
	public MaintenanceController(StaffService staffService) {
		this.staffService = staffService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView maintenanceHome(ModelAndView modelAndView) {
		modelAndView.setViewName("maintenance/maintenance");
		return modelAndView;
	}

	@RequestMapping(value = "/addStaff", method = RequestMethod.GET)
	public ModelAndView addUser(ModelAndView modelAndView) {
		modelAndView.addObject("staff", new Staff());
		modelAndView.addObject("staffTypes", staffService.findStaffType());
		modelAndView.addObject("documentTypes", staffService.findDocumentType());
		modelAndView.setViewName("Staff/addStaff");
		return modelAndView;
	}

	@RequestMapping(value = "/addStaff", method = RequestMethod.POST)
	public ModelAndView addUser(@Valid Staff staff, BindingResult bindingResult, ModelAndView modelAndView) {

		if (!bindingResult.hasErrors()) {
			if (staffService.isStaffExist(staff)) {
				modelAndView.addObject("message", "Provided staff is already registered");
			} else {
				staffService.saveStaff(staff);
				modelAndView.addObject("staff", new Staff());
				modelAndView.addObject("staffTypes", staffService.findStaffType());
				modelAndView.addObject("documentTypes", staffService.findDocumentType());
				modelAndView.addObject("message", "Staff added successfully");
			}
		}
		modelAndView.addObject("staffTypes", staffService.findStaffType());
		modelAndView.setViewName("Staff/addStaff");
		return modelAndView;
	}
	
	@RequestMapping(value = "/updateStaff", method = RequestMethod.GET)
	public ModelAndView updateUser(ModelAndView modelAndView, @RequestParam String id) {
		Staff staff = staffService.findByID(id);
		modelAndView.addObject("staffTypes", staffService.findStaffType());
		modelAndView.addObject("documentTypes", staffService.findDocumentType());
		modelAndView.addObject("staff", staff);
		modelAndView.setViewName("Staff/updateStaff");
		
		return modelAndView;
	}

	@RequestMapping(value = "/updateStaff", method = RequestMethod.POST)
	public ModelAndView updateUser(@Valid Staff staff, BindingResult bindingResult, ModelAndView modelAndView) {

		if (!bindingResult.hasErrors()) {
			staffService.saveStaff(staff);
			modelAndView.addObject("message", userUpdatedSuccessfully);
		}

		modelAndView.addObject("staffTypes", staffService.findStaffType());
		modelAndView.addObject("documentTypes", staffService.findDocumentType());
		modelAndView.setViewName("Staff/updateStaff");

		return modelAndView;
	}
	
	@RequestMapping("/staff")
    public ModelAndView userHome(ModelAndView modelAndView) {
        modelAndView.addObject("staffsList", staffService.findAllStaffs());
        modelAndView.setViewName("Staff/staffList");
        return modelAndView;
    }
	
	@RequestMapping(value = "/viewStaff")
	public ModelAndView viewUser(ModelAndView modelAndView, @RequestParam String id) {
		modelAndView.addObject("staff", staffService.findByID(id));
		modelAndView.setViewName("Staff/staffProfile");

		return modelAndView;
	}

	@RequestMapping(value = "/deleteStaff")
	public ModelAndView deleteUser(ModelAndView modelAndView, @RequestParam String id) {
		modelAndView.addObject("deletedStaff", staffService.deleteStaffById(id).getFirstName());
		modelAndView.addObject("message", userDeleted);
		modelAndView.addObject("staffsList", staffService.findAllStaffs());
		modelAndView.setViewName("Staff/staffList");
		return modelAndView;
	}

}
