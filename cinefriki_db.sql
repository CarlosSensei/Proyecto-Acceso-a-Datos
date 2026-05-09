--
-- PostgreSQL database dump
--

-- Dumped from database version 15.12 (Homebrew)
-- Dumped by pg_dump version 15.12 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
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
-- Name: directores; Type: TABLE; Schema: public; Owner: iCarlos
--

CREATE TABLE public.directores (
    id_director integer NOT NULL,
    nombre character varying(100) NOT NULL,
    nacionalidad character varying(50)
);


ALTER TABLE public.directores OWNER TO "iCarlos";

--
-- Name: directores_id_director_seq; Type: SEQUENCE; Schema: public; Owner: iCarlos
--

CREATE SEQUENCE public.directores_id_director_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.directores_id_director_seq OWNER TO "iCarlos";

--
-- Name: directores_id_director_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: iCarlos
--

ALTER SEQUENCE public.directores_id_director_seq OWNED BY public.directores.id_director;


--
-- Name: generos; Type: TABLE; Schema: public; Owner: iCarlos
--

CREATE TABLE public.generos (
    id_genero integer NOT NULL,
    nombre character varying(50) NOT NULL
);


ALTER TABLE public.generos OWNER TO "iCarlos";

--
-- Name: generos_id_genero_seq; Type: SEQUENCE; Schema: public; Owner: iCarlos
--

CREATE SEQUENCE public.generos_id_genero_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.generos_id_genero_seq OWNER TO "iCarlos";

--
-- Name: generos_id_genero_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: iCarlos
--

ALTER SEQUENCE public.generos_id_genero_seq OWNED BY public.generos.id_genero;


--
-- Name: pelicula_genero; Type: TABLE; Schema: public; Owner: iCarlos
--

CREATE TABLE public.pelicula_genero (
    id_pelicula integer NOT NULL,
    id_genero integer NOT NULL
);


ALTER TABLE public.pelicula_genero OWNER TO "iCarlos";

--
-- Name: peliculas; Type: TABLE; Schema: public; Owner: iCarlos
--

CREATE TABLE public.peliculas (
    id_pelicula integer NOT NULL,
    titulo character varying(150) NOT NULL,
    anio integer NOT NULL,
    duracion integer,
    id_director integer NOT NULL,
    sinopsis text,
    CONSTRAINT peliculas_anio_check CHECK ((anio >= 1900)),
    CONSTRAINT peliculas_duracion_check CHECK ((duracion > 0))
);


ALTER TABLE public.peliculas OWNER TO "iCarlos";

--
-- Name: peliculas_id_pelicula_seq; Type: SEQUENCE; Schema: public; Owner: iCarlos
--

CREATE SEQUENCE public.peliculas_id_pelicula_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.peliculas_id_pelicula_seq OWNER TO "iCarlos";

--
-- Name: peliculas_id_pelicula_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: iCarlos
--

ALTER SEQUENCE public.peliculas_id_pelicula_seq OWNED BY public.peliculas.id_pelicula;


--
-- Name: valoraciones; Type: TABLE; Schema: public; Owner: iCarlos
--

CREATE TABLE public.valoraciones (
    id_valoracion integer NOT NULL,
    id_pelicula integer NOT NULL,
    usuario character varying(100) NOT NULL,
    puntuacion numeric(3,1),
    comentario text,
    CONSTRAINT valoraciones_puntuacion_check CHECK (((puntuacion >= (0)::numeric) AND (puntuacion <= (10)::numeric)))
);


ALTER TABLE public.valoraciones OWNER TO "iCarlos";

--
-- Name: valoraciones_id_valoracion_seq; Type: SEQUENCE; Schema: public; Owner: iCarlos
--

CREATE SEQUENCE public.valoraciones_id_valoracion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.valoraciones_id_valoracion_seq OWNER TO "iCarlos";

--
-- Name: valoraciones_id_valoracion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: iCarlos
--

ALTER SEQUENCE public.valoraciones_id_valoracion_seq OWNED BY public.valoraciones.id_valoracion;


--
-- Name: directores id_director; Type: DEFAULT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.directores ALTER COLUMN id_director SET DEFAULT nextval('public.directores_id_director_seq'::regclass);


--
-- Name: generos id_genero; Type: DEFAULT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.generos ALTER COLUMN id_genero SET DEFAULT nextval('public.generos_id_genero_seq'::regclass);


--
-- Name: peliculas id_pelicula; Type: DEFAULT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.peliculas ALTER COLUMN id_pelicula SET DEFAULT nextval('public.peliculas_id_pelicula_seq'::regclass);


