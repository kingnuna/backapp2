package com.example.demo.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
	@Autowired
	private ItemDao dao;
	
	public void save(ItemDto dto) {
		dao.save(new Item(dto.getNum(), dto.getName(), dto.getPath()));
	}
	
	public ArrayList<ItemDto> getAll(){
		List<Item> l = dao.findAll();
		ArrayList<ItemDto> list = new ArrayList<>();
		for(Item i : l) {
			list.add(new ItemDto(i.getNum(), i.getName(), i.getPath(), null));
		}
		return list;
	}
}
