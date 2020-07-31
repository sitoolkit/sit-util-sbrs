package io.sitoolkit.util.sbrs;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository<T extends LoginUser> extends CrudRepository<T, String> {}
