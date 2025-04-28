package org.bobpark.comment.domain.comment.entity;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class CommentPath {

    private String path;

    private static final String CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final int DEPTH_CHUNK_SIZE = 5;
    private static final int MAX_DEPTH = 5;

    // min chunk = "00000"
    private static final String MIN_CHUNK = String.valueOf(CHARSET.charAt(0)).repeat(DEPTH_CHUNK_SIZE);
    private static final String MAX_CHUNK = String.valueOf(CHARSET.charAt(CHARSET.length() - 1))
        .repeat(DEPTH_CHUNK_SIZE);

    @Builder
    private CommentPath(String path) {

        if (isDepthOverflowed(path)) {
            throw new IllegalArgumentException("Depth overflowed");
        }

        this.path = path;
    }

    public int getDepth() {
        return calcPath(getPath());
    }

    public boolean isRoot() {
        return calcPath(getPath()) == 1;
    }

    public String getParentPath() {
        return path.substring(0, path.length() - DEPTH_CHUNK_SIZE);
    }

    public CommentPath createChildCommentPath(String descendantsTopPath) {
        if (descendantsTopPath == null) {
            return new CommentPath(MIN_CHUNK);
        }

        String childrenTopPath = findChildrenTopPath(descendantsTopPath);

        return new CommentPath(increase(childrenTopPath));
    }

    private boolean isDepthOverflowed(String path) {
        return calcPath(path) > MAX_DEPTH;
    }

    private int calcPath(String path) {
        return path.length() / DEPTH_CHUNK_SIZE;
    }

    private String findChildrenTopPath(String descendantsTopPath) {
        return descendantsTopPath.substring(0, (getDepth() + 1) * DEPTH_CHUNK_SIZE);
    }

    private String increase(String path) {
        String lastChunk = path.substring(path.length() - DEPTH_CHUNK_SIZE);

        if (isChunkOverflowedChunk(lastChunk)) {
            throw new IllegalArgumentException("Chunk overflowed");
        }

        int charsetLength = CHARSET.length();

        int value = 0;

        for (char cn : lastChunk.toCharArray()) {
            value = value * charsetLength + CHARSET.indexOf(cn);
        }

        String result = "";
        for (int i = 0; i < DEPTH_CHUNK_SIZE; i++) {
            result = CHARSET.charAt(value % charsetLength) + result;
            value /= charsetLength;
        }

        return path.substring(0, path.length() - DEPTH_CHUNK_SIZE) + result;
    }

    public boolean isChunkOverflowedChunk(String lastChunk) {
        return MAX_CHUNK.equals(lastChunk);
    }

}
