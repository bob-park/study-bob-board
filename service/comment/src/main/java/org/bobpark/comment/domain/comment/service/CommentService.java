package org.bobpark.comment.domain.comment.service;

import static java.util.function.Predicate.*;
import static org.bobpark.comment.domain.comment.model.CommentResponse.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.common.exception.NotFoundException;

import org.bobpark.comment.domain.comment.entity.Comment;
import org.bobpark.comment.domain.comment.model.CommentResponse;
import org.bobpark.comment.domain.comment.model.CreateCommentRequest;
import org.bobpark.comment.domain.comment.repository.CommentRepository;
import org.bobpark.common.snowflake.Snowflake;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final Snowflake snowflake = new Snowflake();

    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse create(CreateCommentRequest createRequest) {
        Comment parent = findParent(createRequest.parentCommentId());

        Comment createdComment =
            Comment.builder()
                .commentId(snowflake.nextId())
                .articleId(createRequest.articleId())
                .content(createRequest.content())
                .parentCommentId(parent == null ? null : parent.getCommentId())
                .writerId(createRequest.writerId())
                .build();

        createdComment = commentRepository.save(createdComment);

        log.debug("created comment. ({})", createdComment);

        return from(createdComment);
    }

    public CommentResponse read(Long commentId) {
        return commentRepository.findById(commentId)
            .map(CommentResponse::from)
            .orElseThrow(() -> new NotFoundException(Comment.class, commentId));
    }

    @Transactional
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
            .filter(not(Comment::getDeleted))
            .ifPresent(comment -> {
                if (hasChildren(comment)) {
                    comment.delete();
                } else {
                    delete(comment);
                }
            });
    }

    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.getArticleId(), comment.getCommentId(), 2L) == 2L;
    }

    private void delete(Comment comment) {
        commentRepository.delete(comment);

        if (!comment.isRoot()) {
            commentRepository.findById(comment.getParentCommentId())
                .filter(not(Comment::getDeleted))
                .filter(not(this::hasChildren))
                .ifPresent(this::delete);
        }

        log.debug("deleted comment. ({})", comment);
    }

    private Comment findParent(Long parentCommentId) {

        if (parentCommentId == null) {
            return null;
        }

        return commentRepository.findById(parentCommentId)
            .filter(not(Comment::getDeleted))
            .filter(Comment::isRoot)
            .orElseThrow(() -> new NotFoundException(Comment.class, parentCommentId));

    }

}
