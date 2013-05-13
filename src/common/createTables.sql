-- CREATE TABLE CAR
CREATE TABLE car (
	id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	vin VARCHAR(100) NOT NULL,
	spz VARCHAR(20) NOT NULL,
	name VARCHAR(100) NOT NULL,
	mileage DOUBLE NOT NULL
);

-- CREATE TABLE DRIVER
CREATE TABLE driver (
	id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name VARCHAR(100) NOT NULL,
	surname VARCHAR(20) NOT NULL,
	license_id VARCHAR(100) NOT NULL
);

-- CREATE TABLE RENTAL
CREATE TABLE rental (
	id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	driver_id BIGINT NOT NULL,
	car_id BIGINT NOT NULL,
	price DECIMAL(19,4) NOT NULL,
	start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	expected_end_time TIMESTAMP NOT NULL,
	end_time TIMESTAMP DEFAULT NULL,
	FOREIGN KEY (driver_id) REFERENCES driver, 
	FOREIGN KEY (car_id) REFERENCES car 
);