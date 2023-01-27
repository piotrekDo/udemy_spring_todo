DROP TABLE IF EXISTS tasks;
CREATE TABLE tasks(
  id INT primary key auto_increment,
  description VARCHAR(100) not null,
  done BIT default 0
);