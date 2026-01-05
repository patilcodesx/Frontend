package com.securevault.controller;

import com.securevault.model.*;
import com.securevault.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamsController {

    private final TeamRepository teamRepo;
    private final TeamMemberRepository memberRepo;
    private final TeamActivityRepository activityRepo;
    private final TeamMessageRepository messageRepo;

    public TeamsController(
            TeamRepository teamRepo,
            TeamMemberRepository memberRepo,
            TeamActivityRepository activityRepo,
            TeamMessageRepository messageRepo
    ) {
        this.teamRepo = teamRepo;
        this.memberRepo = memberRepo;
        this.activityRepo = activityRepo;
        this.messageRepo = messageRepo;
    }

    // ----------------------
    // CREATE TEAM
    // ----------------------
    @PostMapping("/create")
    public ResponseEntity<?> createTeam(
            @RequestBody Map<String, String> body,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("currentUser");

        Team team = new Team();
        team.setName(body.get("name"));
        team.setDescription(body.get("description"));
        team.setCreatedAt(Instant.now());
        team.setCreatedBy(user.getId());

        Team saved = teamRepo.save(team);

        // Add creator as admin
        TeamMember member = new TeamMember();
        member.setTeamId(saved.getId());
        member.setUserId(user.getId());
        member.setEmail(user.getEmail());
        member.setRole("admin");
        member.setJoinedAt(Instant.now().toString());
        memberRepo.save(member);

        return ResponseEntity.ok(saved);
    }

    // ----------------------
    // LIST TEAMS OF USER
    // ----------------------
    @GetMapping("/my")
    public ResponseEntity<?> myTeams(HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");

        List<TeamMember> memberships = memberRepo.findByEmail(user.getEmail());

        List<Team> teams = new ArrayList<>();
        for (TeamMember m : memberships) {
            teamRepo.findById(m.getTeamId()).ifPresent(teams::add);
        }

        return ResponseEntity.ok(teams);
    }

    // ----------------------
    // GET TEAM BY ID
    // ----------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getTeam(@PathVariable Long id) {
        return ResponseEntity.of(teamRepo.findById(id));
    }

    // ----------------------
    // INVITE MEMBER
    // ----------------------
    @PostMapping("/{id}/invite")
    public ResponseEntity<?> invite(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String email = body.get("email");

        return ResponseEntity.ok(Map.of("invited", email));
    }

    // ----------------------
    // REMOVE MEMBER
    // ----------------------
    @PostMapping("/{id}/remove-member")
    public ResponseEntity<?> removeMember(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String userId = body.get("userId");

        memberRepo.findAll().stream()
                .filter(m -> m.getTeamId().equals(id) && m.getUserId().equals(userId))
                .findFirst()
                .ifPresent(memberRepo::delete);

        return ResponseEntity.ok(Map.of("removed", userId));
    }

    // ----------------------
    // CHANGE ROLE
    // ----------------------
    @PostMapping("/{id}/change-role")
    public ResponseEntity<?> changeRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String userId = body.get("userId");
        String role = body.get("role");

        memberRepo.findAll().stream()
                .filter(m -> m.getTeamId().equals(id) && m.getUserId().equals(userId))
                .findFirst()
                .ifPresent(m -> {
                    m.setRole(role);
                    memberRepo.save(m);
                });

        return ResponseEntity.ok(Map.of("status", "updated"));
    }

    // ----------------------
    // LEAVE TEAM
    // ----------------------
    @PostMapping("/{id}/leave")
    public ResponseEntity<?> leave(HttpServletRequest request, @PathVariable Long id) {
        User user = (User) request.getAttribute("currentUser");

        memberRepo.findAll().stream()
                .filter(m -> m.getTeamId().equals(id) && m.getUserId().equals(user.getId()))
                .findFirst()
                .ifPresent(memberRepo::delete);

        return ResponseEntity.ok(Map.of("left", true));
    }

    // ----------------------
    // DELETE TEAM
    // ----------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        teamRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("deleted", true));
    }

    // ----------------------
    // ACTIVITY & MESSAGES
    // ----------------------
    @GetMapping("/{id}/activity")
    public ResponseEntity<?> activity(@PathVariable Long id) {
        return ResponseEntity.ok(activityRepo.findByTeamIdOrderByTimestampDesc(id));
    }

    @PostMapping("/{id}/message")
    public ResponseEntity<?> message(
            @PathVariable Long id,
            @RequestBody TeamMessage msg,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("currentUser");
        msg.setTeamId(id);
        msg.setUserId(String.valueOf(user.getId()));
        msg.setTimestamp(Instant.now().toString());

        TeamMessage saved = messageRepo.save(msg);

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<?> messages(@PathVariable Long id) {
        return ResponseEntity.ok(messageRepo.findByTeamIdOrderByTimestampAsc(id));
    }
}
