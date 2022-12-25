package com.in.pathshala.donarblood.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in.pathshala.donarblood.config.MessageStrings;
import com.in.pathshala.donarblood.exceptions.AuthenticationFailException;
import com.in.pathshala.donarblood.model.AuthenticationToken;
import com.in.pathshala.donarblood.model.Seller;
import com.in.pathshala.donarblood.repository.TokenRepository;
import com.in.pathshala.donarblood.utils.Helper;



@Service
public class AuthenticationService {

	@Autowired
	TokenRepository tokenRepository;
	public void saveConfirmationToken(AuthenticationToken authenticationToken) {
		tokenRepository.save(authenticationToken);
	}
	public AuthenticationToken getToken(Seller seller) {
		return tokenRepository.findBySeller(seller);
	}
	public void authenticate(String token) throws AuthenticationFailException {
        if (!Helper.notNull(token)) {
            throw new AuthenticationFailException(MessageStrings.AUTH_TOEKN_NOT_PRESENT);
        }
        if (!Helper.notNull(getSeller(token))) {
            throw new AuthenticationFailException(MessageStrings.AUTH_TOEKN_NOT_VALID);
        }
    }
	public Seller  getSeller(String token) {
		AuthenticationToken authenticationToken = tokenRepository.findTokenByToken(token);
        if (Helper.notNull(authenticationToken)) {
            if (Helper.notNull(authenticationToken.getSeller())) {
                return authenticationToken.getSeller();
            }
        }
        return null;
    }
}
