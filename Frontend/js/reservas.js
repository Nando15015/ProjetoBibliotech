async function getReservas() {
    const response = await fetch(`${API_URL}/reservas`);
    const reservas = await response.json();

    const listaReservas = document.getElementById('listaReservas');
    listaReservas.innerHTML = '';

    reservas.forEach(reserva => {
        const row = document.createElement('tr');

        loadLivroFromReserva(reserva.id_livro).then(nomeLivro => {
            loadPessoaFromReserva(reserva.id_pessoa).then(nomePessoa => {
                row.innerHTML = `
                    <td class="no-hover">
                        <button class="btn-edit" onclick="editReserva(${reserva.id})" aria-label="Editar Reserva">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-delete" onclick="deleteReserva(${reserva.id})" aria-label="Excluir Reserva">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                    <td>${nomeLivro}</td>
                    <td>${nomePessoa}</td>
                    <td>${formatDate(reserva.data_reserva)}</td>
                    <td>${formatDate(reserva.data_validade)}</td>
                `;
                listaReservas.appendChild(row);
            });
        });
    });
}

function editReserva(id) {
    fetch(`${API_URL}/reservas/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar os dados da reserva');
            }
            return response.json();
        })
        .then(reserva => {
            window.location.href = `form_reserva.html?id=${id}&data_reserva=${reserva.data_reserva}&data_validade=${reserva.data_validade}&id_livro=${reserva.id_livro}&id_pessoa=${reserva.id_pessoa}`;
        })
        .catch(error => {
            console.error('Erro:', error);
            Swal.fire({
                icon: 'error',
                title: 'Erro',
                text: 'Erro ao editar a reserva. Por favor, tente novamente.'
            });
        });
}

async function deleteReserva(id) {
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
        const response = await fetch(`${API_URL}/reservas/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            Swal.fire(
                'Excluído!',
                'Reserva excluída com sucesso.',
                'success'
            );
            getReservas();
        } else {
            Swal.fire(
                'Erro!',
                'Erro ao excluir a reserva.',
                'error'
            );
        }
    }
}

async function loadLivroFromReserva(id_livro) {
    const response = await fetch(`${API_URL}/livros/${id_livro}`);
    const livro = await response.json();
    return livro.titulo;
}

async function loadPessoaFromReserva(id_pessoa) {
    const response = await fetch(`${API_URL}/pessoas/${id_pessoa}`);
    const pessoa = await response.json();
    return pessoa.nome;
}

function formatDate(dateString) {
    const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
    return new Date(dateString).toLocaleDateString('pt-BR', options);
}