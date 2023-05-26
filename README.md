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

1. Crear un nuevo proyecto **jacx** para especificar las [credencias](https://console.cloud.google.com/project/_/google/maps-apis/credentials?utm_source=Docs_Credentials&hl=es-419&_gl=1*1txe4fw*_ga*MjM3MDU4NTIyLjE2Nzk0NDA4Njk.*_ga_NRWSTWS78N*MTY4NTEyMzQyMi4xLjEuMTY4NTEyMzYxOS4wLjAuMA..)

2. Seleccionar la clave API para la que deseas establecer una restricción.

3. Cuando registre la aplicación, incluir el nombre del paquete (com.apm.jacx) para que el servicio de cuentas de Google devuelva la llamada a la aplicación después de autorizarla. Además, se debe añadir la huella digital del certificado.

Una vez realizamos los pasos anteriores, guardamos.
