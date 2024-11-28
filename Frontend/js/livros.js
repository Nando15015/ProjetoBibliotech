async function getLivros() {
    try {
        const response = await fetch(`${API_URL}/livros`);
        if (!response.ok) throw new Error('Erro ao buscar livros');
        const livros = await response.json();

        const listaLivros = document.getElementById('listaLivros');
        listaLivros.innerHTML = '';

        for (const livro of livros) {
            const row = document.createElement('tr');
            
            const [nomeAutor, nomeGenero] = await Promise.all([
                loadAutorFromLivro(livro.id_autor),
                loadGeneroFromLivro(livro.id_genero)
            ]);
            
            row.innerHTML = `
            <td class="no-hover">
                <button class="btn-edit" onclick="editLivro(${livro.id})" aria-label="Editar Livro">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn-delete" onclick="deleteLivro(${livro.id})" aria-label="Excluir Livro">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
            <td>${livro.titulo}</td>
            <td>${nomeAutor}</td>
            <td>${nomeGenero}</td>
            <td>${livro.ano_publicacao}</td>
            <td class="no-hover">
                <span class="status-tag ${livro.status === 'DISPONIVEL' ? 'available' : 'borrowed'}">
                    ${livro.status === 'DISPONIVEL' ? 'DISPONÍVEL' : 'EMPRESTADO'}
                </span>
            </td>
        `;        
            listaLivros.appendChild(row);
        }
    } catch (error) {
        console.error("Erro ao carregar livros:", error);
        swal.fire({
            icon: 'error',
            title: 'Erro',
            text: 'Erro ao carregar a lista de livros. Tente novamente mais tarde.',
        });
    }
}

function editLivro(id) {
    fetch(`${API_URL}/livros/${id}`)
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar os dados do livro');
            return response.json();
        })
        .then(livro => {
            window.location.href = `form_livro.html?id=${id}&titulo=${livro.titulo}&isbn=${livro.isbn}&autor=${livro.id_autor}&genero=${livro.id_genero}&ano_publicacao=${livro.ano_publicacao}`;
        })
        .catch(error => {
            console.error("Erro ao editar o livro:", error);
            swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao editar o livro. Por favor, tente novamente.',
            });
        });
}

async function deleteLivro(id) {
    swal.fire({
        title: 'Tem certeza?',
        text: "Você não poderá reverter esta ação!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#007bff',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sim, excluir!',
        cancelButtonText: 'Cancelar'
    }).then(async (result) => {
        if (result.isConfirmed) {
            try {
                const response = await fetch(`${API_URL}/livros/${id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    swal.fire({
                        icon: 'success',
                        title: 'Excluído!',
                        text: 'Livro excluído com sucesso.',
                    });
                    getLivros();
                } else {
                    swal.fire({
                        icon: 'error',
                        title: 'Erro',
                        text: 'Erro ao excluir o livro. Por favor, tente novamente.',
                    });
                }
            } catch (error) {
                swal.fire({
                    icon: 'error',
                    title: 'Erro',
                    text: 'Erro ao processar a exclusão. Tente novamente mais tarde.',
                });
            }
        }
    });
}

async function loadLivroFromUrl() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    if (id) {
        await loadLivro(id);
    }
}

function loadAutorFromLivro(id) {
    return fetch(`${API_URL}/autores/${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar autor');
            return response.json();
        })
        .then(autor => autor.nome)
        .catch(error => {
            console.error("Erro ao buscar autor:", error);
            return 'Erro ao carregar autor';
        });
}

function loadGeneroFromLivro(id) {
    return fetch(`${API_URL}/generos/${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar gênero');
            return response.json();
        })
        .then(genero => genero.nome)
        .catch(error => {
            console.error("Erro ao buscar gênero:", error);
            return 'Erro ao carregar gênero';
        });
}