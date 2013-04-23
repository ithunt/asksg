package edu.rit.asksg.specification;

public interface Specification<T> extends org.springframework.data.jpa.domain.Specification<T> {
    Specification<T> and(Specification<T> other);

    Specification<T> or(Specification<T> other);

    Specification<T> not();
}
