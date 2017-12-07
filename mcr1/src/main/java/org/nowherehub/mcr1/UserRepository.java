package org.nowherehub.mcr1;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, QuerydslPredicateExecutor<User>,
        QuerydslBinderCustomizer<QUser> {

    default void customize(QuerydslBindings bindings, QUser user) {

        bindings.bind(String.class).first(
                (SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }
}
