package id.ac.ui.cs.advprog.heymartbeproduct.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.HashSet;

class ProductResponseDtoTest {
        @Test
        void testNoArgsConstructor() {
                ProductResponseDto productDto = new ProductResponseDto();
                assertNull(productDto.getId());
                assertNull(productDto.getName());
                assertEquals(0.0, productDto.getPrice());
                assertNull(productDto.getDescription());
                assertEquals(0, productDto.getQuantity());
                assertNull(productDto.getImage());
                assertNull(productDto.getCategoryNames());
        }

        @Test
        void testAllArgsConstructor() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);

                assertEquals("1", productDto.getId());
                assertEquals("Product1", productDto.getName());
                assertEquals(100.0, productDto.getPrice());
                assertEquals("Description1", productDto.getDescription());
                assertEquals(10, productDto.getQuantity());
                assertEquals("image1.jpg", productDto.getImage());
                assertEquals(categoryNames, productDto.getCategoryNames());
        }

        @Test
        void testSettersAndGetters() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto = new ProductResponseDto();
                productDto.setId("1");
                productDto.setName("Product1");
                productDto.setPrice(100.0);
                productDto.setDescription("Description1");
                productDto.setQuantity(10);
                productDto.setImage("image1.jpg");
                productDto.setCategoryNames(categoryNames);

                assertEquals("1", productDto.getId());
                assertEquals("Product1", productDto.getName());
                assertEquals(100.0, productDto.getPrice());
                assertEquals("Description1", productDto.getDescription());
                assertEquals(10, productDto.getQuantity());
                assertEquals("image1.jpg", productDto.getImage());
                assertEquals(categoryNames, productDto.getCategoryNames());
        }

        @Test
        void testToString() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductResponseDto productDto2 = new ProductResponseDto("2", "Product2", 200.0, "Description2", 20,
                                "image2.jpg",
                                categoryNames);

                String result1 = productDto1.toString();
                String result2 = productDto2.toString();

                assertTrue(result1.contains("id=1"));
                assertTrue(result1.contains("name=Product1"));
                assertTrue(result1.contains("price=100.0"));
                assertTrue(result1.contains("description=Description1"));
                assertTrue(result1.contains("quantity=10"));
                assertTrue(result1.contains("image=image1.jpg"));
                assertTrue(result1.contains("categoryNames=[Electronics, Books]")
                                || result1.contains("categoryNames=[Books, Electronics]"));

                assertTrue(result2.contains("id=2"));
                assertTrue(result2.contains("name=Product2"));
                assertTrue(result2.contains("price=200.0"));
                assertTrue(result2.contains("description=Description2"));
                assertTrue(result2.contains("quantity=20"));
                assertTrue(result2.contains("image=image2.jpg"));
                assertTrue(result2.contains("categoryNames=[Electronics, Books]")
                                || result2.contains("categoryNames=[Books, Electronics]"));
        }

        @Test
        void testToStringWithDifferentStates() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");

                ProductResponseDto productDto = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);

                String result = productDto.toString();

                assertTrue(result.contains("id=1"));
                assertTrue(result.contains("name=Product1"));
                assertTrue(result.contains("price=100.0"));
                assertTrue(result.contains("description=Description1"));
                assertTrue(result.contains("quantity=10"));
                assertTrue(result.contains("image=image1.jpg"));
                assertTrue(result.contains("categoryNames=[Electronics]"));
        }

        @Test
        void testGettersAndSetters() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto = new ProductResponseDto();
                productDto.setId("1");
                productDto.setName("Product1");
                productDto.setPrice(100.0);
                productDto.setDescription("Description1");
                productDto.setQuantity(10);
                productDto.setImage("image1.jpg");
                productDto.setCategoryNames(categoryNames);

                assertEquals("1", productDto.getId());
                assertEquals("Product1", productDto.getName());
                assertEquals(100.0, productDto.getPrice());
                assertEquals("Description1", productDto.getDescription());
                assertEquals(10, productDto.getQuantity());
                assertEquals("image1.jpg", productDto.getImage());
                assertEquals(categoryNames, productDto.getCategoryNames());

                // Test setters with null values
                productDto.setId(null);
                productDto.setName(null);
                productDto.setPrice(0.0);
                productDto.setDescription(null);
                productDto.setQuantity(0);
                productDto.setImage(null);
                productDto.setCategoryNames(null);

                assertNull(productDto.getId());
                assertNull(productDto.getName());
                assertEquals(0.0, productDto.getPrice());
                assertNull(productDto.getDescription());
                assertEquals(0, productDto.getQuantity());
                assertNull(productDto.getImage());
                assertNull(productDto.getCategoryNames());
        }

        @Test
        void testEqualsAndHashCode() {
                Set<String> categoryNames1 = new HashSet<>();
                categoryNames1.add("Electronics");
                categoryNames1.add("Books");

                Set<String> categoryNames2 = new HashSet<>(categoryNames1);

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames1);
                ProductResponseDto productDto2 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames2);
                ProductResponseDto productDto3 = new ProductResponseDto("2", "Product2", 200.0, "Description2", 20,
                                "image2.jpg",
                                categoryNames2);

                // Reflexivity
                assertTrue(productDto1.equals(productDto1));

                // Symmetry
                assertTrue(productDto1.equals(productDto2));
                assertTrue(productDto2.equals(productDto1));

                // Inequality
                assertFalse(productDto1.equals(productDto3));
                assertFalse(productDto3.equals(productDto1));

                // Consistency
                assertTrue(productDto1.equals(productDto2));
                assertTrue(productDto1.equals(productDto2));

                // Check null
                assertFalse(productDto1.equals(null));

                // Check hashCode
                assertEquals(productDto1.hashCode(), productDto2.hashCode());

                // Inequality of hashCode
                assertNotEquals(productDto1.hashCode(), productDto3.hashCode());
        }

        @Test
        void testEqualsAndHashCodeWithDifferentFields() {
                Set<String> categoryNames1 = new HashSet<>();
                categoryNames1.add("Electronics");

                Set<String> categoryNames2 = new HashSet<>();
                categoryNames2.add("Books");

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames1);
                ProductResponseDto productDto2 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames2);

                // Inequality
                assertFalse(productDto1.equals(productDto2));
                assertFalse(productDto2.equals(productDto1));

                // Inequality of hashCode
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }

        @Test
        void testEqualsAndHashCodeWithDifferentTypes() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);

                // Inequality with different type
                assertFalse(productDto.equals("string"));
        }

        @Test
        void testEqualsWithDifferentId() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductResponseDto productDto2 = new ProductResponseDto("2", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);

                assertFalse(productDto1.equals(productDto2));
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }

        @Test
        void testEqualsWithDifferentName() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductResponseDto productDto2 = new ProductResponseDto("1", "Product2", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);

                assertFalse(productDto1.equals(productDto2));
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }

        @Test
        void testEqualsWithDifferentPrice() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductResponseDto productDto2 = new ProductResponseDto("1", "Product1", 200.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);

                assertFalse(productDto1.equals(productDto2));
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }

        @Test
        void testEqualsWithDifferentDescription() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductResponseDto productDto2 = new ProductResponseDto("1", "Product1", 100.0, "Description2", 10,
                                "image1.jpg",
                                categoryNames);

                assertFalse(productDto1.equals(productDto2));
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }

        @Test
        void testEqualsWithDifferentQuantity() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductResponseDto productDto2 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 20,
                                "image1.jpg",
                                categoryNames);

                assertFalse(productDto1.equals(productDto2));
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }

        @Test
        void testEqualsWithDifferentImage() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductResponseDto productDto2 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image2.jpg",
                                categoryNames);

                assertFalse(productDto1.equals(productDto2));
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }

        @Test
        void testEqualsWithNullCategoryNames() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductResponseDto productDto2 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                null);

                assertFalse(productDto1.equals(productDto2));
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }

        @Test
        void testEqualsWithEmptyCategoryNames() {
                Set<String> categoryNames1 = new HashSet<>();
                categoryNames1.add("Electronics");
                categoryNames1.add("Books");

                Set<String> categoryNames2 = new HashSet<>();

                ProductResponseDto productDto1 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames1);
                ProductResponseDto productDto2 = new ProductResponseDto("1", "Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames2);

                assertFalse(productDto1.equals(productDto2));
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }
}