--
-- PostgreSQL database dump
--

\restrict wgFhCC3g9a12Vw6gMvbJlz96qnt5G7NtMCOmHYp9eqD7DYVSedwg2c8bOrzll9J

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-04-30 17:44:56

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
-- TOC entry 5023 (class 0 OID 0)
-- Dependencies: 219
-- Name: libro_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.libro_id_seq OWNED BY public.libro.id;


--
-- TOC entry 221 (class 1259 OID 16430)
-- Name: preferiti; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.preferiti (
    id_libro integer NOT NULL
);


ALTER TABLE public.preferiti OWNER TO postgres;

--
-- TOC entry 4860 (class 2604 OID 16420)
-- Name: libro id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.libro ALTER COLUMN id SET DEFAULT nextval('public.libro_id_seq'::regclass);


--
-- TOC entry 5016 (class 0 OID 16417)
-- Dependencies: 220
-- Data for Name: libro; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.libro (id, isbn, titolo, autore, editore, prezzo, quantita_magazzino) FROM stdin;
7	978-88-06	Il Nome della Rosa	Umberto Eco	Bompiani	14.00	5
8	978-88-17	Il Signore degli Anelli	J.R.R. Tolkien	Bompiani	25.00	3
9	978-88-31	Harry Potter e la Pietra Filosofale	J.K. Rowling	Salani	10.50	15
10	978-88-38	Piccole Donne	Louisa May Alcott	Mondadori	9.90	8
11	978-88-07	Sapiens: Da animali a dei	Yuval Noah Harari	Bompiani	18.00	12
12	978-88-22	One Piece - Vol. 1	Eiichiro Oda	Star Comics	5.20	20
13	978-88-91	La Profezia dell'Armadillo	Zerocalcare	Bao Publishing	18.00	7
14	978-88-69	Batman: Il Ritorno del Cavaliere Oscuro	Frank Miller	Panini Comics	22.50	4
15	978-88-08	Fisica 1 - Meccanica e Termodinamica	Resnick, Halliday	CEA	55.00	10
16	978-88-24	I Promessi Sposi (Ed. Scolastica)	Alessandro Manzoni	Zanichelli	15.50	30
17	978-88-84	Matematica Blu 2.0	Massimo Bergamini	Zanichelli	32.90	25
18	978-88-04	1984	George Orwell	Mondadori	11.50	14
19	978-88-11	Orgoglio e Pregiudizio	Jane Austen	Garzanti	8.50	9
20	978-88-45	Dune	Frank Herbert	Fanucci	20.00	6
21	978-01-32	Clean Code	Robert C. Martin	Prentice Hall	35.00	5
\.


--
-- TOC entry 5017 (class 0 OID 16430)
-- Dependencies: 221
-- Data for Name: preferiti; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.preferiti (id_libro) FROM stdin;
\.


--
-- TOC entry 5024 (class 0 OID 0)
-- Dependencies: 219
-- Name: libro_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.libro_id_seq', 21, true);


--
-- TOC entry 4862 (class 2606 OID 16429)
-- Name: libro libro_isbn_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.libro
    ADD CONSTRAINT libro_isbn_key UNIQUE (isbn);


--
-- TOC entry 4864 (class 2606 OID 16427)
-- Name: libro libro_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.libro
    ADD CONSTRAINT libro_pkey PRIMARY KEY (id);


--
-- TOC entry 4866 (class 2606 OID 16435)
-- Name: preferiti preferiti_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.preferiti
    ADD CONSTRAINT preferiti_pkey PRIMARY KEY (id_libro);


--
-- TOC entry 4867 (class 2606 OID 16436)
-- Name: preferiti preferiti_id_libro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.preferiti
    ADD CONSTRAINT preferiti_id_libro_fkey FOREIGN KEY (id_libro) REFERENCES public.libro(id) ON DELETE CASCADE;


-- Completed on 2026-04-30 17:44:56

--
-- PostgreSQL database dump complete
--

\unrestrict wgFhCC3g9a12Vw6gMvbJlz96qnt5G7NtMCOmHYp9eqD7DYVSedwg2c8bOrzll9J

