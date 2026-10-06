package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.Usuario;
import br.org.edu.ifrn.LojaCarro.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario salvar(Usuario usuario) {
        Usuario salvo = usuarioRepository.save(usuario);
        log.info("USUARIO_CRIADO id={} nome={} email={}", salvo.getId(), salvo.getNome(), salvo.getEmail());
        return salvo;
    }

    public List<Usuario> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        log.info("USUARIO_LISTAGEM quantidade={}", usuarios.size());
        return usuarios;
    }

    public Optional<Usuario> buscarPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        log.info("USUARIO_CONSULTA id={} encontrado={}", id, usuario.isPresent());
        return usuario;
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setSenha(dados.getSenha());

        Usuario atualizado = usuarioRepository.save(usuario);
        log.info("USUARIO_ATUALIZADO id={} nome={} email={}", atualizado.getId(), atualizado.getNome(), atualizado.getEmail());
        return atualizado;
    }

    public void excluir(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        usuarioRepository.deleteById(id);
        log.info("USUARIO_EXCLUIDO id={} nome={} email={}", usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
