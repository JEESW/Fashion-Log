package com.example.fashionlog.service;

import com.example.fashionlog.domain.DailyLook;
import com.example.fashionlog.domain.DailyLookComment;
import com.example.fashionlog.dto.DailyLookCommentDto;
import com.example.fashionlog.dto.DailyLookDto;
import com.example.fashionlog.repository.DailyLookCommentRepository;
import com.example.fashionlog.repository.DailyLookRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class DailyLookService {

    private final DailyLookRepository dailyLookRepository;

    private final DailyLookCommentRepository dailyLookCommentRepository;

    public DailyLookService(DailyLookRepository dailyLookRepository,
        DailyLookCommentRepository dailyLookCommentRepository) {
        this.dailyLookRepository = dailyLookRepository;
        this.dailyLookCommentRepository = dailyLookCommentRepository;
    }

    public List<DailyLookDto> getAllDailyLookPost() {
        List<DailyLook> dailyLookList = dailyLookRepository.findAll();
        return dailyLookList.stream().map(DailyLookDto::convertToDto).collect(Collectors.toList());
    }

    public void createDailyLookPost(DailyLookDto dailyLookDto) {
        System.out.println("Content in service: " + dailyLookDto.getContent());
        DailyLook dailyLook = DailyLookDto.convertToEntity(dailyLookDto);
        dailyLookRepository.save(dailyLook);
    }
}