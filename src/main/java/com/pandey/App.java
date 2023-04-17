package com.pandey;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import com.pandey.data.Item;
import com.pandey.data.Nike;

public class App {
    private static String color, outputPref;
    private static char size, gender;

    public static void main(String[] args) {

        // addDataToDatabase();

        // Getting user input using scanner class
        takeUserInputForPreferences();

        // Init priority queue for sorting
        PriorityQueue<Item> pq = new PriorityQueue<>();
        pq = sort(pq, outputPref);

        searchForTShirt(pq, color, size, gender);

        // Finally printing the output
        if (pq.size() > 0) {
            printOutput(pq);
        } else {
            System.out.println("Sorry! We didn't find any match.");
        }

    }

    // Taking user input using scanner class!
    private static void takeUserInputForPreferences() {
        Scanner sc = new Scanner(System.in);
        System.out.println("For Searching : ");
        System.out.println("Please enter color : (PURPLE,MAROON,BLACK, YELLOW, BLUE, GREY, PINK, WHITE) : ");
        color = sc.next();
        System.out.println("Please enter size : (S,M,L)");
        size = sc.next().charAt(0);
        System.out.println("Please enter gender : (M,F,U) : ");
        gender = sc.next().charAt(0);
        System.out.println("Enter you preference to filter : (PRICE, RATING , BOTH) ");
        outputPref = sc.next();
        sc.close();
    }

    // fetching the appropriate data from db
    // @SuppressWarnings("unchecked")
    private static void searchForTShirt(PriorityQueue<Item> pq, String color, char size, char gender) {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Nike.class)
                .addAnnotatedClass(Item.class);
        ServiceRegistry reg = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
        SessionFactory sFactory = cfg.buildSessionFactory(reg);
        Session session = null;
        Transaction transaction = null;
        try {
            session = sFactory.openSession();
            transaction = session.beginTransaction();;
            String str = "Select * from Nike as n where n.colour = ?1 and n.size = ?2 and n.gender_recommendation = ?3";
            try{
                Query<Nike> query = session.createNativeQuery(str, Nike.class);
                query.setParameter(1, color).setParameter(2, size).setParameter(3, gender);
                List<Nike> nikelist = (List<Nike>)query.getResultList();
                for (Nike n : nikelist)
                pq.offer(n.getItem());
            }catch(ClassCastException e){
                System.out.println("Caught ClassCastException: " + e.getMessage());
            }catch(Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }finally{
            if(transaction != null)
                transaction.commit();
            if(session != null)    
                 session.close();
        }       
        

    }

    private static void addDataToDatabase() {
        Nike tshirt = prepareObj("NICB7952103MC", "Nike x AMBUSH Gillet Tshirt-II", "Pink", 'F', 'M', 6900.00, 4.0,
                "Y");
        saveIntoDb(tshirt);
    }

    private static Nike prepareObj(String id, String name, String color, char gender, char size, double price,
            double rtg, String availability) {
        Nike tshirt = new Nike();
        tshirt.setId(id);
        Item item = new Item(name,
                color, gender, size, price, rtg, availability);
        tshirt.setItem(item);
        return tshirt;
    }

    private static void saveIntoDb(Nike tshirt) {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Nike.class)
                .addAnnotatedClass(Item.class);
        ServiceRegistry reg = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
        SessionFactory sFactory = cfg.buildSessionFactory(reg);
        Session session = null;
        Transaction transaction = null;
        try {
            session = sFactory.openSession();
            transaction = session.beginTransaction();
            session.merge(tshirt);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session != null) session.close();
            if(transaction != null) transaction.commit();
        }
    }

    // Method to sort the priority queue according to preference given!
    private static PriorityQueue<Item> sort(PriorityQueue<Item> pq, String str) {
        if (str.equalsIgnoreCase("Price")) {
            pq = new PriorityQueue<>((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
        } else if (str.equalsIgnoreCase("Rating")) {
            pq = new PriorityQueue<>((a, b) -> Double.compare(a.getRating(), b.getRating()));
        } else {
            pq = new PriorityQueue<>((a, b) -> Double.compare(Double.compare(a.getPrice(), b.getPrice()),
                    Double.compare(a.getRating(), b.getRating())));
        }
        return pq;
    }

    // Method to print final output to console.
    private static void printOutput(PriorityQueue<Item> pq) {
        while (pq.size() > 0) {
            Item it = pq.poll();
            System.out.println(it.toString());
        }
    }
}
