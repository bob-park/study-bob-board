package org.bobpark.comment.domain.comment.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.bobpark.comment.domain.comment.model.CommentResponse;
import org.bobpark.comment.domain.comment.model.CreateCommentRequest;
import org.bobpark.comment.domain.comment.service.CommentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/comments")
public class CommentV1Controller {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public CommentResponse create(@RequestBody CreateCommentRequest createRequest) {
        return commentService.create(createRequest);
    }

    @GetMapping(path = "{id:\\d+}")
    public CommentResponse read(@PathVariable Long id) {
        return commentService.read(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{id:\\d+}")
    public CommentResponse delete(@PathVariable Long id) {
        commentService.delete(id);

        return CommentResponse.builder()
            .commentId(id)
            .build();
    }

}