--
-- Name: valoraciones id_valoracion; Type: DEFAULT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.valoraciones ALTER COLUMN id_valoracion SET DEFAULT nextval('public.valoraciones_id_valoracion_seq'::regclass);


--
-- Data for Name: directores; Type: TABLE DATA; Schema: public; Owner: iCarlos
--

COPY public.directores (id_director, nombre, nacionalidad) FROM stdin;
1	Stanley Kubrick	Estadounidense
2	George Lucas	Estadounidense
3	Irvin Kershner	Estadounidense
4	Ridley Scott	Británica
5	James Cameron	Canadiense
6	Lana Wachowski y Lilly Wachowski	Estadounidense
7	Steven Spielberg	Estadounidense
8	Robert Zemeckis	Estadounidense
9	Christopher Nolan	Británica
10	Denis Villeneuve	Canadiense
11	George Miller	Australiana
12	Peter Jackson	Neozelandesa
13	Hayao Miyazaki	Japonesa
14	Guillermo del Toro	Mexicana
15	Victor Fleming	Estadounidense
16	Rob Reiner	Estadounidense
17	Alfonso Cuarón	Mexicana
\.


--
-- Data for Name: generos; Type: TABLE DATA; Schema: public; Owner: iCarlos
--

COPY public.generos (id_genero, nombre) FROM stdin;
1	Ciencia ficción
2	Fantasía
3	Space opera
4	Aventura
5	Cyberpunk
6	Distopía
7	Terror
8	Acción
9	Viajes en el tiempo
10	Drama
11	Postapocalíptica
12	Épica
13	Animación
14	Filosófica
\.


--
-- Data for Name: pelicula_genero; Type: TABLE DATA; Schema: public; Owner: iCarlos
--

COPY public.pelicula_genero (id_pelicula, id_genero) FROM stdin;
1	1
1	14
2	1
2	3
2	4
3	1
3	3
3	4
4	1
4	7
5	1
5	5
5	6
6	1
6	8
7	1
7	5
8	1
8	4
9	1
9	4
9	9
10	1
10	10
11	1
11	10
12	1
12	3
12	4
13	1
13	8
13	11
14	2
14	4
14	12
15	2
15	4
15	12
16	2
16	4
16	13
17	2
17	10
18	2
18	4
19	2
19	4
20	2
20	4
21	1
\.


--
-- Data for Name: peliculas; Type: TABLE DATA; Schema: public; Owner: iCarlos
--

COPY public.peliculas (id_pelicula, titulo, anio, duracion, id_director, sinopsis) FROM stdin;
1	2001: Una odisea del espacio	1968	149	1	Una misión espacial se convierte en una reflexión sobre la evolución humana, la inteligencia artificial y el destino de la humanidad.
2	Star Wars: Una nueva esperanza	1977	121	2	Un joven granjero se une a la Rebelión para enfrentarse al Imperio Galáctico.
3	Star Wars: El Imperio contraataca	1980	124	3	La Rebelión continúa su lucha contra el Imperio mientras Luke Skywalker entrena con Yoda.
4	Alien, el octavo pasajero	1979	117	4	La tripulación de una nave espacial se enfrenta a una criatura alienígena letal.
5	Blade Runner	1982	117	4	En un futuro distópico, un blade runner debe perseguir a replicantes fugitivos.
6	Terminator 2: El juicio final	1991	137	5	Un cyborg es enviado para proteger al futuro líder de la resistencia humana.
7	Matrix	1999	136	6	Un hacker descubre que la realidad que conoce es una simulación controlada por máquinas.
8	E.T., el extraterrestre	1982	115	7	Un niño entabla amistad con un extraterrestre perdido en la Tierra.
9	Regreso al futuro	1985	116	8	Un adolescente viaja accidentalmente al pasado en una máquina del tiempo.
10	Interstellar	2014	169	9	Un grupo de astronautas viaja a través de un agujero de gusano para buscar un nuevo hogar para la humanidad.
11	Arrival	2016	116	10	Una lingüista intenta comunicarse con visitantes extraterrestres para evitar una crisis global.
12	Dune: Parte Dos	2024	166	10	Paul Atreides continúa su camino entre los fremen mientras se enfrenta al poder imperial.
13	Mad Max: Fury Road	2015	120	11	En un mundo postapocalíptico, Max se une a Furiosa en una huida salvaje por el desierto.
14	El Señor de los Anillos: La Comunidad del Anillo	2001	178	12	Un hobbit inicia una misión para destruir un anillo de poder capaz de dominar la Tierra Media.
15	El Señor de los Anillos: El retorno del rey	2003	201	12	La batalla final por la Tierra Media decide el destino del Anillo Único.
16	El viaje de Chihiro	2001	125	13	Una niña queda atrapada en un mundo espiritual y debe encontrar la forma de salvar a sus padres.
17	El laberinto del fauno	2006	118	14	Una niña descubre un mundo fantástico en la España de posguerra.
18	El mago de Oz	1939	102	15	Una joven viaja a un mundo mágico donde busca el camino de vuelta a casa.
19	La princesa prometida	1987	98	16	Una aventura fantástica llena de romance, humor, duelos y rescates imposibles.
20	Harry Potter y el prisionero de Azkaban	2004	142	17	Harry regresa a Hogwarts mientras un peligroso fugitivo parece ir tras él.
21	Avatar	2009	162	5	Un exmarine viaja al peligroso mundo de Pandora.
\.


