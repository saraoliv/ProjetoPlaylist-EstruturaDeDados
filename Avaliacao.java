

public class Avaliacao {
    private String nomeUsuario;
    private double nota;
    private String comentario;

    public Avaliacao(String nomeUsuario, double nota, String comentario) {
        this.nomeUsuario = nomeUsuario;
        this.nota = nota;
        this.comentario = comentario;
    }
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public double getNota() {
        return nota;
    }

    @Override
    public String toString() {
        return nomeUsuario + " - Nota: " + nota + " - " + comentario;
    }
}

