# Servicios de usuarios y Postman

Cada funcion se desarrollo en su rama: `Editar`, `Logico` y `Fisico`.
`Eliminar` integra las dos eliminaciones y `Crud` integra las tres funciones.

## Arrancar la version integrada

Detener la ejecucion anterior de Spring Boot con Ctrl+C y ejecutar desde la raiz:

```powershell
git switch Crud
.\gradlew.bat bootRun
```

Se requiere JDK 21. La URL base es `http://localhost:8080`.
La configuracion actual usa H2 en memoria: los usuarios de una ejecucion anterior
no se conservan al reiniciar. Crear un usuario nuevo antes de probar.

## Importar y ejecutar

En Postman, seleccionar Import y abrir `postman/Crud.postman_collection.json`.
Ejecutar las solicitudes numeradas en orden, o ejecutar la coleccion con Run.
La primera solicitud crea un usuario de prueba y guarda automaticamente su ID
en la variable de coleccion `usuarioId`. La variable `baseUrl` permite cambiar el puerto.
Las solicitudes incluyen comprobaciones de respuesta en sus scripts de pruebas.

Las colecciones `Editar`, `Logico` y `Fisico` permiten probar cada funcion por separado
en su rama o sobre la version integrada de `Crud`.

## Endpoints

| Metodo | Ruta | Resultado |
| --- | --- | --- |
| POST | `/usuarios` | Crea un usuario activo; devuelve 200 y el usuario con ID. |
| GET | `/usuarios` | Lista solo usuarios activos; devuelve 200. |
| PUT | `/usuarios/{id}` | Actualiza nombre y email; devuelve 200 y el usuario. |
| DELETE | `/usuarios/{id}/logico` | Marca activo=false sin borrar el registro; devuelve 204 sin cuerpo. |
| DELETE | `/usuarios/{id}/fisico` | Borra definitivamente el registro, incluso si esta inactivo; devuelve 204 sin cuerpo. |

Para POST y PUT, usar Body -> raw -> JSON, con Content-Type: application/json:

```json
{
  "nombre": "Gabriel actualizado",
  "email": "gabriel@example.com"
}
```

PUT toma el ID de la URL y no cambia el ID del registro. En `Crud`, un usuario
inactivo no puede editarse. Un ID inexistente devuelve 404. Repetir una baja logica
devuelve 204 mientras el registro exista; repetir un borrado fisico devuelve 404.
Las solicitudes DELETE y GET no necesitan cuerpo.

## Pruebas automatizadas

```powershell
.\gradlew.bat test
```

Las pruebas comprueban edicion, conservacion de datos tras la baja logica,
filtrado del listado, borrado fisico de usuarios activos e inactivos y errores 404.
Las pruebas HTTP arrancan su propio servidor en un puerto aleatorio y una base
H2 de prueba, sin utilizar la aplicacion abierta en el puerto 8080.
