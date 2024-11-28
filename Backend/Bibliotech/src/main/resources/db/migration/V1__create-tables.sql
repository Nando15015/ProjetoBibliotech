create table genero(
	id int not null auto_increment primary key,
	nome varchar(255)
);

create table autor(
	id int not null auto_increment primary key,
	nome varchar(255)
);

create table livro(
	id int not null auto_increment primary key,
	titulo varchar(255),
	isbn varchar(30),
	ano_publicacao int,
	status varchar(20),
	foto longtext,
	id_genero int,
	id_autor int,
	foreign key(id_genero) references genero(id),
	foreign key(id_autor) references autor(id)
);

create table pessoa(
	id int not null auto_increment primary key,
	nome varchar(120),
	email varchar(255),
	telefone varchar(20)
);

create table emprestimo(
	id int not null auto_increment primary key,
	data_emprestimo varchar(20),
	data_devolucao varchar(20),
	id_livro int,
	id_pessoa int,
	foreign key(id_livro) references livro(id),
	foreign key(id_pessoa) references pessoa(id)
);

create table reserva(
	id int not null auto_increment primary key,
	data_reserva varchar(20),
	data_validade varchar(20),
	id_livro int,
	id_pessoa int,
	foreign key(id_livro) references livro(id),
	foreign key(id_pessoa) references pessoa(id)
);