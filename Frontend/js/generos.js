async function getGeneros() {
    try {
        const response = await fetch(`${API_URL}/generos`);
        if (!response.ok) throw new Error('Erro ao buscar gêneros');
        const generos = await response.json();

        const listaGeneros = document.getElementById('listaGeneros');
        listaGeneros.innerHTML = '';

        generos.forEach(genero => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td class="no-hover">
                    <button class="btn-edit" onclick="editGenero(${genero.id})" aria-label="Editar Gênero">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn-delete" onclick="deleteGenero(${genero.id})" aria-label="Excluir Gênero">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
                <td>${genero.nome}</td>
            `;
            listaGeneros.appendChild(row);
        });
    } catch (error) {
        console.error("Erro ao carregar gêneros:", error);
        swal.fire({
            icon: 'error',
            title: 'Erro',
            text: 'Erro ao carregar a lista de gêneros. Tente novamente mais tarde.',
        });
    }
}

function editGenero(id) {
    fetch(`${API_URL}/generos/${id}`)
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar os dados do gênero');
            return response.json();
        })
        .then(genero => {
            window.location.href = `form_genero.html?id=${id}&nome=${genero.nome}`;
        })
        .catch(error => {
            console.error("Erro:", error);
            swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao editar o gênero. Por favor, tente novamente.',
            });
        });
}

async function deleteGenero(id) {
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
                const response = await fetch(`${API_URL}/generos/${id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    swal.fire({
                        icon: 'success',
                        title: 'Excluído!',
                        text: 'Gênero excluído com sucesso.',
                    });
                    getGeneros();
                } else {
                    swal.fire({
                        icon: 'error',
                        title: 'Erro',
                        text: 'Erro ao excluir o gênero. Por favor, tente novamente.',
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