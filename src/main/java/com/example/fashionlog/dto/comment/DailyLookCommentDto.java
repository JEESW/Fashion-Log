package com.example.fashionlog.dto.comment;

import com.example.fashionlog.domain.baseentity.CommentUpdatable;
import com.example.fashionlog.domain.board.DailyLook;
import com.example.fashionlog.domain.comment.DailyLookComment;

import com.example.fashionlog.domain.Member;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link DailyLookComment}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyLookCommentDto implements CommentUpdatable {

    private Long id;
    private Long memberId;
    private Long dailyLookId;
    @Size(max = 25, message = "이름은 25자를 초과할 수 없습니다.")
    private String authorName;
    @Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
    private String authorEmail;
    @Size(max = 65535, message = "글자 수는 65535자를 초과할 수 없습니다.")
    private String content;
    private Boolean commentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Override
    public String getContent() {
        return this.content;
    }

    public static DailyLookCommentDto convertToDto(DailyLookComment dailyLookComment) {

        return DailyLookCommentDto.builder()
            .id(dailyLookComment.getId())
            .memberId(dailyLookComment.getMember().getMemberId())
            .dailyLookId(dailyLookComment.getDailyLook().getId())
            .authorName(dailyLookComment.getMember().getNickname())
            .authorEmail(dailyLookComment.getMember().getEmail())
            .content(dailyLookComment.getContent())
            .commentStatus(dailyLookComment.getCommentStatus())
            .createdAt(dailyLookComment.getCreatedAt())
            .updatedAt(dailyLookComment.getUpdatedAt())
            .deletedAt(dailyLookComment.getDeletedAt())
            .build();
    }

    public static DailyLookComment convertToEntity(DailyLookCommentDto dailyLookCommentDto,
        DailyLook dailyLook, Member member) {

        return DailyLookComment.builder()
            .id(dailyLookCommentDto.getId())
            .member(member)
            .dailyLook(dailyLook)
            .content(dailyLookCommentDto.getContent())
            .commentStatus(dailyLookCommentDto.getCommentStatus())
            .createdAt(dailyLookCommentDto.getCreatedAt())
            .updatedAt(dailyLookCommentDto.getUpdatedAt())
            .deletedAt(dailyLookCommentDto.getDeletedAt())
            .build();
    }
}