package com.capgemini.csd.tippkick.spielplan.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Match")
public class MatchNotFoundException extends RuntimeException {
}
