package sample.domain.user;

import io.sitoolkit.util.sbrs.TmpAccountRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TmpUserRepository extends TmpAccountRepository<TmpUserEntity> {}
