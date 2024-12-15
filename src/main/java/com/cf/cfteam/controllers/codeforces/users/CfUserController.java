package com.cf.cfteam.controllers.codeforces.users;

import com.cf.cfteam.services.codeforces.users.CfUserService;
import com.cf.cfteam.transfer.payloads.codeforces.users.CfUserPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cf/user")
public class CfUserController {

    private final CfUserService cfUserService;

    @PostMapping
    public ResponseEntity<Void> createCfUser(@RequestBody CfUserPayload cfUserPayload,
                                           Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getAllCfUsers() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getCfUserById(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCfUser(@PathVariable Long id,
                                           @RequestBody CfUserPayload cfUserPayload,
                                           Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCfUsers(Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCfUserById(@PathVariable Long id,
                                               Authentication authentication) {
        return ResponseEntity.ok().build();
    }
}
