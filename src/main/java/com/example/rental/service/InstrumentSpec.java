package com.example.rental.service;

import com.example.rental.entity.Instrument;
import org.springframework.data.jpa.domain.Specification;

public class InstrumentSpec {

    public static Specification<Instrument> titleLike(String q){
        return (root, query, cb) ->
                q == null ? null :
                        cb.like(cb.lower(root.get("title")), "%" + q.toLowerCase() + "%");
    }

    public static Specification<Instrument> hasCategory(String c){
        return (root, query, cb) ->
                c == null ? null :
                        cb.equal(root.get("category"), c);
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
