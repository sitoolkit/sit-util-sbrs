package io.sitoolkit.util.sbrs;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepositoryBase<T extends AccountEntityBase>
    extends CrudRepository<T, String> {}
