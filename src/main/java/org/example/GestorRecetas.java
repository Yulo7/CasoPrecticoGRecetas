package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorRecetas {
    private static final int LONGITUD_NOMBRE = 27;
    static File receta = new File("src/main/java/org/example/recetas.dat");
    static File documentoxml = new File("src/main/java/org/example/recetas.xml");

    public static void main(String[] args) {
        comprobarFichero();
        boolean salida = false;

        comprobar();
        while (!salida) {
            System.out.println("1. Añadir receta");
            System.out.println("2. Mostrar recetas");
            System.out.println("3. Modificar tiempo de preparación");
            System.out.println("4. Buscar receta por ID");
            System.out.println("5. Exportar recetas a XML");
            System.out.println("6. Salir");
            Scanner sc = new Scanner(System.in);
            int opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    anadir();
                    break;
                case 2:
                    mostrarRecetas();
                    break;
                case 3:
                    modificarTiempoPreparacion();
                    break;
                case 4:
                    buscarRecetaPorID();
                    break;
                case 5:
                    exportarXML();
                    break;
                case 6:
                    salida = true;
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private static List<Receta> leerRecetas() {
        List<Receta> recetas = new ArrayList<>();

        try (RandomAccessFile leer = new RandomAccessFile(receta, "r")) {
            while (leer.getFilePointer() < leer.length()) {
                char[] nombre = new char[LONGITUD_NOMBRE];
                for (int i = 0; i < LONGITUD_NOMBRE; i++) {
                    nombre[i] = leer.readChar();
                }
                String nombreReceta = new String(nombre).trim();
                int id = leer.readInt();
                int tiempo = leer.readInt();
                recetas.add(new Receta(id, tiempo, nombreReceta));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer las recetas", e);
        }

        return recetas;
    }

    private static void guardarRecetas(List<Receta> recetas) {
        try (RandomAccessFile archivo = new RandomAccessFile(receta, "rw")) {
            archivo.setLength(0);
            for (Receta recetaActual : recetas) {
                String nombreFormateado = String.format("%-" + LONGITUD_NOMBRE + "s", recetaActual.getNombre());
                archivo.writeChars(nombreFormateado.substring(0, LONGITUD_NOMBRE));
                archivo.writeInt(recetaActual.getId());
                archivo.writeInt(recetaActual.getTiempoPreparacion());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar las recetas", e);
        }
    }

    private static void comprobar() {
        if (receta.length() == 0) {
            List<Receta> recetasIniciales = new ArrayList<>();
            recetasIniciales.add(new Receta(1, 40, "Croquetas de jamon"));
            recetasIniciales.add(new Receta(2, 20, "macarrones con tomate"));
            recetasIniciales.add(new Receta(3, 20, "Pizza"));
            guardarRecetas(recetasIniciales);
            System.out.println("Registros almacenados correctamente.");
        }
    }

    public static void comprobarFichero() {
        if (!receta.exists()) {
            try {
                receta.createNewFile();
            } catch (Exception e) {
                System.out.println("Error al crear el fichero: " + e.getMessage());
            }
        }
    }

    private static void anadir() {
        Scanner sc = new Scanner(System.in);
        List<Receta> recetas = leerRecetas();

        System.out.println("Ingrese el nombre de la receta.");
        String nombre = sc.nextLine();
        System.out.println("Ingrese el tiempo de elaboracion");
        int tiempo = sc.nextInt();

        int nuevoId = recetas.stream().mapToInt(Receta::getId).max().orElse(0) + 1;
        recetas.add(new Receta(nuevoId, tiempo, nombre));
        guardarRecetas(recetas);
        System.out.println("Receta añadida con exito.");
    }

    public static void mostrarRecetas() {
        List<Receta> recetas = leerRecetas();
        if (recetas.isEmpty()) {
            System.out.println("No hay recetas almacenadas.");
            return;
        }

        for (Receta recetaActual : recetas) {
            System.out.println(recetaActual);
        }
    }

    public static void buscarRecetaPorID() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el ID de la receta a buscar: ");
        int idBuscado = sc.nextInt();

        boolean encontrado = false;
        for (Receta recetaActual : leerRecetas()) {
            if (recetaActual.getId() == idBuscado) {
                System.out.println("Receta encontrada:");
                System.out.println("Nombre: " + recetaActual.getNombre());
                System.out.println("ID: " + recetaActual.getId());
                System.out.println("Tiempo de preparación: " + recetaActual.getTiempoPreparacion() + " minutos");
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontró ninguna receta con el ID proporcionado.");
        }
    }

    public static void modificarTiempoPreparacion() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el ID de la receta que desea modificar:");
        int idBuscado = sc.nextInt();

        List<Receta> recetas = leerRecetas();
        boolean encontrado = false;

        for (Receta recetaActual : recetas) {
            if (recetaActual.getId() == idBuscado) {
                System.out.println("Receta encontrada:");
                System.out.println("Nombre: " + recetaActual.getNombre());
                System.out.println("ID: " + recetaActual.getId());
                System.out.println("Tiempo de preparación actual: " + recetaActual.getTiempoPreparacion() + " minutos");
                System.out.println("Ingrese el nuevo tiempo de preparación (en minutos):");
                int nuevoTiempo = sc.nextInt();
                recetaActual.setTiempoPreparacion(nuevoTiempo);
                guardarRecetas(recetas);
                System.out.println("Tiempo de preparación modificado correctamente.");
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontró ninguna receta con el ID proporcionado.");
        }
    }

    private static void exportarXML() {
        try {
            if (documentoxml.exists()) {
                documentoxml.delete();
                documentoxml.createNewFile();
            } else {
                documentoxml.createNewFile();
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xml = builder.newDocument();

            Element raiz = xml.createElement("Catalogo");
            xml.appendChild(raiz);
            Element recetas = xml.createElement("Recetas");
            raiz.appendChild(recetas);

            for (Receta recetaActual : leerRecetas()) {
                Element recetaXml = xml.createElement("Receta");
                recetaXml.setAttribute("id", String.valueOf(recetaActual.getId()));
                recetas.appendChild(recetaXml);

                Element nombre = xml.createElement("Nombre");
                nombre.setTextContent(recetaActual.getNombre());
                recetaXml.appendChild(nombre);

                Element tiempo = xml.createElement("Tiempo");
                tiempo.setTextContent(String.valueOf(recetaActual.getTiempoPreparacion()));
                recetaXml.appendChild(tiempo);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(xml), new StreamResult(new File(documentoxml.toURI())));
            System.out.println("Exportado correctamente");
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            throw new RuntimeException("Error al exportar a XML", e);
        }
    }
}
