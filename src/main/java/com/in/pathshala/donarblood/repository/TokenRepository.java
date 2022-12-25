package com.in.pathshala.donarblood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.pathshala.donarblood.model.AuthenticationToken;
import com.in.pathshala.donarblood.model.Seller;


@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken,Long>{
	AuthenticationToken findBySeller(Seller seller);
	AuthenticationToken findTokenByToken(String token);
}
