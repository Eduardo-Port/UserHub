package io.github.Eduardo_Port.userhubapi.repository.specs;

import io.github.Eduardo_Port.userhubapi.model.Status;
import io.github.Eduardo_Port.userhubapi.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class UserSpecs {
    public static Specification<User>withFilter(String name, String email) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(name != null && !name.isBlank()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if(email != null && !email.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("email"), email));
            }

            Status status = Status.ACTIVE;

            predicates.add(criteriaBuilder.equal(root.get("status"), status.name()));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
