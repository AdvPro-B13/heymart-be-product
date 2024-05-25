package id.ac.ui.cs.advprog.heymartbeproduct.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.HashSet;

class ProductRequestDtoTest {
        @Test
        void testNoArgsConstructor() {
                ProductRequestDto productDto = new ProductRequestDto();
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

                ProductRequestDto productDto = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);

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

                ProductRequestDto productDto = new ProductRequestDto();
                productDto.setName("Product1");
                productDto.setPrice(100.0);
                productDto.setDescription("Description1");
                productDto.setQuantity(10);
                productDto.setImage("image1.jpg");
                productDto.setCategoryNames(categoryNames);

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

                ProductRequestDto productDto1 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductRequestDto productDto2 = new ProductRequestDto("Product2", 200.0, "Description2", 20,
                                "image2.jpg",
                                categoryNames);

                String result1 = productDto1.toString();
                String result2 = productDto2.toString();

                assertTrue(result1.contains("name=Product1"));
                assertTrue(result1.contains("price=100.0"));
                assertTrue(result1.contains("description=Description1"));
                assertTrue(result1.contains("quantity=10"));
                assertTrue(result1.contains("image=image1.jpg"));
                assertTrue(result1.contains("categoryNames=[Electronics, Books]")
                                || result1.contains("categoryNames=[Books, Electronics]"));

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

                ProductRequestDto productDto = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);

                String result = productDto.toString();

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

                ProductRequestDto productDto = new ProductRequestDto();
                productDto.setName("Product1");
                productDto.setPrice(100.0);
                productDto.setDescription("Description1");
                productDto.setQuantity(10);
                productDto.setImage("image1.jpg");
                productDto.setCategoryNames(categoryNames);

                assertEquals("Product1", productDto.getName());
                assertEquals(100.0, productDto.getPrice());
                assertEquals("Description1", productDto.getDescription());
                assertEquals(10, productDto.getQuantity());
                assertEquals("image1.jpg", productDto.getImage());
                assertEquals(categoryNames, productDto.getCategoryNames());

                // Test setters with null values
                productDto.setName(null);
                productDto.setPrice(0.0);
                productDto.setDescription(null);
                productDto.setQuantity(0);
                productDto.setImage(null);
                productDto.setCategoryNames(null);

                assertNull(productDto.getName());
                assertEquals(0.0, productDto.getPrice());
                assertNull(productDto.getDescription());
                assertEquals(0, productDto.getQuantity());
                assertNull(productDto.getImage());
                assertNull(productDto.getCategoryNames());
        }

        @Test
        void testEqualsAndHashCodeWithDifferentFields() {
                Set<String> categoryNames1 = new HashSet<>();
                categoryNames1.add("Electronics");

                Set<String> categoryNames2 = new HashSet<>();
                categoryNames2.add("Books");

                ProductRequestDto productDto1 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames1);
                ProductRequestDto productDto2 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
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

                ProductRequestDto productDto = new ProductRequestDto("Product1", 100.0, "Description1", 10,
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

                ProductRequestDto productDto1 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg", categoryNames);
                ProductRequestDto productDto2 = new ProductRequestDto("Product2", 100.0, "Description1", 10,
                                "image1.jpg", categoryNames);

                assertFalse(productDto1.equals(productDto2));
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }

        @Test
        void testEqualsWithDifferentName() {
                Set<String> categoryNames = new HashSet<>();
                categoryNames.add("Electronics");
                categoryNames.add("Books");

                ProductRequestDto productDto1 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductRequestDto productDto2 = new ProductRequestDto("Product2", 100.0, "Description1", 10,
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

                ProductRequestDto productDto1 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductRequestDto productDto2 = new ProductRequestDto("Product1", 200.0, "Description1", 10,
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

                ProductRequestDto productDto1 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductRequestDto productDto2 = new ProductRequestDto("Product1", 100.0, "Description2", 10,
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

                ProductRequestDto productDto1 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductRequestDto productDto2 = new ProductRequestDto("Product1", 100.0, "Description1", 20,
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

                ProductRequestDto productDto1 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductRequestDto productDto2 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
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

                ProductRequestDto productDto1 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames);
                ProductRequestDto productDto2 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
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

                ProductRequestDto productDto1 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames1);
                ProductRequestDto productDto2 = new ProductRequestDto("Product1", 100.0, "Description1", 10,
                                "image1.jpg",
                                categoryNames2);

                assertFalse(productDto1.equals(productDto2));
                assertNotEquals(productDto1.hashCode(), productDto2.hashCode());
        }
}