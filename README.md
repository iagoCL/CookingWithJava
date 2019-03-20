- [CookingWithJava](#cookingwithjava)
  - [EQUIPO](#equipo)
  - [FASE 1 - Equipo de desarrollo y temática de la web](#fase-1---equipo-de-desarrollo-y-tem%C3%A1tica-de-la-web)
    - [Descripción de la temática de la web](#descripci%C3%B3n-de-la-tem%C3%A1tica-de-la-web)
    - [Nombre y descripción de las entidades principales.](#nombre-y-descripci%C3%B3n-de-las-entidades-principales)
    - [Descripción de las funcionalidades del servicio interno](#descripci%C3%B3n-de-las-funcionalidades-del-servicio-interno)
  - [FASE 2 - Desarrollo de la aplicación web en local](#fase-2---desarrollo-de-la-aplicaci%C3%B3n-web-en-local)
    - [Capturas de pantalla y una breve descripción de cada una de las páginas principales](#capturas-de-pantalla-y-una-breve-descripci%C3%B3n-de-cada-una-de-las-p%C3%A1ginas-principales)
    - [Página de incio](#p%C3%A1gina-de-incio)
    - [Acceso](#acceso)
    - [Subir receta](#subir-receta)
    - [Buscar recetas](#buscar-recetas)
    - [Recetas](#recetas)
    - [Perfil](#perfil)
    - [Diagrama de navegación de las principales páginas.](#diagrama-de-navegaci%C3%B3n-de-las-principales-p%C3%A1ginas)
    - [Modelo de datos de la aplicación](#modelo-de-datos-de-la-aplicaci%C3%B3n)
      - [Diagrama de clases UML](#diagrama-de-clases-uml)
      - [Diagrama Entidad/Relación que muestre cómo se persisten dichos datos en la base de datos relacional](#diagrama-entidadrelaci%C3%B3n-que-muestre-c%C3%B3mo-se-persisten-dichos-datos-en-la-base-de-datos-relacional)
  - [FASE 3 - Inclusión de seguridad y servicio interno](#fase-3---inclusi%C3%B3n-de-seguridad-y-servicio-interno)
    - [Instrucciones de despliegue](#instrucciones-de-despliegue)
      - [Instrucciones de despliegue windows](#instrucciones-de-despliegue-windows)
      - [Intrucciones de despliegue en ubuntu 18.04 server](#intrucciones-de-despliegue-en-ubuntu-1804-server)
  - [FASE 4 - Incluir tolerancia a fallos en la aplicación](#fase-4---incluir-tolerancia-a-fallos-en-la-aplicaci%C3%B3n)
  - [FASE 5 - Automatizar el despliegue de la aplicación.](#fase-5---automatizar-el-despliegue-de-la-aplicaci%C3%B3n)

# CookingWithJava
Este proyecto consiste en la práctica final de la asignatura de Desarrollo de Aplicaciones Distribuidas del grado de Ingeniería de Computadores de la URJC. Consistente en realizar una aplicación web de temática libre.

## EQUIPO
Nombre | Apellidos | Grado | Correo | Cuenta de GitHub
-- | -- | -- | -- | --
Iago | Calvo Lista | Videojuegos + Ing. de Computadores | [i.calvol@alumnos.urjc.es](mailto:i.calvol@alumnos.urjc.es) | [iagoCL](https://github.com/iagoCL)
Javier | Mártinez Pablo | Videojuegos + Ing. de Computadores | [j.martinezpa@alumnos.urjc.es](mailto:j.martinezpa@alumnos.urjc.es) | [JaviBJ99](https://github.com/JaviBJ99)
Juan | Jimenez Galvez | Videojuegos + Ing. de Computadores | [j.jimenezgal@alumnos.urjc.es](mailto:j.martinezpa@alumnos.urjc.es) | [Who1ne](https://github.com/Who1ne)

## FASE 1 - Equipo de desarrollo y temática de la web
### Descripción de la temática de la web
Para la temática de la web se ha decidido hacer una web de recetas de cocina, en la que los usuarios podrán ir subiendo sus propias recetas y compartirlas con la comunidad. Permitiendo comentar dichas recetas, marcarlas como favoritas o buscar una receta por distintos campos.

A continuación, se procede hacer un resumen de las principales funcionalidades que se planea soportar en nuestra página web junto a su nivel de acceso:
* **Funcionalidades públicas**: Disponibles a todos los usuarios.
  * **Visitar una receta**: Se podrá visualizar y consultar la receta.
  * **Buscar una receta**: Se podrá consultar en la base de datos las recetas disponibles, pudiéndose realizar búsquedas por plato, nombre, ingredientes, usuario, etc.
  * **Registro o *login***: Que permitirá el acceso a la parte privada.
* **Funcionalidades privadas**: Disponibles solo a los usuarios registrados.
  * **Marcar una receta como favorita**: Se podrá marcar una receta como favorita.
  * **Consultar las recetas favoritas**: Se podrá consultar un listado de todas las recetas marcadas como favoritas.
  * **Crear una nueva receta**: Se podrá crear una nueva receta.
  * **Consultar las recetas creadas**: Se podrá consultar un listado de todas las recetas creadas por el usuario.
  * **Comentar en una receta**: Se podrá comentar en una receta.

### Nombre y descripción de las entidades principales.
Las principales entidades que se planea implementar en nuestra aplicación son:
* **Usuario**: Contiene a un usuario en concreto.
  * **Identificador de usuario**: Identificación interna del usuario. Es usado como identificador y se autogenera. 
  * **Nombre de usuario**: *Nick* público en los comentarios. Es una variable requerida. 
  * **Contraseña**: Es una variable requerida. 
  * **Correo electrónico**: Usado para enviar notificaciones. Es una variable requerida. 
* **Receta**: Contiene a una receta creada por un usuario.
  * **Identificador de receta**: Identificación interna de la receta. Es usado como identificador y se autogenera. 
  * **Plato de la receta**: Tipo de plato de la receta (ej.: pastel de chocolate). Es una variable requerida. 
  * **Nombre de la receta**: Nombre concreto de esta receta (ej.: pastel de chocolate al estilo de Martita). Es una variable requerida. 
  * **Usuario Creador de la receta**: Identificador de usuario del creador de la receta. Es una variable requerida. 
  * **Preparación de la receta**: Explicación de cómo realizar la receta. Es una variable requerida y será de tipo texto largo.
  * **Imagen de la receta**: Foto de la receta. Es una variable opcional y se analizara detalladamente la complejidad que implique su complejidad. 
  * **Números de favoritos**: Es una variable obtenida de buscar en otras tablas. 
  * **Número de comentarios**:  Es una variable obtenida de buscar en otras tablas.
* **Ingrediente**: Contiene los distintos ingredientes de una receta para así facilitar la búsqueda de recetas por ingredientes.
  * **Nombre del ingrediente**: Es una variable requerida. 
  * **Cantidad del ingrediente**: Cantidad usada en la receta. Es una variable requerida. 
  * **Identificador de la receta**: Identificador de la receta donde se usa el ingrediente. Es una variable requerida. 
* **Utensilio de cocina**: Contiene los diferentes utensilios para crear una receta, como ollas, planchas y demás.
  * **Nombre del utensilio**: Es una variable requerida.
  * **Identificador de receta**: Identificación de la receta que usa los utensilios marcados. Es una variable requerida.
* **Comentario**: Contiene un comentario hecho por un usuario a una receta.
  * **Identificador de usuario**: Identificación interna del usuario que ha hecho el comentario. Es una variable requerida.
  * **Identificador de receta**: Identificación interna de la receta donde se ha hecho el comentario. Es una variable requerida.
  * **Fecha**: Día y hora cuándo se realizó el comentario. Es una variable requerida.
  * **Texto del comentario**: Texto puesto en el comentario. Es una variable requerida.

### Descripción de las funcionalidades del servicio interno
Las principales funcionalidades de las que se ha pensado en dotar al servicio interno son:
* Un sistema de **correo electrónico** que notifique a los usuarios cuando una de sus recetas sea marcada como favorita o cuando esta reciba algún comentario.
* Generar un **documento PDF** de la receta, permitiendo guardar la receta en local para un usuario, se planea basarse en este tutorial: <http://chuwiki.chuidiang.org/index.php?title=Ejemplo_sencillo_de_creaci%C3%B3n_de_un_pdf_con_iText>.

## FASE 2 - Desarrollo de la aplicación web en local
### Capturas de pantalla y una breve descripción de cada una de las páginas principales


### Página de incio


![alt text](gddImages/index.JPG "Figura 1: Primera imagen de la página de inicio sin logearse.")

*Figura 1: Primera imagen de la página de inicio sin logearse.*


![alt text](gddImages/index_logeado.JPG "Figura 1: Primera imagen de la página de inicio con el usuario logeado.")

*Figura 2: Primera imagen de la página de inicio con el usuario logeado.*


![alt text](gddImages/index_2.JPG "Figura 1: Segunda imagen de la página de inicio.")

*Figura 3: Segunda imagen de la página de inicio.*

* Está página actuará como inicio de la página web. Aquí, el usuario podrá acceder a las funciones principales tales como subir una nueva receta, logearse y registrarse, acceder a su perfil, ver todas las recetas y buscar recetas concretas.


### Acceso


![alt text](gddImages/register_1.JPG "Figura 3: Primera imagen de la página donde el usuario podrá logearse o registarse.")

*Figura 4: Primera imagen de la página donde el usuario podrá logearse o registarse.*

![alt text](gddImages/register_2.JPG "Figura 4: Segunda imagen de la página donde el usuario podrá logearse o registarse.")

*Figura 5: Segunda imagen de la página donde el usuario podrá logearse o registarse.*

![alt text](gddImages/register_3.JPG "Figura 5: Tercera imagen de la página donde el usuario podrá logearse o registarse.")

*Figura 6: Tercera imagen de la página donde el usuario podrá logearse o registarse.*

* En esta página el usuario podrá o bien registrarse como nuevo usuario, donde tendrá que rellenar los campos de nombre y apellidos, nickname, correo electrónico y contraseña; o bien logearse directamente si ya tiene cuenta, indicando su nick y su contraseña.



### Subir receta


![alt text](gddImages/nuevareceta_1.JPG "Figura 6: Primera imagen de la página que el usuario usará para subir recetas nuevas.")

*Figura 7: Primera imagen de la página que el usuario usará para subir recetas nuevas.*

![alt text](gddImages/nuevareceta_2.JPG "Figura 7: Segunda imagen de la página que el usuario usará para subir recetas nuevas.")

*Figura 8: Segunda imagen de la página que el usuario usará para subir recetas nuevas.*

![alt text](gddImages/nuevarectea_3.JPG "Figura 8: Tercera imagen de la página que el usuario usará para subir recetas nuevas.")

*Figura 9: Tercera imagen de la página que el usuario usará para subir recetas nuevas.*

![alt text](gddImages/nuevareceta_4.JPG "Figura 9: Cuarta imagen de la página que el usuario usará para subir recetas nuevas.")

*Figura 10: Cuarta imagen de la página que el usuario usará para subir recetas nuevas.*

* En esta sección de la página, el usuario podrá crear y subir nuevas recetas. Para poder crear nuevas recetas, deberá rellenar una serie de campos en los que se incluyen: nombre de la receta, tipo de plato, nivel de dificultad,los nombres y las cantidades de  los diferentes ingredientes, los utensilios que se van a usar y los pasos a seguir para crear la receta.


### Buscar recetas

![alt text](gddImages/buscarReceta_1.JPG "Figura 10: Primera imagen del buscador de recetas.")

*Figura 11: Primera imagen del buscador de recetas.*

![alt text](gddImages/buscarReceta_2.JPG "Figura 11: Segunda imagen del buscador de recetas.")

*Figura 12: Segunda imagen del buscador de recetas.*

* En esta página, el usuario podrá buscar recetas en función del nombre de la receta, el tipo de plato, el nivel de dificultad, el número de pasos, la duración y el número de comentarios o de favoritos que tiene.


### Recetas

![alt text](gddImages/recetas.JPG "Figura 12: Imagen de la página donde se muestran todas las recetas.")

*Figura 13: Imagen de la página donde se muestran todas las recetas.*

* En esta parte de la web se mostrarán todas las recetas subidas de todos los usuarios.

![alt text](gddImages/receta.JPG "Figura 12: Ejemplo de receta.")

*Figura 14: Ejemplo de receta.*






### Perfil

![alt text](gddImages/perfil.JPG "Figura 13: Página del perfil del usuario.")

*Figura 15: Página del perfil del usuario.*

* Aquí se mostrará la información del usuario y dos links a sus recetas y su recetas favoritas.














### Diagrama de navegación de las principales páginas.
A continuación, se muestra un diagrama simplificado de la navegación en la web.


![alt text](gddImages/paginas.jpg "Figura 14: Diagrama de navegación.")

*Figura 16: Diagrama de navegación.*

Se ha decidido usar este esquema más simplificado porque en la aplicación que se ha desarrollado, se puede acceder a cualquier página desde la página en la que esté el usuario y, por lo tanto, daría como resultado un diagrama de navegación casi ilegible, pues habría que unir cada página con el resto. Como resultado, se ha optado por definir este esquema en el que se explica que desde la barra de navegación, presente en todas las páginas, puedes acceder al resto.



### Modelo de datos de la aplicación
#### Diagrama de clases UML
<img src="gddImages/database-export.png">
*Figura 17: Diagrama de clases UML.*


#### Diagrama Entidad/Relación que muestre cómo se persisten dichos datos en la base de datos relacional
<img src="gddImages/entidadrelacion.jpg">
*Figura 18: Diagrama entidad/relación.*

## FASE 3 - Inclusión de seguridad y servicio interno
### Diagrama de clases de la aplicación
![alt text](gddImages/diagrama_clases.jpg "Figura 14: Diagrama de clases de la aplicación.")

*Figura 19: Diagrama de clases de la aplicación.*

### Instrucciones de despliegue
#### Instrucciones de despliegue windows
Es necesario java-jre versión 9 o superior para ello se puede usar el gestor de paquetes [chocolatey](https://chocolatey.org/). Una vez instalado chocolatey se puede simplemente instalar con ejecutar como administrador:

```
choco install server-jre9 -y
```
A continuación se procedará a instalar mysql 8.0.15; siguiendo con cholatey:
```
choco install mysql --version 8.0.15 -y
```

Despues se debe abrir una consola mysql con permisos necesarios, para ello en windows se puede ejecutar para que pregunte la contraseña del administrador de la base de datos (nula por defecta):
```
mysql -u root -p
```
A continuacion se debe configuar la base de datos y otorgar permiso al usuario por defecto.
```
create database db_cooking_with_java; -- Crea la base de datos
create user 'cookingWithJavaDefaultUser'@'%' identified by 'cookingWithJavaDefaultPass'; -- Crea el usaurio por defecto
grant all on db_cooking_with_java.* to 'cookingWithJavaDefaultUser'@'%'; --Otorga privilegios al usuario sobre la base de datos
```
Finalmente la aplicación puede ser ejecutada con 
```
java -jar ruta/del/archivo
```
#### Intrucciones de despliegue en ubuntu 18.04 server
Lo primero que se hará es configurar ssh para permitir control remoto de la máquina, para ello intale los paquetes necesarios:
```
sudo apt intall nano openssh-server -y
```
A continuacion se debe editar el archivo /etc/ssh/sshd_config para cambiar la linea *#Port 22* a *Port 1337*. Para ello ejecute:
```
sudo nano /etc/ssh/sshd_config
```
Cambie la linea y presione *ctrl+x*, a continuación presion *Y* y finalmente *enter*. A continuación ya podremos conectarnos desde nuestra máquina local.

En caso de usar virtualbox se deberá poner como tipo de conexión y hacer un reenvio de puertos del 1337 al 1337.
```
ssh username@ip -p1337
```
En caso de que nuestro usuario sea thejavacookers y estemos usando la ip predefinida de virtual box:
```
ssh thejavacookers@127.0.0.1 -p1337
```
Empezamos installando java 9 o superior.
```
sudo add-apt-repository ppa:webupd8team/java -y
sudo apt update
sudo apt install default-jre -y
```
Continuamos intallando mysql:
```
sudo apt install mysql-server -y
```
Abrimos una consola mysql y creamos la base de datos y damos permiso a la aplicion para modificarla con:
```
sudo mysql
create database db_cooking_with_java; 
create user 'cookingWithJavaDefaultUser'@'%' identified by 'cookingWithJavaDefaultPass'; 
grant all on db_cooking_with_java.* to 'cookingWithJavaDefaultUser'@'%';
exit;
```
Finalmente envaimos el jar ejecutando en el remoto:
```
scp -P 1337 ruta/local/archivo.jar nombreUsuario@ip:/ruta/destino
```
Supondremos que lo guardaremos en home y que abrimos una consola en la carpeta del jar:
```
scp -P 1337 ./CookingWithJava.jar thejavacookers@127.0.0.1:/home/thejavacookers
scp -P 1337 ./CookingWithJavaInternalService.jar thejavacookers@127.0.0.1:/home/thejavacookers
```
Finalmente se pueden ejecutar con:
```
java -jar CookingWithJava.jar
java -jar CookingWithJavaInternalService.jar
```
Notose que si se usa virtualbox se debería hacer un reenvío de puertos del 9000 al 9000 (servicio interno) y otro para el 8443 (conexión https).
## FASE 4 - Incluir tolerancia a fallos en la aplicación

## FASE 5 - Automatizar el despliegue de la aplicación.
