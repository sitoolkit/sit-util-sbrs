package io.sitoolkit.util.sbrs;

import org.springframework.data.repository.CrudRepository;

public interface TmpAccountRepository<T extends TmpAccountEntity>
    extends CrudRepository<T, String> {}
