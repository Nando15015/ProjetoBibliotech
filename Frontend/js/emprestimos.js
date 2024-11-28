const baseUrlEmprestimos = `${API_URL}/emprestimos`;

async function getEmprestimos() {
    const response = await fetch(baseUrlEmprestimos);
    const emprestimos = await response.json();

    const listaEmprestimos = document.getElementById('listaEmprestimos');
    listaEmprestimos.innerHTML = '';

    emprestimos.forEach(emprestimo => {
        const row = document.createElement('tr');

        loadLivroFromEmprestimo(emprestimo.id_livro).then(nomeLivro => {
            loadPessoaFromEmprestimo(emprestimo.id_pessoa).then(nomePessoa => {
                row.innerHTML = `
                    <td class="no-hover">
                        <button class="btn-edit" onclick="editEmprestimo(${emprestimo.id})" aria-label="Editar Empréstimo">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-delete" onclick="confirmDeleteEmprestimo(${emprestimo.id})" aria-label="Excluir Empréstimo">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                    <td>${nomeLivro}</td>
                    <td>${nomePessoa}</td>
                    <td>${formatDate(emprestimo.data_emprestimo)}</td>
                    <td>${formatDate(emprestimo.data_devolucao)}</td>
                    <td class="no-hover"><button class="btn-devolucao" onclick="confirmDevolucao(${emprestimo.id}, '${nomeLivro}', '${nomePessoa}', '${formatDate(emprestimo.data_devolucao)}')" aria-label="Devolver Empréstimo">
                            <i class="fas fa-undo"></i>
                        </button></td>
                `;
                listaEmprestimos.appendChild(row);
            });
        });
    });
}

async function confirmDevolucao(id, nomeLivro, nomePessoa, dataDevolucao) {
    if (!dataDevolucao || dataDevolucao === null || dataDevolucao === "") {
        Swal.fire({
            title: 'Confirmar Devolução',
            text: `Deseja realmente devolver o livro "${nomeLivro}" emprestado para "${nomePessoa}"?`,
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#007bff',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Sim, devolver',
            cancelButtonText: 'Cancelar'
        }).then(async (result) => {
            if (result.isConfirmed) {
                await devolverEmprestimo(id);
            }
        });
    } else {
        Swal.fire({
            icon: 'error',
            title: 'Opss...',
            text: `Livro já foi devolvido no dia ${dataDevolucao}.`,
            confirmButtonColor: '#007bff'
        });
    }
}

async function devolverEmprestimo(id) {
    try {
        const response = await fetch(`${baseUrlEmprestimos}/${id}/devolver`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Devolução Registrada!',
                text: 'A devolução foi registrada com sucesso.',
                confirmButtonColor: '#007bff'
            });
            getEmprestimos();
        } else {
            throw new Error('Erro ao registrar a devolução.');
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Erro',
            text: 'Não foi possível registrar a devolução.',
            confirmButtonColor: '#007bff'
        });
    }
}

function editEmprestimo(id) {
    fetch(`${baseUrlEmprestimos}/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar os dados do empréstimo');
            }
            return response.json();
        })
        .then(emprestimo => {
            window.location.href = `form_emprestimo.html?id=${id}&data_emprestimo=${emprestimo.data_emprestimo}&data_devolucao=${emprestimo.data_devolucao}&livro=${emprestimo.id_livro}&pessoa=${emprestimo.id_pessoa}`;
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao editar o empréstimo. Por favor, tente novamente.',
                confirmButtonColor: '#007bff'
            });
        });
}

async function confirmDeleteEmprestimo(id) {
    Swal.fire({
        title: 'Tem certeza?',
        text: "Você não poderá reverter isso!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#007bff',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sim, excluir!',
        cancelButtonText: 'Cancelar'
    }).then(async (result) => {
        if (result.isConfirmed) {
            await deleteEmprestimo(id);
        }
    });
}

async function deleteEmprestimo(id) {
    try {
        const response = await fetch(`${baseUrlEmprestimos}/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Excluído!',
                text: 'O empréstimo foi excluído com sucesso.',
                confirmButtonColor: '#007bff'
            });
            getEmprestimos();
        } else {
            throw new Error('Erro ao excluir o empréstimo.');
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Erro',
            text: 'Não foi possível excluir o empréstimo.',
            confirmButtonColor: '#007bff'
        });
    }
}

function loadLivroFromEmprestimo(id) {
    return fetch(`${API_URL}/livros/${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar livro');
            }
            return response.json();
        })
        .then(livro => livro.titulo)
        .catch(error => {
            console.error("Erro ao buscar livro: ", error);
            return 'Erro ao carregar livro';
        });
}

function loadPessoaFromEmprestimo(id) {
    return fetch(`${API_URL}/pessoas/${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar pessoa');
            }
            return response.json();
        })
        .then(pessoa => pessoa.nome)
        .catch(error => {
            console.error("Erro ao buscar pessoa: ", error);
            return 'Erro ao carregar pessoa';
        });
}

function formatDate(dateString) {
    if (!dateString) {
        return '';
    }
    const [year, month, day] = dateString.split('-');
    return `${day}/${month}/${year}`;
}
