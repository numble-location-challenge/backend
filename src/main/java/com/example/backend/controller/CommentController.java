package com.example.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.Comment;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.comment.CommentRequestDTO;
import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.service.comment.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

;

@Tag(name = "comment", description = "댓글 API")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 조회", description = "게시물의 ID를 이용하여 해당 게시물의 댓글을 조회합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    @GetMapping("/comment/{postId}")
    public ResponseDTO<CommentResponseDTO> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getComments(postId);

        List<CommentResponseDTO> result = comments.stream()
            .map(comment -> new CommentResponseDTO(comment))
            .collect(Collectors.toList());

        return ResponseDTO.<CommentResponseDTO>builder().success(true).message("정상 조회되었습니다.").data(result).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 작성", description = "사용자가 댓글을 작성합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    @PostMapping("/comment/{postId}")
    public ResponseDTO createComment(@PathVariable Long postId, @Valid @RequestBody CommentRequestDTO commentRequestDTO,
        @AuthenticationPrincipal String userEmail) {
        commentService.createComment(postId, "hello@numble.com", commentRequestDTO);
        return ResponseDTO.builder().success(true).message("정상 생성되었습니다.").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "대댓글 작성", description = "사용자가 대댓글을 작성합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    @PostMapping("/comment/{postId}/{commentId}")
    public ResponseDTO createReply(@PathVariable Long postId, @PathVariable Long commentId,
        @Valid @RequestBody CommentRequestDTO commentRequestDTO, @AuthenticationPrincipal String userEmail) {
        commentService.createReply(postId, "hello@numble.com", commentId, commentRequestDTO);
        return ResponseDTO.builder().success(true).message("정상 생성되었습니다.").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 삭제", description = "사용자가 자신의 댓글을 삭제합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            , @ApiResponse(responseCode = "403", description = "Forbidden")})
    @DeleteMapping("/comment/{commentId}")
    public ResponseDTO deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal String userEmail) {
        commentService.deleteComment(commentId, "hello@numble.com");
        return ResponseDTO.builder().success(true).message("정상 삭제되었습니다.").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 수정", description = "사용자가 자신의 댓글을 수정합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            , @ApiResponse(responseCode = "403", description = "Forbidden")})
    @PutMapping("/comment/{commentId}")
    public ResponseDTO updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO result = commentService.updateComment(commentId, "hello@numble.com", commentRequestDTO);
        return ResponseDTO.builder().success(true).message("정상 수정되었습니다").data(List.of(result)).build();
    }
}
