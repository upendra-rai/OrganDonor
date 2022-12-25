package com.in.pathshala.donarblood.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in.pathshala.donarblood.Seller.SignInDto;
import com.in.pathshala.donarblood.Seller.SignInResponseDto;
import com.in.pathshala.donarblood.Seller.SignupDto;
import com.in.pathshala.donarblood.config.MessageStrings;
import com.in.pathshala.donarblood.dto.ResponseDto;
import com.in.pathshala.donarblood.exceptions.AuthenticationFailException;
import com.in.pathshala.donarblood.exceptions.CustomException;
import com.in.pathshala.donarblood.model.AuthenticationToken;
import com.in.pathshala.donarblood.model.Seller;
import com.in.pathshala.donarblood.repository.SellerRepository1;
import com.in.pathshala.donarblood.utils.Helper;

@Service
public class SellerServiceImpl1 implements SellerService1 {
	Logger logger = LoggerFactory.getLogger(SellerServiceImpl1.class);
	@Autowired
	SellerRepository1 sellerRepository1;
	@Autowired
	AuthenticationService authenticationService;

	@Transactional
	public ResponseDto signup(SignupDto signupDto) {
		String message;
		String status;
		Seller checkSeller = sellerRepository1.findByEmail(signupDto.getEmail());
		if (Objects.nonNull(checkSeller)) {
			throw new CustomException("User Already Present");
		}
		String encryptedpassword = signupDto.getPassword();
		try {
			encryptedpassword = hashPassword(signupDto.getPassword());
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		Seller seller = new Seller(0, signupDto.getName(), signupDto.getAge(), signupDto.getMobileNo(),
				signupDto.getAddress(), signupDto.getEmail(), encryptedpassword);
		sellerRepository1.save(seller);
		// save to user
		// create the token
		final AuthenticationToken authenticationToken = new AuthenticationToken(seller);
		authenticationService.saveConfirmationToken(authenticationToken);
		ResponseDto responseDto = new ResponseDto(status = "success", message = "Registration successful");
		return responseDto;
	}

	@Override
	public List<Seller> findAll() {
		try {
			logger.info("requesting findAll from SellerRepository1");
			return sellerRepository1.findAll();
		} catch (Exception e) {
			logger.error("try catch block in findAll from SellerRepository1");
			return null;
		}
	}

	@Override
	public Seller findSellerById(long id) {
		try {
			logger.info("requesting findAll from SellerRepository1");
			return sellerRepository1.findById(id).get();
		} catch (Exception e) {
			logger.error("try catch block in findAll from SellerRepository1");
			return null;
		}
	}

	@Override
	public Seller saveSeller(Seller seller) {
		try {
			logger.info("requesting saveSeller from SellerServiceImpl");
			return sellerRepository1.save(seller);
		} catch (Exception e) {
			logger.info("try catch block in SellerRepository");
			return null;
		}
	}

	@Override
	public Seller updateSeller(Seller seller) {
		try {
			logger.info("requesting updateSeller from SellerServiceImpl");
			return sellerRepository1.save(seller);
		} catch (Exception e) {
			logger.info("try catch block updateSeller in SellerRepository");
			return null;
		}
	}

	@Override
	public void deleteSellerById(long id) {
		sellerRepository1.deleteById(id);
	}

	@Override
	public SignInResponseDto signIn(SignInDto signInDto) throws CustomException {
		// first find User by email
		Seller seller = sellerRepository1.findByEmail(signInDto.getEmail());
		if (!Helper.notNull(seller)) {
			throw new AuthenticationFailException("User Not Present");
		}
		try {
			// check if password is right
			if (!seller.getPassword().equals(hashPassword(signInDto.getPassword()))) {
				// password does not match
				throw new AuthenticationFailException(MessageStrings.WRONG_PASSWORD);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error("hashing password failed {}", e.getMessage());
			throw new CustomException(e.getMessage());
		}

		AuthenticationToken token = authenticationService.getToken(seller);

		if (!Helper.notNull(token)) {
			// token not present
			throw new CustomException("token not present");
		}
		return new SignInResponseDto("success", token.getToken());
	}

	String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		return myHash;
	}

}
