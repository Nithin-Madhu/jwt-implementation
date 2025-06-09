package com.project.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0/demo")
public class Controller {

	@GetMapping
	public ResponseEntity<String> hello(){
		return ResponseEntity.ok("Hello Controller class");
	}
}
