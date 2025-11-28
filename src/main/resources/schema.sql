CREATE TABLE departments (
                             department_id BIGINT PRIMARY KEY,
                             name VARCHAR(100) NOT NULL UNIQUE,
                             established_date TIMESTAMP NOT NULL,
                             description TEXT NOT NULL,
                             created_at TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP
);

CREATE TABLE files (
                       file_id BIGINT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       file_type VARCHAR(100) NOT NULL,
                       size BIGINT NOT NULL,
                       created_at TIMESTAMP NOT NULL
);


CREATE TABLE employees (
                           employee_id BIGINT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(100) NOT NULL UNIQUE,
                           position VARCHAR(100) NOT NULL,
                           hire_date TIMESTAMP NOT NULL,
                           employee_number VARCHAR(255) NOT NULL UNIQUE,
                           status VARCHAR(100) NOT NULL DEFAULT 'ACTIVE',
                           created_at TIMESTAMP NOT NULL,
                           updated_at TIMESTAMP,
                           department_id BIGINT NOT NULL,
                           profile_id BIGINT UNIQUE,
                           CONSTRAINT fk_department_id FOREIGN KEY (department_id) REFERENCES departments (department_id),
                           CONSTRAINT fk_profile_id FOREIGN KEY (profile_id) REFERENCES files (file_id) ON DELETE SET NULL,
                           CONSTRAINT check_status CHECK (status IN ('ACTIVE', 'ON_LEAVE', 'RESIGNED'))
)


CREATE TABLE backups (
                         backup_id BIGINT PRIMARY KEY,
                         worker VARCHAR(100) NOT NULL,
                         started_at TIMESTAMP NOT NULL,
                         ended_at TIMESTAMP NOT NULL,
                         status VARCHAR(100) NOT NULL,
                         created_at TIMESTAMP NOT NULL,
                         file_id BIGINT UNIQUE,
                         CONSTRAINT fk_file_id FOREIGN KEY (file_id) REFERENCES files(file_id) ON DELETE SET NULL,
                         CONSTRAINT check_status CHECK (status IN ('IN_PROGRESS', 'COMPLETED','FAILED' , 'SKIPPED'))
);


CREATE TABLE change_logs(
                            change_log_id BIGINT PRIMARY KEY,
                            type VARCHAR(100) NOT NULL,
                            ip_address VARCHAR(255) NOT NULL,
                            at TIMESTAMP NOT NULL,
                            memo TEXT NOT NULL,
                            created_at TIMESTAMP NOT NULL,
                            employee_number VARCHAR(255) NOT NULL,
                            CONSTRAINT check_type CHECK (type IN ('CREATED','UPDATED', 'DELETED'))
);


CREATE TABLE diffs (
                       diff_id BIGINT PRIMARY KEY,
                       property_name VARCHAR(100) NOT NULL,
                       before VARCHAR(255),
                       after VARCHAR(255),
                       created_at TIMESTAMP NOT NULL,
                       change_log_id BIGINT NOT NULL,
                       CONSTRAINT fk_change_log_id FOREIGN KEY (change_log_id) REFERENCES change_logs(change_log_id) ON DELETE CASCADE
);