# TeLoTengoCA

## Descripción del Proyecto

TeLoTengoCA es una aplicación de gestión de ventas diseñada para facilitar el manejo de productos, facturas y empleados en una tienda. La aplicación permite a los vendedores registrar ventas, gestionar inventarios y generar reportes de ventas.

## Estructura del Proyecto

El proyecto está organizado en varios paquetes y archivos, cada uno con una responsabilidad específica:

### Paquetes Principales

- **Models**: Contiene las clases que representan las entidades del negocio, como `Product`, `Bill`, `BillDetail`, `Employee` y `Log`.
- **Repositories**: Contiene las clases que manejan la persistencia de datos, como `BillRepository`, `BillDetailRepository`, `ProductRepository` y `EmployeeRepository`.
- **Views**: Contiene las clases que manejan la interfaz gráfica de usuario, como `VendorScreen`, `AdminScreen` y `LoginScreen`.
- **Utils**: Contiene utilidades como `CSVReader` y `Cypher` para la lectura de archivos CSV y el cifrado de datos.
- **State**: Contiene una clase llamada `AppContext` que simula la sesión en la app, y persiste el objeto del usuario que se loguea en la aplicación.

### Archivos Clave

- **build.xml**: Archivo de configuración de Ant para la construcción del proyecto.
- **nbproject/**: Contiene archivos de configuración específicos de NetBeans, como `project.properties` y `build-impl.xml`.
- **src/**: Directorio principal del código fuente del proyecto.
- **manifest.mf**: Archivo de manifiesto para la configuración del JAR ejecutable.

## Requisitos del Sistema

- **Java**: JDK 8 o superior.
- **NetBeans**: IDE recomendado para el desarrollo y la gestión del proyecto.

## Instrucciones de Instalación

1. Clonar el repositorio en tu máquina local.
2. Abrir el proyecto en NetBeans.
3. Configurar las propiedades del proyecto según sea necesario.
4. Construir el proyecto utilizando el archivo `build.xml`.

## Uso

1. Ejecutar la clase principal `TeLoTengoCA` desde NetBeans o desde la línea de comandos.
2. Iniciar sesión en la aplicación utilizando las credenciales de un empleado registrado.
3. Utilizar las diferentes pantallas para gestionar productos, registrar ventas y generar reportes.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.
