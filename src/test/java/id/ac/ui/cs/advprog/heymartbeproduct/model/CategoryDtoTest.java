package id.ac.ui.cs.advprog.heymartbeproduct.model;

import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.CategoryDto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

class CategoryDtoTest {
    @Test
    void testNoArgsConstructor() {
        CategoryDto categoryDto = new CategoryDto();
        assertNull(categoryDto.getId());
        assertNull(categoryDto.getName());
        assertTrue(categoryDto.getProductIds().isEmpty());
    }

    @Test
    void testAllArgsConstructor() {
        Set<String> productIds = new HashSet<>();
        productIds.add("product1");
        productIds.add("product2");

        CategoryDto categoryDto = new CategoryDto(1L, "Electronics", productIds);

        assertEquals(1L, categoryDto.getId());
        assertEquals("Electronics", categoryDto.getName());
        assertEquals(productIds, categoryDto.getProductIds());
    }

    @Test
    void testSetProductIds() {
        Set<String> productIds = new HashSet<>();
        productIds.add("product1");
        productIds.add("product2");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setProductIds(productIds);

        assertEquals(productIds, categoryDto.getProductIds());
    }

    @Test
    void testSetProductIdsWithNull() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setProductIds(null);

        assertTrue(categoryDto.getProductIds().isEmpty());
    }

    @Test
    void testEquals() {
        Set<String> productIds1 = new HashSet<>();
        productIds1.add("product1");
        productIds1.add("product2");

        Set<String> productIds2 = new HashSet<>(productIds1);

        CategoryDto categoryDto1 = new CategoryDto(1L, "Electronics", productIds1);
        CategoryDto categoryDto2 = new CategoryDto(1L, "Electronics", productIds2);
        CategoryDto categoryDto3 = new CategoryDto(2L, "Electronics", productIds2); // different id
        CategoryDto categoryDto4 = new CategoryDto(1L, "Books", productIds2); // different name
        CategoryDto categoryDto5 = new CategoryDto(1L, "Electronics", null); // different productIds

        // Reflexivity
        assertTrue(categoryDto1.equals(categoryDto1));

        // Symmetry
        assertTrue(categoryDto1.equals(categoryDto2));
        assertTrue(categoryDto2.equals(categoryDto1));

        // Transitivity
        assertTrue(categoryDto1.equals(categoryDto2));
        assertTrue(categoryDto2.equals(categoryDto1));
        assertTrue(categoryDto1.equals(categoryDto1));

        // Consistency
        assertTrue(categoryDto1.equals(categoryDto2));
        assertTrue(categoryDto1.equals(categoryDto2));

        // Cek null
        assertFalse(categoryDto1.equals(null));

        // Beda id
        assertFalse(categoryDto1.equals(categoryDto3));
        assertFalse(categoryDto3.equals(categoryDto1));

        // Beda name
        assertFalse(categoryDto1.equals(categoryDto4));
        assertFalse(categoryDto4.equals(categoryDto1));

        // Beda productIds
        assertFalse(categoryDto1.equals(categoryDto5));
        assertFalse(categoryDto5.equals(categoryDto1));
    }

    @Test
    void testHashCode() {
        Set<String> productIds1 = new HashSet<>();
        productIds1.add("product1");
        productIds1.add("product2");

        Set<String> productIds2 = new HashSet<>(productIds1);

        CategoryDto categoryDto1 = new CategoryDto(1L, "Electronics", productIds1);
        CategoryDto categoryDto2 = new CategoryDto(1L, "Electronics", productIds2);
        CategoryDto categoryDto3 = new CategoryDto(2L, "Electronics", productIds2); // different id
        CategoryDto categoryDto4 = new CategoryDto(1L, "Books", productIds2); // different name
        CategoryDto categoryDto5 = new CategoryDto(1L, "Electronics", null); // different productIds

        // hashCode consistency
        assertEquals(categoryDto1.hashCode(), categoryDto1.hashCode());

        // Cek kesamaan hashcode
        assertTrue(categoryDto1.equals(categoryDto2));
        assertEquals(categoryDto1.hashCode(), categoryDto2.hashCode());

        // Beda id
        assertFalse(categoryDto1.hashCode() == categoryDto3.hashCode());

        // Beda name
        assertFalse(categoryDto1.hashCode() == categoryDto4.hashCode());

        // Beda productIds
        assertFalse(categoryDto1.hashCode() == categoryDto5.hashCode());
    }

    @Test
    void testEqualsAndHashCodeEdgeCases() {
        Set<String> productIds1 = new HashSet<>();
        productIds1.add("product1");
        productIds1.add("product2");

        CategoryDto categoryDto1 = new CategoryDto(1L, "Electronics", productIds1);

        // name dan productIds null
        CategoryDto categoryDto6 = new CategoryDto(1L, null, null);
        assertFalse(categoryDto1.equals(categoryDto6));
        assertFalse(categoryDto6.equals(categoryDto1));
        assertFalse(categoryDto1.hashCode() == categoryDto6.hashCode());

        // Name null tapi productIds tidak
        CategoryDto categoryDto7 = new CategoryDto(1L, null, productIds1);
        assertFalse(categoryDto1.equals(categoryDto7));
        assertFalse(categoryDto7.equals(categoryDto1));
        assertFalse(categoryDto1.hashCode() == categoryDto7.hashCode());

        // Name not null tapi productIds null
        CategoryDto categoryDto8 = new CategoryDto(1L, "Electronics", null);
        assertFalse(categoryDto1.equals(categoryDto8));
        assertFalse(categoryDto8.equals(categoryDto1));
        assertFalse(categoryDto1.hashCode() == categoryDto8.hashCode());

        // productIds itu empty set
        CategoryDto categoryDto9 = new CategoryDto(1L, "Electronics", new HashSet<>());
        assertFalse(categoryDto1.equals(categoryDto9));
        assertFalse(categoryDto9.equals(categoryDto1));
        assertFalse(categoryDto1.hashCode() == categoryDto9.hashCode());

        // productIds punya values yang saling berbeda
        Set<String> productIds3 = new HashSet<>();
        productIds3.add("product3");
        productIds3.add("product4");
        CategoryDto categoryDto10 = new CategoryDto(1L, "Electronics", productIds3);
        assertFalse(categoryDto1.equals(categoryDto10));
        assertFalse(categoryDto10.equals(categoryDto1));
        assertFalse(categoryDto1.hashCode() == categoryDto10.hashCode());
    }

    @Test
    void testEqualsAndHashCodeWithDifferentObjects() {
        CategoryDto categoryDto1 = new CategoryDto(1L, "Electronics",
                new HashSet<>(Arrays.asList("product1", "product2")));
        String notACategoryDto = "Not a CategoryDto";
        assertFalse(categoryDto1.equals(notACategoryDto));
        assertFalse(notACategoryDto.equals(categoryDto1));
    }

    @Test
    void testEqualsAndHashCodeWithDifferentProductIdsOrder() {
        CategoryDto categoryDto1 = new CategoryDto(1L, "Electronics",
                new HashSet<>(Arrays.asList("product1", "product2")));
        CategoryDto categoryDto2 = new CategoryDto(1L, "Electronics",
                new HashSet<>(Arrays.asList("product2", "product1")));
        assertTrue(categoryDto1.equals(categoryDto2));
        assertEquals(categoryDto1.hashCode(), categoryDto2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeWithEmptyProductIds() {
        CategoryDto categoryDto1 = new CategoryDto(1L, "Electronics", new HashSet<>());
        CategoryDto categoryDto2 = new CategoryDto(1L, "Electronics", new HashSet<>());
        assertTrue(categoryDto1.equals(categoryDto2));
        assertEquals(categoryDto1.hashCode(), categoryDto2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeWithNullId() {
        CategoryDto categoryDto1 = new CategoryDto(null, "Electronics",
                new HashSet<>(Arrays.asList("product1", "product2")));
        CategoryDto categoryDto2 = new CategoryDto(null, "Electronics",
                new HashSet<>(Arrays.asList("product1", "product2")));
        assertTrue(categoryDto1.equals(categoryDto2));
        assertEquals(categoryDto1.hashCode(), categoryDto2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeWithNullName() {
        CategoryDto categoryDto1 = new CategoryDto(1L, null, new HashSet<>(Arrays.asList("product1", "product2")));
        CategoryDto categoryDto2 = new CategoryDto(1L, null, new HashSet<>(Arrays.asList("product1", "product2")));
        assertTrue(categoryDto1.equals(categoryDto2));
        assertEquals(categoryDto1.hashCode(), categoryDto2.hashCode());
    }

    @Test
    void testToString() {
        Set<String> productIds = new HashSet<>();
        productIds.add("product1");
        productIds.add("product2");

        CategoryDto categoryDto = new CategoryDto(1L, "Electronics", productIds);

        String result = categoryDto.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=Electronics"));
        assertTrue(result.contains("productIds=[product1, product2]")
                || result.contains("productIds=[product2, product1]"));
    }

    @Test
    void testGettersAndSetters() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Electronics");

        Set<String> productIds = new HashSet<>();
        productIds.add("product1");
        productIds.add("product2");
        categoryDto.setProductIds(productIds);

        assertEquals(1L, categoryDto.getId());
        assertEquals("Electronics", categoryDto.getName());
        assertEquals(productIds, categoryDto.getProductIds());
    }
}