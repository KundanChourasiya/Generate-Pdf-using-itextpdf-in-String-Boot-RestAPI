package com.demo.Controller;


import com.demo.Entity.Product;
import com.demo.Payload.ApiResponse;
import com.demo.Service.ProductService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET: Retrieve all Product
    // URL: http://localhost:8080/api/v1/product
    @GetMapping("/product")
    public ResponseEntity<ApiResponse<?>> getAllProducts() {
        List<Product> allProduct = productService.getAllProduct();
        if (allProduct.isEmpty()) {
            ApiResponse<Object> response = new ApiResponse<>(false, "Product List Empty!!!", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<List<Product>> response = new ApiResponse<>(true, "Product List Found!!!", allProduct);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // POST: Save a new Product
    // URL: http://localhost:8080/api/v1/product
    @PostMapping(value = "/product")
    public ResponseEntity<ApiResponse<?>> saveProduct(@RequestBody Product product) {
        try {
            Product saveProduct = productService.saveProduct(product);
            ApiResponse<Product> response = new ApiResponse<>(true, "Product saved successfully!!!", saveProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<Product> response = new ApiResponse<>(false, "Product Not Saved!!!", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // GET: Generate a product list Report
    // URL: http://localhost:8080/api/v1/product/pdf-report
    @GetMapping(value = "/product/pdf-report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> productReport() throws IOException {
        ByteArrayInputStream generatedPdf = productService.generatePdf();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Product_List.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(generatedPdf));
    }

}
