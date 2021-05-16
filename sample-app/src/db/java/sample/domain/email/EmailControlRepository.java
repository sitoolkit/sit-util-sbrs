package sample.domain.email;

import org.springframework.stereotype.Repository;

import io.sitoolkit.util.sbrs.EmailRepository;

@Repository
public interface EmailControlRepository extends EmailRepository<EmailControlEntity> {}