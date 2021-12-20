package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostDtoV2;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping()
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // create blog post
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    // get all posts rest api
    @GetMapping("/api/v1/posts")
    public PostResponse getAllPosts(
            @RequestParam(value="pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value="pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value="sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    // get post by id api
    @GetMapping(value = "/api/v1/posts/{id}")
    public ResponseEntity<PostDto> getPostByIdV1(@PathVariable("id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

//    @GetMapping(value = "/api/posts/{id}", produces = "application/vnd.javaguides.v2+json")
//    public ResponseEntity<PostDtoV2> getPostByIdV2(@PathVariable("id") long id) {
//        PostDto postDto = postService.getPostById(id);
//        PostDtoV2 postDtoV2 = new PostDtoV2();
//        postDtoV2.setId(postDto.getId());
//        postDtoV2.setTitle(postDto.getTitle());
//        postDtoV2.setDescription(postDto.getDescription());
//        postDtoV2.setContent(postDto.getContent());
//        postDtoV2.setComments(postDto.getComments());
//        List<String> tags = new ArrayList<>();
//        tags.add("Java");
//        tags.add("String boot");
//        tags.add("AWS");
//        postDtoV2.setTags(tags);
//        return ResponseEntity.ok(postDtoV2);
//    }

    // update post by id api
    @PutMapping("/api/v1/posts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable("id") long id) {
        return new ResponseEntity<>(postService.updatePost(postDto, id), HttpStatus.OK);
    }

    // delete post rest api
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully", HttpStatus.OK);
    }
}
