CREATE TABLE skills (
  skill_id int(11) NOT NULL,
  skill_name varchar(255),
  PRIMARY KEY (skill_id)
);

CREATE TABLE persons (
  person_id int(11) NOT NULL,
  person_name varchar(255) DEFAULT NULL,
  person_age int(11) NOT NULL,
  skill_id int(11),
  PRIMARY KEY (person_id),
  FOREIGN KEY (skill_id) REFERENCES skills(skill_id)
);
