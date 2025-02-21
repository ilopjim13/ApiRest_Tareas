## Nombre del proyecto

- ApiRest_Tareas

## Descripción de los documentos

- Para este proyecto tendremos los siguientes documentos:
  - Usuarios: Que contendrá los datos de los usuarios como su username, password, roles y dirección.
  - Tareas: Con los datos de las tareas, nombre, descripción, fecha, estado.
  - Direccion: Con los datos de las direcciones de los usuarios.

## Endpoints a desarrollar

- Para el usuario vamos a utilizar el Endpoint de "/usuarios". Dentro de este endpoint vamos a generar otros diferentes endpoints que ejecutarán las funciones:
  - POST "/login" que realizará el logeo de un usuario, o dará fallo si se equivoca.
  - POST "/register" que permite registrar un usuario que no exista ya y que tenga sus campos correctamente.

 prueba
- Para las tareas utilizaremos el endpoint "/tareas". Y dentro tendremos estos endopoints:
  - GET "/mostrar/{username}" que muestra las tareas de un usuario.
  - GET "/mostrarTodas" que muestra todas las tareas pero debes ser admin para poder verlas.
  - POST "/agregarTarea/{username}" que agrega una tarea a un usuario.
  - POST "/actualizarEstado/{username}" que actualiza el estado de la tarea.
  - DELETE "/eliminarTarea{username}" que permite eliminar una tarea de un usuario.

## Lógica de negocio

- Para el documento usuario:
  - Para registrarse el usuario deberá tener todos sus campos correctamente y no tener campos únicos iguales a alguno en la base de datos.
  - Para que se inserte correctamente deberá tener en la dirección una dirección que exista.
  - Para logearse el usuario deberá existir en la base de datos y que los datos coincidan, tanto username como password.


- Para las tareas se diferenciará en dos tipos de usuarios según sus roles:
  - USER: Estos usuarios solo tendrán acceso a sus tareas y podrán borrar, actualizar o insertar tareas propias, no la de los demás.
  - ADMIN: Estos usuarios con poder administrativo podrán ver todas las tareas, eliminar cualquier tarea de cualquier usuario, dar de altar tareas a cualquier usuario y marcar cualquier tarea como realizada.

## Excepciones

- Tendremos varias excepciones en nuestro programa:
  - NotFoundException que dará un error 404 al no encontrar datos en la base de datos.
  - BadRequestException que dará un error 400 cuando algo haya salido como no se esperaba.
  - UnauthorizedException que dará un error 401 cuando no se pueda autorizar el usuario en el login.

## Restricciones 

- Las restricciones de nuestro proyecto se dividirán en públicos:
  - Los endpoints para login y register que podrá acceder cualquier persona.
  

- Y privados, de los cuales se divirán en dos a su vez:
  - Los privados que podrán entrar todos los usuarios en donde pueden entrar en los endpoints referentes a sus propias tareas.
  - Y los privados solo para los ADMIN: que solo pueden acceder los usuarios con rol ADMIN, como por ejemplo al endpoint de mostrarTodas.

## PRUEBAS GESTIÓN USUARIOS

# Login
- Para logearnos en la app deberemos introducir nuestro usuario y contraseña que exista en la base de datos. 
  - Para que funcione los campos deben estar rellenados correctamente ya que si no saltará un mensaje de error.

  ![img_6.png](src/main/resources/pruebas/img_6.png)


  - La primera prueba que vamos a realizar es la de ingresar un usuario y contraseña erróneo para ver como nos salta la excepción en ambos lados. En este caso vamos a intentar logearnos con Pepito78 y con la contraseña 771122 que no existe en la base de datos.
  
  ![img_1.png](src/main/resources/pruebas/img_1.png)


  - Le daremos a Iniciar sesión y nos saltará una ventana emergente con el error que nos está mandando la API, que podremos ver en la app, en el log y en la api.
  
  ![img_2.png](src/main/resources/pruebas/img_2.png)
  ![img_3.png](src/main/resources/pruebas/img_3.png)


  - En cambio si introducimos datos correctos como el usuario jose y la contraseña 1234 nos devolverá el token con el que podremos iniciar sesión y obtener las tareas de jose.
  
  ![img_4.png](src/main/resources/pruebas/img_4.png)


  - Como podemos ver en el log de la imagen en Login habremos obtenido el token y habremos entrado en la app mostrando las tareas del usuario. Ya que si miramos la base de datos tenemos 3 tareas y solo una es de jose.
  
  ![img_5.png](src/main/resources/pruebas/img_5.png)

- Aqui tenemos un video de prueba del login: https://drive.google.com/file/d/1hrbgAJx3_XmbZ7LFET6CEthBx-Gcv7RB/view?usp=drive_link

# Register

- Una vez hemos terminado el login ahora pasaremos con el registro. Para el registro la app pedirá al usuario que ingrese todos sus datos para agregar al usuario. Para que funcione deberemos rellenar todos los campos y que el formato del gmail sea correcto. Aunque solo falte uno dará fallo.

![img_11.png](src/main/resources/pruebas/img_11.png)

  - Si nos equivocamos introduciendo las contraseñas iguales nos saltará un error de la api.

  ![img_7.png](src/main/resources/pruebas/img_7.png)
  ![img_8.png](src/main/resources/pruebas/img_8.png)


  - Si introducimos un municipio no válido nos dará un error indicando que ese municipio no es válido.

  ![img_9.png](src/main/resources/pruebas/img_9.png)


  - Lo mismo pasará si nos equivocamos en la provincia.

  ![img_10.png](src/main/resources/pruebas/img_10.png)


  - Y por último pero no menos importante si intentamos agregar un usuario ya existente nos dará error

  ![img_12.png](src/main/resources/pruebas/img_12.png)


  - Tambíen en la api porbamos que el rol sea admin o user aunque en esta parte lo he facilitado al usuario utilizando un menú desplegable para que no pueda introducir otro valor que no sea USER o ADMIN, aunque lo dejo en la api por si las moscas.

  ![img.png](src/main/resources/pruebas/img13.png)
  
- Ahora ingresaremos datos correctamente y se registrará el usuario y nos mandará a la pantalla del login para que iniciemos sesión con ese usuario

![img_3.png](src/main/resources/pruebas/img_16.png)
![img_1.png](src/main/resources/pruebas/img_14.png)

- Como podemos observar en el log se ha registrado y si miramos en la base de datos podremos ver como está añadido.

![img_2.png](src/main/resources/pruebas/img_15.png)

- Y aquí tenemos un vide de prueba del register: https://drive.google.com/file/d/1HpMDthfmTVLv4zpzO8Xvu_AHsMNeNxAH/view?usp=drive_link