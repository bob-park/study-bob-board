package org.bobpark.comment.domain.comment.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CommentPathTest {

    @Test
    void createChildCommentPath() {

        // 00000
        createChildCommentPath(CommentPath.builder().path("").build(), null, "00000");

        // 00000
        //      00000 <-  생성
        createChildCommentPath(CommentPath.builder().path("00000").build(), "0000000000", "0000000000");




    }

    void createChildCommentPath(CommentPath commentPath, String descendantsTopPath, String expectedChildPath) {
        CommentPath childCommentPath = commentPath.createChildCommentPath(descendantsTopPath);

        Assertions.assertThat(childCommentPath.getPath()).isEqualTo(expectedChildPath);
    }


}