DROP VIEW IF EXISTS public.position;
DROP TABLE IF EXISTS public.security_order CASCADE;
DROP TABLE IF EXISTS public.account CASCADE;
DROP TABLE IF EXISTS public.trader CASCADE;
DROP TABLE IF EXISTS public."quote" CASCADE;

CREATE TABLE public."quote" (
	ticker varchar NOT NULL,
	last_price float8 NOT NULL,
	bid_price float8 NOT NULL,
	bid_size int4 NOT NULL,
	ask_price float8 NOT NULL,
	ask_size int4 NOT NULL,
	CONSTRAINT quote_pk PRIMARY KEY (ticker)
);

CREATE TABLE public.trader (
	id serial NOT NULL,
	first_name varchar NOT NULL,
	last_name varchar NOT NULL,
	dob date NOT NULL,
	country varchar NOT NULL,
	email varchar NOT NULL,
	CONSTRAINT trader_pk PRIMARY KEY (id)
);

CREATE TABLE public.account (
	id serial NOT NULL,
	trader_id int4 NOT NULL,
	amount float8 NOT NULL,
	CONSTRAINT account_pk PRIMARY KEY (id)
);

ALTER TABLE public.account ADD CONSTRAINT account_trader_fk FOREIGN KEY (trader_id) REFERENCES trader(id);

CREATE TABLE public.security_order (
	id serial NOT NULL,
	account_id int4 NOT NULL,
	status varchar NOT NULL,
	ticker varchar NOT NULL,
	"size" int4 NOT NULL,
	price float8 NULL,
	notes varchar NULL,
	CONSTRAINT security_order_pk PRIMARY KEY (id)
);

ALTER TABLE public.security_order ADD CONSTRAINT security_order_account_fk FOREIGN KEY (account_id) REFERENCES account(id);
ALTER TABLE public.security_order ADD CONSTRAINT security_order_quote_fk FOREIGN KEY (ticker) REFERENCES quote(ticker);

CREATE OR REPLACE VIEW public."position"
AS SELECT security_order.account_id,
    security_order.ticker,
    sum(security_order.size) AS "position"
   FROM security_order
  WHERE security_order.status::text = 'FILLED'::text
  GROUP BY security_order.account_id, security_order.ticker;