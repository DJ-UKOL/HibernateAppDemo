package org.example;

import org.example.model.*;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Item.class);
                //.addAnnotatedClass(Passport.class)
               // .addAnnotatedClass(Actor.class)
               // .addAnnotatedClass(Movie.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        try (sessionFactory){
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();

            Person person = session.get(Person.class, 1);
            System.out.println("Получили человека");

            session.getTransaction().commit();
            System.out.println("Сессия завершилась");

            // Открываем сессию и транзакцию еще раз (в любом другом месте коде)
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            System.out.println("Внутри другой транзакции");

            person = (Person) session.merge(person);

            //Hibernate.initialize(person.getItems());
            // HQL - запрос
            List<Item> items = session.createQuery("select i from Item i where i.owner.id=:person_id", Item.class)
                    .setParameter("person_id", person.getId()).getResultList();

            System.out.println(items);

            session.getTransaction().commit();

            System.out.println("Вне второй сессии");

            // Это работает т.к. связанные товары были загружены
            // System.out.println(person.getItems());
        }
    }
}