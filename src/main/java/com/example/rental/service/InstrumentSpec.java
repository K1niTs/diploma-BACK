package com.example.rental.service;

import com.example.rental.entity.Instrument;
import org.springframework.data.jpa.domain.Specification;

public class InstrumentSpec {

    public static Specification<Instrument> titleLike(String query) {
        return (root, cq, cb) -> {
            if (query == null || query.isBlank()) {
                return cb.conjunction();
            }
            String q = "%" + query.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("title")), q);
        };
    }

    public static Specification<Instrument> hasCategory(String category) {
        return (root, cq, cb) -> {
            if (category == null || category.isBlank()) {
                return cb.conjunction();
            }
            String c = "%" + category.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("category")), c);
        };
    }

    public static Specification<Instrument> priceBetween(Double min, Double max){
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null)
                return cb.between(root.get("pricePerDay"), min, max);
            return (min != null)
                    ? cb.ge(root.get("pricePerDay"), min)
                    : cb.le(root.get("pricePerDay"), max);
        };
    }
}
