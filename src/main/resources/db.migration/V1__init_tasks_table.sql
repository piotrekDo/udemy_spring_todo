DROP TABLE IF EXISTS tasks;
CREATE TABLE tasks(
    id int primary key auto_increment,
    description varchar(100) not null,
    done bit default 0
)