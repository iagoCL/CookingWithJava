package com.TheJavaCooker.CookingWithJava.DataBase.Entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Table(name = "Comentario",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"fecha_comentario", "usuario_id", "receta_id"}, name = Comentario.constraintComentarioUnico)
        }
)
@Entity(name = "Comentario")
public class Comentario implements Comparable<Comentario> {
    public static final String constraintComentarioUnico = "CONSTRAINT_COMENTARIO_UNICO";
    public static DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("dd LLL yy - HH:MM");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(nullable = false, name = "descripcion_comentario")
    private String descripcion_comentario;
    @Column(nullable = false, name = "titulo_comentario")
    private String titulo_comentario;
    @Column(nullable = false, name = "fecha_comentario")
    private LocalDateTime fecha_comentario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta receta_id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario_id;

    protected Comentario() {
    }

    public LocalDateTime getFechaComentario() {
        return fecha_comentario;
    }

    public void resetFechaComentario() {
        setFechaComentario(LocalDateTime.now());
    }

    public void setFechaComentario(LocalDateTime fecha_comentario_) {
        this.fecha_comentario =
                fecha_comentario_.minusNanos(fecha_comentario_.getNano())
                        .minusSeconds(fecha_comentario_.getSecond() % 20);
    }

    public String getDescripcionComentario() {
        return descripcion_comentario;
    }

    public String getTituloComentario() {
        return titulo_comentario;
    }

    public Comentario(String descripcion_comentario,
                      String titulo_comentario,
                      LocalDateTime fecha_comentario,
                      Receta receta_id,
                      Usuario usuario_id) {
        this.descripcion_comentario = descripcion_comentario;
        this.titulo_comentario = titulo_comentario;
        setFechaComentario(fecha_comentario);
        this.receta_id = receta_id;
        this.usuario_id = usuario_id;
    }

    public Usuario getUsuario() {
        return usuario_id;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "id=" + id +
                ", descripcion_comentario='" + descripcion_comentario + '\'' +
                ", titulo_comentario='" + titulo_comentario + '\'' +
                ", fecha_comentario=" + fecha_comentario.format(formatoFechaHora) +
                ", receta_id=" + receta_id.getId() +
                ", usuario_id=" + usuario_id.getId() +
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
        if (other_ == null || other_.fecha_comentario == null)
            return 1;
        int fecha = other_.fecha_comentario.compareTo(this.fecha_comentario);
        if (fecha == 0) {
            return (int) (this.id - other_.id);
        } else {
            return fecha;
        }
    }
}
