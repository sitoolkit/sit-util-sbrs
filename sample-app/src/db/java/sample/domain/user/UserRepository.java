package sample.domain.user;

import io.sitoolkit.util.sbrs.AccountRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AccountRepository<UserEntity> {}
