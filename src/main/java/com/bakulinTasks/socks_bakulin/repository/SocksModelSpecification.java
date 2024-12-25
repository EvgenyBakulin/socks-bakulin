package com.bakulinTasks.socks_bakulin.repository;


import com.bakulinTasks.socks_bakulin.dto.SocksSpecificationDTO;
import com.bakulinTasks.socks_bakulin.model.SocksModel;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class SocksModelSpecification {
    public static Specification<SocksModel> filterSocks(SocksSpecificationDTO dto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nonNull(dto.getColour())) {
                predicates.add(criteriaBuilder.equal(root.get("colour"), dto.getColour()));
            }
            if (nonNull(dto.getCottonContent())) {
                predicates.add(criteriaBuilder.equal(root.get("cottonContent"), dto.getCottonContent()));
            }
            if(nonNull(dto.getLessThanCottonContent())&&nonNull(dto.getMoreThanCottonContent())) {
                predicates.add(criteriaBuilder.between(root.get("cottonContent"), dto.getMoreThanCottonContent(), dto.getLessThanCottonContent()));
            }
             if (nonNull(dto.getLessThanCottonContent())) {
                predicates.add(criteriaBuilder.lessThan(root.get("cottonContent"), dto.getLessThanCottonContent()));
            }
            if (nonNull(dto.getMoreThanCottonContent())) {
                predicates.add(criteriaBuilder.greaterThan(root.get("cottonContent"), dto.getMoreThanCottonContent()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
