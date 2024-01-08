package com.corenect.task.repositories;

import com.corenect.task.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route,String> {
    Route findByRouteId(String routeId);
}
