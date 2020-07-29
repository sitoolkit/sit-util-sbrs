package sample.domain.user;

import io.sitoolkit.util.sbrs.AccountRepositoryBase;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AccountRepositoryBase<UserEntity> {}
