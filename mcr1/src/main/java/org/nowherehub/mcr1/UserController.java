package org.nowherehub.mcr1;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public List<User> getAllUser() {
        return Lists.newArrayList(userRepository.findAll());
    }

    @GetMapping
    @RequestMapping("/search")
    public Page<User> getPageUser(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable page) {
        return userRepository.findAll(predicate, page);
    }


}
