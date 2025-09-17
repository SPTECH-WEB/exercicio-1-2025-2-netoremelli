package school.sptech.prova_ac1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (usuarioRepository.findByCpf(usuario.getCpf()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Usuario novoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam("nascimento") String nascimento) {
        LocalDate dataNascimento = LocalDate.parse(nascimento);
        List<Usuario> usuarios = usuarioRepository.findByDataNascimentoGreaterThan(dataNascimento);
        if (usuarios.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent() && !usuarioRepository.findById(id).get().getEmail().equals(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (usuarioRepository.findByCpf(usuario.getCpf()).isPresent() && !usuarioRepository.findById(id).get().getCpf().equals(usuario.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            Usuario usuarioAtualizado = usuarioRepository.save(usuario);
            return ResponseEntity.ok(usuarioAtualizado);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
