package com.TheJavaCooker.CookingWithJava.DataBase;

import com.TheJavaCooker.CookingWithJava.PersonalDebug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class DatabaseManager {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public enum Errores{
        SIN_ERRORES,
        NOMBRE_USUARIO_REPETIDO,
        CORREO_ELECTRONICO_REPETIDO,
        NOMBRE_USUARIO_NULO,
        CONTRASENA_NULA,
        CORREO_ELECTRONICO_NULO,
        NOMBRE_APELLIDOS_NULOS,
        ERRROR_DESCONOCIDO,
    }

    public void crearUsuariosEjemplo(int numUsuarios_){
        for( int i = 1; i<numUsuarios_; ++i) {
            crearUsuario("Usuario" + i,
                    "contasena" + i,
                    "correo" + i + "@example.com",
                    "Nombre"+i+" Apellidos"+i);
        }
    }

    public Pair<Errores, Usuario> crearUsuario(String nombreUsuario_, String contrasena_, String correo_, String nombreApellidos_) {
        return actualizarOCrearUsuario(new Usuario(nombreUsuario_, contrasena_, correo_, nombreApellidos_));
    }

    public Pair<Errores, Usuario> actualizarOCrearUsuario(Usuario usuario_){
        if (usuario_.getNombreUsuario().isEmpty()) {
            PersonalDebug.imprimir("Nombre de usuario nulo: " + usuario_.getNombreUsuario());
            return Pair.of(Errores.NOMBRE_USUARIO_NULO, usuario_);
        } else if (usuario_.getContrasena().isEmpty()) {
            PersonalDebug.imprimir("Contraseña nula: " + usuario_.getContrasena());
            return Pair.of(Errores.CONTRASENA_NULA, usuario_);
        } else if (usuario_.getNombreApellidos().isEmpty()) {
        PersonalDebug.imprimir("Nombre y apellidos nulos: " + usuario_.getNombreApellidos());
        return Pair.of(Errores.NOMBRE_APELLIDOS_NULOS, usuario_);
        } else if (usuario_.getCorreoElectronico().isEmpty()) {
        PersonalDebug.imprimir("Correo electronico nulo: " + usuario_.getCorreoElectronico());
        return Pair.of(Errores.CORREO_ELECTRONICO_NULO, usuario_);
        } else {
            try {
                usuarioRepository.save(usuario_);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                if (e.toString().contains(Usuario.constraintNombreUsuario)) {
                    PersonalDebug.imprimir("Nombre Usuario Repetido: " + e.toString());
                    return Pair.of(Errores.NOMBRE_USUARIO_REPETIDO, usuario_);
                } else if (e.toString().contains(Usuario.constraintCorreoElectronico)) {
                    PersonalDebug.imprimir("Correo electronico repetido: " + e.toString());
                    return Pair.of(Errores.CORREO_ELECTRONICO_REPETIDO, usuario_);
                } else {
                    PersonalDebug.imprimir("ConstraintViolationException desconocida: " + e.toString());
                    return Pair.of(Errores.ERRROR_DESCONOCIDO, usuario_);
                }

            } catch (Exception e) {
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                PersonalDebug.imprimir("Excepcion desconocida: " + e.toString());
                return Pair.of(Errores.ERRROR_DESCONOCIDO, usuario_);
            }
            return Pair.of(Errores.SIN_ERRORES, usuario_);
        }
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }

    public boolean testUsuarios(){
        crearUsuariosEjemplo(15);
        String nombreUsuario = "nombreUsuarioEjemplo";
        String correoElectronico = "correoDeEjemplo@example.com";
        String contrasena = "contraseñaDeEjemplo";
        String nombreApellidos = "nombreDeEjemplo ApellidosDeEjemplo";
        Pair<DatabaseManager.Errores,Usuario> p = crearUsuario(nombreUsuario,contrasena,correoElectronico,nombreApellidos);
        if(p.getFirst() != DatabaseManager.Errores.SIN_ERRORES)
        {
            PersonalDebug.imprimir("ERROR: SE DEBERIA PODER INSERTAR");
            return false;
        }
        Usuario usuarioDeEjemplo = p.getSecond();
        if ( crearUsuario(nombreUsuario,"XXX","XXX","XXX").getFirst() != DatabaseManager.Errores.NOMBRE_USUARIO_REPETIDO)
        {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE REPETIDO");
            assert(true);
        }
        else
        {
            PersonalDebug.imprimir("TEST: Detectado nombre de usuario repetido");
        }
        if ( crearUsuario("XXX","XXX",correoElectronico,"XXX").getFirst() != DatabaseManager.Errores.CORREO_ELECTRONICO_REPETIDO)
        {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL CORREO ELECTRONICO REPETIDO");
            return false;
        }
        else
        {
            PersonalDebug.imprimir("TEST: Detectado correo electronico repetido");
        }
        if ( crearUsuario("","XXX","XXX","XXX").getFirst() != DatabaseManager.Errores.NOMBRE_USUARIO_NULO)
        {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL NOMBRE DE USUARIO NULO");
            return false;
        }
        else
        {
            PersonalDebug.imprimir("TEST: Detectado nombre de usuario nulo");
        }
        if ( crearUsuario("XXX","","XXX","XXX").getFirst() != DatabaseManager.Errores.CONTRASENA_NULA)
        {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA LA CONTRASEÑA NULA");
            return false;
        }
        else
        {
            PersonalDebug.imprimir("TEST: Detectado contraseña  nula");
        }
        if ( crearUsuario("XXX","XXX","","XXX").getFirst() != DatabaseManager.Errores.CORREO_ELECTRONICO_NULO)
        {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA EL CORREO ELECTRONICO NULO");
            return false;
        }
        else
        {
            PersonalDebug.imprimir("TEST: Detectado correo electronico nulo");
        }
        if ( crearUsuario("XXX","XXX","XXX","").getFirst() != Errores.NOMBRE_APELLIDOS_NULOS)
        {
            PersonalDebug.imprimir("ERROR: NO SE DETECTA LOS NOMBRES Y APELLIDOS NULOS");
            return false;
        }
        else
        {
            PersonalDebug.imprimir("TEST: Detectado correo electronico nulo");
        }
        Usuario usuarioPorBusqueda = usuarioRepository.buscarPorCorreoElectronico(correoElectronico);
        if(!(usuarioDeEjemplo.equals(usuarioPorBusqueda)))
        {
            PersonalDebug.imprimir("ERROR: BUSCANDO POR CORREO ELECTRONICO, DEBERIAN SER IGUALES");
            return false;
        }
        else
        {
            PersonalDebug.imprimir("TEST: Busqueda por correo correcta");
        }
        usuarioPorBusqueda = usuarioRepository.buscarPorNombreUsuario(nombreUsuario);
        if(!(usuarioDeEjemplo.equals(usuarioPorBusqueda)))
        {
            PersonalDebug.imprimir("ERROR: BUSCANDO POR NOMBRE, DEBERIAN SER IGUALES");
            return false;
        }
        else
        {
            PersonalDebug.imprimir("TEST: Busqueda por nombre correcta");
        }


        long numUsuarios = usuarioRepository.count();
        usuarioPorBusqueda.setContrasena("nuevaContrasena");
        usuarioPorBusqueda.setCorreoElectronico("nuevCorreo");
        usuarioPorBusqueda.setNombreUsuario("nuevoNombre");
        usuarioPorBusqueda.setNombreApellidos("nuevosNombreYApellidos");
        p = actualizarOCrearUsuario(usuarioPorBusqueda);
        if(p.getFirst() != DatabaseManager.Errores.SIN_ERRORES)
        {
            PersonalDebug.imprimir("ERROR: SE DEBERÍA PODER INSERTAR");
            return false;
        }
        if(numUsuarios != usuarioRepository.count())
        {
            PersonalDebug.imprimir("ERROR: SE HA ACTUALIZADO Y NO INSERTADO");
            return false;
        }
        else
        {
            PersonalDebug.imprimir("TEST: Actualización correcta");
        }
        PersonalDebug.imprimir("TEST DE USUARIOS SUPERADO");
        return true;
    }
}
