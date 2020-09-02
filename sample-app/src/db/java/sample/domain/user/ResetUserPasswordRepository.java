package sample.domain.user;

import io.sitoolkit.util.sbrs.ResetPasswordRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetUserPasswordRepository
    extends ResetPasswordRepository<ResetUserPasswordEntity> {}
