package com.gpch.login.controller;

import com.gpch.login.model.GuestEntry;
import com.gpch.login.model.GuestEntryDTO;
import com.gpch.login.model.User;
import com.gpch.login.service.GuestEntryService;
import com.gpch.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    
    @Autowired
    private GuestEntryService guestEntryService;
    
    

    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @GetMapping(value="/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @GetMapping(value="/admin/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }
    
    @GetMapping(value="/admin/listEntries")
    public ModelAndView listEntries(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        String mimeType = "image/png";
        
        List<GuestEntry> guestEntries= guestEntryService.findAll();      
        
        List<GuestEntryDTO> guestEntriesDTOList = new ArrayList<GuestEntryDTO>();
        
        for(GuestEntry guestEntry: guestEntries ) {
        	
        	String htmlValue = "data:"+mimeType+";base64," + Base64.encodeBase64String(guestEntry.getImage());
        	
        	GuestEntryDTO guestEntryDTO = new GuestEntryDTO(guestEntry.getId(),guestEntry.getUser(),guestEntry.getText(),guestEntry.getImage(),htmlValue,guestEntry.getApproved());
        	guestEntriesDTOList.add(guestEntryDTO);
        	  

        }
        modelAndView.addObject("guestEntries",guestEntriesDTOList);
        modelAndView.setViewName("admin/listEntries");
        return modelAndView;
    }
    

    @GetMapping(value="/guest/home")
    public ModelAndView gusetHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("gusetMessage","landing page for guest users");
        GuestEntry entry = new GuestEntry();
        modelAndView.addObject("guestEntry", entry);
        
        modelAndView.setViewName("guest/home");
        return modelAndView;
    }
    
    @PostMapping(value = "/writeEntry")  
    public ModelAndView writeEntry(@RequestParam("image") MultipartFile file,@Valid GuestEntry entry, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        entry.setUser(user);
        try {
			entry.setImage(file.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        guestEntryService.saveEntry(entry);
              
         modelAndView.addObject("successMessage", "Entry is posted successfully");          
         modelAndView.setViewName("guest/home");

       
        return modelAndView;
    }
    
 
    
    
    @PostMapping(value = "/admin/saveEntries")  
    public ModelAndView saveEntries( @RequestParam("approveList") List<String> approveList) {
        ModelAndView modelAndView = new ModelAndView();
   
        System.out.println("approved list:"+approveList); 
      /*  if (action.equals("approve")) {
        	 System.out.println("approved list:"+approveList); 
        }

        if (action.equals("delete")) {
        	 System.out.println("Delete list:"+deleteList);
        }*/
       
        
       
        
//        User user = new User();
//        modelAndView.addObject("user", user);
//        modelAndView.setViewName("registration");
        
        modelAndView.setViewName("/admin/listEntries");
        return modelAndView;
    }
    
    
    @InitBinder
    protected void initBinder(HttpServletRequest request,
    		ServletRequestDataBinder binder) throws ServletException {
    			binder.registerCustomEditor(byte[].class,
    				new ByteArrayMultipartFileEditor());
    	}
    
    

}
