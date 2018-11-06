package com.animal.aniwhere.web.board.animal.bird;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.animal.aniwhere.service.AllCommentDTO;
import com.animal.aniwhere.service.AllCommentService;

@Controller
public class BiQueCmtController {

	@Resource(name="allCommentService")
	private AllCommentService allCommentService;
	
	@ResponseBody
	@RequestMapping(value="/security/bird/quest/cmtWrite.awa",produces="text/html; charset=UTF-8",method = RequestMethod.POST)
	public String write(@RequestParam Map map,HttpSession session,Model model) throws Exception{
		
		map.put("mem_no", session.getAttribute("mem_no"));
		map.put("table_name", "quest");
		map.put("no", map.get("no"));
		
		allCommentService.insert(map);
		
		return map.get("no").toString();
		
	}///////////////////
	
	@ResponseBody
	@RequestMapping(value="/bird/quest/cmtList.awa",produces="text/html; charset=UTF-8",method = RequestMethod.POST)
	public String list(@RequestParam Map map,HttpSession session) throws Exception{
		
		map.put("table_name", "quest");
		map.put("origin_no", map.get("no"));
		
		List<AllCommentDTO> collections = allCommentService.selectList(map);
		
		List<Map> comments = new Vector<>();
		
		for (AllCommentDTO dto : collections) {
	         Map record = new HashMap();
	         record.put("cmt_no", dto.getCmt_no());
	         session.setAttribute("cmt_No",dto.getCmt_no());
	         record.put("cmt_content", dto.getCmt_content());
	         record.put("mem_nickname", dto.getMem_nickname());
	         record.put("regidate", dto.getRegidate().toString());
	         record.put("origin_no", dto.getOrigin_no());
	         record.put("mem_no", dto.getMem_no());
	         

	         comments.add(record);
	      }
		
		return JSONArray.toJSONString(comments);
	}//////////////////
	
	@ResponseBody
	@RequestMapping(value="/bird/quest/cmtDelete.awa",produces="text/html; charset=UTF-8",method = RequestMethod.POST)
	public String delete(@RequestParam Map map,HttpSession session) throws Exception{
		map.put("table_name", "quest");
		map.put("cmt_no",map.get("cmt_no"));
		allCommentService.delete(map);
		
		return map.get("no").toString();
	}
}
