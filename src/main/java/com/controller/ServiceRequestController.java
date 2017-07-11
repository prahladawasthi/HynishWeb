package com.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.login.MongoUserDetails;
import com.model.ServiceRequest;
import com.model.ServiceRequestStatusEnum;
import com.model.Staff;
import com.model.User;
import com.services.ServiceRequestService;
import com.services.StaffService;
import com.services.UserService;

@Controller
@PropertySources(value = { @PropertySource("classpath:messages.properties") })
@RequestMapping("/request")
public class ServiceRequestController {

	private ServiceRequestService serviceRequestService;
	private UserService userService;
	private StaffService staffService;

	@Value("${user.updated}")
	private String reqUpdatedSuccessfully;
	@Value("${user.deleted}")
	private String reqDeleted;

	@Autowired
	public ServiceRequestController(ServiceRequestService serviceRequestService, StaffService staffService,
			UserService userService) {
		this.serviceRequestService = serviceRequestService;
		this.userService = userService;
		this.staffService = staffService;
	}

	@RequestMapping(value = "/addReq", method = RequestMethod.GET)
	public ModelAndView addRequest(ModelAndView modelAndView) {
		modelAndView.addObject("statusList", serviceRequestService.findAllServiceRequestStatus());
		modelAndView.addObject("maintenanceTeam", userService.findAllUsersByRole("ROLE_MAINTENANCE"));
		modelAndView.addObject("serviceRequest", new ServiceRequest());
		modelAndView.setViewName("ServiceRequest/addReq");
		return modelAndView;
	}

