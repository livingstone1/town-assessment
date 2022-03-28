CREATE TABLE d_region
(
    id   SERIAL CONSTRAINT d_region_pk PRIMARY KEY,
    code VARCHAR(16) UNIQUE NOT NULL,
    name VARCHAR(64) NOT NULL
);
COMMENT ON TABLE d_region IS 'Регион';
COMMENT ON COLUMN d_region.id IS 'Идентификатор';
COMMENT ON COLUMN d_region.code IS 'Код';
COMMENT ON COLUMN d_region.name IS 'Наименование';

CREATE TABLE d_town
(
    id   SERIAL CONSTRAINT d_town_pk PRIMARY KEY,
    code VARCHAR(16) UNIQUE NOT NULL,
    name VARCHAR(64) NOT NULL,
    region_id INTEGER NOT NULL
);
ALTER TABLE d_town
    ADD CONSTRAINT d_town_region_fk FOREIGN KEY (region_id) REFERENCES d_region(id) ON DELETE CASCADE;
COMMENT ON TABLE d_town IS 'Город';
COMMENT ON COLUMN d_town.id IS 'Идентификатор';
COMMENT ON COLUMN d_town.code IS 'Код';
COMMENT ON COLUMN d_town.name IS 'Наименование';
COMMENT ON COLUMN d_town.region_id IS 'Регион';

CREATE TABLE d_criteria_type
(
    id   SERIAL CONSTRAINT d_criteria_type_pk PRIMARY KEY,
    code VARCHAR(16) UNIQUE NOT NULL,
    name VARCHAR(64) NOT NULL
);
COMMENT ON TABLE d_criteria_type IS 'Тип критерия';
COMMENT ON COLUMN d_criteria_type.id IS 'Идентификатор';
COMMENT ON COLUMN d_criteria_type.code IS 'Код';
COMMENT ON COLUMN d_criteria_type.name IS 'Наименование';

CREATE TABLE block
(
    id   SERIAL CONSTRAINT block_pk PRIMARY KEY,
    name VARCHAR(1024) UNIQUE NOT NULL,
    k NUMERIC NOT NULL DEFAULT 1,
    update_date TIMESTAMP(2) WITHOUT TIME ZONE,
    delete_date TIMESTAMP(2) WITHOUT TIME ZONE
);
COMMENT ON TABLE block IS 'Концептуальный блок';
COMMENT ON COLUMN block.id IS 'Идентификатор';
COMMENT ON COLUMN block.name IS 'Наименование';
COMMENT ON COLUMN block.k IS 'Коэффициент';
COMMENT ON COLUMN block.update_date IS 'Дата-время последнего изменения';
COMMENT ON COLUMN block.delete_date IS 'Дата удаления';

CREATE TABLE criteria
(
    id   SERIAL CONSTRAINT criteria_pk PRIMARY KEY,
    d_criteria_type_id INTEGER NOT NULL,
    name VARCHAR(1024) UNIQUE NOT NULL,
    k NUMERIC NOT NULL DEFAULT 1,
    is_stimulus BOOLEAN NOT NULL DEFAULT TRUE,
    description VARCHAR,
    update_date TIMESTAMP(2) WITHOUT TIME ZONE,
    delete_date TIMESTAMP(2) WITHOUT TIME ZONE,
    block_id INTEGER NOT NULL
);
ALTER TABLE criteria
    ADD CONSTRAINT criteria_type_fk FOREIGN KEY (d_criteria_type_id) REFERENCES d_criteria_type(id);
ALTER TABLE criteria
    ADD CONSTRAINT criteria_block_fk FOREIGN KEY (block_id) REFERENCES block(id) ON DELETE CASCADE;
COMMENT ON TABLE criteria IS 'Критерий';
COMMENT ON COLUMN criteria.id IS 'Идентификатор';
COMMENT ON COLUMN criteria.d_criteria_type_id IS 'Тип критерия';
COMMENT ON COLUMN criteria.name IS 'Наименование';
COMMENT ON COLUMN criteria.k IS 'Коэффициент';
COMMENT ON COLUMN criteria.is_stimulus IS 'Стимулирующий ли критерий';
COMMENT ON COLUMN criteria.description IS 'Описание';
COMMENT ON COLUMN criteria.update_date IS 'Дата-время последнего изменения';
COMMENT ON COLUMN criteria.delete_date IS 'Дата удаления';
COMMENT ON COLUMN criteria.block_id IS 'Блок';

CREATE TABLE assessment
(
    id   SERIAL CONSTRAINT assessment_pk PRIMARY KEY,
    town_id INTEGER NOT NULL,
    criteria_id INTEGER NOT NULL,
    value NUMERIC NOT NULL,
    description VARCHAR,
    update_date TIMESTAMP(2) WITHOUT TIME ZONE
);
ALTER TABLE assessment
    ADD CONSTRAINT assessment_town_fk FOREIGN KEY (town_id) REFERENCES d_town(id) ON DELETE CASCADE;
ALTER TABLE assessment
    ADD CONSTRAINT assessment_criteria_fk FOREIGN KEY (criteria_id) REFERENCES criteria(id) ON DELETE CASCADE;
ALTER TABLE assessment ADD CONSTRAINT assessment_town_criteria_unique UNIQUE (town_id, criteria_id);
COMMENT ON TABLE assessment IS 'Актуальная оценка городов';
COMMENT ON COLUMN assessment.id IS 'Идентификатор';
COMMENT ON COLUMN assessment.town_id IS 'Город';
COMMENT ON COLUMN assessment.criteria_id IS 'Критерий';
COMMENT ON COLUMN assessment.value IS 'Оценка';
COMMENT ON COLUMN assessment.description IS 'Описание';
COMMENT ON COLUMN assessment.update_date IS 'Дата-время последнего изменения';

CREATE TABLE assessment_hist
(
    id   SERIAL CONSTRAINT assessment_hist_pk PRIMARY KEY,
    town_id INTEGER NOT NULL,
    criteria_id INTEGER NOT NULL,
    value NUMERIC NOT NULL,
    description VARCHAR,
    update_date TIMESTAMP(2) WITHOUT TIME ZONE
);
ALTER TABLE assessment_hist
    ADD CONSTRAINT assessment_hist_town_fk FOREIGN KEY (town_id) REFERENCES d_town(id) ON DELETE CASCADE;
ALTER TABLE assessment_hist
    ADD CONSTRAINT assessment_hist_criteria_fk FOREIGN KEY (criteria_id) REFERENCES criteria(id) ON DELETE CASCADE;
COMMENT ON TABLE assessment_hist IS 'Историческая оценка городов';
COMMENT ON COLUMN assessment_hist.id IS 'Идентификатор';
COMMENT ON COLUMN assessment_hist.town_id IS 'Город';
COMMENT ON COLUMN assessment_hist.criteria_id IS 'Критерий';
COMMENT ON COLUMN assessment_hist.value IS 'Оценка';
COMMENT ON COLUMN assessment_hist.description IS 'Описание';
COMMENT ON COLUMN assessment_hist.update_date IS 'Дата-время последнего изменения';