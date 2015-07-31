
package ru.simple.dao;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import ru.simple.entity.Cars;
import ru.simple.utils.HibernateUtil;

@ManagedBean
@SessionScoped
public class DataAccess {
        private Cars car = new Cars();
        private String searchQuery; // текущий поисковый запрос
        private Cars currentCar;
        private Boolean wcards = false; // вкл/выкл расширенный поиск
        private String sort = "by_id_asc"; // опция сортировки

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    public Cars getCurrentCar() {
        return currentCar;
    }

    public void setCurrentCar(Cars currentCar) {
        this.currentCar = currentCar;
    }
        

    public Boolean getWcards() {
        return wcards;
    }

    public void setWcards(Boolean wcards) {
        this.wcards = wcards;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
    
    public String prepareEdit(int id){
        currentCar = getCarById(id);
    return "caredit.xhtml";
    }
    
    public String prepareNew(){
        currentCar = new Cars();
    return "caredit.xhtml";
     }
    
    public String doSorting(){
    return "index.xhtml";
    }    
                
        public List<Cars> getAllCars() {
        Session session = HibernateUtil.getSessionFactory().openSession();    
        List<Cars> result;
        switch (sort) {
            case "by_year_asc": result = session.createCriteria(Cars.class).addOrder(Order.asc("year")).list();
                     break;
            case "by_year_desc": result = session.createCriteria(Cars.class).addOrder(Order.desc("year")).list();
                     break;
            case "by_price_asc": result = session.createCriteria(Cars.class).addOrder(Order.asc("price")).list();
                     break;
            case "by_price_desc": result = session.createCriteria(Cars.class).addOrder(Order.desc("price")).list();
                     break;
            case "by_id_asc": result = session.createCriteria(Cars.class).addOrder(Order.asc("id")).list();
                     break;
            case "by_id_desc": result = session.createCriteria(Cars.class).addOrder(Order.desc("id")).list();
                     break;
            default: result = session.createCriteria(Cars.class).addOrder(Order.asc("id")).list(); 
                     break;
        }
        session.close();
        if (!result.isEmpty()) {
            return result;
        } else {
            Cars car = new Cars("На сервере нет ниодной записи");   
            result.add(car);
            return result;
        }
      
    }       
  
    public Cars getCarById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Cars car = (Cars) session.get(Cars.class, id);
        session.close();
        return car;
    }
    
    public List<Cars> findCarByName(){
        String W;  // параметр вставляемый в SQL запрос для поиска с wildcards
        if (wcards == true) {
            W = "%";
        } else {
            W = "";
        }
        String sql = "SELECT * FROM cars WHERE model LIKE '" + W + searchQuery + W + "'";
        Session session = HibernateUtil.getSessionFactory().openSession();
        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List<Cars> result = query.list();
        session.close();
        if (result.isEmpty()) {
            car.setModel("Ничего не найдено");
            result.add(car);
            return result;
        } else {
            return result;
        }
    }
    
     public String deleteCarById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(getCarById(id));
        session.flush();
        session.getTransaction().commit();
        session.close();
        return "index?faces-redirect=true"; // защищаем от F5 и повторного удаления
    }
          
     public String saveCar() {
               
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(currentCar);
            session.flush();
            session.getTransaction().commit();
            session.close();
       return "index?faces-redirect=true";
       
    }
    
    public String getCount(){ // выводит количество всех записей
         Session session = HibernateUtil.getSessionFactory().openSession();

Criteria crit = session.createCriteria(Cars.class);
crit.setProjection(Projections.countDistinct("id"));
Long count = (Long)  crit.uniqueResult();
    return String.valueOf(count);
    }
}
