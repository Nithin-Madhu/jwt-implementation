package com.project.security.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final String SECRET_KEY = "/EH4pOWzUfvHzUQgH0EQD/RGjusg3ycJwOH+f12mvECTkuWzrLNrA408gLFoRyO3lJ1XqgLiuXziQail9k1Bbr5S5jrqQL0vqCNrAmOEX+d0rZtNFqUXtbpo4RqwPYg0KDM0Sats/eomaoEVl6Am1mcL0fk/udf28Y+G8X50E+yu/bd7szcVOoXwvhv163Ho0zIdamNm1t2L9Wf/x9lgiE8gU1k/YtKWxXYh8xbAM8Mit/5ZCxCFKoVY5z/dyNgffQpUwn58I+p7u8etNQoZEP+RbCmNfUOEc0dbGK3TWNlg75GqC05kMWQ08qihjcqF7e2ZsW+j0Rw+Un6ihwY5uPtrQO71B8VqHTDZ+MrMFLI=\r\n"
			+ "";
	
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim (String token, Function<Claims,T> claimsResolver){
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	// generation of token
	
	public String generateToken(Map<String ,Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
		
	}
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<String, Object>(), userDetails);
		
	}
	
	// token validation
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return userName.equals(userDetails.getUsername()) && isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

}
