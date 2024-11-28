const baseUrl = `${API_URL}/autores`;

async function getAutores() {
    const response = await fetch(baseUrl);
    const autores = await response.json();

    const listaAutores = document.getElementById('listaAutores');
    listaAutores.innerHTML = '';

    autores.forEach(autor => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="no-hover">
                <button class="btn-edit" onclick="editAutor(${autor.id})" aria-label="Editar Autor">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn-delete" onclick="confirmDeleteAutor(${autor.id})" aria-label="Excluir Autor">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
            <td>${autor.nome}</td>
        `;
        listaAutores.appendChild(row);
    });
}

function editAutor(id) {
    fetch(`${baseUrl}/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar os dados do Autor');
            }
            return response.json();
        })
        .then(autor => {
            window.location.href = `form_autor.html?id=${id}&nome=${autor.nome}`;
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao editar o Autor. Por favor, tente novamente.',
                confirmButtonColor: '#007bff'
            });
        });
}

async function confirmDeleteAutor(id) {
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
            await deleteAutor(id);
        }
    });
}

async function deleteAutor(id) {
    try {
        const response = await fetch(`${baseUrl}/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Excluído!',
                text: 'O autor foi excluído com sucesso.',
                confirmButtonColor: '#007bff'
            });
            getAutores();
        } else {
            throw new Error('Erro ao excluir o autor.');
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Erro',
            text: 'Não foi possível excluir o autor.',
            confirmButtonColor: '#007bff'
        });
    }
}