CREATE TABLE projects(
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(100) NOT NULL
);

ALTER TABLE TASK_GROUPS
    ADD COLUMN project_id INT NULL;
ALTER TABLE TASK_GROUPS
    ADD FOREIGN KEY (project_id)
    REFERENCES projects(id);

CREATE TABLE project_steps(
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(100) NOT NULL,
    project_id INT,
    days_to_deadline INT,
    CONSTRAINT FK_ProjectsProject_steps FOREIGN KEY (project_id)
                          REFERENCES projects(id)
);