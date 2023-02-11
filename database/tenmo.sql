BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transfer;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance numeric(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

-- INSERT INTO account(user_id, balance) VALUES (1001, 1000)
-- SELECT * FROM ACCOUNT

CREATE SEQUENCE seq_transfer_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;

CREATE TABLE transfer (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	from_user int NOT NULL,
	to_user int NOT NULL,
	TE_bucks numeric(13, 2) NOT NULL,
	transfer_status varchar DEFAULT 'APPROVED' NOT NULL,
	
	CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT fk_from_user FOREIGN KEY (from_user) REFERENCES account (account_id),
	CONSTRAINT fk_to_user FOREIGN KEY (to_user) REFERENCES account (account_id),
-- 	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT CHK_transfer_status CHECK (transfer_status IS NULL OR transfer_status IN ('APPROVED', 'PENDING', 'REJECTED')),
	CONSTRAINT CHK_different_account CHECK (from_user <> to_user),
	CONSTRAINT CHK_amount_over_zero CHECK (TE_bucks > 0)

-- 	mysql> create table id_parent_table (
--     -> main_id bigint unsigned not null,
--     -> parent_id bigint unsigned not null,
--     -> constraint columns_cannot_equal check (main_id <> parent_id)
--     -> );
	
-- 	add constraint so it only has Approved, Pending
	
	
-- 	A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
);



COMMIT;
Select * from transfer
SELECT * FROM transfer WHERE transfer_id = 3005 AND (to_user = 2001 OR from_user = 2001)
Select * from tenmo_user;
Select * from account;
Select * from transfer where from_user = 2001 or to_user = 2001
Select * from transfer where from_user = 2001
Select * from transfer where transfer_id = 3005 and from_user = 2001 OR to_user = 2001
Select * from transfer where from_user = 2001 OR to_user = 2001
SELECT balance FROM account JOIN tenmo_user on tenmo_user.user_id = account.user_id WHERE username = 'Gio'
SELECT * FROM transfer WHERE from_user = 1001 OR to_user = 1001
Select * from tenmo_user Join account on tenmo_user.user_id = account.user_id 

SELECT username, tenmo_user.user_id, account_id, balance FROM account JOIN tenmo_user on tenmo_user.user_id = account.user_id WHERE username = 'Gio';

