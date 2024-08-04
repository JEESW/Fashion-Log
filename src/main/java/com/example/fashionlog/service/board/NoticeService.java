package com.example.fashionlog.service.board;

import com.example.fashionlog.aop.AuthCheck;
import com.example.fashionlog.aop.AuthorType;
import com.example.fashionlog.domain.Category;
import com.example.fashionlog.domain.Member;
import com.example.fashionlog.domain.board.Notice;
import com.example.fashionlog.domain.comment.NoticeComment;
import com.example.fashionlog.dto.comment.NoticeCommentDto;
import com.example.fashionlog.dto.board.NoticeDto;
import com.example.fashionlog.repository.comment.NoticeCommentRepository;
import com.example.fashionlog.repository.board.NoticeRepository;
import com.example.fashionlog.security.CurrentUserProvider;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NoticeService implements BoardService {

	private final NoticeRepository noticeRepository;
	private final NoticeCommentRepository noticeCommentRepository;
	private final CurrentUserProvider currentUserProvider;

	public Optional<List<NoticeDto>> getAllNotices() {
		List<NoticeDto> notices = noticeRepository.findAllByStatusIsTrueOrderByCreatedAtDesc()
			.stream()
			.map(NoticeDto::convertToDto)
			.collect(Collectors.toList());
		return notices.isEmpty() ? Optional.empty() : Optional.of(notices);
	}

	@AuthCheck(value = {"NORMAL", "ADMIN"}, Type = "Notice", AUTHOR_TYPE = AuthorType.POST)
	@Transactional
	public void createNotice(NoticeDto noticeDto) {
		Member currentUser = currentUserProvider.getCurrentUser();
		noticeDto.setAuthorName(currentUser.getNickname());
		noticeDto.setStatus(true);
		Notice notice = NoticeDto.convertToEntity(noticeDto, currentUser);
		noticeRepository.save(notice);
	}

	public NoticeDto getNoticeDetail(Long id) {
		Notice notice = findByIdAndStatusIsTrue(id);
		return NoticeDto.convertToDto(notice);
	}

	@AuthCheck(value = {"NORMAL", "ADMIN"}, checkAuthor = true, Type = "Notice", AUTHOR_TYPE = AuthorType.POST)
	@Transactional
	public void editNotice(Long id, NoticeDto noticeDto) {
		Notice notice = findByIdAndStatusIsTrue(id);
		notice.update(noticeDto);
	}

	@AuthCheck(value = {"NORMAL", "ADMIN"}, checkAuthor = true, Type = "Notice", AUTHOR_TYPE = AuthorType.POST)
	@Transactional
	public void deleteNotice(Long id) {
		Notice notice = findByIdAndStatusIsTrue(id);
		notice.delete();
	}

	public List<NoticeCommentDto> getNoticeCommentList(Long id) {
		List<NoticeComment> noticeCommentDtoList
			= noticeCommentRepository.findAllByCommentStatusIsTrueAndNoticeId(id);
		return noticeCommentDtoList.stream().map(NoticeCommentDto::convertToDto)
			.collect(Collectors.toList());
	}

	@AuthCheck(value = {"NORMAL", "ADMIN"}, Type = "Notice", AUTHOR_TYPE = AuthorType.COMMENT)
	@Transactional
	public void createNoticeComment(Long id, NoticeCommentDto noticeCommentDto) {
		Notice notice = findByIdAndStatusIsTrue(id);
		Member currentUser = currentUserProvider.getCurrentUser();

		noticeCommentDto.setNoticeId(null);
		noticeCommentDto.setNoticeId(id);
		noticeCommentDto.setCommentStatus(true);

		NoticeComment noticeComment = NoticeCommentDto.convertToEntity(noticeCommentDto, notice, currentUser);
		noticeCommentRepository.save(noticeComment);
	}

	@AuthCheck(value = {"NORMAL", "ADMIN"}, checkAuthor = true, Type = "Notice", AUTHOR_TYPE = AuthorType.COMMENT)
	@Transactional
	public void editNoticeComment(Long postId, Long commentId,
		NoticeCommentDto noticeCommentDto) {
		findByIdAndStatusIsTrue(postId);
		NoticeComment noticeComment = findByIdAndCommentStatusIsTrue(commentId);
		noticeComment.updateComment(noticeCommentDto);
	}

	@AuthCheck(value = {"NORMAL", "ADMIN"}, checkAuthor = true, Type = "Notice", AUTHOR_TYPE = AuthorType.COMMENT)
	@Transactional
	public void deleteNoticeComment(Long commentId) {
		NoticeComment noticeComment = findByIdAndCommentStatusIsTrue(commentId);
		noticeComment.deleteComment();
	}

	public Optional<List<NoticeDto>> getNoticeListByCategory(Category category) {
		System.out.println(category);
		List<NoticeDto> getNotice = noticeRepository.findTop5ByCategoryAndStatusIsTrueOrderByCreatedAtDesc(category)
			.stream()
			.map(NoticeDto::convertToDto)
			.collect(Collectors.toList());
		System.out.println(getNotice);
		return getNotice.isEmpty() ? Optional.empty() : Optional.of(getNotice);
	}

	private Notice findByIdAndStatusIsTrue(Long id) {
		return noticeRepository.findByIdAndStatusIsTrue(id)
			.orElseThrow(() -> new EntityNotFoundException("공지사항을 찾지 못했습니다."));
	}

	private NoticeComment findByIdAndCommentStatusIsTrue(Long id) {
		return noticeCommentRepository.findByIdAndCommentStatusIsTrue(id)
			.orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
	}

	/**
	 * 주어진 게시글 ID의 작성자가 현재 사용자인지 확인합니다.
	 *
	 * @param postId      확인할 게시글 ID
	 * @param memberEmail 현재 사용자의 이메일
	 * @return 현재 사용자가 게시글의 작성자인 경우 true, 그렇지 않은 경우 false
	 */
	@Override
	public boolean isPostAuthor(Long postId, String memberEmail) {
		return noticeRepository.findById(postId)
			.map(notice -> notice.isAuthor(memberEmail))
			.orElse(false);
	}

	/**
	 * 주어진 댓글 ID의 작성자가 현재 사용자인지 확인합니다.
	 *
	 * @param commentId   확인할 게시글 ID
	 * @param memberEmail 현재 사용자의 이메일
	 * @return 현재 사용자가 게시글의 작성자인 경우 true, 그렇지 않은 경우 false
	 */
	@Override
	public boolean isCommentAuthor(Long commentId, String memberEmail) {
		return noticeCommentRepository.findById(commentId)
			.map(notice -> notice.isAuthor(memberEmail))
			.orElse(false);
	}
}