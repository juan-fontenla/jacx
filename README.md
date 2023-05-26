# JACX

## Spotify
A la hora de comprobar el funcionamiento en Spotify es necesario realizar los siguientes pasos:

1. Registrar la aplicación  [panel de control del desarrollador de Spotify](https://developer.spotify.com/dashboard)
2. Obtener una identificación de cliente (CLIENT_ID)
3. Cuando registre la aplicación, incluir una URI de redirección (jacx://authcallback) para que el servicio de cuentas de Spotify devuelva la llamada a la aplicación después de autorizarla.
4. Agregar el nombre del paquete (com.apm.jacx) y la huella digital de la aplicación, y así, verificar la identidad de la aplicación. Para ello ejecute el siguiente comando en su terminal:

```
keytool -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore -list -v
```

Una vez ejecutado el comando anterior, debería recibir una huella digital similar a esta:

`SHA1: E7:47:B5:45:71:A9:B4:47:EA:AD:21:D7:7C:A2:8D:B4:89:1C:BF:75`

Copia la huella digital y el nombre del paquete e ingrésalo en el [panel de control del desarrollador de Spotify](https://developer.spotify.com/dashboard), en la sección `Editar configuración`.

## Google

Para poder establecer la conexión con una cuenta de google es necesario realizar los siguientes pasos:

1. Iniciar sesión en google cloud [enlace](https://console.cloud.google.com/welcome?_ga=2.209109969.-2128765216.1681486254&_gac=1.195838686.1685131589.CjwKCAjwscGjBhAXEiwAswQqNK2stkv5JqBgcN8E4wEKR3Q7zaihHnrQp2gpnt9YZFQvqyOYv_GsIBoCaJoQAvD_BwE&hl=es&authuser=1&project=jacx-apm) con la cuenta del equipo. Ver que está JACX como proyecto seleccionado.

2. Seleccionar la clave API para la que deseas establecer una restricción, en este caso OAuth 2.0. Se podría modificar la existente o crear una nueva.

3. Si se modifica la existente, solo sería necesario actualizar la huella digital del certificado. En otro caso, cuando registre la aplicación, incluir el nombre del paquete (com.apm.jacx) para que el servicio de cuentas de Google devuelva la llamada a la aplicación después de autorizarla. Además, se debe añadir la huella digital del certificado. Para generar la huella es necesario realizar lo mismo que para spotify:

```
keytool -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore -list -v
```

Una vez ejecutado el comando anterior, debería recibir una huella digital similar a esta:

`SHA1: E7:47:B5:45:71:A9:B4:47:EA:AD:21:D7:7C:A2:8D:B4:89:1C:BF:75`

Una vez realizamos los pasos anteriores, guardamos.
