package com.example.msablog.user.controller;

import com.example.msablog.post.dto.ResponseDto;
import com.example.msablog.user.dto.LoginDto;
import com.example.msablog.user.dto.RegisterDto;
import com.example.msablog.user.dto.UpdateUserDto;
import com.example.msablog.user.dto.UserProfileDto;
import com.example.msablog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseDto register(@RequestBody RegisterDto request) {
        try {
            return ResponseDto.success(userService.register(request));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(500, "API FAILED");
        }
    }

    @PostMapping("/login")
    public ResponseDto login(@RequestBody LoginDto request) {
        try {
            return ResponseDto.success(userService.login(request));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(500, "API FAILED");
        }
    }

    @GetMapping("/me")
    public ResponseDto getMe() {
        try {
            return ResponseDto.success(userService.getMe());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(500, "API FAILED");
        }
    }

    @PostMapping("/me/update")
    public ResponseDto updateMe(@RequestBody UpdateUserDto request) {
        try {
            return ResponseDto.success(userService.updateMe(request));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(500, "API FAILED");
        }
    }

    @GetMapping("/me/delete")
    public ResponseDto deleteMe() {
        try {
            userService.deleteMe();
            return ResponseDto.success();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(500, "API FAILED");
        }
    }

    // 마이페이지 병합

    @GetMapping("/mypage/{id}")
    public ResponseDto getUser(@PathVariable Long id) {
        try {
            return ResponseDto.success(userService.getUserProfile(id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(500, "API FAILED");
        }
    }

    // 개인정보수정
    @PutMapping("/mypage/{id}")
    public ResponseDto updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        try {
            return ResponseDto.success(userService.updateUserProfile(id, userProfileDto));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(500, "API FAILED");
        }
    }
}
