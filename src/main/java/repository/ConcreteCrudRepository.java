package repository;

import domain.HasID;
import validators.ValidationException;
import validators.Validator;

import java.util.HashMap;
import java.util.Map;

public class ConcreteCrudRepository<ID, E extends HasID<ID>> implements CrudRepository<ID, E> {
    protected Map<ID, E> repo;
    protected Validator<E> validator;

    /**
     * Constructorul explicit al clasei
     * @param v - Validatorul folosit pt a valida fiecare element inainte de efectuarea unei operatii CRUD
     */
    public ConcreteCrudRepository(Validator v) {
        repo = new HashMap<ID, E>();
        validator = v;
    }

    /**
     * Functie ce returneaza elementul cu id-ul dat
     * @param id -the id of the entity to be returned
     * id must not be null
     * @return entitatea gasita avand id-ul specificat
     */
    @Override
    public E findOne(ID id) {
        return repo.get(id);
    }

    /**
     * Returneaza o referinta catre toate elementele curente
     * @return Un element de tip Iterable pt a putea itera prin toate elementele curente
     */
    @Override
    public Iterable<E> findAll() {
        return repo.values();
    }

    /**
     * Functia de adaugare in repository
     * @param entity - Elementul ce va fi adaugat
     * entity must be not null
     * @return entity-ul dat daca acesta exista deja sau null in caz contrar
     * @throws ValidationException in cazul in care entity-ul dat nu este valid
     */
    @Override
    public E save(E entity) throws ValidationException {
        validator.validate(entity);

        return repo.putIfAbsent(entity.getID(), entity);
    }

    /**
     * Functia de stergere din repository
     * @param id - ID-ul elementului ce va fi sters
     * id must be not null
     * @return elementul ce a fost  sters
     */
    @Override
    public E delete(ID id) {
        return repo.remove(id);
    }

    /**
     * Functia de update a unui element din repository
     * @param entity - Noua entitate
     * entity must not be null
     * @return null daca elementul nu exista in repo, elementul modificat in caz contrar
     */
    @Override
    public E update(E entity) {
        validator.validate(entity);

        return repo.replace(entity.getID(), entity);
    }

    /**
     * Functie pt size
     * @return size-ul repositoryului
     */
    public int size() {
        return repo.size();
    }

}
