package org.bobpark.like.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.bobpark.like.service.ViewService;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/articles-views/{articleId}")
public class ViewController {

    private final ViewService viewService;

    @GetMapping(path = "")
    public long readViewCount(@PathVariable Long articleId) {
        return viewService.read(articleId);
    }

    @PostMapping(path = "users/{userId}")
    public long increaseViewCount(@PathVariable Long articleId, @PathVariable Long userId) {
        return viewService.increase(articleId, userId);
    }

}
