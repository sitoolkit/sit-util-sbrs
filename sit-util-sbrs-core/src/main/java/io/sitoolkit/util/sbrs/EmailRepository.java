package io.sitoolkit.util.sbrs;

import org.springframework.data.repository.CrudRepository;

public interface EmailRepository<T extends EmailEntity> extends CrudRepository<T, String> {}