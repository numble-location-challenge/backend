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
import com.example.backend.dto.comment.CommentRequestDTO;
import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.dto.comment.MyCommentResponseDTO;
import com.example.backend.dto.response.ResponseDTO;
import com.example.backend.global.security.CustomUserDetails;
import com.example.backend.service.comment.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@Tag(name = "comment", description = "댓글 API")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 조회", description = "게시물의 ID를 이용하여 해당 게시물의 댓글을 조회합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK")})
    @GetMapping("/comment/{postId}")
    public ResponseDTO<?> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getComments(postId);

        List<CommentResponseDTO> result = comments.stream()
            .map(comment -> new CommentResponseDTO(comment))
            .collect(Collectors.toList());

        return new ResponseDTO<>(result,"댓글 조회 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "내가 작성한 댓글 조회", description = "내가 작성한 댓글을 조회합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @GetMapping("/comment/me")
    public ResponseDTO<?> getMyComments(@AuthenticationPrincipal CustomUserDetails user) {
        List<Comment> comments = commentService.getMyComments(user.getUserId());

        List<MyCommentResponseDTO> result = comments.stream()
            .map(comment -> new MyCommentResponseDTO(comment))
            .collect(Collectors.toList());

        return new ResponseDTO<>(result,"내가 작성한 댓글 조회 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 작성", description = "사용자가 댓글을 작성합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
        ,@ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @PostMapping("/comment/{postId}")
    public ResponseDTO<?> createComment(@PathVariable Long postId, @Valid @RequestBody CommentRequestDTO commentRequestDTO,
        @AuthenticationPrincipal CustomUserDetails user) {
        commentService.createComment(postId, user.getUserId(), commentRequestDTO);
        return new ResponseDTO<>(null,"댓글 작성 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "대댓글 작성", description = "사용자가 대댓글을 작성합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
        ,@ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @PostMapping("/comment/{postId}/{commentId}")
    public ResponseDTO<?> createReply(@PathVariable Long postId, @PathVariable Long commentId,
        @Valid @RequestBody CommentRequestDTO commentRequestDTO, @AuthenticationPrincipal CustomUserDetails user) {
        commentService.createReply(postId, user.getUserId(), commentId, commentRequestDTO);
        return new ResponseDTO<>(null,"대댓글 작성 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 삭제", description = "사용자가 자신의 댓글을 삭제합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            , @ApiResponse(responseCode = "403", description = "FORBIDDEN")
        ,@ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @DeleteMapping("/comment/{commentId}")
    public ResponseDTO<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails user) {
        commentService.deleteComment(commentId, user.getUserId());
        return new ResponseDTO<>(null,"댓글 삭제 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 수정", description = "사용자가 자신의 댓글을 수정합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            , @ApiResponse(responseCode = "403", description = "FORBIDDEN")
        ,@ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @PutMapping("/comment/{commentId}")
    public ResponseDTO<?> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentRequestDTO commentRequestDTO,
        @AuthenticationPrincipal CustomUserDetails user) {
        Comment comment = commentService.updateComment(commentId, user.getUserId(), commentRequestDTO);
        CommentResponseDTO result = new CommentResponseDTO(comment);
        return new ResponseDTO<>(result,"댓글 수정 성공");
    }
}
