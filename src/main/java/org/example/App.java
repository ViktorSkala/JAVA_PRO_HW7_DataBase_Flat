package org.example;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;

public class App {

    public static EntityManagerFactory emf;
    public static EntityManager em;

    public static void main( String[] args ) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("JPA_FLAT");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add flat");
                    System.out.println("2: add random flats");
                    System.out.println("3: delete flat");
//                    System.out.println("4: change flat");
                    System.out.println("5: view flats by parameters");
                    System.out.println("6: view all flats");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addFlat(sc);
                            break;
                        case "2":
                            insertRandomFlats(sc);
                            break;
                        case "3":
                            deleteFlat(sc);
                            break;
//                        case "4":
//                            changeFlat(sc);
//                            break;
                        case "5":
                            viewFlats(sc);
                            break;
                        case "6":
                            viewAllFlats(sc);
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void addFlat(Scanner sc) {
        System.out.print("Enter Flat name of district: ");
        String district = sc.nextLine();
        System.out.print("Enter flat square: ");
        String sSquare = sc.nextLine();
        int square = Integer.parseInt(sSquare);
        System.out.print("Enter Flat price: ");
        String sPrice = sc.nextLine();
        System.out.print("Enter City for address: ");
        String sCity = sc.nextLine();
        System.out.print("Enter street for address: ");
        String sStreet = sc.nextLine();
        System.out.print("Enter build number for address: ");
        String sBuildNum = sc.nextLine();
        int buildNum = Integer.parseInt(sBuildNum);
        System.out.print("Enter flat number for address: ");
        String sFlatNum = sc.nextLine();
        int flatNum = Integer.parseInt(sFlatNum);
        int price = Integer.parseInt(sPrice);

        em.getTransaction().begin();
        try {
            Flat f = new Flat(district,square,price);
            Address ad = new Address(sCity, sStreet, buildNum, flatNum);
            f.setAddress(ad);
            em.persist(f);
            em.getTransaction().commit();

            System.out.println(f.getId());
        } catch (Exception ex) {
            System.out.println("problem");
            em.getTransaction().rollback();
            ex.printStackTrace();
        }
    }

    private static void deleteFlat(Scanner sc) {
        System.out.print("Enter flat id: ");
        String sId = sc.nextLine();
        long id = Long.parseLong(sId);

        Flat f = em.getReference(Flat.class, id);
        if (f == null) {
            System.out.println("Flat not found!");
            return;
        }

        em.getTransaction().begin();
        try {
            em.remove(f);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

//    private static void changeFlat(Scanner sc) {
//        System.out.print("Enter flat adress: ");
//        String adress = sc.nextLine();
//        String[] adr = adress.split(",");
//        String city = adr[0];
//        String street = adr[1];
//        int duild = Integer.parseInt(adr[2]);
//        int flat = Integer.parseInt(adr[3]);
//
//        System.out.print("Enter new price: ");
//        String sPrice = sc.nextLine();
//        int price = Integer.parseInt(sPrice);
//
//        Flat f = null;
//        try {
//            Query query = em.createQuery(
//                    "SELECT x FROM Flat x WHERE x.flat = :flat", SimpleClient.class);
//            query.setParameter("name", name);
//            c = (SimpleClient) query.getSingleResult();
//        } catch (NoResultException ex) {
//            System.out.println("Client not found!");
//            return;
//        } catch (NonUniqueResultException ex) {
//            System.out.println("Non unique result!");
//            return;
//        }
//
//        ///........
//
//        em.getTransaction().begin();
//        try {
//            c.setAge(age);
//            em.getTransaction().commit();
//        } catch (Exception ex) {
//            em.getTransaction().rollback();
//        }
//    }

    private static void viewFlats(Scanner sc) {
        System.out.println("enter any parameters for search in next format: district=centr;city=Kyiv;square=2;...");
        String sParameters = sc.nextLine();
        String[] mParameters = sParameters.split(";");
        Map<String, String> param = new HashMap<>();
        for (String str:mParameters) {
            String[] sParam = str.split("=");
            param.put(sParam[0], sParam[1]);
        }
        Field[] fieldsF = Flat.class.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT f FROM Flat f WHERE ");
        for (Field field : fieldsF) {
            if (param.containsKey(field.getName())) {
                if (field.getType().getSimpleName().equals("String")) {
                    sb.append("f." + field.getName() + " = '" + param.get(field.getName()) + "' AND ");
                } else {
                    sb.append("f." + field.getName() + " = " + param.get(field.getName()) + " AND ");
                }
            }
        }
        Field[] fieldsA = Address.class.getDeclaredFields();
        for (Field field : fieldsA) {
            if (param.containsKey(field.getName())) {
                if (field.getType().getSimpleName().equals("String")) {
                    sb.append("f.address." + field.getName() + " = '" + param.get(field.getName()) + "' AND ");
                    } else {
                    sb.append("f.address." + field.getName() + " = " + param.get(field.getName()) + " AND ");
                    }
                }
        }

        String sql = sb.substring(0, sb.length() - 5);
        Query query = em.createQuery(sql, Flat.class);
        List<Flat> fl = (List<Flat>) query.getResultList();
        if (fl.size() == 0) {
            System.out.println("There is not any flat with such parameters");
        } else {
            System.out.println("List of flats you search");
            for (Flat f : fl)
                System.out.println(f);
        }
    }

    private static void viewAllFlats(Scanner sc) {
        Query query = em.createQuery("SELECT f FROM Flat f", Flat.class);
        List<Flat> fl = (List<Flat>) query.getResultList();
        System.out.println("view list of flats");

        for (Flat f : fl)
            System.out.println(f);
    }

    private static void insertRandomFlats(Scanner sc) {
        System.out.print("Enter flats count: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);

        em.getTransaction().begin();
        try {
            for (int i = 0; i < count; i++) {
                Address address = new Address(randomCity(),randomStreet(),RND.nextInt(5),RND.nextInt(5));
                Flat flat = new Flat(randomDistrict(), RND.nextInt(5), RND.nextInt(5));
                flat.setAddress(address);
                em.persist(flat);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }
    static final String[] NAMES = {"Centr", "Centr2"};
    static final String[] CITYS = {"Kyiv", "Odesa", "Lviv", "Kharkiv", "Teplodar"};
    static final String[] STREETS = {"Stolbova", "Primorsk", "Balkivska", "Grushevsk", "Skisna"};
    static final Random RND = new Random();
    static String randomDistrict() {
        return NAMES[RND.nextInt(NAMES.length)];
    }
    static String randomCity() { return CITYS[RND.nextInt(CITYS.length)];}
    static String randomStreet() { return STREETS[RND.nextInt(STREETS.length)];}
}
