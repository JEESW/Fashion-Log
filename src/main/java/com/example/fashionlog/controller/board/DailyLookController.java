package com.example.fashionlog.controller.board;

import com.example.fashionlog.domain.Category;
import com.example.fashionlog.dto.comment.DailyLookCommentDto;
import com.example.fashionlog.dto.board.DailyLookDto;
import com.example.fashionlog.dto.board.NoticeDto;
import com.example.fashionlog.service.board.DailyLookService;

import com.example.fashionlog.service.board.NoticeService;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fashionlog/dailylook")
public class DailyLookController {

    private final DailyLookService dailyLookService;
    private final NoticeService noticeService;

    public DailyLookController(DailyLookService dailyLookService, NoticeService noticeService) {
        this.dailyLookService = dailyLookService;
        this.noticeService = noticeService;
    }

    @GetMapping
    public String getAllDailyLookPost(Model model) {
        List<NoticeDto> noticeDto = noticeService.getNoticeListByCategory(Category.DAILY_LOOK)
            .orElse(Collections.emptyList());
        model.addAttribute("dailylooks", dailyLookService.getAllDailyLookPost());
        model.addAttribute("dailyLookNotice", noticeDto);
        return "dailylook/list";
    }

    @GetMapping("/new")
    public String getCreateDailyLookPostForm(Model model) {
        model.addAttribute("dailyLook", new DailyLookDto());
        return "dailylook/form";
    }

    @PostMapping("/new")
    public String createDailyLookPost(@ModelAttribute("dailyLook") DailyLookDto dailyLookDto) {
        System.out.println("Received DTO: " + dailyLookDto);
        System.out.println("Content in Service: " + dailyLookDto.getContent());
        dailyLookService.createDailyLookPost(dailyLookDto);
        return "redirect:/fashionlog/dailylook";
    }

    @GetMapping("/{id}")
    public String getDailyLookPostById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("dailyLook", dailyLookService.getDailyLookPostById(id));
        model.addAttribute("dailyLookComments",
            dailyLookService.getAllDailyLookCommentByDailyLookId(id));
        model.addAttribute("dailyLookComment", new DailyLookCommentDto());
        return "dailylook/detail";
    }

    @GetMapping("/{id}/edit")
    public String getDailyLookEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("dailyLook", dailyLookService.getDailyLookPostById(id));
        return "dailylook/edit";
    }

    @PostMapping("/{id}/edit")
    public String editDailyLookPost(@PathVariable("id") Long id,
        @ModelAttribute DailyLookDto dailyLookDto) {
        dailyLookService.editDailyLookPost(id, dailyLookDto);
        return "redirect:/fashionlog/dailylook/" + id;
    }

    @PostMapping("{id}/delete")
    public String deleteDailyPost(@PathVariable("id") Long id) {
        dailyLookService.deleteDailyLookPost(id);
        return "redirect:/fashionlog/dailylook";
    }

    @PostMapping("/{id}/comment")
    public String createDailyLookComment(
        @PathVariable("id") Long id,
        @ModelAttribute("dailyLookComment")
        DailyLookCommentDto dailyLookCommentDto) {
        dailyLookService.createDailyLookComment(id, dailyLookCommentDto);
        return "redirect:/fashionlog/dailylook/" + id;
    }

    @PostMapping("/{postid}/edit-comment/{commentid}")
    public String editDailyLookComment(
        @PathVariable("postid") Long postId,
        @PathVariable("commentid") Long commentId,
        @ModelAttribute("dailyLookComment") DailyLookCommentDto dailyLookCommentDto
    ) {
        dailyLookService.editDailyLookComment(commentId, dailyLookCommentDto);

        return "redirect:/fashionlog/dailylook/" + postId;
    }

    @PostMapping("/{postid}/delete-comment/{commentid}")
    public String deleteDailyLookComment(
        @PathVariable("postid") Long postId,
        @PathVariable("commentid") Long commentId
    ) {
        dailyLookService.deleteDailyLookComment(commentId);

        return "redirect:/fashionlog/dailylook/" + postId;
    }
}