package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GestorRecetas {
    static File receta = new File("src/main/java/org/example/Ficheros/recetas.dat");
    static File documentoxml = new File("src/main/java/org/example/Ficheros/recetas.xml");
    static File documentojson = new File("src/main/java/org/example/Ficheros/recetas.json");
    static Configuracion configuracion = new Configuracion();
    private static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        comprobarFichero();
        boolean salida = false;

        comprobar();
        historialRecetas();
        while (!salida) {
            contar();
            System.out.println("1. Añadir receta");
            System.out.println("2. Mostrar recetas");
            System.out.println("3. Modificar tiempo de preparación");
            System.out.println("4. Buscar receta por ID");
            System.out.println("5. Exportar recetas a XML");
            System.out.println("6. Salir");
            int opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    configuracion.setUsado("Añadir receta");
                    anadir();
                    break;
                case 2:
                    configuracion.setUsado("Mostrar recetas");
                    mostrarRecetas();
                    // Mostrar recetas
                    break;
                case 3:
                    configuracion.setUsado("Modificar tiempo de preparación");
                    modificarTiempoPreparacion();
                    // Modificar tiempo de preparación
                    break;
                case 4:
                    configuracion.setUsado("Buscar receta");
                    buscarRecetaPorID();
                    break;

                case 5:
                    configuracion.setUsado("Exportar recetas a XML");
                    exportarXML();
                    break;
                case 6:
                    metergson();
                    salida = true;
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }



    private static void comprobar() {
        if (receta.length()==0){
            try( DataOutputStream escribeFichero = new DataOutputStream( new FileOutputStream(receta,true))){
                String[] Nombres = {"Croquetas de jamon", "macarrones con tomate", "Pizza"};
                int[] ids={1,2,3};
                int [] tiempo={40,20,20};
                try (RandomAccessFile fichero = new RandomAccessFile(receta, "rw")) {
                    for (int i = 0; i < Nombres.length; i++) {

                        //Escribe nombre de tamaño fijo 10 caracteres
                        StringBuffer nombre = new
                                StringBuffer(Nombres[i]);
                        nombre.setLength(27);
                        escribeFichero.writeChars(nombre.toString());
                        //Escribe edad
                        escribeFichero.writeInt(ids[i]);
                        //Escribe tiempo
                        escribeFichero.writeInt(tiempo[i]);

                    }
                    System.out.println("Registros almacenados correctamente.");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void historialRecetas() {
       if(!documentojson.exists() || documentojson.length() == 0) {
           return;
       }
       Gson gson = new Gson();
         try (FileReader reader = new FileReader(documentojson)) {
             Configuracion [] historial = gson.fromJson(reader, Configuracion[].class);
             if (historial != null) {
                 for (Configuracion registory : historial) {
                     System.out.println(registory);
                     System.out.println();
                 }
             }
         }catch (Exception e){
                System.out.println("Error al leer el archivo JSON: " + e.getMessage());
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
        char[] nombre=new char[27];
        ArrayList<Integer> ids=new ArrayList<>();

        try(RandomAccessFile leer=new RandomAccessFile(receta,"rw")) {
            while(leer.getFilePointer()<receta.length()){
                for (int i=0;i<nombre.length;i++){
                    nombre[i] = leer.readChar();
                }
                ids.add(leer.readInt());
                int tiempo2=leer.readInt();
            }
            System.out.println("Ingrese el nombre de la receta.");
            String nombre1=sc.nextLine();
            int newid= Collections.max(ids)+1;
            System.out.println("Ingrese el tiempo de elaboracion");
            int newtiempo=sc.nextInt();
            StringBuffer newnombre=new StringBuffer(nombre1);
            newnombre.setLength(27);
            leer.writeChars(newnombre.toString());
            leer.writeInt(newid);
            leer.writeInt(newtiempo);
            System.out.println("Receta añadida con exito.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void mostrarRecetas(){
        try(RandomAccessFile leer=new RandomAccessFile(receta,"r")) {

            while(leer.getFilePointer()<receta.length()){
                char[] nombre=new char[27];

                for (int i=0;i<nombre.length;i++){
                    nombre[i] = leer.readChar();
                }
                String nombreaparece=new String(nombre).trim();
                Receta receta = new Receta(leer.readInt(), leer.readInt(), nombreaparece);

                System.out.println("ID: "+receta.getId());
                System.out.println("Nombre: "+receta.getNombre());
                System.out.println("Tiempo de elavoracion: "+receta.getTiempoPreparacion());
                System.out.println("-----------------------------------");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void buscarRecetaPorID() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el ID de la receta a buscar: ");
        int idBuscado = sc.nextInt();

        try (RandomAccessFile leer = new RandomAccessFile(receta, "r")) {
            boolean encontrado = false;
            while (leer.getFilePointer() < receta.length()) {
                char[] nombre = new char[27];
                for (int i = 0; i < nombre.length; i++) {
                    nombre[i] = leer.readChar();
                }
                String nombreReceta = new String(nombre).trim();
                int idReceta = leer.readInt();
                int tiempoPreparacion = leer.readInt();

                if (idReceta == idBuscado) {
                    System.out.println("Receta encontrada:");
                    System.out.println("Nombre: " + nombreReceta);
                    System.out.println("ID: " + idReceta);
                    System.out.println("Tiempo de preparación: " + tiempoPreparacion + " minutos");
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                System.out.println("No se encontró ninguna receta con el ID proporcionado.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void modificarTiempoPreparacion() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el ID de la receta que desea modificar:");
        int idBuscado = sc.nextInt();
        try (RandomAccessFile leer = new RandomAccessFile(receta, "rw")) {
            boolean encontrado = false;
            while (leer.getFilePointer() < receta.length()) {
                char[] nombre = new char[27];
                for (int i = 0; i < nombre.length; i++) {
                    nombre[i] = leer.readChar();
                }
                String nombreReceta = new String(nombre).trim();
                int idReceta = leer.readInt();
                int tiempoPreparacion = leer.readInt();

                if (idReceta == idBuscado) {
                    System.out.println("Receta encontrada:");
                    System.out.println("Nombre: " + nombreReceta);
                    System.out.println("ID: " + idReceta);
                    System.out.println("Tiempo de preparación actual: " + tiempoPreparacion + " minutos");
                    System.out.println("Ingrese el nuevo tiempo de preparación (en minutos):");
                    int nuevoTiempo = sc.nextInt();

                    // Mover el puntero del archivo al inicio del tiempo de preparación
                    leer.seek(leer.getFilePointer() - 4); // Retroceder 4 bytes para sobrescribir el tiempo
                    leer.writeInt(nuevoTiempo);
                    System.out.println("Tiempo de preparación modificado correctamente.");
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                System.out.println("No se encontró ninguna receta con el ID proporcionado.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void exportarXML() {
        try(RandomAccessFile leer=new RandomAccessFile(receta,"r")) {
            if(documentoxml.exists()){
                documentoxml.delete();
                documentoxml.createNewFile();
            }else{
                documentoxml.createNewFile();
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xml = builder.newDocument(); // <-- AQUÍ ESTABA EL CAMBIO PRINCIPAL

            // 2. Creamos los nodos del XML
            Element raiz = xml.createElement("Catalogo");
            xml.appendChild(raiz);
            Element recetas = xml.createElement("Recetas");
            raiz.appendChild(recetas);

            while(leer.getFilePointer()<receta.length()){
                char[] nombres = new char[27];
                for (int i=0;i<nombres.length;i++){
                    nombres[i] = leer.readChar();
                }
                String newreceta = new String(nombres).trim();
                int ids=leer.readInt();
                int tiempo2=leer.readInt();
                Element receta=xml.createElement("Receta");
                receta.setAttribute("id", String.valueOf(ids));
                recetas.appendChild(receta);
                Element nombre=xml.createElement("Nombre");
                nombre.setTextContent(newreceta);
                receta.appendChild(nombre);
                Element tiempo=xml.createElement("Tiempo");
                tiempo.setTextContent(String.valueOf(tiempo2));
                receta.appendChild(tiempo);

            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(xml),new
                    StreamResult(new File(documentoxml.toURI())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Exportado correctamente");

    }
    public static void metergson(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Configuracion> historial = new ArrayList<>();
        if (documentojson.exists() && documentojson.length() > 0) {
            try (FileReader reader = new FileReader(documentojson)) {
                Configuracion[] existentes = gson.fromJson(reader, Configuracion[].class);
                if (existentes != null) {
                    Collections.addAll(historial, existentes);
                }
            } catch (IOException e) {
                System.out.println("Error al leer el archivo JSON: " + e.getMessage());
            }
        }
        historial.add(configuracion);
        try (FileWriter writer = new FileWriter(documentojson)) {
            gson.toJson(historial, writer);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo JSON: " + e.getMessage());
        }
    }
    private static void contar() {
        char[] nombre=new char[27];

        int nrecetas=0;
        try(RandomAccessFile leer=new RandomAccessFile(receta,"rw")) {
            while(leer.getFilePointer()<receta.length()){
                for (int i=0;i<nombre.length;i++){
                    nombre[i] = leer.readChar();
                }
                int id=(leer.readInt());
                int tiempo2=leer.readInt();
                nrecetas++;
            }
            configuracion.setRecetas(nrecetas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
