package com.scuola.apichallenge.film;

public class Film {
    private String id;
    private String titolo;
    private int anno;
    private String regista;

    public Film() {}

    public Film(String id, String titolo, int anno, String regista) {
        this.id = id;
        this.titolo = titolo;
        this.anno = anno;
        this.regista = regista;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public int getAnno() { return anno; }
    public void setAnno(int anno) { this.anno = anno; }

    public String getRegista() { return regista; }
    public void setRegista(String regista) { this.regista = regista; }
}
