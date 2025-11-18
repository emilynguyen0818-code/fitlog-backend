package com.fitlog.workout_backend.controller;

import com.fitlog.workout_backend.model.Workout;
import com.fitlog.workout_backend.repository.WorkoutRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173",
        "https://fitlog-frontend.vercel.app",
        "https://fitlog-frontend-ehn9147.vercel.app"
})
public class WorkoutController {

    private final WorkoutRepository repo;

    public WorkoutController(WorkoutRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Workout> getAll(@RequestParam(required = false) String userId) {
        if (userId != null && !userId.isBlank()) {
            return repo.findByUserId(userId);
        }
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Workout getOne(@PathVariable String id) {
        return repo.findById(id).orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Workout create(@RequestBody Workout workout) {
        if (workout.getExercises() == null) {
            workout.setExercises(new ArrayList<>());
        }
        return repo.save(workout);
    }

    @PutMapping("/{id}")
    public Workout update(@PathVariable String id, @RequestBody Workout updated) {
        Workout existing = repo.findById(id).orElseThrow();

        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setDate(updated.getDate());
        existing.setDuration(updated.getDuration());
        existing.setNotes(updated.getNotes());
        existing.setUserId(updated.getUserId());
        existing.setExercises(
                updated.getExercises() != null ? updated.getExercises() : new ArrayList<>()
        );

        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        repo.deleteById(id);
    }
}
