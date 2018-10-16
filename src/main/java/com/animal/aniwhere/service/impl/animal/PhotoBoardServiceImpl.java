package com.animal.aniwhere.service.impl.animal;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.animal.aniwhere.service.AllBoardService;
import com.animal.aniwhere.service.animal.PhotoBoardDTO;

@Service("photoService")
public class PhotoBoardServiceImpl implements AllBoardService {

	@Resource(name = "photoBoardDAO")
	private PhotoBoardDAO dao;
	
	@Override
	public List<PhotoBoardDTO> selectList(Map map) {
		return dao.selectList(map);
	}////////// selectList

	@Override
	public int getTotalRecord(Map map) {
		return dao.getTotalRecord(map);
	}////////// getTotalRecord

	@SuppressWarnings("unchecked")
	@Override
	public PhotoBoardDTO selectOne(Map map) {
		return dao.selectOne(map);
	}////////// selectOne

	@Override
	public int insert(Map map) {
		return dao.insert(map);
	}////////// insert

	@Override
	public int update(Map map) {
		return dao.update(map);
	}////////// update

	@Override
	public int delete(Map map) {
		return dao.delete(map);
	}////////// delete

}//////////////////// PhotoBoardServiceImpl class
