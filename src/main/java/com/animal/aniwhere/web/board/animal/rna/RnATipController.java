package com.animal.aniwhere.web.board.animal.rna;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.animal.aniwhere.service.animal.TipBoardDTO;
import com.animal.aniwhere.service.impl.PagingUtil;
import com.animal.aniwhere.service.impl.animal.TipBoardServiceImpl;
import com.animal.aniwhere.web.board.FileUpDownUtils;	

@Controller
public class RnATipController {
	
	
	@Resource(name="tipService")
	private TipBoardServiceImpl tipservice;
	
	@Value("${PAGESIZE}")
	private int pageSize;
	@Value("${BLOCKPAGE}")
	private int blockPage;
	
	@RequestMapping("/board/animal/rNa/tip/list.aw")
	public String list(Model model,
			HttpServletRequest req,//페이징용 메소드에 전달
			@RequestParam Map map,//검색용 파라미터 받기
			@RequestParam(required=false,defaultValue="1") int nowPage//페이징용 nowPage파라미터 받기용
			)throws Exception {
		map.put("ani_category",3);
		//서비스 호출]
		//페이징을 위한 로직 시작]
		//전체 레코드 수
		int totalRecordCount= tipservice.getTotalRecord(map);			
		//시작 및 끝 ROWNUM구하기]
		int start = (nowPage-1)*pageSize+1;
		int end   = nowPage*pageSize;
		map.put("start",start);
		map.put("end",end);
		//페이징을 위한 로직 끝]
		List list = tipservice.selectList(map);
		//페이징 문자열을 위한 로직 호출]
		String pagingString=PagingUtil.pagingBootStrapStyle(totalRecordCount, pageSize, blockPage, nowPage,req.getContextPath()+ "/board/animal/rNa/tip/list.aw?");
		//데이터 저장]
		model.addAttribute("pagingString", pagingString);
		model.addAttribute("list", list);
		model.addAttribute("totalRecordCount", totalRecordCount);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("nowPage", nowPage);
		//뷰정보 반환]
		return "board/animal/rNa/tip/tip_list.tiles";
		
	}////////// tip_main
	
	//상세보기
	@RequestMapping("/animal/rNa/tip/tip_view.aw")
	public String view(@RequestParam Map map,Model model) throws Exception {
		//서비스 호출]
		//게시글
		TipBoardDTO record = tipservice.selectOne(map);
		//데이터 저장]
		model.addAttribute("record", record);
		//줄바꿈처리
		record.setTip_content(record.getTip_content().replace("\r\n", "<br/>"));
		//뷰정보 반환]
		return "board/animal/rNa/tip/tip_view.tiles";
	}////////// tip_main
	//등록 폼으로 이동 및 입력처리]
	@RequestMapping(value="/security/animal/rNa/tip/write.aw",method=RequestMethod.GET)
	public String write() throws Exception{
		return "board/animal/rNa/tip/tip_write.tiles";
	}////////////////
	//입력처리]
	@RequestMapping(value="/security/animal/rNa/tip/write.aw",method=RequestMethod.POST)
	public String writeOk(@RequestParam Map map,HttpSession session //,org.springframework.security.core.Authentication auth 아직 적용 안함
		) throws Exception{
		
		map.put("mem_no", session.getAttribute("mem_no"));
		
		tipservice.insert(map);
		//뷰정보반환
		return "forward:/board/animal/rNa/tip/list.aw";//접두어 접미어 설정 적용 안되게끔 하려고 forward:를 붙임
	}////////////////

	//수정폼으로 이동 및 수정 처리]
	@RequestMapping("/security/board/animal/rNa/tip/edit.aw")
	public String edit(HttpServletRequest req,@RequestParam Map map,Model model) throws Exception{
		if(req.getMethod().equals("GET")) {
			//서비스 호출]
			TipBoardDTO record = tipservice.selectOne(map);
			//수정 폼으로 이동]
			model.addAttribute("record", record);
			return "board/animal/rNa/tip/tip_edit.tiles";
		}
		//수정처리후 메시지 뿌려주는 페이지(Message.jsp)로 이동
		int successFail = tipservice.update(map);
		model.addAttribute("successFail", successFail);
		model.addAttribute("WHERE", "EDT");
		return "board/animal/rNa/tip/Message";
	}//////////////edit()
	
	//삭제 처리
	@RequestMapping("/board/animal/rNa/tip/delete.aw")
	public String delete(@RequestParam Map map,Model model) throws Exception{
		int successFail = tipservice.delete(map);
		model.addAttribute("successFail", successFail);
		return "board/animal/rNa/tip/Message";
	}//////////////delete()
		
	//Summernote 업로드 기능
	@ResponseBody
    @RequestMapping(value="/animal/rNa/tip/Upload.aw")
    public String imageUpload(MultipartHttpServletRequest mhsr) throws Exception {
		String phisicalPath = mhsr.getServletContext().getRealPath("/Upload");
		MultipartFile upload = mhsr.getFile("file");
		
		String newFilename = FileUpDownUtils.getNewFileName(phisicalPath, upload.getOriginalFilename());
		File file = new File(phisicalPath+File.separator+newFilename);
		upload.transferTo(file);
        return "/Upload/"+newFilename;
   }
	@ResponseBody
	@RequestMapping(value="/animal/rNa/tip/tip_hit.aw",method=RequestMethod.POST)
	public String hit(@RequestParam Map map) throws Exception{
	
		map.put("no", map.get("no").toString());
		int hitCount= tipservice.addHitCount(map);
		
		return "success";
	}//////////////hit()
}//////////////////// tipboardController

