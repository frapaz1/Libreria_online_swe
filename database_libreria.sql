--
-- PostgreSQL database dump
--

\restrict 5QHRbtl9kVcDe48nsq3xnpUCWPAsTRV4NNWGjsBmY7OO5L8swz1unegDGBzBr4W

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-04-30 16:36:33

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 16417)
-- Name: libro; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.libro (
    id integer NOT NULL,
    isbn character varying(20) NOT NULL,
    titolo character varying(150) NOT NULL,
    autore character varying(100),
    editore character varying(100),
    prezzo numeric(6,2) NOT NULL,
    quantita_magazzino integer NOT NULL
);


ALTER TABLE public.libro OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16416)
-- Name: libro_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.libro_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.libro_id_seq OWNER TO postgres;

--
-- TOC entry 5015 (class 0 OID 0)
-- Dependencies: 219
-- Name: libro_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.libro_id_seq OWNED BY public.libro.id;


--
-- TOC entry 4856 (class 2604 OID 16420)
-- Name: libro id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.libro ALTER COLUMN id SET DEFAULT nextval('public.libro_id_seq'::regclass);


--
-- TOC entry 5009 (class 0 OID 16417)
-- Dependencies: 220
-- Data for Name: libro; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.libro (id, isbn, titolo, autore, editore, prezzo, quantita_magazzino) FROM stdin;
5	978-88-38	Piccole Donne	Louisa May Alcott	Mondadori	9.90	8
6	978-88-07	Sapiens: Da animali a dei	Yuval Noah Harari	Bompiani	18.00	11
3	978-88-17	Il Signore degli Anelli	J.R.R. Tolkien	Bompiani	25.00	0
2	978-88-06	Il Nome della Rosa	Umberto Eco	Bompiani	14.00	4
4	978-88-31	Harry Potter e la Pietra Filosofale	J.K. Rowling	Salani	10.50	8
1	978-88-04	1984	George Orwell	Mondadori	12.50	4
\.


--
-- TOC entry 5016 (class 0 OID 0)
-- Dependencies: 219
-- Name: libro_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.libro_id_seq', 6, true);


--
-- TOC entry 4858 (class 2606 OID 16429)
-- Name: libro libro_isbn_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.libro
    ADD CONSTRAINT libro_isbn_key UNIQUE (isbn);


--
-- TOC entry 4860 (class 2606 OID 16427)
-- Name: libro libro_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.libro
    ADD CONSTRAINT libro_pkey PRIMARY KEY (id);


-- Completed on 2026-04-30 16:36:33

--
-- PostgreSQL database dump complete
--

\unrestrict 5QHRbtl9kVcDe48nsq3xnpUCWPAsTRV4NNWGjsBmY7OO5L8swz1unegDGBzBr4W

