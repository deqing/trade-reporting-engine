package com.devin.tradereporting.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;


@RequiredArgsConstructor
public class TradeQuery implements Specification<Trade> {

    private final String field;
    private final String value;

    /**
     * @return a query clause: "where field=value"
     */
    @Override
    public Predicate toPredicate(Root<Trade> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.equal(root.get(field), value);
    }
}
