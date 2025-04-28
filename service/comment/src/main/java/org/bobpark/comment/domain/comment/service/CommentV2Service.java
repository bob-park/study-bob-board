package org.bobpark.comment.domain.comment.service;

import static org.bobpark.comment.domain.comment.model.CommentResponse.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.common.exception.NotFoundException;

import org.bobpark.comment.domain.comment.entity.CommentPath;
import org.bobpark.comment.domain.comment.entity.CommentV2;
import org.bobpark.comment.domain.comment.model.CommentResponse;
import org.bobpark.comment.domain.comment.model.CreateCommentRequestV2;
import org.bobpark.comment.domain.comment.repository.CommentV2Repository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentV2Service {

    private final CommentV2Repository commentRepository;

    @Transactional
    public CommentResponse create(CreateCommentRequestV2 createRequest) {
        CommentV2 parent = findParent(createRequest.parentPath());

        CommentPath parentCommentPath =
            parent == null ? CommentPath.builder().path("").build() : parent.getCommentPath();

        // 흠....이게.. 이해가 안간다...
        // 보면...youtube 는 댓글 대댓글인 경우 클릭해서 나오도록되어 있음
        // 굳이... 한번에...음....
        CommentV2 createdComment =
            CommentV2.builder()
                .content(createRequest.content())
                .articleId(createRequest.articleId())
                .writerId(createRequest.writerId())
                .commentPath(
                    parentCommentPath.createChildCommentPath(
                        commentRepository.findDescendantTopPath(
                            createRequest.articleId(), parentCommentPath.getPath()).orElse(null)))
                .build();

        createdComment = commentRepository.save(createdComment);

        return from(createdComment);
    }

    private CommentV2 findParent(String parentPath) {
        if (parentPath == null) {
            return null;
        }

        return commentRepository.findByPath(parentPath)
            .filter(CommentV2::getDeleted)
            .orElseThrow(() -> new NotFoundException("parent comment not found"));
    }

}
