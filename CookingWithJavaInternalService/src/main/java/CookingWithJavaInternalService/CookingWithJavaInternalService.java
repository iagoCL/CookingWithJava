package CookingWithJavaInternalService;
public class CookingWithJavaInternalService {
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        if( args.length>0 && args[0].equals("-d"))
        {
            PersonalDebug.setDebug(true);
            PersonalDebug.imprimir("Servicio interno iniciado");
        }
        servidor.start();
        PersonalDebug.imprimir("Servicio interno Cerrado");

    }
}
