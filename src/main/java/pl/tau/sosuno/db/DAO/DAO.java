package pl.tau.sosuno.db.DAO;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    Optional<T> get(long id);

    List<T> getAll();

    Long save(T t);

    T update(T t);

    void delete(T t);
}
