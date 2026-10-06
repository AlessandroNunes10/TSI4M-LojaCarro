const form = document.getElementById("usuarioForm");

document.addEventListener("DOMContentLoaded", listarUsuarios);

function mostrarAba(aba) {
    document.querySelectorAll(".aba").forEach(elemento => {
        elemento.classList.add("escondida");
    });

    document.getElementById(aba).classList.remove("escondida");

    if (aba === "usuarios") {
        listarUsuarios();
    }
}

async function listarUsuarios() {
    const resposta = await fetch("/usuario");
    if (!resposta.ok) {
        alert("Não foi possível carregar os usuários.");
        return;
    }

    const usuarios = await resposta.json();
    const tabela = document.getElementById("usuariosTabela");
    tabela.innerHTML = "";

    usuarios.forEach(usuario => {
        const linha = document.createElement("tr");

        linha.innerHTML = `
            <td>${usuario.id}</td>
            <td>${usuario.nome}</td>
            <td>${usuario.email}</td>
            <td>
                <button onclick="editarUsuario(${usuario.id})">Editar</button>
                <button onclick="excluirUsuario(${usuario.id})">Excluir</button>
            </td>
        `;

        tabela.appendChild(linha);
    });
}

form.addEventListener("submit", async event => {
    event.preventDefault();

    const id = document.getElementById("usuarioId").value;

    const usuario = {
        nome: document.getElementById("nome").value,
        email: document.getElementById("email").value,
        senha: document.getElementById("senha").value
    };

    const resposta = await fetch(id ? `/usuario/${id}` : "/usuario", {
        method: id ? "PUT" : "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(usuario)
    });

    if (!resposta.ok) {
        alert("Não foi possível salvar o usuário.");
        return;
    }

    limparFormulario();
    await listarUsuarios();
});

async function editarUsuario(id) {
    const resposta = await fetch(`/usuario/${id}`);

    if (!resposta.ok) {
        alert("Usuário não encontrado.");
        return;
    }

    const usuario = await resposta.json();

    document.getElementById("usuarioId").value = usuario.id;
    document.getElementById("nome").value = usuario.nome;
    document.getElementById("email").value = usuario.email;
    document.getElementById("senha").value = usuario.senha;

    mostrarAba("usuarios");
}

async function excluirUsuario(id) {
    if (!confirm("Deseja realmente excluir este usuário?")) {
        return;
    }

    const resposta = await fetch(`/usuario/${id}`, {
        method: "DELETE"
    });

    if (!resposta.ok) {
        alert("Não foi possível excluir o usuário.");
        return;
    }

    await listarUsuarios();
}

function limparFormulario() {
    form.reset();
    document.getElementById("usuarioId").value = "";
}
