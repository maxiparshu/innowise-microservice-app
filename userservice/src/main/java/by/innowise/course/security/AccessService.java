package by.innowise.course.security;

import org.springframework.stereotype.Service;

@Service
public class AccessService {

    public boolean isOwner(Long userId) {
        return SecurityUtils
                .getCurrentUserId().equals(userId);
    }
}
