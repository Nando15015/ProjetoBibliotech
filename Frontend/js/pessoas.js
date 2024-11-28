async function getPessoas() {
    const response = await fetch(`${API_URL}/pessoas`);
    const pessoas = await response.json();

    const listaPessoas = document.getElementById('listaPessoas');
    listaPessoas.innerHTML = '';

    pessoas.forEach(pessoa => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="no-hover">
                <button class="btn-edit" onclick="editPessoa(${pessoa.id})" aria-label="Editar Reserva">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn-delete" onclick="deletePessoa(${pessoa.id})" aria-label="Excluir Reserva">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
            <td>${pessoa.nome}</td>
            <td>${pessoa.email}</td>
            <td>${pessoa.telefone}</td>
        `;
        listaPessoas.appendChild(row);
    });
}

function editPessoa(id) {
    fetch(`${API_URL}/pessoas/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar os dados da pessoa');
            }
            return response.json();
        })
        .then(pessoa => {
            window.location.href = `form_pessoa.html?id=${id}&nome=${pessoa.nome}&email=${pessoa.email}&telefone=${pessoa.telefone}`;
        })
        .catch(error => {
            console.error("Erro:", error);
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao editar a pessoa. Por favor, tente novamente.'
            });
        });
}

async function deletePessoa(id) {
    const result = await Swal.fire({
        title: 'Tem certeza?',
        text: 'Você não poderá reverter isso!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sim, excluir!',
        cancelButtonText: 'Cancelar'
    });

    if (result.isConfirmed) {
        const response = await fetch(`${API_URL}/pessoas/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            Swal.fire(
                'Excluído!',
                'Pessoa excluída com sucesso.',
                'success'
            );
            getPessoas();
        } else {
            Swal.fire(
                'Erro!',
                'Erro ao excluir a pessoa.',
                'error'
            );
        }
    }
}