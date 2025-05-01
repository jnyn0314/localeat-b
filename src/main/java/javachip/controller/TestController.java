/*
* 파일명 : TestController.java
* 파일설명 : 프론트엔드랑 연결하기 위한 test 파일입니다.
* 작성자 : 정여진
* 기간 : 2025.05.01.
 * */
package javachip.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello from javachip!";
    }
}
