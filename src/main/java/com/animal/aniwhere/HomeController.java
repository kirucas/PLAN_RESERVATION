package com.animal.aniwhere;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.animal.aniwhere.service.impl.miss.LostAnimalServiceImpl;
import com.animal.aniwhere.service.miss.LostAnimalDTO;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Resource(name="lostAniService")
	private LostAnimalServiceImpl lostService;
	
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "forward:/main.aw";
	}
	
	@RequestMapping("/main.aw")
	public String goMain(@RequestParam Map map,Model model) throws Exception {
		
		//종료일 임박한 동물 10마리중 랜덤하게
		int end = (int) (Math.random() * 10) + 1;
		map.put("start", end);
		map.put("end", end);
		List<LostAnimalDTO> list = lostService.selectList(map);
		model.addAttribute("lost_one", list);
		model.addAttribute("serverTime",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return "mainTemplate.tiles";
	}
	
}
