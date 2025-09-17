package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.prova_ac1.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);
    List<Usuario> findByDataNascimentoGreaterThan(LocalDate dataNascimento);
}