	@RequestMapping(value = "/addReq", method = RequestMethod.POST)
	public ModelAndView addRequest(@Valid ServiceRequest serviceRequest, BindingResult bindingResult,
			ModelAndView modelAndView) {

		if (!bindingResult.hasErrors()) {
			MongoUserDetails loggedInUser = (MongoUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Optional<User> optional = userService.findUserByEmail(loggedInUser.getEmail());

			serviceRequest.setRaisedBy(optional.get().getId());
			serviceRequest.setCurrentStatus(ServiceRequestStatusEnum.Open.toString());

			serviceRequestService.saveServiceRequest(serviceRequest);
			modelAndView.addObject("message", "Request raised successfully");
			modelAndView.addObject("serviceRequest", new ServiceRequest());
		}
		modelAndView.addObject("statusList", serviceRequestService.findAllServiceRequestStatus());

		modelAndView.addObject("serviceRequest", new ServiceRequest());
		modelAndView.setViewName("ServiceRequest/addReq");

		return modelAndView;
	}

	@RequestMapping(value = "/updateReq", method = RequestMethod.GET)
	public ModelAndView updateRequest(ModelAndView modelAndView, @RequestParam String id) {
		modelAndView.addObject("serviceRequest", serviceRequestService.findRequestByID(id));
		modelAndView.addObject("statusList", serviceRequestService.findAllServiceRequestStatus());
		modelAndView.addObject("maintenanceTeam", userService.findAllUsersByRole("ROLE_MAINTENANCE"));
		modelAndView.addObject("staffTeam", staffService.findAllStaffs());
		modelAndView.setViewName("ServiceRequest/updateReq");

		return modelAndView;
	}

	@RequestMapping(value = "/updateReq", method = RequestMethod.POST)
	public ModelAndView updateRequest(@Valid ServiceRequest serviceRequest, BindingResult bindingResult,
			ModelAndView modelAndView) {

		if (!bindingResult.hasErrors()) {
			if (!serviceRequest.getCurrentStatus().equalsIgnoreCase(ServiceRequestStatusEnum.Close.toString())) {
				serviceRequest.setResolvedDate(null);
			}
			if (!(serviceRequest.getUserCommentList().size() > 0)) {
				serviceRequest.setUserCommentList(new ArrayList<String>());
			}
			if (!(serviceRequest.getMaintenanceCommentList().size() > 0)) {
				serviceRequest.setMaintenanceCommentList(new ArrayList<String>());
			}

			if (null != serviceRequest.getUserComment() && !serviceRequest.getUserComment().isEmpty()) {
				List<String> userComment = serviceRequestService.findRequestByID(serviceRequest.getId())
						.getUserCommentList();
				if (null == userComment) {
					userComment = new ArrayList<String>();
					userComment.add(serviceRequest.getRaisedDate() + " : " + serviceRequest.getUserComment());
				} else {
					userComment.add(serviceRequest.getRaisedDate() + " : " + serviceRequest.getUserComment());
				}
				serviceRequest.setUserCommentList(userComment);
			} else if (!(serviceRequest.getUserCommentList().size() > 0))
				serviceRequest.getUserCommentList().clear();

			if (null != serviceRequest.getMaintenanceComment() && !serviceRequest.getMaintenanceComment().isEmpty()) {
				List<String> mainComment = serviceRequestService.findRequestByID(serviceRequest.getId())
						.getMaintenanceCommentList();
				if (null == mainComment) {
					mainComment = new ArrayList<String>();
					mainComment.add(serviceRequest.getRaisedDate() + " : " + serviceRequest.getMaintenanceComment());
				} else {
					mainComment.add(serviceRequest.getRaisedDate() + " : " + serviceRequest.getMaintenanceComment());
				}
				serviceRequest.setMaintenanceCommentList(mainComment);
			} else if (!(serviceRequest.getMaintenanceCommentList().size() > 0))
				serviceRequest.getMaintenanceCommentList().clear();

			serviceRequest.setUserComment(null);
			serviceRequest.setMaintenanceComment(null);

			serviceRequestService.saveServiceRequest(serviceRequest);
			modelAndView.addObject("message", reqUpdatedSuccessfully);
		}
		modelAndView.addObject("staffTeam", staffService.findAllStaffs());
		modelAndView.addObject("statusList", serviceRequestService.findAllServiceRequestStatus());
		modelAndView.addObject("maintenanceTeam", userService.findAllUsersByRole("ROLE_MAINTENANCE"));
		modelAndView.setViewName("ServiceRequest/updateReq");

		return modelAndView;
	}

	@RequestMapping("/requests")
	public ModelAndView requestList(ModelAndView modelAndView) {
		MongoUserDetails loggedInUser = (MongoUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		List<String> roles = new ArrayList<String>();

		for (GrantedAuthority a : loggedInUser.getAuthorities()) {
			roles.add(a.getAuthority());
		}

		if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_MAINTENANCE")) {
			modelAndView.addObject("reqList", serviceRequestService.findAllServiceRequest());
		} else {
			Optional<User> optional = userService.findUserByEmail(loggedInUser.getEmail());
			modelAndView.addObject("reqList", serviceRequestService.findServiceRequestByUserID(optional.get().getId()));
		}

		modelAndView.setViewName("ServiceRequest/reqList");
		return modelAndView;
	}

	@RequestMapping(value = "/viewReq")
	public ModelAndView viewRequest(ModelAndView modelAndView, @RequestParam String id) {
		ServiceRequest serviceRequest = serviceRequestService.findRequestByID(id);
		modelAndView.addObject("req", serviceRequest);

		User raisedByUser = userService.findByID(serviceRequest.getRaisedBy());
		modelAndView.addObject("raisedByUser", raisedByUser.getFirstName() + " " + raisedByUser.getLastName());

		if (!serviceRequest.getAssignedTo().equalsIgnoreCase("Maintenance Team")) {
			User assignedToUser = userService.findByID(serviceRequest.getAssignedTo());
			modelAndView.addObject("assignedToUser",
					assignedToUser.getFirstName() + " " + assignedToUser.getLastName());
		} else {
			modelAndView.addObject("assignedToUser", serviceRequest.getAssignedTo());
		}
		if (!serviceRequest.getAssignedStaff().equalsIgnoreCase("Staff Team")) {
			Staff assignedToStaff = staffService.findByID(serviceRequest.getAssignedStaff());
			modelAndView.addObject("assignedToStaff", assignedToStaff.getFirstName() + " "
					+ assignedToStaff.getLastName() + " [" + assignedToStaff.getStaffType() + "]");

		} else {
			modelAndView.addObject("assignedToStaff", serviceRequest.getAssignedStaff());
		}
		modelAndView.setViewName("ServiceRequest/viewRequest");
		return modelAndView;
	}

	@RequestMapping(value = "/deleteReq")
	public ModelAndView deleteRequest(ModelAndView modelAndView, @RequestParam String id) {
		modelAndView.addObject("deletedReq", serviceRequestService.deleteRequestById(id).getTitle());
		modelAndView.addObject("message", reqDeleted);
		MongoUserDetails loggedInUser = (MongoUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		List<String> roles = new ArrayList<String>();

		for (GrantedAuthority a : loggedInUser.getAuthorities()) {
			roles.add(a.getAuthority());
		}

		if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_MAINTENANCE")) {
			modelAndView.addObject("reqList", serviceRequestService.findAllServiceRequest());
		} else {
			Optional<User> optional = userService.findUserByEmail(loggedInUser.getEmail());
			modelAndView.addObject("reqList", serviceRequestService.findServiceRequestByUserID(optional.get().getId()));
		}
		modelAndView.setViewName("ServiceRequest/reqList");
		return modelAndView;
	}
}
