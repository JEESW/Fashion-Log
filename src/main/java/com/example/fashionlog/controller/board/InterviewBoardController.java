package com.example.fashionlog.controller.board;

import com.example.fashionlog.domain.Category;
import com.example.fashionlog.domain.board.InterviewBoard;
import com.example.fashionlog.dto.comment.InterviewBoardCommentDto;
import com.example.fashionlog.dto.board.InterviewBoardDto;
import com.example.fashionlog.dto.board.NoticeDto;
import com.example.fashionlog.service.board.InterviewBoardService;
import com.example.fashionlog.service.board.NoticeService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/fashionlog/interviewboard")
public class InterviewBoardController {

	private final InterviewBoardService interviewBoardService;
	private final NoticeService noticeService;

	@GetMapping
	public String getAllInterviewBoards(Model model) {
		List<NoticeDto> noticeDto = noticeService.getNoticeListByCategory(Category.INTERVIEW)
			.orElse(Collections.emptyList());
		model.addAttribute("interviewBoards", interviewBoardService.getAllInterviewPosts());
		model.addAttribute("interviewNotice", noticeDto);
		return "interviewboard/list";
	}

	@GetMapping("/new")
	public String newInterviewPostForm(Model model) {
		model.addAttribute("interviewPost", new InterviewBoard());
		return "interviewboard/form";
	}

	@PostMapping("/new")
	public String newInterviewPost(@ModelAttribute InterviewBoardDto interviewBoardDto) {
		interviewBoardService.createInterviewPost(interviewBoardDto);
		return "redirect:/fashionlog/interviewboard";
	}

	@GetMapping("/{id}")
	public String getInterviewBoardPostById(@PathVariable Long id, Model model) {
		model.addAttribute("interviewPost", interviewBoardService.getInterviewPostDetail(id));
		model.addAttribute("interviewComment", interviewBoardService.getCommentList(id));
		return "interviewboard/detail";
	}

	@GetMapping("/{id}/edit")
	public String editInterviewPostForm(@PathVariable Long id, Model model) {
		model.addAttribute("interviewPost", interviewBoardService.getInterviewPostDetail(id));
		return "interviewboard/edit";
	}

	@PostMapping("/{id}/edit")
	public String editInterviewPost(@PathVariable Long id,
		@ModelAttribute InterviewBoardDto interviewBoardDto) {
		interviewBoardService.updateInterviewPost(id, interviewBoardDto);
		return "redirect:/fashionlog/interviewboard/{id}";
	}

	@PostMapping("/{id}/delete")
	public String deleteInterviewPost(@PathVariable Long id) {
		interviewBoardService.deleteInterviewPost(id);
		return "redirect:/fashionlog/interviewboard";
	}

	@PostMapping("/{id}/comment")
	public String addComment(@PathVariable Long id,
		@ModelAttribute InterviewBoardCommentDto interviewBoardCommentDto) {
		interviewBoardService.addComment(id, interviewBoardCommentDto);
		return "redirect:/fashionlog/interviewboard/{id}";
	}

	@PostMapping("/{postId}/edit-comment/{commentId}")
	public String editComment(@PathVariable("postId") Long postId,
		@PathVariable("commentId") Long commentId,
		@ModelAttribute InterviewBoardCommentDto interviewBoardCommentDto) {
		interviewBoardService.updateInterviewComment(postId, commentId, interviewBoardCommentDto);
		return "redirect:/fashionlog/interviewboard/" + postId;
	}

	@PostMapping("/{postId}/delete-comment/{commentId}")
	public String deleteInterviewBoardComment(@PathVariable("postId") Long postId,
		@PathVariable("commentId") Long commentId) {
		interviewBoardService.deleteInterviewBoardComment(commentId);
		return "redirect:/fashionlog/interviewboard/" + postId;
	}
}
