package com.TheJavaCooker.CookingWithJava.DataBase;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity(name = "Comentario")
@Table(name = "comentario",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"fechaComentario", "usuario_id", "receta_id"}, name = Comentario.constraintComentarioUnico)
        }
)
public class Comentario implements Comparable<Comentario> {
    public static final String constraintComentarioUnico = "CONSTRAINT_COMENTARIO_UNICO";
    public static DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("dd LLL yy - HH:MM");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(nullable = false)
    private String descripcionComentario;
    @Column(nullable = false)
    private String tituloComentario;
    @Column(nullable = false)
    private LocalDateTime fechaComentario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta recetaId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioId;

    protected Comentario() {
    }

    public LocalDateTime getFechaComentario() {
        return fechaComentario;
    }

    public void resetFechaComentario() {
        setFechaComentario(LocalDateTime.now());
    }

    public void setFechaComentario(LocalDateTime fechaComentario_) {
        this.fechaComentario =
                fechaComentario_.minusNanos(fechaComentario_.getNano())
                        .minusSeconds(fechaComentario_.getSecond() % 20);
    }

    public String getDescripcionComentario() {
        return descripcionComentario;
    }

    public String getTituloComentario() {
        return tituloComentario;
    }

    public Comentario(String descripcionComentario,
                      String tituloComentario,
                      LocalDateTime fechaComentario,
                      Receta recetaId,
                      Usuario usuarioId) {
        this.descripcionComentario = descripcionComentario;
        this.tituloComentario = tituloComentario;
        setFechaComentario(fechaComentario);
        this.recetaId = recetaId;
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "id=" + id +
                ", descripcionComentario='" + descripcionComentario + '\'' +
                ", tituloComentario='" + tituloComentario + '\'' +
                ", fechaComentario=" + fechaComentario.format(formatoFechaHora) +
                ", recetaId=" + recetaId.getId() +
                ", usuarioId=" + usuarioId.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comentario that = (Comentario) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Comentario other_) {
        int fecha = other_.fechaComentario.compareTo(this.fechaComentario);
        if (fecha == 0) {
            return (int) (this.id - other_.id);
        } else {
            return fecha;
        }
    }
}
