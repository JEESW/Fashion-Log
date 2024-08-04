package com.example.fashionlog.dto.board;

import com.example.fashionlog.domain.Member;
import com.example.fashionlog.domain.board.Trade;
import com.example.fashionlog.domain.baseentity.Updatable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TradeDto implements Updatable {

	private Long id;
	private Long memberId;
	private String authorName;
	private String title;
	private String content;
	private Boolean status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getContent() {
		return this.content;
	}

	public static Trade convertToEntity(TradeDto tradeDto, Member member) {
		return Trade.builder()
			.member(member)
			.title(tradeDto.getTitle())
			.content(tradeDto.getContent())
			.status(tradeDto.getStatus() != null ? tradeDto.getStatus() : true)
			.createdAt(
				tradeDto.getCreatedAt() != null ? tradeDto.getCreatedAt() : LocalDateTime.now())
			.updatedAt(tradeDto.getUpdatedAt())
			.deletedAt(tradeDto.getDeletedAt())
			.build();
	}

	public static TradeDto convertToDto(Trade trade) {
		return TradeDto.builder()
			.id(trade.getId())
			.memberId(trade.getMember().getMemberId())
			.authorName(trade.getMember().getNickname())
			.title(trade.getTitle())
			.content(trade.getContent())
			.status(trade.getStatus())
			.createdAt(trade.getCreatedAt())
			.updatedAt(trade.getUpdatedAt())
			.deletedAt(trade.getDeletedAt())
			.build();
	}
}
