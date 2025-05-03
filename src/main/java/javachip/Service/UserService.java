package javachip.service;

import javachip.entity.User;
import javachip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor // 생성자 주입 자동 생성
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public User registerUser(User user) {
        // 이미 존재하는 이메일 or ID 체크
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        if (userRepository.existsByUserId(user.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 ID입니다.");
        }

        // TODO: 비밀번호 해싱 필요
        return userRepository.save(user);
    }

    // 로그인 (간단한 버전)
    public Optional<User> login(String userId, String rawPassword) {
        return userRepository.findByUserId(userId)
                .filter(user -> user.getPassword().equals(rawPassword)); // 실제 서비스에서는 해싱된 비밀번호 비교
    }

    // 사용자 조회
    public Optional<User> getUserById(String userId) {
        return userRepository.findByUserId(userId);
    }

    // 이메일 중복 확인
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}