--
-- Data for Name: valoraciones; Type: TABLE DATA; Schema: public; Owner: iCarlos
--

COPY public.valoraciones (id_valoracion, id_pelicula, usuario, puntuacion, comentario) FROM stdin;
1	1	Carlos	9.5	Obra maestra
2	2	Laura	8.8	Muy buena
3	3	Miguel	9.0	Clásico imprescindible
4	4	Ana	9.7	Brutal
5	21	Javier	9.3	Unica e imprescindible
6	14	Sandra	9.0	Un viaje fantástico que marcó a varias generaciones
\.


--
-- Name: directores_id_director_seq; Type: SEQUENCE SET; Schema: public; Owner: iCarlos
--

SELECT pg_catalog.setval('public.directores_id_director_seq', 17, true);


--
-- Name: generos_id_genero_seq; Type: SEQUENCE SET; Schema: public; Owner: iCarlos
--

SELECT pg_catalog.setval('public.generos_id_genero_seq', 14, true);


--
-- Name: peliculas_id_pelicula_seq; Type: SEQUENCE SET; Schema: public; Owner: iCarlos
--

SELECT pg_catalog.setval('public.peliculas_id_pelicula_seq', 21, true);


--
-- Name: valoraciones_id_valoracion_seq; Type: SEQUENCE SET; Schema: public; Owner: iCarlos
--

SELECT pg_catalog.setval('public.valoraciones_id_valoracion_seq', 6, true);


--
-- Name: directores directores_pkey; Type: CONSTRAINT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.directores
    ADD CONSTRAINT directores_pkey PRIMARY KEY (id_director);


--
-- Name: generos generos_nombre_key; Type: CONSTRAINT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.generos
    ADD CONSTRAINT generos_nombre_key UNIQUE (nombre);


--
-- Name: generos generos_pkey; Type: CONSTRAINT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.generos
    ADD CONSTRAINT generos_pkey PRIMARY KEY (id_genero);


--
-- Name: pelicula_genero pelicula_genero_pkey; Type: CONSTRAINT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.pelicula_genero
    ADD CONSTRAINT pelicula_genero_pkey PRIMARY KEY (id_pelicula, id_genero);


--
-- Name: peliculas peliculas_pkey; Type: CONSTRAINT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.peliculas
    ADD CONSTRAINT peliculas_pkey PRIMARY KEY (id_pelicula);


--
-- Name: valoraciones valoraciones_pkey; Type: CONSTRAINT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.valoraciones
    ADD CONSTRAINT valoraciones_pkey PRIMARY KEY (id_valoracion);


--
-- Name: peliculas fk_pelicula_director; Type: FK CONSTRAINT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.peliculas
    ADD CONSTRAINT fk_pelicula_director FOREIGN KEY (id_director) REFERENCES public.directores(id_director) ON DELETE RESTRICT;


--
-- Name: pelicula_genero pelicula_genero_id_genero_fkey; Type: FK CONSTRAINT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.pelicula_genero
    ADD CONSTRAINT pelicula_genero_id_genero_fkey FOREIGN KEY (id_genero) REFERENCES public.generos(id_genero) ON DELETE RESTRICT;


--
-- Name: pelicula_genero pelicula_genero_id_pelicula_fkey; Type: FK CONSTRAINT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.pelicula_genero
    ADD CONSTRAINT pelicula_genero_id_pelicula_fkey FOREIGN KEY (id_pelicula) REFERENCES public.peliculas(id_pelicula) ON DELETE CASCADE;


--
-- Name: valoraciones valoraciones_id_pelicula_fkey; Type: FK CONSTRAINT; Schema: public; Owner: iCarlos
--

ALTER TABLE ONLY public.valoraciones
    ADD CONSTRAINT valoraciones_id_pelicula_fkey FOREIGN KEY (id_pelicula) REFERENCES public.peliculas(id_pelicula) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

