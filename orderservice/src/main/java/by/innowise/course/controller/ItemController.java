package by.innowise.course.controller;

import by.innowise.course.dto.item.ItemFilterDto;
import by.innowise.course.dto.item.ItemRequestDto;
import by.innowise.course.dto.item.ItemResponseDto;
import by.innowise.course.service.ItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemResponseDto> create(
            @Valid @RequestBody ItemRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getById(
            @Positive @PathVariable Long id
    ) {
        return ResponseEntity.ok(itemService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ItemResponseDto>> getItems(
            @ModelAttribute ItemFilterDto filterDto,
            Pageable pageable
    ) {
        return ResponseEntity.ok(itemService.getItems(filterDto, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemResponseDto> update(
            @Positive @PathVariable Long id,
            @Valid @RequestBody ItemRequestDto requestDto
    ) {
        return ResponseEntity.ok(itemService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(
            @Positive @PathVariable Long id
    ) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}