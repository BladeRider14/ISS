package Persistence;

import Persistence.Exception.RepositoryException;

public interface IRepo<E,ID> {
    void add(E entity);
    void delete(E entity);
    void update(E oldEntity, E newEntity);
    E findById(ID id) throws RepositoryException;
    Iterable<E> getAll();
}
