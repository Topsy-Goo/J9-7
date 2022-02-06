CREATE TABLE students
(	id			bigserial,
	name		VARCHAR(256)	NOT NULL,
	age			INT 			NOT NULL,
	created_at	TIMESTAMP DEFAULT current_timestamp,
	updated_at	TIMESTAMP DEFAULT current_timestamp,
	PRIMARY KEY (id)
);
INSERT INTO students (name, age) VALUES
('Twenty One', 21), ('Twenty Two', 22), ('Twenty Three', 23), ('Twenty Four', 24), ('Twenty Five', 25);
