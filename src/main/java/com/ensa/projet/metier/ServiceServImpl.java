package com.ensa.projet.metier;

import com.ensa.projet.dao.ServiceDao;
import com.ensa.projet.models.Servicee;
import com.ensa.projet.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ServiceServImpl implements ServiceServ {
    @Autowired
    UserService userService;
    @Autowired
    ServiceDao serviceDao;



    @Override
    public Page<Servicee> getUserServicesAsChef(long user_id, Pageable pageable) {
        return serviceDao.findServicesByChefId(user_id, pageable);
    }

    @Override
    public Page<Servicee> getUserServicesAsEmployee(long user_id, Pageable pageable) {
        return serviceDao.findServicesByEmployeeId(user_id,pageable);
    }

    @Override
    public Page<User> getServiceEmployees(long service_id,Pageable pageable) {
        return userService.getServiceEmployees(service_id,pageable);
    }

    @Override
    public User getServiceChef(long service_id) {

        return  getServiceById(service_id).getChef();
    }


    @Override
    public Servicee getServiceById(long service_id) {
        return serviceDao.findById(service_id)
                .orElseThrow(()-> new EntityNotFoundException("service not found"));
    }

    @Override
    public Page<Servicee> getAllServices(Pageable pageable) {

        return serviceDao.findAll(pageable);
    }

    @Override
    public void deleteService(long service_id)
    {
        serviceDao.deleteById(service_id);
    }

    @Override
    public Servicee createOrUpdateService(Servicee servicee) {

        return serviceDao.save(servicee);
    }

    @Override
    public void deleteEmployeeFromService(long service_id, long employee_id) {
        User employee = userService.getUserById(employee_id);
        getServiceById(service_id).getEmployees().removeIf(user -> user.getId() == employee_id);
    }

    @Override
    public Servicee addEmployeeToService(long service_id, User employee) {
        Servicee servicee = getServiceById(service_id);
        servicee.getEmployees().add(employee);
        return servicee;
    }

    @Override
    public Page<Servicee> searchServices(String by, String value, Pageable pageable) {
        return serviceDao.findAllByNameContaining(value,pageable);
    }

    @Override
    public boolean isServiceChef(long service_id, long chef_id)
    {
        System.out.println("im"+serviceDao.isServiceChef(service_id,chef_id));
        return true;
    }

    @Override
    public boolean isServiceEmployee(long service_id, long employee_id) {
        return serviceDao.isServiceEmployee(service_id, employee_id);
    }

    @Override
    public Page<Servicee> getAllServicesByStatus(Servicee.Status status, Pageable pageable) {
        return serviceDao.findAllByStatus(status, pageable);
    }


}