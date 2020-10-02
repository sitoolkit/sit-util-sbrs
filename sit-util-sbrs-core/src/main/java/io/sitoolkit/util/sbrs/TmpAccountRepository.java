package io.sitoolkit.util.sbrs;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface TmpAccountRepository<T extends TmpAccountEntity>
    extends CrudRepository<T, String> {
        Optional<T> findByLoginId(String loginId);
    }
